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
        - Comment를 생성하기 위해서는 Post가 필요
        - 특정 id의 Post 가져오기
         */
        Post post = postService.getPostById(postId);
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    // 게시글 ID로 특정 게시글의 모든 댓글 조회
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment foundComment = commentRepository.findById(commentId).orElseThrow();
        commentRepository.delete(foundComment);
    }

}
