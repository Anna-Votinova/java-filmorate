package ru.yandex.practicum.filmorate.utilities;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.LocalDate;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AfterOrEqualData.ValidatorOfDate.class)
public @interface AfterOrEqualData {
    String message() default "{message.key}";
    String movieDay();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    public class ValidatorOfDate implements ConstraintValidator<AfterOrEqualData, LocalDate> {

        private String validDate;
        private LocalDate movieBirthday;

        @Override
        public void initialize(AfterOrEqualData constraintAnnotation) {
            validDate = constraintAnnotation.movieDay();
        }


        @Override
        public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
            boolean isProperDate = false;
            String[] splitDate = validDate.split("-");
            movieBirthday = LocalDate.of(Integer.valueOf(splitDate[0]), Integer.valueOf(splitDate[1]), Integer.valueOf(splitDate[2]));
            if (date.isAfter(movieBirthday) || date.isEqual(movieBirthday)) {
                isProperDate = true;
            }
            return isProperDate;
        }
    }
}
