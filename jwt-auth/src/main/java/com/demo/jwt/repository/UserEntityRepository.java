package com.demo.jwt.repository;


import com.demo.jwt.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * com.demo.jwt.Repository
 *
 * @author : idasom
 * @data : 4/28/25
 */
@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    <T> ScopedValue<T> findByUserId(String username);
}
