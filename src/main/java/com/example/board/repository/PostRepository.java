package com.example.board.repository;

import java.util.List;
import com.example.board.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 기본 CRUD 메서드

    // 저장 (INSERT or UPDATE)
    // Post save(Post entity);

    // 조회
    // Optional<Post> findById(Long id);
    // List<Post> findAll();
    // List<Post> findAll(Sort sort);

    // 삭제
    // void deleteById(Long id);
    // void delete(Post entity);

    // 개수 조회
    // long count();
    // 존재 여부 확인
    // boolean existsById(Long id);

    // findBy + 필드명 + 조건

    // LIKE %keyword%
    List<Post> findByTitleContaining(String keyword);

    // @Query 방식
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword%")
    List<Post> searchByTitle(@Param("keyword") String keyword);

    // LIKE keyword%
    List<Post> findByTitleStartingWith(String keyword);

    // >
    List<Post> findByIdGreaterThan(Long id);

    // ORDER BY id DESC
    List<Post> findAllByOrderByIdDesc();

    // 제목 or 내용 으로 검색
    List<Post> findByTitleContainingOrContentContaining(
            String titleKeyword, String contentKeyword
    );

    // 제목 or 내용 으로 검색
    @Query("""
        SELECT p FROM Post p 
        WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% 
        ORDER BY p.createdAt DESC
    """)
    List<Post> searchByKeyword(@Param("keyword") String keyword);

    @Query(value="""
        SELECT * FROM post 
        WHERE title LIKE %:keyword% 
        ORDER BY id DESC
    """, nativeQuery=true)
    List<Post> searchByTitleNative(@Param("keyword") String keyword);

    // 1. query method
    List<Post> findTop3ByOrderByCreatedAtDesc();

    // 2. jpql
    @Query("""
      SELECT p FROM Post p
      ORDER BY p.createdAt DESC
    """)
    List<Post> findRecentPosts(Pageable pageable);

    // 3. native sql
    @Query(value= """
        SELECT * FROM post
        ORDER BY created_at DESC
        LIMIT 3
    """, nativeQuery = true)
    List<Post> findRecentPostsNative();

    // List<Post> findAll() => JpaRepository가 구현해둔 메서드
    // 오버로딩 (동일한 이름이지만 매개변수가 다름)
    Page<Post> findAll(Pageable pageable);

    Page<Post> findByTitleContaining(String keyword, Pageable pageable);

    Slice<Post> findAllBy(Pageable pageable);

    /*
    * 게시글을 조회할 때 댓글도 함께 조회하기 위함 (N+1 문제 방지)
    * FETCH의 의미: Post 엔티티를 조회할 때 연관된 컬렉션인 comments를
                    한 번의 쿼리에서 함께 조회하여 N+1 문제를 방지한다.
    */
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.comments")
    List<Post> findAllWithComments();

    /*
    * @EntityGraph(attributePaths = {"comments"})
        - @Query로 작성한 쿼리를 실행할 때,
          @EntityGraph에 지정한 연관 필드(comments)를
          fetch join처럼 즉시 로딩하도록 JPA에게 지시.
        - comments 필드 => Post 엔티티 내부에 존재하는 필드.
    * 결과 리스트에서 엔티티 중복이 생길 수 있는데, JPA가 자동으로 DISTINCT 처리.
    */
    @EntityGraph(attributePaths = {"comments"})
    @Query("SELECT p FROM Post p")
    List<Post> findAllWithCommentsEntityGraph();

}
