package com.cooper_filme.shared_model.repository;

import com.cooper_filme.shared_model.model.entity.Vote;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote, Long> {
}
