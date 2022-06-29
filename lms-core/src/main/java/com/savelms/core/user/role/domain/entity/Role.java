package com.savelms.core.user.role.domain.entity;

import com.savelms.core.BaseEntity;
import com.savelms.core.user.authority.domain.entity.Authority;
import java.util.Arrays;
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
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="ROLE")
public class Role extends BaseEntity {

    //********************************* static final 상수 필드 *********************************/


    /********************************* PK 필드 *********************************/

    /**
     * 기본 키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ROLE_ID")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/
    @Column(nullable = false)
    private String name;

    /********************************* 비영속 필드 *********************************/

    /********************************* 연관관계 매핑 *********************************/

    @Singular
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "ROLE_AUTHORITY",
        joinColumns = {
            @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID", nullable = false)},
        inverseJoinColumns = {
            @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "AUTHORITY_ID", nullable = false)})
    private final Set<Authority> authorities = new HashSet<>();


    /********************************* 비니지스 로직 *********************************/

    public void addAuthorities(Authority... authorities) {
        this.authorities.addAll(Arrays.stream(authorities).collect(Collectors.toSet()));
    }


}
