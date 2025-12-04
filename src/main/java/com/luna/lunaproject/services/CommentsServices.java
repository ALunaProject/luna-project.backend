package com.luna.lunaproject.services;

import com.luna.lunaproject.dto.CommentsReqDTO;
import com.luna.lunaproject.dto.CommentsResDTO;
import com.luna.lunaproject.entity.Comment;
import com.luna.lunaproject.repository.CommentRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class CommentsServices {
    private final CommentRepo commentRepo;

    public CommentsServices(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public List<CommentsResDTO> getAllComments() {
        List<Comment> comments = commentRepo.findAll();
        List<CommentsResDTO> res = comments.stream().map(CommentsResDTO::new).collect(toList());
        return res;
    }

    public List<CommentsResDTO> getCommentById(int id) {
        Optional<Comment> comment = commentRepo.findById(id);
        return comment.stream().map(CommentsResDTO::new).toList();
    }

    public ResponseEntity<?> saveComment(CommentsReqDTO req) {
        Comment comment = new Comment(req);
        commentRepo.save(comment);
        return ResponseEntity.ok("Comment saved successfully");
    }

    public ResponseEntity<?> editComment(int id, CommentsReqDTO req) {
        Comment comment = commentRepo.findById(id).get();
        comment.setContent(req.getContent());
        commentRepo.save(comment);
        return ResponseEntity.ok("Comment edited successfully");
    }

    public ResponseEntity<?> deleteComments(int id) {
        if (commentRepo.existsById(id)) {
            commentRepo.deleteById(id);
            return ResponseEntity.ok("Comment deleted successfully");
        } else {
            return ResponseEntity.ok("Comment not found");
        }
    }
}
