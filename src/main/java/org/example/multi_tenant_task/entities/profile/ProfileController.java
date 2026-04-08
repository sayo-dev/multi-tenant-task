package org.example.multi_tenant_task.entities.profile;

import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {


    private final ProfileService profileService;


    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(
            @RequestParam MultipartFile image
    ) {
        String uploadedFile = profileService.upload(image);
        return ResponseEntity.ok(ApiResponse.success(uploadedFile));
    }

}
