package validators;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;
import com.jgoodies.validation.util.ValidationUtils;

import domain.Copy;

public class SearchCopyForLoanValidator implements Validator<SearchResult<Copy>> {

	@Override
	public ValidationResult validate(SearchResult<Copy> searchResult) {
		PropertyValidationSupport support = new PropertyValidationSupport(searchResult, "Exemplar");
		if (ValidationUtils.isBlank(searchResult.getSearchString())) {
			support.addError("Exemplar-ID", "Muss ausgefüllt werden");
			return support.getResult();
		}
		if (!searchResult.hasObject()) {
			support.addError("Exemplar-ID", "Nicht gefunden");
			return support.getResult();
		}
		Copy c = searchResult.getObject();
		if (c.isLent()) {
			support.addError("Exemplar-ID", "Exemplar ist bereits ausgeliehen");
		}
		return support.getResult();
	}

}
