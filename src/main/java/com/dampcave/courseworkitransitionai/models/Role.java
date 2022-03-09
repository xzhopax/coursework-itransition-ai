package com.dampcave.courseworkitransitionai.models;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name")
    private String name;


    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}
