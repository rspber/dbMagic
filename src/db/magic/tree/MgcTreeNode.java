package db.magic.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import db.magic.util.Str;

public class MgcTreeNode extends MgcTreeLeaf {

	private List<IMgcTreeNode> children = new ArrayList<>();

	private boolean isExpanded;

	public MgcTreeNode(final String name) {
		super(name);
	}

	@Override
	public void setModified(final boolean value) {
		children.forEach ( v -> v.setModified(value) );
		super.setModified(value);
	}

	@Override
	public boolean wasModified() {
		for (int i = children.size(); --i >= 0; ) {
			if (children.get(i).wasModified()) {
				return true;
			}
		}
		return super.wasModified();
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(final boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	@Override
	public IMgcTreeNode[] getChildrens() {
		return children.toArray(new IMgcTreeNode[children.size()]);
	}

	@Override
	public boolean hasChildren() {
		return children.size() > 0;
	}

	@Override
	public void forEach(final Consumer<IMgcTreeNode> proc) {
		for (int i = 0; i < children.size(); ++i) {
			proc.accept(children.get(i));
		}
	}

	@Override
	public void forEachReverse(final Consumer<IMgcTreeNode> proc) {
		for (int i = children.size(); --i >= 0; ) {
			proc.accept(children.get(i));
		}
	}

	public void addChild(final IMgcTreeNode child) {
		if (child != null) {
			modified_ = true;
			children.add(child);
			child.setParent(this);
		}
	}

	@Override
	public void removeChild(final IMgcTreeNode child) {
		if (child != null) {
			modified_ = true;
			children.remove(child);
			child.setParent(null);
		}
	}

	public void removeChildAll() {
		for (int i = children.size(); --i >= 0; ) {
			removeChild(children.get(i));
		}
	}

	public void setChildren(final List<IMgcTreeNode> children) {
		removeChildAll();
		children.forEach( node -> addChild(node));
	}

	@Override
	public IMgcTreeNode getChild(final String name) {
		for (final IMgcTreeNode node : children) {
			if (Str.eq(node.getName(), name)) {
				return node;
			}
		}
		return null;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

}