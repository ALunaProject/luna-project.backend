package com.luna.lunaproject.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "news")
@Getter
@Setter
@NoArgsConstructor
public class NewsCache {

    @Id
    @Column(name = "id", length = 512)
    private String id;

    public NewsCache(String id) {
        this.id = id;
    }
}
