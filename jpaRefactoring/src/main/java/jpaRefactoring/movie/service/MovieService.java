package jpaRefactoring.movie.service;

import jpaRefactoring.movie.dto.MovieDto;
import jpaRefactoring.movie.dto.MoviePosterDto;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface MovieService {
    List<MovieDto> selectMovieList();
    void insertMovie(MovieDto movieDto, MultipartHttpServletRequest request);
    MovieDto selectMovieDetail(Long movieId);
    void updateMovie(MovieDto movieDto);
    void deleteMovie(Long movieId);
    MoviePosterDto selectMoviePosterInfo(Long posterId, Long movieId);
}
