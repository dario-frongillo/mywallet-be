package it.italian.coders.service.authentication.impl;

import it.italian.coders.dao.authentication.UserDao;
import it.italian.coders.model.authentication.User;
import it.italian.coders.service.authentication.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManagerImpl implements UserManager {

    @Autowired
    private UserDao userDao;

    @Override
    public User findByUsernameIgnoreCase(String username) {
        return userDao.findByUsernameIgnoreCase(username);
    }

    @Override
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmailIgnoreCase(email);
    }


}
