package com.demo.member.repository;

import com.demo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * com.demo.member.repository
 *
 * @author : idasom
 * @data : 5/4/25
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    List<Member> findAllByOrderByUpdateDt();

    Optional<Member> findByUsernameAndDelYn(String username, String delYn);
}
