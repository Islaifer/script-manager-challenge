package com.cooper_filme.shared_model.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Role other)
            return Objects.equals(id, other.id);

        return false;
    }

    @Override
    public String toString() {
        return "Id: " + id + " Name: " + name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
