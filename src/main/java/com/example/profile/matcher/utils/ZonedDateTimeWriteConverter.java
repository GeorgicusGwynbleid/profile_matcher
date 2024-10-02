package com.example.profile.matcher.utils;

import org.springframework.core.convert.converter.Converter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeWriteConverter implements Converter<ZonedDateTime, String> {

    @Override
    public String convert(ZonedDateTime newDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");
        return newDate.format(formatter);
    }
}
