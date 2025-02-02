package com.crud.crudpractice.movie.service;

import com.crud.crudpractice.movie.common.FileUtils;
import com.crud.crudpractice.movie.dto.MovieDto;
import com.crud.crudpractice.movie.dto.MoviePosterDto;
import com.crud.crudpractice.movie.mapper.MovieMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@Slf4j
@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private FileUtils fileUtils;

    // 영화 목록 조회 메서드
    @Override
    public List<MovieDto> selectMovieList() {
        return movieMapper.selectMovieList();
    }

    @Override
    public void insertMovie(MovieDto movieDto, MultipartHttpServletRequest request) {
        movieMapper.insertMovie(movieDto);

        try {
            // 첨부 파일을 디스크에 저장하고, 첨부 파일 정보를 반환
            List<MoviePosterDto> fileInfoList = fileUtils.parseFileInfo(movieDto.getMovieId(), request);

            // 첨부 파일 정보를 DB에 저장
            if (!CollectionUtils.isEmpty(fileInfoList)) {
                movieMapper.insertMoviePosterList(fileInfoList);
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public MovieDto selectMovieDetail(Long movieId) {
        MovieDto movieDto = movieMapper.selectMovieDetail(movieId);
        List<MoviePosterDto> moviePostersList = movieMapper.selectMoviePosterList(movieId);
        movieDto.setMoviePostersList(moviePostersList);

        return movieDto;
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

    @Override
    public MoviePosterDto selectMoviePosterInfo(Long posterId, Long movieId) {
        return movieMapper.selectMoviePosterInfo(posterId, movieId);
    }
}
