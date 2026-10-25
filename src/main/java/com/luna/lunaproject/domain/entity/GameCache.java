package com.luna.lunaproject.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
public class GameCache {
    @Id
    @Column(name = "appid")
    private Integer appid;
}