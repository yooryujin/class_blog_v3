package com.tenco.blog.board;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // IoC + 싱글톤 패턴 관리
@RequiredArgsConstructor // 생성자 자동 생성 + 멤버 변수 -> DI 처리 됨
public class BoardRepository {

    private final EntityManager entityManager;

    /*
    * 전체 게시글 조회
    * */
    public List<Board> findByAll() {
        // 조회 - JPQL 쿼리 선택
        String jpql = "select b from Board b order by b.id desc";
        TypedQuery query = entityManager.createQuery(jpql,Board.class);
        List<Board> boardList = query.getResultList();
        return entityManager.createQuery(jpql,Board.class).getResultList();
    }

    public Board findById(Long id) {
        // 상세 조회
        return entityManager.find(Board.class,id);
    }
}
