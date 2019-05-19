package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

  @PersistenceContext
  private EntityManager entityManager;

  public UserEntity createUser(UserEntity userEntity){
    entityManager.persist(userEntity);
    return userEntity;
  }

  public UserEntity getUserByUuid(final String uuid) {
    try {
      return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", uuid).getSingleResult();
    } catch (NoResultException nre) {

      return null;
    }

  }

  public UserEntity getUserByEmail(final String email){
    try {
      return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
    }
    catch (NoResultException nre){
      return null;
    }
  }

  public UserEntity getUserByUsername(final String username){
    try {
      return entityManager.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", username).getSingleResult();
    }
    catch (NoResultException nre){
      return null;
    }
  }

  public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity){
    entityManager.persist(userAuthTokenEntity);
    return userAuthTokenEntity;
  }

  public UserAuthTokenEntity getUserByAccessToken(final String accessToken) {
    try {
      return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class).setParameter("accessToken", accessToken).getSingleResult();
    }
    catch (NoResultException nre){
      return null;
    }
  }

  public void updateUserAuthToken(UserAuthTokenEntity userAuthTokenEntity) {
    entityManager.merge(userAuthTokenEntity);
  }

  public UserEntity getUser(String userId) {
    try {
      return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", userId).getSingleResult();
    }catch (NoResultException nre){
      return null;
    }
  }

  public void deleteUser(String userId) {
    entityManager.createNamedQuery("deleteUserByUuid").setParameter("uuid", userId).executeUpdate();
  }
}
