package rest.antifraud.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import rest.antifraud.annotations.CardNumberConstraint;

import java.util.Arrays;


public class CardNumberValidator implements ConstraintValidator<CardNumberConstraint, String> {
    @Override
    public void initialize(CardNumberConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        int[] values = value.chars().map(Character::getNumericValue).toArray();

        for (int i = 0; i < values.length; i+=2) {
            values[i] *= 2;
            if (values[i] >= 10) {
                values[i] -= 9;
            }
        }
        int sum = Arrays.stream(values).sum();

        return sum % 10 == 0;
    }
}
