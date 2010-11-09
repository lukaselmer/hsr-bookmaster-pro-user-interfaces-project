package validators;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;
import com.jgoodies.validation.util.ValidationUtils;

import domain.Copy;

public class LoanValidator implements Validator<Copy> {

	@Override
	public ValidationResult validate(Copy copy) {
		PropertyValidationSupport support = new PropertyValidationSupport(copy, "Exemplar");
		
		if (ValidationUtils.isBlank("" + copy.getInventoryNumber())){
			support.addError("Exemplar-ID", "Muss ausgefüllt werden");
		} else if (ValidationUtils.hasBoundedLength("Exemplar-ID", 3, 100)){
			support.addError("Exemplar-ID", "blah");
		}
		return support.getResult();
	}

}
