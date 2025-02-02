package com.crud.crudpractice.movie.service;

import com.crud.crudpractice.movie.dto.MovieDto;
import com.crud.crudpractice.movie.mapper.MovieMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieMapper movieMapper;

    // 영화 목록 조회 메서드
    @Override
    public List<MovieDto> selectMovieList() {
        return movieMapper.selectMovieList();
    }

    @Override
    public void insertBoard(MovieDto movieDto) {
        movieMapper.insertMovie(movieDto);
    }

    @Override
    public MovieDto selectMovieDetail(Long movieId) {
        return movieMapper.selectMovieDetail(movieId);
    }

    @Override
    public void updateMovie(MovieDto movieDto) {
        movieMapper.updateMovie(movieDto);
    }

    @Override
    public void deleteMovie(Long movieId) {
        MovieDto movieDto = new MovieDto();
        movieDto.setMovieId(movieId);
        movieMapper.deleteMovie(movieDto);
    }


}
