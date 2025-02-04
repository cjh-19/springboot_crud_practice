package jpaRefactoring.movie.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "jpa_movieposters")
@Data
public class MoviePosterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long posterId;

    @Column(nullable = false)
    private String posterUrl;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;
}
