package com.luna.lunaproject.controller;


import com.luna.lunaproject.dto.CommentsReqDTO;
import com.luna.lunaproject.entity.Comment;
import com.luna.lunaproject.services.CommentsServices;
import jakarta.validation.Valid;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    public final CommentsServices commentsServices;

    public CommentsController(CommentsServices commentsServices) {
        this.commentsServices = commentsServices;
    }

    @GetMapping(value = "getAll")
    public ResponseEntity<?> getAllComments() {
        return ResponseEntity.ok(commentsServices.getAllComments());
    }

    @PostMapping
    public ResponseEntity<?> addComment(CommentsReqDTO req) {
        return ResponseEntity.ok(commentsServices.saveComment(req));
    }

    public RequestEntity<?> getCommentById(int id) {}

}
