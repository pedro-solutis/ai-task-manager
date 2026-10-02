package br.com.solutis.backend.dto;

import br.com.solutis.backend.domain.enums.TaskStatus;
import br.com.solutis.backend.validation.ValidEnum;

public record TaskStatusUpdateDTO(
    @ValidEnum (enumClass = TaskStatus.class, message = "Status inválido. Valores aceitos: TODO, IN_PROGRESS, DONE")
    String status
) {

}
