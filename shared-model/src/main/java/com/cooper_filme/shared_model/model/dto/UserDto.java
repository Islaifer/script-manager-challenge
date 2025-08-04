package com.cooper_filme.shared_model.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private String name;
    private String email;
    @JsonIgnore
    private List<RoleDto> roles;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UserDto other)
            return Objects.equals(name, other.name) && Objects.equals(email, other.email);

        return false;
    }

    @Override
    public String toString() {
        return "Name: " + name + " Email: " + email;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
