package hack.core.config.security;

import hack.core.dao.PlayerDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RegistrationValidator implements Validator {

	@Autowired
	private PlayerDAO playerDAO;
	
	@Override
	public boolean supports(Class<?> aClass) {
		return RegistrationForm.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		RegistrationForm reg = (RegistrationForm) o;
		
		reg.setEmail(reg.getEmail().toLowerCase());
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty","You must enter an email address");
		if (reg.getEmail().length() < 6 || reg.getEmail().length() > 50) {
            errors.rejectValue("email", "Size.userForm.username","Your email must be between 6 and 50 characters");
        }
		if(playerDAO.doesPlayerAlreadyExist(reg.getEmail())) {
			errors.rejectValue("email", "Duplicate.userForm.username", "Someone has already registered with this email address");
		}

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty", "You must enter a password");
        if (reg.getPassword().length() < 1 || reg.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password", "The password must be between 1 and 32 characters");
        }

        if (!reg.getPasswordConfirm().equals(reg.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm", "The passwords must match");
        }

        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty", "You must enter an name");
		if (reg.getName().length() < 1 || reg.getName().length() > 50) {
            errors.rejectValue("name", "Size.userForm.username", "Your name must be between 1 and 50 characters");
        }
		
	}

}
