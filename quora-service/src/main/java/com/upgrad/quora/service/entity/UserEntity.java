package com.upgrad.quora.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "users")
@NamedQueries(
        {
                @NamedQuery(name = "userByUuid", query = "select u from UserEntity u where u.uuid = :uuid"),
                @NamedQuery(name = "userByEmail", query = "from UserEntity u where u.email =:email"),
                @NamedQuery(name = "userByUsername", query = "select u from UserEntity u where u.username =:username"),
                @NamedQuery(name = "deleteUserByUuid", query = "delete from UserEntity u where u.uuid = :uuid")
        }
)


public class UserEntity implements Serializable {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Integer id;

  @Column(name = "UUID")
  @Size(max = 200)
  private String uuid;

  @Column(name = "FIRSTNAME")
  @NotNull
  @Size(max = 30)
  private String firstname;

  @Column(name = "LASTNAME")
  @NotNull
  @Size(max = 30)
  private String lastname;

  @Column(name = "USERNAME")
  @NotNull
  @Size(max = 30)
  private String username;

  @Column(name = "EMAIL")
  @NotNull
  @Size(max = 50)
  private String email;

  @Column(name = "PASSWORD")
  @NotNull
  @Size(max = 255)
  private String password;

  @Column(name = "SALT")
  @NotNull
  @Size(max = 200)
  //@ToStringExclude
  private String salt;

  @Column(name = "COUNTRY")
  @NotNull
  @Size(max = 30)
  private String country;

  @Column(name = "ABOUTME")
  @NotNull
  @Size(max = 50)
  private String aboutme;

  @Column(name = "DOB")
  @NotNull
  @Size(max = 30)
  private String dob;

  @Column(name = "ROLE")
  @Size(max = 30)
  private String role;

  @Column(name = "CONTACTNUMBER")
  @NotNull
  @Size(max = 30)
  private String contactnumber;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getAboutme() {
    return aboutme;
  }

  public void setAboutme(String aboutme) {
    this.aboutme = aboutme;
  }

  public String getDob() {
    return dob;
  }

  public void setDob(String dob) {
    this.dob = dob;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getContactnumber() {
    return contactnumber;
  }

  public void setContactnumber(String contactnumber) {
    this.contactnumber = contactnumber;
  }

  @Override
  public boolean equals(Object obj) {
    return new EqualsBuilder().append(this, obj).isEquals();
  }


  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this).hashCode();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }


}