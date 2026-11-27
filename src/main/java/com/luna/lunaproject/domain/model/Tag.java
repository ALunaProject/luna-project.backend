package com.luna.lunaproject.domain.model;

/**
 * Tag de jogo (mesmos ids da Steam). Um record basta: o catálogo é estático,
 * então não existe tabela de tags. No banco só ficam os ids (tabela post_tags).
 */
public record Tag(Integer id, String name) {
}
