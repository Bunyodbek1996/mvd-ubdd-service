package uz.ciasev.ubdd_service.mvd_core.api.court.service.material;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uz.ciasev.ubdd_service.mvd_core.api.court.CourtEventHolder;
import uz.ciasev.ubdd_service.mvd_core.api.court.CourtMethod;
import uz.ciasev.ubdd_service.mvd_core.api.court.service.CheckCourtDuplicateRequestService;
import uz.ciasev.ubdd_service.mvd_core.api.court.service.CourtRequestOrderService;
import uz.ciasev.ubdd_service.mvd_core.api.court.service.MaterialHelpCourtService;
import uz.ciasev.ubdd_service.mvd_core.api.court.service.material.action.CourtActionManager;
import uz.ciasev.ubdd_service.mvd_core.api.court.service.material.action.MaterialActionContext;
import uz.ciasev.ubdd_service.mvd_core.api.court.service.material.action.MaterialBuilderContext;
import uz.ciasev.ubdd_service.mvd_core.api.court.dto.third.ksinn.ThirdCourtRequest;
import uz.ciasev.ubdd_service.entity.court.CourtMaterial;
import uz.ciasev.ubdd_service.entity.court.CourtMaterialDecision;
import uz.ciasev.ubdd_service.entity.court.CourtMaterialFields;
import uz.ciasev.ubdd_service.entity.resolution.decision.Decision;
import uz.ciasev.ubdd_service.exception.court.CourtValidationException;
import uz.ciasev.ubdd_service.service.publicapi.dto.eventdata.PublicApiWebhookEventDataCourtDTO;
import uz.ciasev.ubdd_service.service.publicapi.eventdata.PublicApiWebhookEventCourtDataService;
import uz.ciasev.ubdd_service.service.publicapi.eventdata.PublicApiWebhookEventPopulationService;
import uz.ciasev.ubdd_service.repository.court.CourtMaterialDecisionRepository;
import uz.ciasev.ubdd_service.repository.court.CourtMaterialRepository;
import uz.ciasev.ubdd_service.service.court.material.CourtMaterialFieldsService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourtThreeMaterialMethodService {

    private final CourtActionManager actionManager;
    private final CourtMaterialRepository materialRepository;
    private final CourtMaterialDecisionRepository materialDecisionRepository;
    private final CourtMaterialFieldsService courtFieldsService;

    private final CourtEventHolder courtEventHolder;
    private final MaterialHelpCourtService helpCourtService;

    @Transactional
    public void accept(ThirdCourtRequest request) {
        handle(request);
    }


    private void handle(ThirdCourtRequest request) {

        CourtMaterial material = getMaterialSupplier(request).get();

        List<CourtMaterialDecision> materialDecisions = materialDecisionRepository.findAllByCourtMaterialId(material.getId());

        CourtMaterialFields courtFields = courtFieldsService.getCurrent(material);

        MaterialBuilderContext builderContext = MaterialBuilderContext.builder()
                .material(material)
                .courtMaterialFields(courtFields)
                .materialDecisions(materialDecisions)
                .violatorsId(materialDecisions.stream().map(CourtMaterialDecision::getDecision).map(Decision::getViolatorId).collect(Collectors.toList()))
                .build();

        MaterialActionContext actionContext = MaterialActionContext.builder()
                .material(material)
                .courtFields(courtFields)
                .materialDecisions(materialDecisions)
                .decisions(materialDecisions.stream().map(CourtMaterialDecision::getDecision).collect(Collectors.toList()))
                .build();


        courtEventHolder.init();

        actionManager.accept(builderContext, actionContext, request);

        courtEventHolder.close();

    }

    private Supplier<CourtMaterial> getMaterialSupplier(ThirdCourtRequest request) {
        if (request.getMaterialId() != null) {
            return () -> materialRepository.findById(request.getMaterialId()).orElseThrow(() -> new CourtValidationException("Material case not find"));
        }

        if (request.getInstance().equals(2L) && request.getClaimReviewId() != null) {
            return () -> courtFieldsService.findByClaimId(request.getClaimReviewId())
                    .map(f -> materialRepository.findById(f.getMaterialId()).get())
                    .orElseThrow(() -> new CourtValidationException("Material case not find by claimReviewId for empty material id"));
        }
        throw new CourtValidationException("Material id empty");
    }
}
