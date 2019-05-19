package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditQuestionBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;

    /**
     * @param  questionEntity the first {@code QuestionEntity} object to update stored question.
     * @param  authorizationToken the second {@code String} to check if the access is available.
     * @return QuestionEntity object is returned after updating in the database.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestionContent(final QuestionEntity questionEntity, final String authorizationToken) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthEntity = userDao.getUserByAccessToken(authorizationToken);

        // Validate if user is signed in or not using access token
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validate if user has signed out using access token
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
        }

        // Validate if requested question exist or not by question UUID
        QuestionEntity existingQuestionEntity = questionDao.getQuestionByQUuid(questionEntity.getUuid());
        if (existingQuestionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        // Validate if current user is the owner of requested question after getting the user
        UserEntity currentUser = userAuthEntity.getUser();
        UserEntity questionOwner = questionDao.getQuestionByQUuid(questionEntity.getUuid()).getUser();
        if (currentUser.getId() != questionOwner.getId()) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }

        questionEntity.setId(existingQuestionEntity.getId());
        questionEntity.setUser(existingQuestionEntity.getUser());
        questionEntity.setDate(existingQuestionEntity.getDate());

        return questionDao.updateQuestion(questionEntity);
    }
}
