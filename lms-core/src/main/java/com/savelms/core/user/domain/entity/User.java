package com.savelms.core.user.domain.entity;


import com.savelms.core.BaseEntity;
import com.savelms.core.user.role.domain.entity.UserRole;
import com.savelms.core.team.domain.entity.UserTeam;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "USER", uniqueConstraints = {
    @UniqueConstraint(name = "USERNAME_UNIQUE", columnNames = {"username"}),
    @UniqueConstraint(name = "NICK_NAME_UNIQUE", columnNames = {"nickname"})
})

@Entity
@Builder
public class User extends BaseEntity implements UserDetails, CredentialsContainer {

    //********************************* static final 상수 필드 *********************************/

    /**
     * email 뒤에 붙는 문자열
     */
    private static final String emailSuffix = "@student.42seoul.kr";

    /********************************* PK 필드 *********************************/

    /**
     * 기본 키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/

    /**
     * AUTH에 필요한 필드
     */

    @Column(unique = true, nullable = false, updatable = false, length = 30)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 20)
    private String nickname;

    @Builder.Default
    @Column(nullable = false)
    private Boolean accountNonExpired = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean accountNonLocked = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean credentialsNonExpired = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(nullable = false, length = 80)
    private String email;

    @Column(nullable = false)
    private String apiId;
    /********************************* 비영속 필드 *********************************/

    /********************************* 연관관계 매핑 *********************************/


    /**
     * role
     */
    @Singular
    @OneToMany(mappedBy = "user")
    private final Set<UserRole> userRoles = new HashSet<>();

    @Singular
    @OneToMany(mappedBy = "user")
    private final Set<UserTeam> userTeams = new HashSet<>();

    /********************************* 비니지스 로직 *********************************/

    /**
     *
     * @return
     */
    public Set<SimpleGrantedAuthority> getAuthorities() {
        return userRoles.stream()
            .map((userRole -> userRole.getRole()))
            .map((role) ->
                role.getAuthorities())
            .flatMap(Set::stream)
            .map((authority) ->
                new SimpleGrantedAuthority(authority.getPermission()))
            .collect(Collectors.toSet());
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public static User createDefaultUser(String username, String encodedPassword, String email) {
        return User.builder()
            .username(username)
            .password(encodedPassword)
            .email(email)
            .nickname(username)
            .apiId(UUID.randomUUID().toString())
            .build();
    }
    /********************************* 연관관계 편의 메서드 *********************************/

}
