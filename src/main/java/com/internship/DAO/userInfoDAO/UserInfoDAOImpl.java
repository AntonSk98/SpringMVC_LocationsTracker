package com.internship.DAO.userInfoDAO;

import com.internship.models.userInfoModel.UserInfo;
import com.internship.models.userInfoModel.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserInfoDAOImpl implements UserInfoDAO {
    @Autowired
    JdbcTemplate setupJdbcTemplate;
    @Override
    public UserInfo findByLogin(String login) {
        UserInfo user;
        String GET_USER_BY_LOGIN = "select * from user_info where login = '"+login+"';";
        try{
            user = setupJdbcTemplate.queryForObject(GET_USER_BY_LOGIN, new UserInfoMapper());
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return user;
    }

    @Override
    public boolean updateByLogin(String login, UserInfo userInfo) {
        String SQL_UPDATE="UPDATE user_info SET name=?, surname=?, age=? WHERE login=?;";
        setupJdbcTemplate.update(SQL_UPDATE, userInfo.getName(), userInfo.getSurname(), userInfo.getAge(), login);
        return true;
    }
}
