package uz.ciasev.ubdd_service.utils.validator;

import uz.ciasev.ubdd_service.exception.ErrorCode;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = QualificationValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidQualification {

    String message() default ErrorCode.PROTOCOL_QUALIFICATION_INVALID;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
