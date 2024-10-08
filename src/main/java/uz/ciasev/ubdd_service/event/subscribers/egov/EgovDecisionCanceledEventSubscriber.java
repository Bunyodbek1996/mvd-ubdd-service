package uz.ciasev.ubdd_service.event.subscribers.egov;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.ciasev.ubdd_service.entity.resolution.Resolution;
import uz.ciasev.ubdd_service.entity.resolution.decision.Decision;
import uz.ciasev.ubdd_service.event.AdmEventType;
import uz.ciasev.ubdd_service.event.subscribers.DecisionCanceledSubscriber;
import uz.ciasev.ubdd_service.service.webhook.egov.EgovWebhookCreateService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EgovDecisionCanceledEventSubscriber extends DecisionCanceledSubscriber {

    private final EgovWebhookCreateService service;

    @Override
    public void apply(List<Decision> decisions) {
        decisions.stream()
                .findFirst()
                .map(Decision::getResolution)
                .map(Resolution::getAdmCase)
                .ifPresent(admCase -> service.createWebhooks(admCase, AdmEventType.DECISIONS_CANCEL));
    }
}
