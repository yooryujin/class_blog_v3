package com.tenco.blog.board;

import com.tenco.blog.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;

    // 전체 게시물 조회
    @GetMapping("/")
    public String index(HttpServletRequest request) {

        // 1. 게시글 목록 조회
        List<Board> boards = boardRepository.findByAll();
        request.setAttribute("boardList", boards);
        return "index";
    }

    // 게시물 상세 조회
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id, HttpServletRequest request) {

        // 1. 게시글 상세 조회
        Board board = boardRepository.findById(id);
        request.setAttribute("board", board);
        return "board/detail";
    }

    @GetMapping("/board/{id}/board-update")
    public String updateForm(@PathVariable(name = "id") Long id, HttpServletRequest request) {

        // 1. 게시글 상세 조회
        Board board = boardRepository.findById(id);
        request.setAttribute("board", board);
        return "board/board-update";
    }
    // 게시글 작성 화면 요청

    /**
     * 주소 설계 : http://localhost:8081/board/save-form
     *
     * @param session
     * @return
     */
    @GetMapping("/board/save-form")
    public String saveForm(HttpSession session) {
        // 권한 체크 -> 로그인된 사용자만 이동
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            // 로그인 안한 경우 다시 로그인 페이지로 리다이렉트 처리
            return "redirect:/login-form";
        }


        return "board/save-form";
    }
    // http://localhost:8081:/board/save
    // 게시글 저장 액션 처리
    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO reqDTO, HttpSession session) {
        try {
            // 1. 권한 체크
            User sessionUser = (User) session.getAttribute("sessionUser");
            if (sessionUser == null) {
                // 로그인 안한 경우 다시 로그인 페이지로 리다이렉트 처리

                return "redirect:/login-form";
            }

            // 2. 유효성 검사
            reqDTO.validate();

            // 3. SaveDTO --> 저장시키기 위해 --> Board 변환을 해 주어야 한다
            Board board = reqDTO.toEntity(sessionUser);
            boardRepository.save(reqDTO.toEntity(sessionUser));

            return "redirect:/";

        } catch (Exception e) {
            e.printStackTrace();
            // 필요하다면 에러 메세지 내려 줄 수 있음
            return "board/save-form";
        }

    }
}
