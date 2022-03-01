package com.dampcave.courseworkitransitionai.models;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name")
    private String Name;


    @Override
    public String toString() {
        return "Role{" +
                "Name='" + Name + '\'' +
                '}';
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}
