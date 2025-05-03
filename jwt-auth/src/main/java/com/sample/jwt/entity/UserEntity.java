package com.sample.jwt.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * com.demo.jwt.entity
 *
 * @author : idasom
 * @data : 4/28/25
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Access(AccessType.FIELD)
public class UserEntity implements Serializable {
    @Id
    private long userSeq;

    private String userId;
    private String userNm;
    private String userPswd;

}
