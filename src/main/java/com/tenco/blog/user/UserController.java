package com.tenco.blog.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor // DI 처리
@Controller
public class UserController {
    private final UserRepository userRepository;
    // httpSession <--- session 메모리에 접근을 할 수 있음
    private final HttpSession httpSession;

    /**
     * 회원 가입 화면 요청
     *
     * @return join-form.mustache
     */
    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    // 회원 가입 액션 처리
    @PostMapping("/join")
    public String join(UserRequest.JoinDTO joinDTO, HttpServletRequest request) {
        System.out.println(" === 회원 가입 요청 ===");
        System.out.println("사용자 명 : " + joinDTO.getUsername());
        System.out.println("사용자 이메일 : " + joinDTO.getEmail());

        try {
            // 1. 입력된 데이터 검증 (유효성 검사)
            joinDTO.validate();

            // 2.  사용자 명 중복 체크
            User exitUser = userRepository.findByUsername(joinDTO.getUsername());
            if (exitUser != null) {
                throw new IllegalArgumentException(" 이미 존재하는 사용자 명 입니다" + joinDTO.getUsername());
            }
            // 3. DTO  를 User Object 변환
            User user = joinDTO.toEntity();

            // 4. User Object 를 영속화 처리
            userRepository.save(user);
            return "redirect:/login-form";

        } catch (Exception e) {
            // 검증 실패 시 보통 에러 메세지와 함께 다시 form 에 전달
            request.setAttribute("errorMessage", "잘못된 요청입니다");
            return "user/join-form";
        }
    }

    /**
     * 로그인 화면 요청
     *
     * @return login-form.mustache
     */
    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    // 로그인 액션 처리
    // 자원에 요청은 GET 방식이다
    // 단, 로그인 요청은 예외 - 보안상 이유

    // DTO 패턴 활용
    // 1. 입력 데이터 검증
    // 2. 사용자명과 비밀번호를 DB 에 접근해서 조회
    // 3. 로그인 성공/실패 처리
    // 4. 로그인 성공이라면 서버측 메모리에 사용자 정보를 저장
    // 5. 메인 화면으로 리다이렉트 처리
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO loginDTO) {

        System.out.println("=== 로그인 시도 ===");
        System.out.println("사용자명 : " + loginDTO.getUsername());

        try {
            // 1.
            loginDTO.validate();
            // 2.
            User user = userRepository.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword());
            // 3. 로그인 실패
            if (user == null) {
                // 로그인 실패 : 일치 된 사용자 없음
                throw new IllegalArgumentException("사용자명 또는 비밀번호가 틀렸어");
            }
            // 4. 로그인 성공
            httpSession.setAttribute("sessionUser", user);

            // 5. 로그인 성공 후 리스트 페이지 이동
            return "redirect:/";

        } catch (Exception e) {
            // 필요하다면 errorMessage 생성해서 내려 보냄
            e.printStackTrace();
            return "user/login-form";
        }


    }

    @GetMapping("/user/update-form")
    public String updateForm() {
        return "user/update-form";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout() {
        httpSession.invalidate();
        return "redirect:/";
    }

}
