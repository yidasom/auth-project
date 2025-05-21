package com.demo.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * com.demo.member.dto
 *
 * @author : idasom
 * @data : 5/11/25
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MemberDTO implements Serializable {
    private String uuid;

    private String username;
    private String userPswd;

    // token 시간
    private long accessExp;
    private long refreshExp;

}
