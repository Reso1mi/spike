package top.imlgw.spike.validator;

import org.apache.commons.lang3.StringUtils;
import top.imlgw.spike.utils.ValidatorUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author imlgw.top
 * @date 2019/5/13 18:12
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required=false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required=constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return  ValidatorUtil.isMobile(s);
        }else {
            if(StringUtils.isEmpty(s)){
                return true;
            }else {
                return  ValidatorUtil.isMobile(s);
            }
        }
    }
}
