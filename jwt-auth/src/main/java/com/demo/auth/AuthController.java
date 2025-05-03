package com.demo.auth;

import com.demo.auth.dto.LoginDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * com.demo.auth
 *
 * @author : idasom
 * @data : 5/3/25
 */
@RestController
@RequestMapping(value = "/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDTO loginDTO) throws Exception {
        return ResponseEntity.ok().build();
    }

}
