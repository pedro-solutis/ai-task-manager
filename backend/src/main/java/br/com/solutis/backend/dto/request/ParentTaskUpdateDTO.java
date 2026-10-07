package br.com.solutis.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Data Transfer Object for updating a parent task")
public record ParentTaskUpdateDTO(
    @Schema(description = "The ID of the parent task", example = "123e4567-e89b-12d3-a456-426614174000")
    @Size (min = 36, max = 36, message = "Parent task id must have 36 characters")
    String parentTaskId
) {

}
