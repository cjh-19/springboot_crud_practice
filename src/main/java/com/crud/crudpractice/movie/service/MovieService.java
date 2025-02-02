package com.crud.crudpractice.movie.service;

import com.crud.crudpractice.movie.dto.MovieDto;
import com.crud.crudpractice.movie.dto.MoviePosterDto;
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
