package com.internship.services;

import com.internship.DAO.UsersDAO.AppUserDAO;
import com.internship.models.userDetailModels.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {
    @Autowired
    private AppUserDAO appUserDAO;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserDAO.getUserByName(username);
        if(appUser==null)
            throw new UsernameNotFoundException("User "+appUser+"wasn't fount in the database!");
        return new User(appUser.getUserName(), appUser.getEncryptedPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))); //возвращаем пользователя с логином и паролем и даём ему роль юзера

    }
}
