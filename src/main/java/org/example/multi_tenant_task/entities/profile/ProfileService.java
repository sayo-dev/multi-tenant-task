package org.example.multi_tenant_task.entities.profile;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.config.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final CloudinaryService cloudinaryService;


    public String upload(MultipartFile file) {

        if (file.isEmpty() || !Objects.requireNonNull(file.getContentType()).startsWith("image/"))
            throw new IllegalArgumentException("File must not be empty and must a valid type.");


//        if (file.getSize() > 1024 * 1024)
//            throw new IllegalArgumentException("File too large.");

        try {
            return cloudinaryService.upload(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
