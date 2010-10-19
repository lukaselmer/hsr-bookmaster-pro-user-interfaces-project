package validators;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;
import com.jgoodies.validation.util.ValidationUtils;

import domain.Customer;

public class CustomerValidator implements Validator<Customer> {

	public ValidationResult validate(Customer customer) {
		PropertyValidationSupport support = new PropertyValidationSupport(customer, "Kunde");

		if (!ValidationUtils.hasBoundedLength(customer.getName(), 3, 50))
			support.addError("Vorname", "Länge muss zwischen 3 und 50 Zeichen lang sein");
		if (!ValidationUtils.hasBoundedLength(customer.getSurname(), 3, 50))
			support.addError("Nachname", "Länge muss zwischen 3 und 50 Zeichen lang sein");
		if (!ValidationUtils.hasBoundedLength(customer.getStreet(), 3, 50))
			support.addError("Strasse", "Länge muss zwischen 3 und 50 Zeichen lang sein");
		if (!ValidationUtils.hasBoundedLength(customer.getCity(), 3, 50))
			support.addError("Stadt", "Länge muss zwischen 3 und 50 Zeichen lang sein");
		if (1000 > customer.getZip() || customer.getZip() > 9999)
			support.addError("PLZ", "Wert zwischen 1000 und 9999 muss eingegeben werden");

		return support.getResult();
	}

}