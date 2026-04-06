package org.example.multi_tenant_task.entities.task.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;


public record TaskRequest(@NotNull @NotBlank String title,
                          @NotNull @NotBlank String description,
                          @Future LocalDateTime dueDate){
}