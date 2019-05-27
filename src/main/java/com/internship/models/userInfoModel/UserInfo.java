package com.internship.models.userInfoModel;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserInfo { //pojo класс для представления таблицы хранящей данные о пользователе
    private String login;
    private String name;
    private String surname;
    private int age;
    public UserInfo() {
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                '}';
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
