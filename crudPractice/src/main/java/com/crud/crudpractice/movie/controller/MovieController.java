package com.crud.crudpractice.movie.controller;

import com.crud.crudpractice.movie.dto.MovieDto;
import com.crud.crudpractice.movie.dto.MoviePosterDto;
import com.crud.crudpractice.movie.service.MovieService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    // 영화 목록 조회 메서드
    @GetMapping("/movie/openMovieList.do")
    public ModelAndView openMovieList() throws Exception {
        ModelAndView mv = new ModelAndView("/movie/movieList");

        List<MovieDto> list = movieService.selectMovieList();
        mv.addObject("list", list);

        return mv;
    }

    // 영화 등록 화면 요청을 처리하는 메서드
    @GetMapping("/movie/openMovieWrite.do")
    public String openMovieWrite() throws Exception {
        return "/movie/movieWrite";
    }

    // 영화 저장 요청을 처리하는 메서드
    @PostMapping("/movie/insertMovie.do")
    public String insertMovie(MovieDto movieDto, MultipartHttpServletRequest request) throws Exception {
        movieService.insertMovie(movieDto, request);
        return "redirect:/movie/openMovieList.do";
    }

    // 영화 상세 조회 요청을 처리하는 메서드
    @GetMapping("/movie/openMovieDetail.do")
    public ModelAndView openBoardDetail(@RequestParam("movieId") Long movieId) throws Exception {
        MovieDto movieDto = movieService.selectMovieDetail(movieId);

        ModelAndView mv = new ModelAndView("/movie/movieDetail");
        mv.addObject("movie", movieDto);
        return mv;
    }

    // 수정 요청을 처리할 메서드
    @PostMapping("/movie/updateMovie.do")
    public String updateMovie(MovieDto movieDto) throws Exception {
        movieService.updateMovie(movieDto);
        return "redirect:/movie/openMovieList.do";
    }

    // 삭제 요청을 처리할 메서드
    @PostMapping("/movie/deleteMovie.do")
    public String deleteMovie(@RequestParam("movieId") Long movieId) throws Exception {
        movieService.deleteMovie(movieId);
        return "redirect:/movie/openMovieList.do";
    }

    // 파일 다운로드 요청을 처리하는 메서드
    @GetMapping("/movie/downloadMoviePoster.do")
    public void downloadBoardFile(@RequestParam("posterId") Long posterId,
                                  @RequestParam("movieId") Long movieId,
                                  HttpServletResponse response) throws Exception {
        // posterId와 movieId가 일치하는 파일 정보를 조회
        MoviePosterDto moviePosterDto = movieService.selectMoviePosterInfo(posterId, movieId);
        if (ObjectUtils.isEmpty(moviePosterDto)) {
            return;
        }

        String posterUrl = moviePosterDto.getPosterUrl();
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
