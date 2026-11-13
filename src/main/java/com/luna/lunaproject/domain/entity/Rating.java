package com.luna.lunaproject.domain.entity;

import com.luna.lunaproject.domain.enums.RatingTargetType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "ratings",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_rating_user_target",
                columnNames = {"user_id", "target_type", "target_id"}
        ),
        indexes = @Index(name = "idx_ratings_target", columnList = "target_type, target_id")
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    private RatingTargetType targetType;

    /** No caso de GAME, é o appId da Steam. */
    @Column(name = "target_id", nullable = false, length = 50)
    private String targetId;

    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal score;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedDate;
}