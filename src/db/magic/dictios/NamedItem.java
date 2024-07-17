package db.magic.dictios;

public class NamedItem<T> {

	private String name;
	private T item;

	public NamedItem(final String name, final T item) {
		this.name = name;
		this.item = item;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public T getItem() {
		return item;
	}

	public void setItem(final T p) {
		this.item = p;
	}

}