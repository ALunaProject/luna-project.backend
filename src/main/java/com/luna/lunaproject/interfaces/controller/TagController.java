package com.luna.lunaproject.interfaces.controller;

import com.luna.lunaproject.domain.model.Tag;
import com.luna.lunaproject.domain.model.TagCatalog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        return ResponseEntity.ok(TagCatalog.all());
    }
}
