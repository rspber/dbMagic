package db.magic.ui.views.dbs.driver;

class TreeLeaf {

	private TreeLeaf parent;
	private String name;

	public TreeLeaf(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public TreeLeaf getParent() {
		return parent;
	}

	public void setParent(final TreeLeaf parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return name;
	}

}