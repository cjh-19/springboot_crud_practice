package com.crud.crudpractice.movie.mapper;

import com.crud.crudpractice.movie.dto.MovieDto;
import com.crud.crudpractice.movie.dto.MoviePosterDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MovieMapper {
    List<MovieDto> selectMovieList();
    void insertMovie(MovieDto movieDto);
    MovieDto selectMovieDetail(Long movieId);
    void updateMovie(MovieDto movieDto);
    void deleteMovie(MovieDto movieDto);
    void insertMoviePosterList(List<MoviePosterDto> MoviePosterList);
    List<MoviePosterDto> selectMoviePosterList(Long movieId);
    MoviePosterDto selectMoviePosterInfo(@Param("posterId") Long posterId, @Param("movieId") Long movieId);
}
