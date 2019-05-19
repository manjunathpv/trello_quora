package com.upgrad.quora.service.exception;

import com.upgrad.quora.service.common.EndPoint;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

/**
 * Method to provide service for validating a user authentication token through a access token
 */
@Service
public class UserAuthTokenValidifierService implements EndPoint {


    @Autowired
    UserDao userDao;

    /**
     * @param  authorizationToken the first {@code String} to check if the access is available.
     * @return true or false
     */
    public boolean userSignOutStatus(String authorizationToken) {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserByAccessToken(authorizationToken);
        ZonedDateTime loggedOutStatus = userAuthTokenEntity.getLogoutAt();
        ZonedDateTime loggedInStatus = userAuthTokenEntity.getLoginAt();
        if (loggedOutStatus != null && loggedOutStatus.isAfter(loggedInStatus)) {
            return true;
        } else return false;
    }

}