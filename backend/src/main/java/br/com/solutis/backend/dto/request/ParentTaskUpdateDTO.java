package br.com.solutis.backend.dto.request;

import jakarta.validation.constraints.Size;

public record ParentTaskUpdateDTO(
    @Size (min = 36, max = 36, message = "Parent task id must have 36 characters")
    String parentTaskId
) {

}
