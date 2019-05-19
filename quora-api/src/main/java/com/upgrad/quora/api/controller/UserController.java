package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {
  //to enable access to methods defined in respective Business services
  @Autowired
  private UserBusinessService userBusinessService;

  /**
   * @param  signupUserRequest the first {@code SignupUserRequest} to signup a particular user with details.
   * @return ResponseEntity is returned with Status CREATED.
   */
  @RequestMapping(method = RequestMethod.POST, path = "/user/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {

    final UserEntity userEntity = new UserEntity();

    userEntity.setUuid(UUID.randomUUID().toString());
    userEntity.setFirstname(signupUserRequest.getFirstName());
    userEntity.setLastname(signupUserRequest.getLastName());
    userEntity.setUsername(signupUserRequest.getUserName());
    userEntity.setEmail(signupUserRequest.getEmailAddress());
    userEntity.setPassword(signupUserRequest.getPassword());
    userEntity.setCountry(signupUserRequest.getCountry());
    userEntity.setAboutme(signupUserRequest.getAboutMe());
    userEntity.setDob(signupUserRequest.getDob());
    userEntity.setRole("nonadmin");
    userEntity.setContactnumber(signupUserRequest.getContactNumber());

    final UserEntity createdUserEntity = userBusinessService.signup(userEntity);
    SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");

    return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
  }
/**
   * @param  authorization the first {@code String} to signin a particular user and check access.
   * @return ResponseEntity is returned with Status OK.
   */
  @RequestMapping(method = RequestMethod.POST, path = "/user/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SigninResponse> signin(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
    byte[] decode = Base64.getDecoder().decode(authorization);
    String decodeText = new String(decode);
    String[] decodedArray = decodeText.split(":");

    UserAuthTokenEntity userAuthToken = userBusinessService.authenticate(decodedArray[0], decodedArray[1]);
    UserEntity userEntity = userAuthToken.getUser();

    SigninResponse authorizedUserResponse = new SigninResponse().id(userEntity.getUuid()).message("SIGNED IN SUCCESSFULLY");

    HttpHeaders headers = new HttpHeaders();
    headers.add("access_token", userAuthToken.getAccessToken());

    return new ResponseEntity<SigninResponse>(authorizedUserResponse, headers, HttpStatus.OK);
  }

  /**
   * @param  accessToken the first {@code String} to signout a particular user.
   * @return ResponseEntity is returned with Status OK.
   */
  @RequestMapping(method = RequestMethod.POST, path = "/user/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization") final String accessToken) throws SignOutRestrictedException {
    UserAuthTokenEntity userAuthTokenEntity = userBusinessService.signout(accessToken);

    SignoutResponse signedOutSuccessfully = new SignoutResponse().id(userAuthTokenEntity.getUuid()).message("SIGNED OUT SUCCESSFULLY");

    return new ResponseEntity<SignoutResponse>(signedOutSuccessfully, HttpStatus.OK);
  }
}
