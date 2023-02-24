package com.deshin.springsecurityjwt.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "member", indexes = @Index(name = "idx__unique__user_id", columnList = "user_id", unique = true))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", columnDefinition = "varchar(50) comment '아이디'", nullable = false)
  private String userId;

  @Column(name = "password", columnDefinition = "varchar(100) comment '비밀번호'", nullable = false)
  private String password;

  @Column(name = "name", columnDefinition = "varchar(30) comment '이름'", nullable = false)
  private String name;


  @Column(name = "create_at", nullable = false, updatable = false, columnDefinition = "timestamp comment '가입일시'")
  @CreationTimestamp
  private LocalDateTime createAt;


  @Builder
  public Member(String userId, String password, String name) {
    this.userId = userId;
    this.password = password;
    this.name = name;
  }

}
