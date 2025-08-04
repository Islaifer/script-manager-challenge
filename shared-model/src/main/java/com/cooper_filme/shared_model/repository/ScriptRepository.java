package com.cooper_filme.shared_model.repository;

import com.cooper_filme.shared_model.model.entity.Script;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface ScriptRepository extends CrudRepository<Script, Long> {
    Optional<Set<Script>> findByClientFullNameAndClientEmailAndClientPhone(String clientFullName, String clientEmail, String clientPhone);
}

