package com.savelms;


import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "USER")
@Entity
@Builder
public class User extends BaseEntity {

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

    private String username;
    private String password;
    private String nickName;

    @Builder.Default
    private Boolean accountNonExpired = true;
    @Builder.Default
    private Boolean accountNonLocked = true;
    @Builder.Default
    private Boolean credentialsNonExpired = true;
    @Builder.Default
    private Boolean enabled = true;

    private String email;

    /********************************* 비영속 필드 *********************************/

    /**
     * authorities
     */
    @Transient
    private Set<Authority> authorities = new HashSet<>();

    /********************************* 연관관계 매핑 *********************************/


    /**
     * role
     */
    @Singular
    @OneToMany(mappedBy = "user")
    private Set<UserRole> userRoles = new HashSet<>();

    @Singular
    @OneToMany(mappedBy = "user")
    private Set<UserTeam> userTeams = new HashSet<>();

    /********************************* 비니지스 로직 *********************************/

//    /**
//     * 편의용 getter
//     * @return
//     */
//    public Set<Authority> getAuthorities() {
//        return roles.stream()
//            .map((role) ->
//                role.getAuthorities())
//            .flatMap(Set::stream)
//            .collect(Collectors.toSet());
//    }
//
//
//    /**
//     * 첫 회원가입시 디폴트 상태 정의
//     * @param defaultRole
//     * @param encodedPassword
//     */
//    public void setDefaultJoinStatus(Role defaultRole, String encodedPassword) {
//
//        roles.add(defaultRole);
//        password = password;
//        nickName = username;
//        email = username + User.emailSuffix;
//    }
}
