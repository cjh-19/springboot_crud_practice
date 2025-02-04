package jpaRefactoring.movie.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MovieListResponse {
    private Long movieId;
    private String title;
    private String director;
    private LocalDate releaseDate;
    private String genre;
    private double rating;
    private String description;
    private LocalDateTime createdAt;
}
