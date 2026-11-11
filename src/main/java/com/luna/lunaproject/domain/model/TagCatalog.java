package com.luna.lunaproject.domain.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luna.lunaproject.application.exceptions.BusinessRuleException;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Catálogo de tags carregado uma única vez de resources/steam-tags.json
 * (era o STEAM_TAG_NAMES que ficava no front, em utils/contants.ts).
 */
public final class TagCatalog {

    public static final int MAX_TAGS_PER_POST = 5;

    private static final List<Tag> TAGS = load();
    private static final Map<Integer, Tag> BY_ID = TAGS.stream()
            .collect(Collectors.toUnmodifiableMap(Tag::id, Function.identity(), (first, second) -> first));

    private TagCatalog() {
    }

    private static List<Tag> load() {
        try (InputStream in = new ClassPathResource("steam-tags.json").getInputStream()) {
            return List.copyOf(new ObjectMapper().readValue(in, new TypeReference<List<Tag>>() {
            }));
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível carregar steam-tags.json", e);
        }
    }

    public static List<Tag> all() {
        return TAGS;
    }

    public static Optional<Tag> find(Integer id) {
        return id == null ? Optional.empty() : Optional.ofNullable(BY_ID.get(id));
    }

    /** Ids -> tags, na ordem do catálogo (mais populares primeiro). Ids desconhecidos são ignorados. */
    public static List<Tag> resolve(Collection<Integer> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return TAGS.stream().filter(tag -> ids.contains(tag.id())).toList();
    }

    /** Valida os ids vindos do front (existem no catálogo, sem passar do limite) e devolve um Set novo. */
    public static Set<Integer> requireValid(Collection<Integer> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();

        Set<Integer> unique = new HashSet<>(ids);
        if (unique.size() > MAX_TAGS_PER_POST) {
            throw new BusinessRuleException("Um post pode ter no máximo " + MAX_TAGS_PER_POST + " tags");
        }
        for (Integer id : unique) {
            if (id == null || !BY_ID.containsKey(id)) {
                throw new BusinessRuleException("Tag inválida: " + id);
            }
        }
        return unique;
    }
}
