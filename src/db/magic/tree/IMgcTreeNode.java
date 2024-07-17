package db.magic.tree;

import java.util.function.Consumer;

public interface IMgcTreeNode {

	public String getName();
	public IMgcTreeNode getParent();
	public void setParent(final IMgcTreeNode parent);
	public IMgcTreeNode[] getChildrens();
	public boolean hasChildren();
	public void removeChild(final IMgcTreeNode child);
	public boolean isLeaf();
	public IMgcTreeNode getChild(final String name);
	public void forEach(final Consumer<IMgcTreeNode> proc);
	public void forEachReverse(final Consumer<IMgcTreeNode> proc);
	public void setModified(final boolean value);
	public boolean wasModified();

}