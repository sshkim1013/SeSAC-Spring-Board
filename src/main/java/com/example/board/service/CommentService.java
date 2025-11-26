package com.example.board.service;

import com.example.board.entity.Comment;
import com.example.board.entity.Post;
import com.example.board.repository.CommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional
    public Comment createComment(Long postId, Comment comment) {
        /*
        * Comment를 생성하기 위해서는 Post가 필요
        * 특정 id의 Post 가져오기
        */
        Post post = postService.getPostById(postId);

        System.out.println("===== 댓글 추가 전 =====");
        System.out.println("댓글 수 : " + post.getComments().size());

        // comment.setPost(post);
        post.addComment(comment);
        Comment saved = commentRepository.save(comment);

        System.out.println("===== 댓글 추가 후 =====");
        System.out.println("댓글 수 : " + post.getComments().size());

        return saved;
    }

    // 게시글 ID로 특정 게시글의 모든 댓글 조회
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment foundComment = commentRepository.findById(commentId)
            .orElseThrow();

        // // 고아 객체 방식을 통한 삭제
        // Post post = foundComment.getPost();
        // post.removeComment(foundComment);

        commentRepository.delete(foundComment);
    }

}
