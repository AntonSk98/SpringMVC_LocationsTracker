package com.internship.models.uniqueKeyModel;

public class UniqueKey { //pojo класс для представления таблицы хранящей данные уникальный ключ
    private String key;

    public UniqueKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
