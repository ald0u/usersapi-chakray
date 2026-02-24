package com.aldolagunas.usersapi.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final String MADAGASCAR_tIME = "Indian/Antananarivo";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static String getCurrentMadagascarTimestamp() {
        ZonedDateTime madagascarTime = ZonedDateTime.now(ZoneId.of(MADAGASCAR_tIME));
        return madagascarTime.format(FORMATTER);
    }
}