package com.ryo.muhammad.popularmovies.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AppTypeConverter {

    @TypeConverter
    public static List<Integer> toList(String string) {
        List<Integer> integers = null;
        if (string != null) {
            Type type = new TypeToken<List<Integer>>() {
            }.getType();

            integers = new Gson().fromJson(string, type);
        }
        return integers;
    }

    @TypeConverter
    public static String toString(List<Integer> list) {

        return new Gson().toJson(list);
    }
}
