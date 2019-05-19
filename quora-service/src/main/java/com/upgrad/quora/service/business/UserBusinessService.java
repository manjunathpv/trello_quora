package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class UserBusinessService {

  @Autowired
  private UserDao userDao;

  @Autowired
  private PasswordCryptographyProvider cryptographyProvider;

  //User controller will use this method and validate the user sign up process
  @Transactional(propagation = Propagation.REQUIRED)
  public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
    UserEntity entity = userDao.getUserByUsername(userEntity.getUsername());
    if(entity != null){
      throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
    }
    entity = userDao.getUserByEmail(userEntity.getEmail());
    if(entity != null){
      throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
    }
    String[] encryptedText = cryptographyProvider.encrypt(userEntity.getPassword());
    userEntity.setSalt(encryptedText[0]);
    userEntity.setPassword(encryptedText[1]);
    return userDao.createUser(userEntity);
  }

  //User controller will use this method authenticate after the user sign up
  @Transactional(propagation = Propagation.REQUIRED)
  public UserAuthTokenEntity authenticate(final String username, final String password) throws AuthenticationFailedException {

    UserEntity userEntity = userDao.getUserByUsername(username);
    if(userEntity == null){
      throw new AuthenticationFailedException("ATH-001", "This username does not exist");
    }
    final String encryptedPassword = cryptographyProvider.encrypt(password, userEntity.getSalt());
    if(encryptedPassword.equals(userEntity.getPassword())){
      JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
      UserAuthTokenEntity userAuthTokenEntity = new UserAuthTokenEntity();

      userAuthTokenEntity.setUuid(userEntity.getUuid());
      userAuthTokenEntity.setUser(userEntity);

      final ZonedDateTime now = ZonedDateTime.now();
      final ZonedDateTime expiresAt = now.plusHours(8);
      userAuthTokenEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));

      userAuthTokenEntity.setLoginAt(now);
      userAuthTokenEntity.setExpiresAt(expiresAt);

      userDao.createAuthToken(userAuthTokenEntity);

      return userAuthTokenEntity;

    }else{
      throw new AuthenticationFailedException("ATH-002", "Password Failed");
    }
  }

  //User controller will make use of the sign out after validation with the access token
  @Transactional(propagation = Propagation.REQUIRED)
  public UserAuthTokenEntity signout(String accessToken) throws SignOutRestrictedException {

    UserAuthTokenEntity userAuthTokenEntity = userDao.getUserByAccessToken(accessToken);

    if(userAuthTokenEntity == null){
      throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
    }


    userAuthTokenEntity.setLogoutAt(ZonedDateTime.now());
    userDao.updateUserAuthToken(userAuthTokenEntity);

    return userAuthTokenEntity;
  }

  //This method will return the controller or any service class the user with request of user id
  public UserEntity getUser(String userId, String accessToken) throws AuthorizationFailedException, UserNotFoundException {

     UserAuthTokenEntity userAuthToken = userDao.getUserByAccessToken(accessToken);

     if (userAuthToken == null) {
       throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
     } else if (userAuthToken.getLogoutAt() != null) {
       throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
     } else {
       UserEntity userEntity = userDao.getUser(userId);
       if(userEntity == null){
         throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
       }
       return userEntity;
     }
  }

}
