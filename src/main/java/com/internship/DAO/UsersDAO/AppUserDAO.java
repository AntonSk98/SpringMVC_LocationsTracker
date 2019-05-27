package com.internship.DAO.UsersDAO;

import com.internship.models.userDetailModels.AppUser;

public interface AppUserDAO {
    AppUser getUserByName(String name);
    int getIdByLogin(String name);
}
