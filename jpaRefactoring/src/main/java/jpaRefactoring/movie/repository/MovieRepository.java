package jpaRefactoring.movie.repository;

import jpaRefactoring.movie.entity.MovieEntity;
import jpaRefactoring.movie.entity.MoviePosterEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<MovieEntity, Long> {
    List<MovieEntity> findAllByOrderByMovieIdDesc();

    @Query("SELECT poster FROM MoviePosterEntity poster WHERE poster.movie.movieId = :movieId AND poster.posterId = :posterId")
    MoviePosterEntity findMoviePoster(@Param("movieId") Long movieId, @Param("posterId") Long posterId);
}
