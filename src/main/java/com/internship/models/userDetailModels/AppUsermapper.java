package com.internship.models.userDetailModels;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class AppUsermapper implements RowMapper<AppUser> { //mapper для получения данных из таблицы хранящей логин и пароль
    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        AppUser user = new AppUser();
        user.setUserName(rs.getString("user_name"));
        user.setEncryptedPassword(rs.getString("encryted_password"));
        return user;
    }
}
