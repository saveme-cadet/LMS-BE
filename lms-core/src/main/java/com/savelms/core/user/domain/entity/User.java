package com.savelms.core.user.domain.entity;


import com.savelms.core.BaseEntity;
import com.savelms.core.team.domain.entity.UserTeam;
import com.savelms.core.todo.domain.entity.Todo;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.role.domain.entity.Role;
import com.savelms.core.user.role.domain.entity.UserRole;
import com.savelms.core.vacation.domain.entity.Vacation;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
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
@Builder()
public class User extends BaseEntity implements UserDetails, CredentialsContainer, Serializable {

    //********************************* static final 상수 필드 *********************************/

    /**
     * email 뒤에 붙는 문자열
     */
    public static final String EMAILSUFFIX = "@student.42seoul.kr";


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


    private String refreshToken;
    @Builder.Default
    @Column(nullable = false)
    private Boolean emailAuth = false;

    @Column(nullable = false)
    private String apiId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private AttendStatus attendStatus = AttendStatus.PARTICIPATED;
    /********************************* 비영속 필드 *********************************/

    /********************************* 연관관계 매핑 *********************************/


    /**
     * role
     */
    @Singular
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    private final Set<UserRole>
        userRoles = new HashSet<>();


    @Singular
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST )
    private final List<UserTeam> userTeams = new ArrayList<>();

    @Singular
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY )
    private final List<Todo> todos = new ArrayList<>();

    @Singular
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY )
    private final List<Vacation> vacations = new ArrayList<>();

    /********************************* 연관관계 편의 메서드 *********************************/

    /********************************* 생성 메서드 *********************************/

    public static User createDefaultUser(String username, String encodedPassword, String email) {
        return User.builder()
            .username(username)
            .password(encodedPassword)
            .email(email)
            .nickname(username)
            .apiId(UUID.randomUUID().toString())
            .build();
    }
    /********************************* 비니지스 로직 *********************************/

    /**
     *
     * @return
     */
    public Set<SimpleGrantedAuthority> getAuthorities() {
        return userRoles.stream()
            .filter(UserRole::getCurrentlyUsed)
            .map((UserRole::getRole))
            .map(Role::getAuthorities)
            .flatMap(Set::stream)
            .map(authority ->
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


    public void setNewCurrentlyUsedUserTeam(UserTeam newUserTeam) {
        newUserTeam.setUserAndAddUserTeamToUser(this);
    }

    public void originalUserTeamsCurrentlyUsedToFalse(List<UserTeam> originalUserTeams) {
        originalUserTeams.forEach(UserTeam::notCurrentlyUsed);
    }


    public void changeAttendStatus(AttendStatus attendStatus) {
        this.attendStatus = attendStatus;
    }

    public void setNewCurrentlyUsedUserRole(UserRole newUserRole) {
        newUserRole.setUserAndAddUserRoleToUser(this);
    }

    public void originalUserRolesCurrentlyUsedToFalse(List<UserRole> originalUserRoles) {
        originalUserRoles.forEach(UserRole::notCurrentlyUsed);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void emailVerifiedSuccess() {
        this.emailAuth = true;
    }

    public static String getRamdomPassword(int size) {
        char[] charSet = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '!', '@', '#', '$', '%', '^', '&' };

        StringBuffer sb = new StringBuffer();
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());

        int idx = 0;
        int len = charSet.length;
        for (int i=0; i<size; i++) {
            // idx = (int) (len * Math.random());
            idx = sr.nextInt(len);    // 강력한 난수를 발생시키기 위해 SecureRandom을 사용한다.
            sb.append(charSet[idx]);
        }

        return sb.toString();
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }


}
