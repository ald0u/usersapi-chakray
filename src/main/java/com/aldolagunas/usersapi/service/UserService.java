package com.aldolagunas.usersapi.service;

import com.aldolagunas.usersapi.exception.DuplicateTaxIdException;
import com.aldolagunas.usersapi.model.User;
import com.aldolagunas.usersapi.repository.UserRepository;
import com.aldolagunas.usersapi.util.DateUtil;
import com.aldolagunas.usersapi.util.EncryptionUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;
    private final Validator validator;

    public UserService(UserRepository userRepository, EncryptionUtil encryptionUtil, Validator validator) {
        this.userRepository = userRepository;
        this.encryptionUtil = encryptionUtil;
        this.validator = validator;
    }

    public List<User> getAllUsers(String sortedBy, String filter) {
        validateSort(sortedBy);
        validateFilter(filter);
        List<User> filtered = userRepository.findAllFiltered(filter);
        return applySorting(filtered, sortedBy);
    }

    private void validateFilter(String filter) {
        if (filter == null || filter.trim().isEmpty()) return;
        String[] parts = filter.split("\\+");
        if (parts.length != 3) throw new IllegalArgumentException("Invalid filter format. Use: field+operator+value");
        if (!isValidField(parts[0]) || !isValidOperator(parts[1])) {
            throw new IllegalArgumentException("Invalid filter format. Use: field+operator+value");
        }
    }

    private void validateSort(String sortedBy) {
        if (sortedBy == null || sortedBy.trim().isEmpty()) return;
        if (!isValidField(sortedBy)) throw new IllegalArgumentException("Invalid sortedBy field: " + sortedBy);
    }

    private boolean isValidField(String field) {
        return field != null && switch (field) {
            case "email", "id", "name", "phone", "tax_id", "created_at" -> true;
            default -> false;
        };
    }

    private boolean isValidOperator(String operator) {
        return operator != null && switch (operator) {
            case "co", "eq", "sw", "ew" -> true;
            default -> false;
        };
    }

    private List<User> applySorting(List<User> users, String sortedBy) {
        if (sortedBy == null || sortedBy.trim().isEmpty()) return users;
        Comparator<User> comparator = switch (sortedBy) {
            case "email" -> Comparator.comparing(User::getEmail, String.CASE_INSENSITIVE_ORDER);
            case "id" -> Comparator.comparing(User::getId);
            case "name" -> Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER);
            case "phone" -> Comparator.comparing(User::getPhone);
            case "tax_id" -> Comparator.comparing(User::getTaxId);
            case "created_at" -> Comparator.comparing(User::getCreatedAt);
            default -> throw new IllegalArgumentException("Invalid sortedBy field: " + sortedBy);
        };
        users.sort(comparator);
        return users;
    }

    public User createUser(User user) {
        if (userRepository.existsByTaxId(user.getTaxId())) {
            throw new DuplicateTaxIdException("tax_id must be unique");
        }
        user.setId(UUID.randomUUID());
        user.setPassword(encryptionUtil.encrypt(user.getPassword()));
        user.setCreatedAt(DateUtil.getCurrentMadagascarTimestamp());
        return userRepository.save(user);
    }

    public User updateUser(UUID id, Map<String, Object> updates) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "email" -> user.setEmail((String) value);
                case "name" -> user.setName((String) value);
                case "phone" -> user.setPhone((String) value);
                case "tax_id" -> {
                    String taxId = (String) value;
                    userRepository.findByTaxId(taxId)
                            .filter(existing -> !existing.getId().equals(id))
                            .ifPresent(existing -> { throw new DuplicateTaxIdException("tax_id must be unique"); });
                    user.setTaxId(taxId);
                }
                case "password" -> user.setPassword(encryptionUtil.encrypt((String) value));
                default -> throw new IllegalArgumentException("Field not allowed: " + key);
            }
        });

        validateUser(user);
        return userRepository.save(user);
    }

    private void validateUser(User user) {
        var violations = validator.validate(user);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .findFirst()
                    .orElse("Validation error");
            throw new IllegalArgumentException(message);
        }
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public boolean authenticate(String taxId, String password) {
        return userRepository.findByTaxId(taxId)
                .map(u -> encryptionUtil.decrypt(u.getPassword()).equals(password))
                .orElse(false);
    }
}
