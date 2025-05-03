package com.sample.jwt.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * com.demo.jwt.dto
 *
 * @author : idasom
 * @data : 4/28/25
 */
@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
public class UserDTO {
    private String uuid;

    private String username;
    private String userPswd;

    // token 시간
    private long accessExp;
    private long refreshExp;
}
