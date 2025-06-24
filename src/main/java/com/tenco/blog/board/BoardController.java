package com.tenco.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;

    // 전체 게시물 조회
    @GetMapping("/")
    public String index(HttpServletRequest request){

        // 1. 게시글 목록 조회
        List<Board> boards = boardRepository.findByAll();
        request.setAttribute("boardList",boards);
        return "index";
    }

    // 게시물 상세 조회
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id")Long id, HttpServletRequest request){

        // 1. 게시글 상세 조회
        Board board = boardRepository.findById(id);
        request.setAttribute("board",board);
        return "board/detail";
    }

    @GetMapping("/board/{id}/board-update")
    public String updateForm(@PathVariable(name = "id")Long id, HttpServletRequest request){

        // 1. 게시글 상세 조회
        Board board = boardRepository.findById(id);
        request.setAttribute("board",board);
        return "board/board-update";
    }


}
