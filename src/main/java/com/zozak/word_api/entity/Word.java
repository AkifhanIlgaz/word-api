package com.zozak.word_api.entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "words")
@Data
public class Word {
    @Id
    private String id;

    private String word;

    private String source;

    private Header header;

    private List<Definition> definitions;

    private List<Idiom> idioms;
}

@Data
class Header {
    private Audio audio;

    private String partOfSpeech;

    private String CefrLevel;
}

@Data
class Audio {
    private String UK;

    private String US;
}

@Data
class Definition {
    private String meaning;

    private List<String> examples;
}

@Data
class Idiom {
    private String usage;

    private List<Definition> definitions;
}