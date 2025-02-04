package jpaRefactoring.movie.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MovieDto {
    private Long movieId;
    private String title;
    private String director;
    private LocalDate releaseDate;
    private String genre;
    private double rating;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<MoviePosterDto> moviePostersList; // 영화에 해당하는 포스터 리스트
}
