package com.demo.member.controller;

import com.demo.member.service.MemberService;
import com.sample.jwt.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * com.demo.member.controller
 *
 * @author : idasom
 * @data : 5/3/25
 */
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/regist")
    public ResponseEntity<?> regist(@RequestBody UserDTO model) throws Exception {
        return ResponseEntity.ok(memberService.updateUser(model));
    }
}
