package com.tenco.blog.board;

import com.tenco.blog.user.User;
import lombok.Data;

/**
 * 클라이언트에게 넘어온 데이터를
 * Object 로 변화해서 전달하는 DTO 역할을 담당한다
 */
public class BoardRequest {

    //게시글 저장 DTO
    @Data
    public static class SaveDTO {
        private String title;
        private String content;
        // username 제거 : 세션에서 가져 올 예정

        // (User) <-- toEntity() 호출할 때 세션에서 가져와서 넣어주면 됨
        public Board toEntity(User user) {
            return Board.builder()
                    .title(this.title)
                    .content(this.content)
                    .user(user)
                    .build();
        }

        public void validate() {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("제목은 필수야");
            }
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("제목은 필수야");
            }
        }
    }
}

