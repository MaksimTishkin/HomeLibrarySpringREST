package com.epam.tishkin.server.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Year;

public class YearRangeValidator implements ConstraintValidator<YearRange, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        int yearOfFirstBookInWorld = 1457;
        int currentYear = Year.now().getValue();
        return value >= yearOfFirstBookInWorld && value <= currentYear;
    }
}
