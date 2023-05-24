package com.zireaell1.todolist.data.todo;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

public class Converters {
    @TypeConverter
    public static LocalDateTime fromTimestamp(Long value) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), TimeZone.getDefault().toZoneId());
    }

    @TypeConverter
    public static Long localDateTimeToTimestamp(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneOffset.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }
}
