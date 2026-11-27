package com.luna.lunaproject.application.services.helpers;

import com.luna.lunaproject.domain.enums.ReportTargetType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReportTargetTypeConverter implements Converter<String, ReportTargetType> {
    @Override
    public ReportTargetType convert(String source) {
        return ReportTargetType.fromSlug(source);
    }
}