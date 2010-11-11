package validators;

public class SearchResult<T> {
	private final T t;
	private String searchString;

	public SearchResult(T t, String searchString) {
		this.t = t;
		this.searchString = searchString;
	}

	public boolean hasObject() {
		return t != null;
	}

	public T getObject() {
		return t;
	}

	public String getSearchString() {
		return searchString;
	}

}
