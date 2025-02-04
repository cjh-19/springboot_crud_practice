package jpaRefactoring.movie.service;

import jpaRefactoring.movie.entity.MovieEntity;
import jpaRefactoring.movie.entity.MoviePosterEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface MovieService {
    List<MovieEntity> selectMovieList();
    void insertMovie(MovieEntity movieEntity, MultipartHttpServletRequest request) throws Exception;
    MovieEntity selectMovieDetail(Long movieId);
    void updateMovie(MovieEntity movieEntity);
    void deleteMovie(Long movieId);
    MoviePosterEntity selectMoviePosterInfo(Long posterId, Long movieId);
}
