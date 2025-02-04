package jpaRefactoring.movie.service;

import jpaRefactoring.movie.common.FileUtils;
import jpaRefactoring.movie.entity.MovieEntity;
import jpaRefactoring.movie.entity.MoviePosterEntity;
import jpaRefactoring.movie.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private FileUtils fileUtils;

    // 영화 목록 조회 메서드
    @Override
    public List<MovieEntity> selectMovieList() {
        return movieRepository.findAllByOrderByMovieIdDesc();
    }

    @Override
    public void insertMovie(MovieEntity movieEntity, MultipartHttpServletRequest request) throws Exception {
        // 첨부 파일을 디스크에 저장하고, 첨부 파일 정보를 반환
        List<MoviePosterEntity> fileInfoList = fileUtils.parseFileInfo(request);

        // 첨부 파일 정보를 DB에 저장
        if (!CollectionUtils.isEmpty(fileInfoList)) {
            movieEntity.setMoviePostersList(fileInfoList);
        }
        movieRepository.save(movieEntity);
    }

    @Override
    public MovieEntity selectMovieDetail(Long movieId) {
        Optional<MovieEntity> optional = movieRepository.findById(movieId);
        if (optional.isPresent()) {
            MovieEntity movieEntity = optional.get();

            return movieEntity;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void updateMovie(MovieEntity movieEntity) {
        MovieEntity existingMovie = movieRepository.findById(movieEntity.getMovieId()).orElse(null);
        if (existingMovie != null) {
            movieEntity.setMoviePostersList(existingMovie.getMoviePostersList());
        }
        movieRepository.save(movieEntity);
    }

    @Override
    public void deleteMovie(Long movieId) {
        movieRepository.deleteById(movieId);
    }

    @Override
    public MoviePosterEntity selectMoviePosterInfo(Long posterId, Long movieId) {
        return movieRepository.findMoviePoster(movieId, posterId);
    }
}
