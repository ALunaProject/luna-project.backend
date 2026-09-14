package com.luna.lunaproject.application.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class ImageUploadService {

    private final Cloudinary cloudinary;

    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png");
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB em bytes

    public String uploadImage(MultipartFile file){
        validateImage(file);
        try{
            Map uploadResult = cloudinary.uploader().upload( //
                    file.getBytes(),
                    ObjectUtils.asMap("resource_type", "image")
            );
            return (String) uploadResult.get("secure_url");
        }catch (IOException e){
            throw new RuntimeException("Erro ao fazer upload da imagem", e);
        }
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidImageException("Nenhum arquivo encontrado");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new InvalidImageException("Formato inválido. Apenas JPG e PNG são aceitos");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidImageException("Arquivo muito grande. O limite é 2MB");
        }
    }
}
