package com.ohgiraffers.restapi.section04.hateoas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {
  private int no;
  private String id;
  private String pwd;
  private String name;
  private Date enrollDate;

}
