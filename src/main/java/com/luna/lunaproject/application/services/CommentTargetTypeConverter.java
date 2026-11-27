package com.luna.lunaproject.application.services;

import com.luna.lunaproject.domain.enums.CommentTargetType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommentTargetTypeConverter implements Converter<String, CommentTargetType> {
    @Override
    public CommentTargetType convert(String source) {
        return CommentTargetType.fromSlug(source); // tipo inválido vira 400 automaticamente
    }
}
