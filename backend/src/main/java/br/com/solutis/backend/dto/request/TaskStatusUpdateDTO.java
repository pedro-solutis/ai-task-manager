package br.com.solutis.backend.dto.request;

import br.com.solutis.backend.domain.enums.TaskStatus;
import br.com.solutis.backend.validation.ValidEnum;

public record TaskStatusUpdateDTO(
    @ValidEnum (enumClass = TaskStatus.class, message = "Invalid status. Accepted value: TODO, IN_PROGRESS, DONE, CANCELED")
    String status
) {

}
