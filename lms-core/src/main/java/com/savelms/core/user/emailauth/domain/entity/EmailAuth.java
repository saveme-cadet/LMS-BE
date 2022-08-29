package com.savelms.core.user.emailauth.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class EmailAuth {

    //2일
    private static final Long MAX_EXPIRE_TIME = 60L * 24L * 2L;
    public static final String HOST = "https://www.savvemecadet.click";
    public static final String EMAILAUTHPATH = "/api/auth/email";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String authToken;
    private Boolean expired;
    private LocalDateTime expireDate;

    public EmailAuth(String email, String authToken, Boolean expired) {
        this.email = email;
        this.authToken = authToken;
        this.expired = expired;
        this.expireDate = LocalDateTime.now().plusMinutes(MAX_EXPIRE_TIME);
    }

    public static EmailAuth createEmailAuth(String email, String authToken) {
        return EmailAuth.builder()
            .email(email)
            .authToken(authToken)
            .expired(false)
            .expireDate(LocalDateTime.now().plusMinutes(MAX_EXPIRE_TIME))
            .build();
    }


    public void useToken() {
        this.expired = true;
    }

}