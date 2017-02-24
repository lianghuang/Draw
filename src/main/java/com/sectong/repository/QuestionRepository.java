package com.sectong.repository;

import com.sectong.domain.Question;
import com.sectong.domain.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by huangliangliang on 2/19/17.
 */
@RestResource(exported = false)
public interface QuestionRepository extends CrudRepository<Question, String> {

}
