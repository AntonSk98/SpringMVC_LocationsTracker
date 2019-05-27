package com.internship.DAO.UsersDAO;

import com.internship.models.userDetailModels.AppUser;
import com.internship.models.userDetailModels.AppUsermapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AppUserDAOImpl implements AppUserDAO {

    @Autowired
    JdbcTemplate setupJdbcTemplate;
    @Override
    public AppUser getUserByName(String name) {
        String GET_USER_BY_NAME = "select user_name, encryted_password from app_user where user_name = '"+name+"';";
        return setupJdbcTemplate.queryForObject(GET_USER_BY_NAME, new AppUsermapper());
    }

    @Override
    public int getIdByLogin(String name) {
        String GET_ID_BY_LOGIN = "select user_id from app_user where user_name='"+name+"';";
        return setupJdbcTemplate.queryForObject(GET_ID_BY_LOGIN, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("user_id");
            }
        });
    }
}
