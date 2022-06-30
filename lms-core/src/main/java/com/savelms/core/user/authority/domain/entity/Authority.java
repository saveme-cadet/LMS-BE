package com.savelms.core.user.authority.domain.entity;

import com.savelms.core.user.role.domain.entity.Role;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authority implements Serializable {

    @Id
    @GeneratedValue
    @Column(name="AUTHORITY_ID")
    private Long id;

    @Column(nullable = false)
    private String permission;

    @Singular
    @ManyToMany(mappedBy = "authorities", fetch = FetchType.EAGER)
    private final Set<Role> roles = new HashSet<>();
}
