package jpaRefactoring.movie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jpaRefactoring.movie.dto.MovieListResponse;
import jpaRefactoring.movie.entity.MovieEntity;
import jpaRefactoring.movie.entity.MoviePosterEntity;
import jpaRefactoring.movie.service.MovieService;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    // 영화 목록 조회 메서드
    @GetMapping("/movie")
    public ResponseEntity<Object> openMovieList() throws Exception {
        List<MovieListResponse> result = new ArrayList<>();

        try {
            List<MovieEntity> movieList = movieService.selectMovieList();
            movieList.forEach(dto -> result.add(new ModelMapper().map(dto, MovieListResponse.class)));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("목록 조회 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 영화 저장 요청을 처리하는 메서드
    @PostMapping(value = "/movie", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> insertMovie(@RequestParam("movie") String movieData, MultipartHttpServletRequest request) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MovieEntity movieEntity = objectMapper.readValue(movieData, MovieEntity.class);
        Map<String, String> result = new HashMap<>();
        try {
            movieService.insertMovie(movieEntity, request);
            result.put("message", "영화 등록 성공");
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            result.put("message", "영화 등록 실패");
            result.put("description", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // 영화 상세 조회 요청을 처리하는 메서드
    @GetMapping("/movie/{movieId}")
    public MovieEntity openBoardDetail(@RequestParam("movieId") Long movieId) throws Exception {
        MovieEntity movieEntity = movieService.selectMovieDetail(movieId);

        return movieEntity;
    }

    // 수정 요청을 처리할 메서드
    @PutMapping("/movie/{movieId}")
    public void updateMovie(@PathVariable("movieId") Long movieId, @RequestBody MovieEntity movieEntity) throws Exception {
        movieEntity.setMovieId(movieId);
        movieService.updateMovie(movieEntity);
    }

    // 삭제 요청을 처리할 메서드
    @DeleteMapping("/movie/{movieId}")
    public void deleteMovie(@PathVariable("movieId") Long movieId) throws Exception {
        movieService.deleteMovie(movieId);
    }

    // 파일 다운로드 요청을 처리하는 메서드
    @GetMapping("/movie/file")
    public void downloadBoardFile(@RequestParam("posterId") Long posterId,
                                  @RequestParam("movieId") Long movieId,
                                  HttpServletResponse response) throws Exception {
        // posterId와 movieId가 일치하는 파일 정보를 조회
        MoviePosterEntity moviePosterEntity = movieService.selectMoviePosterInfo(posterId, movieId);
        if (ObjectUtils.isEmpty(moviePosterEntity)) {
            return;
        }

        String posterUrl = moviePosterEntity.getPosterUrl();
        byte[] fileBytes = null;
        String fileName = null;

        // 원격 URL (http:// 또는 https://) 인지 로컬 경로 인지 판단
        if (posterUrl.startsWith("http://") || posterUrl.startsWith("https://")) {
            // 원격 URL인 경우: URL 객체로 연결하여 데이터를 읽어옴
            URL url = new URL(posterUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            try (InputStream inputStream = connection.getInputStream()) {
                fileBytes = org.springframework.util.StreamUtils.copyToByteArray(inputStream);
            }
            // 파일 이름은 URL의 마지막 경로 요소를 사용
            fileName = posterUrl.substring(posterUrl.lastIndexOf("/") + 1);
        } else {
            // 로컬 파일인 경우: Paths.get()으로 파일을 읽어옴
            Path path = Paths.get(posterUrl);
            fileBytes = Files.readAllBytes(path);
            fileName = path.getFileName().toString();
        }

        // HTTP 응답 헤더 설정 후 파일 데이터를 출력
        response.setContentType("application/octet-stream");
        response.setContentLength(fileBytes.length);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.getOutputStream().write(fileBytes);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
