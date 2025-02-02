package com.crud.crudpractice.movie.controller;

import com.crud.crudpractice.movie.dto.MovieDto;
import com.crud.crudpractice.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

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
    public String insertBoard(MovieDto movieDto) throws Exception {
        movieService.insertBoard(movieDto);
        return "redirect:/movie/openMovieList.do";
    }

}
