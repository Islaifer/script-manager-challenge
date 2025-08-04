package com.cooper_filme.shared_model.repository;

import com.cooper_filme.shared_model.model.entity.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
