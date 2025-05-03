package com.demo.member.service;

import com.sample.jwt.dto.UserDTO;

/**
 * com.demo.member.service
 *
 * @author : idasom
 * @data : 5/3/25
 */
public interface MemberService {
    UserDTO updateUser(UserDTO model) throws Exception;
}
