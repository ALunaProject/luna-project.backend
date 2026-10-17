package com.luna.lunaproject.application.exceptions;

import java.util.Set;

/**
 * ADICIONAR AQUI NOVAS ROTAS CASO TENHA!!
 * Usernames que colidem com rotas do front: a rota /[username] disputa a URL com elas
 * (um usuário "games" deixaria o perfil inacessível, porque /games abre a página de jogos).
 */
public final class ReservedUsernames {

    private static final Set<String> RESERVED = Set.of(
            "home",
            "login",
            "signin",
            "signup",
            "register",
            "logout",
            "auth",
            "api",
            "games",
            "game",
            "game-purpose",
            "news",
            "discovery",
            "lfg-posts",
            "community",
            "profile",
            "settings",
            "search",
            "explore",
            "posts",
            "post",
            "comments",
            "lists",
            "notifications",
            "messages",
            "users",
            "user",
            "me",
            "admin",
            "about",
            "terms",
            "privacy",
            "help",
            "support",
            "_next",
            "static",
            "public",
            "assets",
            "mocks",
            "favicon.ico",
            "null",
            "undefined"
    );

    private ReservedUsernames() {
    }

    // compara sem diferenciar maiúscula/minúscula ("Login" também é bloqueado)
    public static boolean isReserved(String username) {
        return username != null && RESERVED.contains(username.trim().toLowerCase());
    }
}
