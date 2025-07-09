package com.zozak.word_api.controller;

import com.zozak.word_api.entity.Word;
import com.zozak.word_api.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/word")
class WordController {
    @Autowired
    private WordRepository wordRepository;

    @GetMapping("/{id}")
    public Word getWord(@PathVariable(name = "id") String id) {
        System.out.println("sdfsdfsdfsdf");
        System.out.println(id);
        return wordRepository.findById(id).orElseThrow();
    }
}
