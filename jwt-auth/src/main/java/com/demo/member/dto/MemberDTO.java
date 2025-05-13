package com.demo.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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
@ToString(callSuper = true)
public class MemberDTO {
    private String uuid;

    private String username;
    private String userPswd;

    // token 시간
    private long accessExp;
    private long refreshExp;

}
