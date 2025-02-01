package com.crud.crudpractice.movie.service;

import com.crud.crudpractice.movie.dto.MovieDto;

import java.util.List;

public interface MovieService {

    List<MovieDto> selectMovieList();
}
