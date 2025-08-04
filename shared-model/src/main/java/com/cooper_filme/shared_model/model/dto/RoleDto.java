package com.cooper_filme.shared_model.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleDto {
    private String name;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RoleDto other)
            return Objects.equals(name, other.name);

        return false;
    }

    @Override
    public String toString() {
        return "Name: " + name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
