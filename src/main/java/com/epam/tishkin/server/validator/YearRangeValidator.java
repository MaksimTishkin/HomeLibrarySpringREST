package com.epam.tishkin.server.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Year;

public class YearRangeValidator implements ConstraintValidator<YearRange, Integer> {
    private int yearOfFirstBookInWorld;
    private int currentYear;

    @Override
    public void initialize(YearRange yearRange) {
        yearOfFirstBookInWorld = yearRange.minDate();
        currentYear = Year.now().getValue();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value >= yearOfFirstBookInWorld && value <= currentYear;
    }
}
