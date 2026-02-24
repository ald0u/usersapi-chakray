package com.aldolagunas.usersapi.repository;

import com.aldolagunas.usersapi.model.Address;
import com.aldolagunas.usersapi.model.User;
import com.aldolagunas.usersapi.util.DateUtil;
import com.aldolagunas.usersapi.util.EncryptionUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class UserRepository {
    private final Map<UUID, User> users = new ConcurrentHashMap<>();
    private final EncryptionUtil encryptionUtil;

    public UserRepository(EncryptionUtil encryptionUtil) {
        this.encryptionUtil = encryptionUtil;
    }

    @PostConstruct
    public void initData() {
        createSampleUser(
                "user1@mail.com",
                "user1",
                "+1 55 555 555 55",
                "AARR990101XXX",
                "7c4a8d09ca3762af61e59520943dc26494f8941b",
                Arrays.asList(
                        Address.builder().id(1L).name("workaddress").street("street No. 1").countryCode("UK").build(),
                        Address.builder().id(2L).name("homeaddress").street("street No. 2").countryCode("AU").build()
                )
        );
        createSampleUser(
                "user2@mail.com",
                "user2",
                "+52 55 444 444 44",
                "BBCC990202YYY",
                "password123",
                Collections.emptyList()
        );
        createSampleUser(
                "user3@test.com",
                "user3",
                "+1 55 666 666 66",
                "DDEE990303ZZZ",
                "test456",
                Collections.emptyList()
        );
    }

    private void createSampleUser(String email, String name, String phone, String taxId, String password, List<Address> addresses) {
        User user = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .name(name)
                .phone(phone)
                .password(encryptionUtil.encrypt(password))
                .taxId(taxId)
                .createdAt(DateUtil.getCurrentMadagascarTimestamp())
                .addresses(addresses)
                .build();
        users.put(user.getId(), user);
    }

    public User save(User user) {
        if (user.getId() == null) user.setId(UUID.randomUUID());
        if (user.getCreatedAt() == null) user.setCreatedAt(DateUtil.getCurrentMadagascarTimestamp());
        if (user.getPassword() != null && !isEncrypted(user.getPassword())) {
            user.setPassword(encryptionUtil.encrypt(user.getPassword()));
        }
        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(UUID id) { return Optional.ofNullable(users.get(id)); }
    public List<User> findAll() { return new ArrayList<>(users.values()); }
    public void deleteById(UUID id) { users.remove(id); }
    public boolean existsByTaxId(String taxId) {
        return users.values().stream().anyMatch(u -> u.getTaxId().equalsIgnoreCase(taxId));
    }
    public Optional<User> findByTaxId(String taxId) {
        return users.values().stream().filter(u -> u.getTaxId().equalsIgnoreCase(taxId)).findFirst();
    }

    public List<User> findAllSorted(String sortBy) {
        List<User> userList = new ArrayList<>(users.values());
        if (sortBy == null || sortBy.trim().isEmpty()) return userList;

        Comparator<User> comparator = switch (sortBy.toLowerCase()) {
            case "email" -> Comparator.comparing(User::getEmail, String.CASE_INSENSITIVE_ORDER);
            case "id" -> Comparator.comparing(User::getId);
            case "name" -> Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER);
            case "phone" -> Comparator.comparing(User::getPhone);
            case "tax_id" -> Comparator.comparing(User::getTaxId);
            case "created_at" -> Comparator.comparing(User::getCreatedAt);
            default -> throw new IllegalArgumentException("Invalid sortedBy field: " + sortBy);
        };
        userList.sort(comparator);
        return userList;
    }

    public List<User> findAllFiltered(String filter) {
        if (filter == null || filter.trim().isEmpty()) return new ArrayList<>(users.values());
        String[] parts = filter.split("\\+");
        if (parts.length != 3) throw new IllegalArgumentException("Invalid filter format. Use: field+operator+value");

        String field = parts[0].trim().toLowerCase();
        String operator = parts[1].trim().toLowerCase();
        String value = parts[2].trim();
        if (!isValidField(field) || !isValidOperator(operator)) {
            throw new IllegalArgumentException("Invalid filter format. Use: field+operator+value");
        }

        return users.values().stream()
                .filter(user -> matchesFilter(user, field, operator, value))
                .collect(Collectors.toList());
    }

    private boolean matchesFilter(User user, String field, String operator, String value) {
        String fieldValue = switch (field) {
            case "email" -> user.getEmail();
            case "id" -> user.getId().toString();
            case "name" -> user.getName();
            case "phone" -> user.getPhone();
            case "tax_id" -> user.getTaxId();
            case "created_at" -> user.getCreatedAt();
            default -> "";
        };

        if (fieldValue == null) return false;
        fieldValue = fieldValue.toLowerCase();
        value = value.toLowerCase();

        return switch (operator) {
            case "co" -> fieldValue.contains(value);
            case "eq" -> fieldValue.equals(value);
            case "sw" -> fieldValue.startsWith(value);
            case "ew" -> fieldValue.endsWith(value);
            default -> false;
        };
    }

    private boolean isValidField(String field) {
        return switch (field) {
            case "email", "id", "name", "phone", "tax_id", "created_at" -> true;
            default -> false;
        };
    }

    private boolean isValidOperator(String operator) {
        return switch (operator) {
            case "co", "eq", "sw", "ew" -> true;
            default -> false;
        };
    }

    private boolean isEncrypted(String text) {
        try { Base64.getDecoder().decode(text); return true; }
        catch (IllegalArgumentException e) { return false; }
    }
}