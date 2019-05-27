package com.internship.models.userDetailModels;

public class AppUser { //pojo класс для представления таблицы хранящей данные логин, пароль
    private String userName;
    private String encryptedPassword;


    public String getUserName() {
        return userName;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    @Override
    public String toString(){
        return "user: "+ userName+" "+"password "+encryptedPassword;
    }
}
