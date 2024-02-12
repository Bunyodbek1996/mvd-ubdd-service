package uz.ciasev.ubdd_service.dto.internal.request.resolution.manual_material;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import uz.ciasev.ubdd_service.entity.dict.resolution.TerminationReason;
import uz.ciasev.ubdd_service.exception.ErrorCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class TerminationManualMaterialDTO {

    @NotNull(message = ErrorCode.MATERIAL_REQUIRED)
    @Valid
    private ManualCourtMaterialDTO material;

    @NotNull(message = ErrorCode.TERMINATION_REASON_REQUIRED)
    @JsonProperty(value = "terminationReasonId")
    private TerminationReason terminationReason;
}
