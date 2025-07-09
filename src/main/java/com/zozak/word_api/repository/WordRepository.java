package com.zozak.word_api.repository;

import com.zozak.word_api.entity.Word;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

public interface WordRepository extends MongoRepository<Word, String> {
    @Query("{word:'?0'}")
    Word findByWord(String word);
}
