package com.internship.models.userInfoModel;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoMapper implements RowMapper<UserInfo> { //mapper для получения информации о залогинненом пользователе из бд
    @Override
    public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserInfo userInfo = new UserInfo();
        userInfo.setLogin(rs.getString("login"));
        userInfo.setName(rs.getString("name"));
        userInfo.setSurname(rs.getString("surname"));
        userInfo.setAge(rs.getInt("age"));
        return userInfo;
    }
}
