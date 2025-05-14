package com.demo.member.service.impl;

import com.demo.auth.dto.LoginDTO;
import com.demo.member.dto.MemberDTO;
import com.demo.member.entity.Member;
import com.demo.member.repository.MemberRepository;
import com.demo.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

/**
 * com.demo.member.service.impl
 *
 * @author : idasom
 * @data : 5/3/25
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;
    private ModelMapper modelMapper;


    /**
     * 비밀번호를 암호화하는 기능(복호화가 되면 안되므로 SHA-256 인코딩 방식 적용)
     *
     * @param data 암호화할 비밀번호
     * @return 암호화된 비밀번호
     * @throws Exception
     */
    public String encryptPassword(String data) throws Exception {
        if (data == null) {
            return "";
        }
        byte[] plainText = null;
        byte[] hashValue = null;

        MessageDigest md;
        plainText = data.getBytes("UTF-8");
        md = MessageDigest.getInstance("SHA-256");
        hashValue = md.digest(plainText);
        return new String(Base64.getEncoder().encode(hashValue));
    }

    @Override
    public MemberDTO updateUser(MemberDTO model) throws Exception {
        Member member = null;
        // DB 조회
        Optional<Member> optionalUser = memberRepository.findById(model.getUuid());

        if (optionalUser.isPresent()) {
            // 수정
            member = optionalUser.get();

            modelMapper.map(model, member);

            // 저장
            memberRepository.save(member);
        } else {
            // 등록
            Optional<Member> existUserId = memberRepository.findByUsernameAndDelYn(model.getUsername(), "N");
            if (existUserId.isPresent()) {
                throw new Exception("이미 존재하는 사용자입니다.");
            }
            //사용자 순번 채번
            String newUUID = String.valueOf(UUID.randomUUID());

            member = new Member();
            model.setUuid(newUUID);
            String endPassword = String.valueOf(encryptPassword(model.getUsername() + "qweasdzxc"));
            log.debug(" User Insert {} ", model);

            model.setUserPswd(endPassword);
            modelMapper.map(model, member);

            memberRepository.save(member);
        }
        modelMapper.map(member, model);
        // 화면으로 비밀번호는 던지지 않도록
        model.setUserPswd(null);
        return model;
    }

    @Transactional(value = "transactionManager")
    public void updateAfterUserLoginSuccess(String uuid, String s, HttpServletRequest request) throws Exception {
        Member member = memberRepository.findById(uuid).get();
        memberRepository.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        LoginDTO loginDTO = null;
        Member principal = (Member) memberRepository.findById(username)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + username);
                });
        return loginDTO;
    }
}
