package com.epam.tishkin.server.validator;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.Year;
import java.util.Date;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = YearRangeValidator.class)
@Documented
public @interface YearRange {
    //The first book in the world was printed in 1457
    int minDate() default 1457;
    String message() default "The year value must be between {minDate} and current year";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
