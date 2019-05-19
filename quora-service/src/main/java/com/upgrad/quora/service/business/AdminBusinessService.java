package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminBusinessService {

  @Autowired
  private UserDao userDao;

  @Transactional(propagation = Propagation.REQUIRED)
  public String getAdminUser(String userId, String accessToken) throws AuthorizationFailedException, UserNotFoundException {
    UserAuthTokenEntity userAuthToken = userDao.getUserByAccessToken(accessToken);

    if (userAuthToken == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuthToken.getLogoutAt() != null) {
      throw new AuthorizationFailedException("ATHR-002", "User is signed out");
    } else if (!(userAuthToken.getUser().getRole().equals("admin"))){
      throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
    } else {
      UserEntity userEntity = userDao.getUser(userId);
      if(userEntity == null){
        throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
      } else{
        userDao.deleteUser(userId);
        return userEntity.getUuid();
      }
    }
  }
}
