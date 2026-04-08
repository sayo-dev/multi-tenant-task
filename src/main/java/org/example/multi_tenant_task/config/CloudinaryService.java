package org.example.multi_tenant_task.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
@Data
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String upload(MultipartFile file) throws IOException {
        var uploadResult = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.asMap("folder", "tenant"));
        return uploadResult.get("secure_url").toString();
    }

}
