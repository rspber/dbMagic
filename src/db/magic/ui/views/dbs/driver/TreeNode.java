package db.magic.ui.views.dbs.driver;

import java.util.Map;
import java.util.TreeMap;

class TreeNode extends TreeLeaf {

	private Map<String, TreeLeaf> map = new TreeMap<>();

	public TreeNode(final String name) {
		super(name);
	}

	public TreeLeaf getChild(final String name) {
		return map.get(name);
	}

	public void addChild(final TreeLeaf leaf) {
		map.put(leaf.getName(), leaf);
	}

	public TreeLeaf[] getChildrens() {
		return map.values().toArray(new TreeLeaf[map.size()]);
	}

	public boolean hasChildren() {
		return map.size() > 0;
	}

}