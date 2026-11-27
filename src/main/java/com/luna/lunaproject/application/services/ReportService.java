package com.luna.lunaproject.application.services;

import com.luna.lunaproject.application.dto.reports.ReportRequestDTO;
import com.luna.lunaproject.application.dto.reports.ReportResponseDTO;
import com.luna.lunaproject.application.dto.reports.ReportTargetResolver;
import com.luna.lunaproject.application.exceptions.BusinessRuleException;
import com.luna.lunaproject.domain.entity.Report;
import com.luna.lunaproject.domain.entity.User;
import com.luna.lunaproject.domain.enums.ReportTargetType;
import com.luna.lunaproject.domain.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportTargetResolver targetResolver;

    @Transactional
    public ReportResponseDTO create(ReportTargetType type, String rawTargetId, ReportRequestDTO dto) {
        User author = getAuthenticatedUser();
        var target = targetResolver.resolve(type, rawTargetId);

        if (target.ownerId().equals(author.getId())) {
            throw new BusinessRuleException("Você não pode denunciar o seu próprio conteúdo");
        }

        Report report = Report.builder()
                .description(dto.getDescription().trim())
                .author(author)
                .targetType(type)
                .targetId(target.targetId())
                .build();

        return toResponseDto(reportRepository.save(report));
    }

    @Transactional
    public String delete(UUID reportId) {
        Report  report = reportRepository.findById(reportId).orElse(null);
        assert report != null;
        reportRepository.delete(report);
        return "Report deleted successfully";
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private ReportResponseDTO toResponseDto(Report r) {
        return ReportResponseDTO.builder()
                .id(r.getId())
                .description(r.getDescription())
                .userId(r.getAuthor().getId())
                .username(r.getAuthor().getUsername())
                .targetType(r.getTargetType().name())
                .targetId(r.getTargetId())
                .creationDate(r.getCreationDate())
                .build();
    }
}