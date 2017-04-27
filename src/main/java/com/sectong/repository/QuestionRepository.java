package com.sectong.repository;

import com.sectong.domain.Question;
import com.sectong.domain.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by huangliangliang on 2/19/17.
 */
@RestResource(exported = false)
public interface QuestionRepository extends CrudRepository<Question, String> {

    @Query(value = "select * from Question q  where q.question=?1 and q.keyword1=?2 and q.keyword2=?3", nativeQuery = true)
    Question findQuestionByNameAndKey1Key2(String name,String key1,String key2);
}
