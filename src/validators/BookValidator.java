package validators;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;
import com.jgoodies.validation.util.ValidationUtils;

import domain.Book;

public class BookValidator implements Validator<Book> {

	public ValidationResult validate(Book book) {
		PropertyValidationSupport support = new PropertyValidationSupport(book, "Buch");

		if (ValidationUtils.isBlank(book.getName()))
			support.addError("Buchtitel", "Muss ausgefüllt werden");
		else if (!ValidationUtils.hasBoundedLength(book.getName(), 3, 255))
			support.addError("Buchtitel", "Länge muss zwischen 3 und 255 Zeichen lang sein");

		if (ValidationUtils.isBlank(book.getAuthor()))
			support.addError("Autor", "Muss ausgefüllt werden");
		else if (!ValidationUtils.hasBoundedLength(book.getAuthor(), 3, 255))
			support.addError("Autor", "Länge muss zwischen 3 und 255 Zeichen lang sein");

		if (ValidationUtils.isBlank(book.getPublisher()))
			support.addError("Verlag", "Muss ausgefüllt werden");
		else if (!ValidationUtils.hasBoundedLength(book.getPublisher(), 3, 100))
			support.addError("Verlag", "Länge muss zwischen 3 und 100 Zeichen lang sein");

		if (book.getShelf() == null)
			support.addError("Regal", "Muss ausgewählt werden");

		return support.getResult();
	}

}