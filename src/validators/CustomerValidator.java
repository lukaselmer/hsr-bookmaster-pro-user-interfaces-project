package validators;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;
import com.jgoodies.validation.util.ValidationUtils;

import domain.Customer;

public class CustomerValidator implements Validator<Customer> {

	public ValidationResult validate(Customer customer) {
		PropertyValidationSupport support = new PropertyValidationSupport(customer, "Kunde");

		if (ValidationUtils.isBlank(customer.getName()))
			support.addError("Vorname", "Muss ausgefüllt werden");
		else if (!ValidationUtils.hasBoundedLength(customer.getName(), 3, 50))
			support.addError("Vorname", "Länge muss zwischen 3 und 50 Zeichen lang sein");

		if (ValidationUtils.isBlank(customer.getSurname()))
			support.addError("Nachname", "Muss ausgefüllt werden");
		else if (!ValidationUtils.hasBoundedLength(customer.getSurname(), 3, 50))
			support.addError("Nachname", "Länge muss zwischen 3 und 50 Zeichen lang sein");

		if (ValidationUtils.isBlank(customer.getStreet()))
			support.addError("Strasse", "Muss ausgefüllt werden");
		else if (!ValidationUtils.hasBoundedLength(customer.getStreet(), 3, 50))
			support.addError("Strasse", "Länge muss zwischen 3 und 50 Zeichen lang sein");

		if (customer.getZip() == null)
			support.addError("PLZ", "Muss ausgefüllt werden");
		else if (1000 > customer.getZip().intValue() || customer.getZip().intValue() > 9999)
			support.addError("PLZ", "Zahl zwischen 1000 und 9999 muss eingegeben werden");

		if (ValidationUtils.isBlank(customer.getCity()))
			support.addError("Stadt", "Muss ausgefüllt werden");
		else if (!ValidationUtils.hasBoundedLength(customer.getCity(), 3, 50))
			support.addError("Stadt", "Länge muss zwischen 3 und 50 Zeichen lang sein");

		return support.getResult();
	}

}