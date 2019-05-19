package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

  @Autowired
  private UserBusinessService userBusinessService;


  @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserDetailsResponse> userProfile(@PathVariable("userId") final String userId, @RequestHeader("access-token") final String accessToken) throws AuthorizationFailedException, UserNotFoundException {
    UserEntity userEntity = userBusinessService.getUser(userId, accessToken);

    UserDetailsResponse userDetailsResponse = new UserDetailsResponse().firstName(userEntity.getFirstname())
            .lastName(userEntity.getLastname())
            .userName(userEntity.getUsername())
            .emailAddress(userEntity.getEmail())
            .country(userEntity.getCountry())
            .aboutMe(userEntity.getAboutme())
            .dob(userEntity.getDob())
            .contactNumber(userEntity.getContactnumber());

    return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
  }
}
