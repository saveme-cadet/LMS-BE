package com.savelms.core.user.domain.entity;

import com.savelms.core.user.domain.entity.Role;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authority {

    @Id
    @GeneratedValue
    @Column(name="AUTHORITY_ID")
    private Long id;

    @Column(nullable = false)
    private String permission;

    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles;
}
