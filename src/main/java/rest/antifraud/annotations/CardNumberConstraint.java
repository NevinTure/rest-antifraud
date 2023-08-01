package rest.antifraud.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import rest.antifraud.validations.CardNumberValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CardNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CardNumberConstraint {

    String message() default "invalid card-number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
