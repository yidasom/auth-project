package com.demo.member.service.impl;

import com.demo.member.entity.Member;
import com.demo.member.repository.MemberRepository;
import com.demo.member.service.MemberService;
import com.sample.jwt.dto.UserDTO;
import com.sample.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDTO updateUser(UserDTO model) throws Exception {
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
            String endPassword = String.valueOf(SecurityUtil.encryptPassword(model.getUsername() + "qweasdzxc"));
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
}
