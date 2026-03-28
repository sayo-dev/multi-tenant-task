package org.example.multi_tenant_task.project.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ProjectResponse {
    Long id;
    String title;
    String description;
}