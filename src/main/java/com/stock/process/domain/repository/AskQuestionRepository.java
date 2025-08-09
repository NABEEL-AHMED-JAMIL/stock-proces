package com.stock.process.domain.repository;

import com.stock.process.domain.model.AskQuestion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface AskQuestionRepository extends CrudRepository<AskQuestion, Long> {

    public List<AskQuestion> findByIsActiveTrue();

}
