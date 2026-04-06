package org.example.multi_tenant_task.entities.task.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Value;
import org.example.multi_tenant_task.entities.task.TaskStatus;

import java.time.LocalDateTime;

@Builder
@Value
@JsonPropertyOrder({"id", "title", "description", "status", "dueDate"})
public class TaskResponse {
    Long id;
    String title;
    String description;
    TaskStatus status;
    LocalDateTime dueDate;
}