package br.com.solutis.backend.dto.request;

import br.com.solutis.backend.domain.enums.TaskStatus;
import br.com.solutis.backend.validation.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for updating task status")
public record TaskStatusUpdateDTO(
    @Schema(description = "The new status of the task", example = "IN_PROGRESS")
    @ValidEnum (enumClass = TaskStatus.class, message = "Invalid status. Accepted value: TODO, IN_PROGRESS, DONE")
    String status
) {

}
