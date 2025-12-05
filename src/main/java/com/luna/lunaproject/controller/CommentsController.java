package com.luna.lunaproject.controller;


import com.luna.lunaproject.dto.CommentsReqDTO;
import com.luna.lunaproject.dto.CommentsResDTO;
import com.luna.lunaproject.services.CommentsServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping(value = "newComment")
    public ResponseEntity<?> addComment(CommentsReqDTO req) {
        return ResponseEntity.ok(commentsServices.saveComment(req));
    }

    @GetMapping(value = "getById")
    public ResponseEntity<List<CommentsResDTO>> getCommentById(int id, CommentsReqDTO req) {
        return ResponseEntity.ok(commentsServices.getCommentById(id));
    }

    @PutMapping(value = "edit")
    public ResponseEntity<?> editComment(@RequestBody CommentsReqDTO req, int id) {
        return ResponseEntity.ok(commentsServices.editComment(id, req));
    }

    @DeleteMapping("delete")
    public ResponseEntity<String> delete(String res) {
        return ResponseEntity.ok(res);
    }
}
