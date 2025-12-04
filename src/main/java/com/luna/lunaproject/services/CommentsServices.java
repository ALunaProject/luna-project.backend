package com.luna.lunaproject.services;

import com.luna.lunaproject.dto.CommentsReqDTO;
import com.luna.lunaproject.dto.CommentsResDTO;
import com.luna.lunaproject.entity.Comments;
import com.luna.lunaproject.repository.CommentsRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class CommentsServices {
    private final CommentsRepo commentsRepo;

    public CommentsServices(CommentsRepo commentsRepo) {
        this.commentsRepo = commentsRepo;
    }

    public List<CommentsResDTO> getAllComments() {
        List<Comments> comments = commentsRepo.findAll();
        List<CommentsResDTO> res = comments.stream().map(CommentsResDTO::new).collect(toList());
        return res;
    }

    public List<CommentsResDTO> getCommentById(int id) {
        Optional<Comments> comment = commentsRepo.findById(id);
        return comment.stream().map(CommentsResDTO::new).toList();
    }

    public ResponseEntity<?> saveComment(CommentsReqDTO req) {
        Comments comment = new Comments(req);
        commentsRepo.save(comment);
        return ResponseEntity.ok("Comments saved successfully");
    }

    public ResponseEntity<?> editComment(int id, CommentsReqDTO req) {
        Comments comment = commentsRepo.findById(id).get();
        comment.setContent(req.getContent());
        commentsRepo.save(comment);
        return ResponseEntity.ok("Comments edited successfully");
    }

    public ResponseEntity<?> deleteComments(int id) {
        if (commentsRepo.existsById(id)) {
            commentsRepo.deleteById(id);
            return ResponseEntity.ok("Comments deleted successfully");
        } else {
            return ResponseEntity.ok("Comments not found");
        }
    }
}
