package com.crud.crudpractice.movie.mapper;

import com.crud.crudpractice.movie.dto.MovieDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MovieMapper {
    List<MovieDto> selectMovieList();
    void insertMovie(MovieDto movieDto);
}
