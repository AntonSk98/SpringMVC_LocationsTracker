package com.internship.DAO.userInfoDAO;

import com.internship.models.userInfoModel.UserInfo;

public interface UserInfoDAO {
    UserInfo findByLogin(String login);
    boolean updateByLogin(String login, UserInfo userInfo);
}
