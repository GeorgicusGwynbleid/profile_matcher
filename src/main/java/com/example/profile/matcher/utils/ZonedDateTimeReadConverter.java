package com.example.profile.matcher.utils;

import org.springframework.core.convert.converter.Converter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeReadConverter implements Converter<String, ZonedDateTime> {

    @Override
    public ZonedDateTime convert(String newDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");
        return ZonedDateTime.parse(newDate, formatter);
    }
}