package com.luna.lunaproject.controller;

import com.luna.lunaproject.dto.CommentsReqDTO;
import com.luna.lunaproject.dto.CommentsResDTO;
import com.luna.lunaproject.entity.Comment;
import com.luna.lunaproject.repository.CommentRepo;
import com.luna.lunaproject.services.CommentsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {

    private final CommentsServices services;

    public CommentsController(CommentsServices services) {
        this.services = services;
    }

    @GetMapping("/display")
    public List<CommentsResDTO> getAll() {
        return services.getAllComments();
    }

    @GetMapping("/display/{id}")
    public List<CommentsResDTO> getCommentId(@PathVariable int id) {
        return services.getCommentById(id);
    }

    @PostMapping("/newComment")
    public ResponseEntity<?> newComment(@RequestBody CommentsReqDTO req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(services.saveComment(req));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editComment(@RequestBody CommentsReqDTO req, @PathVariable int id) {
        return ResponseEntity.ok(services.editComment(id, req));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteComments(@PathVariable int id) {
        return ResponseEntity.ok(services.deleteComments(id));
    }
}
