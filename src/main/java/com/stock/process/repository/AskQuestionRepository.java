package com.stock.process.repository;

import com.stock.process.model.AskQuestion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface AskQuestionRepository extends CrudRepository<AskQuestion, Long> {
}
