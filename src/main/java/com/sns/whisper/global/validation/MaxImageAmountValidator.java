package com.sns.whisper.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class MaxImageAmountValidator implements
        ConstraintValidator<MaxImageAmount, List<MultipartFile>> {

    @Override
    public void initialize(MaxImageAmount constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<MultipartFile> images,
            ConstraintValidatorContext constraintValidatorContext) {
        return images.size() <= 5;
    }
}
