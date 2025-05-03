package com.demo.member.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * com.demo.member.entity
 *
 * @author : idasom
 * @data : 5/3/25
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@ToString(callSuper = true)
@Access(AccessType.FIELD)
public class Member implements Serializable {
    @Id
    private String uuid;

    private String username;
    private String password;

    @CreatedDate
    private LocalDateTime createDt;
    @LastModifiedDate
    private LocalDateTime updateDt;
    @LastModifiedDate
    private LocalDateTime lastLgnDt;

    private String delYn;
}
