package db.magic.tree;

import java.util.function.Consumer;

public class MgcTreeLeaf implements IMgcTreeNode  {

	private IMgcTreeNode parent;
	private String name;

	protected boolean modified_;

	public MgcTreeLeaf(final String name) {
		this.name = name;
	}

	@Override
	public void setModified(final boolean value) {
		modified_ = value;
	}

	@Override
	public boolean wasModified() {
		return modified_;
	}

	public void setName(final String name) {
		modified_ = true;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setParent(final IMgcTreeNode parent) {
		this.parent = parent;
	}

	@Override
	public IMgcTreeNode getParent() {
		return parent;
	}

	@Override
	public IMgcTreeNode[] getChildrens() {
		return null;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public IMgcTreeNode getChild(final String name) {
		return null;
	}

	@Override
	public void removeChild(final IMgcTreeNode child) {
	}
	
	@Override
	public void forEach(final Consumer<IMgcTreeNode> proc) {
	}
	
	@Override
	public void forEachReverse(final Consumer<IMgcTreeNode> proc) {
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

}