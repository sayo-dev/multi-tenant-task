package org.example.multi_tenant_task.entities.project.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonPropertyOrder({"id", "title", "description"})
public class ProjectResponse {
    Long id;
    String title;
    String description;
//    List<Task> task;
}