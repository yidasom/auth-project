package com.demo.member.service;

import com.demo.member.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * com.demo.member.service
 *
 * @author : idasom
 * @data : 5/3/25
 */
public interface MemberService {
    MemberDTO updateUser(MemberDTO model) throws Exception;

    void updateAfterUserLoginSuccess(String uuid, String s, HttpServletRequest request) throws Exception;

    UserDetails loadUserByUsername(String id);
}
