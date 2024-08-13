package com.jygoh.whoever.domain.hashtag.dto;

import com.jygoh.whoever.domain.hashtag.model.Hashtag;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HashtagDto {

    private Long id;
    private String name;

    public HashtagDto(Hashtag hashtag) {
        this.id = hashtag.getId();
        this.name = hashtag.getName();
    }
}