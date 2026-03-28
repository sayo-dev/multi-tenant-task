package org.example.multi_tenant_task.project.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Value;
import org.example.multi_tenant_task.task.Task;

import java.util.List;

@Builder
@Value
@JsonPropertyOrder({"id", "title", "description"})
public class ProjectResponse {
    Long id;
    String title;
    String description;
//    List<Task> task;
}