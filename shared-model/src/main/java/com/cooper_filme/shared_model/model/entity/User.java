package com.cooper_filme.shared_model.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    @ManyToMany
    private Set<Role> roles;

    @OneToMany(mappedBy = "actualUser")
    private Set<Script> scripts;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User other)
            return Objects.equals(id, other.id);

        return false;
    }

    @Override
    public String toString() {
        return "Id: " + id + " Name: " + name + " Email: " + email;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}
