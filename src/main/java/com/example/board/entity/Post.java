package com.example.board.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) // AUTO INCREMENT
    private Long id;

    @Column(nullable = false, length = 100) // NOT NULL, VARCHAR(100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /*
    * @OneToMany : 하나의 Post에 여러 개의 Comment
    * mappedBy : 어느 엔티티가 이 관계에서 주인인지 설정
        - 이 관계에서는 Post 엔티티가 주인이라는 의미
    * mappedBy = "post" 에서 post는 Comment 객체의 post 필드를 의미
        - 이름이 정확히 맞아야 한다
    * cascade : 부모의 상태에 따른 자식의 상태를 설정
        - CascadeType.REMOVE : 부모(Post)가 삭제 되면 자식(Comment)도 삭제
    * orphanRemoval = true : 부모와 연결이 끊긴 "고아 객체" 상태라면 자식을 자동으로 DELETE
        - Comment가 Post와 연결이 끊기면 발동
    */
    @OneToMany(mappedBy = "post",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 생성 시점에 자동으로 현재 시간을 설정
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

}
