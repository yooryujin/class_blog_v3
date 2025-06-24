package com.tenco.blog.board;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // IoC + 싱글톤 패턴 관리
@RequiredArgsConstructor // 생성자 자동 생성 + 멤버 변수 -> DI 처리 됨
public class BoardRepository {

    // DI
    private final EntityManager entityManager;

    /**
     *  게시글 저장 : User 와 연관관계를 가진 Board 엔티티 영속화
     * @param board
     * @return
     */
    @Transactional
    public Board save(Board board) {
        // 비영속 상태의 Board Object 를 영속성 컨텍스트에 저장하면
        entityManager.persist(board);
        // 이후 시점에는 사실 같은 메모리 주소를 가리킴
        return board;
    }


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
