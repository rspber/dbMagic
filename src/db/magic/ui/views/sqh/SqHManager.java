package db.magic.ui.views.sqh;

import java.util.List;

import db.magic.db.SqlUtil;
import db.magic.tree.IMgcTreeNode;
import db.magic.util.Obj;
import db.magic.util.Str;
import db.magic.yaml.YamlIn;
import db.magic.yaml.YamlOut;
import db.magic.yaml.Yamlet;

public class SqHManager extends Yamlet {

	private static final String SQL_HANDBOOK_YAML = "sql_handbook.yaml";
	private static final String history = "history";

	public final SqHRootNode ivisibleRoot = new SqHRootNode("ivisible");

	private static SqHManager fDefault;
	public static SqHManager getDefault() {
		if( fDefault == null ) {
			fDefault = new SqHManager();
			fDefault.initialization(SQL_HANDBOOK_YAML);
		}
		return fDefault;
	}
	
	@Override
	public void clear() {
		ivisibleRoot.removeChildAll();
	}

	@Override
	public void setModified(final boolean value) {
		ivisibleRoot.setModified(value);
	}

	@Override
	public boolean wasModified() {
		return ivisibleRoot.wasModified();
	}

	@Override
	public void load(final Object yamlObject) {
		YamlIn.loadSections(yamlObject, (categoryName, categMap) -> {
			final SqHCategoryNode categoryNode = new SqHCategoryNode((String)categoryName);
			categMap.forEach( (tableName, sqlList) -> {
				categoryNode.addChild( yamlGetSqHFolderNode(tableName, Obj.ofList(sqlList)) );
			});
			ivisibleRoot.addChild(categoryNode);
		});
		if (!ivisibleRoot.hasChildren()) {
			ivisibleRoot.addChild(new SqHCategoryNode(history));
		}
	}

	private static SqHFolderNode yamlGetSqHFolderNode(final String tableName, final List<Object> list) {
		final SqHFolderNode folderNode = new SqHFolderNode(tableName);
		if (list != null) {
			list.forEach( query -> {
				folderNode.addChild(new SqHQueryNode(Obj.ofString(query)));
			});
		}
		return folderNode;
	}

	@Override
	public String toYaml() {
		final StringBuilder sb = new StringBuilder();
		YamlOut.yamlTreeNode(sb, 0, ivisibleRoot);
		return sb.toString();
	}

 	public boolean addHistory(final String sqlQuery) {
		if (!Str.isBlank(sqlQuery)) {
			return addQuery(getFolder(getCategory(ivisibleRoot, history, false), SqlUtil.getSQLname(sqlQuery), true), sqlQuery);
		}
		return false;
 	}

 	private static SqHCategoryNode getCategory(final SqHRootNode root, final String name, final boolean force) {
 		if (root != null && !Str.isBlank(name)) {
 			final IMgcTreeNode node = root.getChild(name);
 			if (node != null) {
 				return (SqHCategoryNode)node;
	 		}
	 		if (force) {
	 	 		return root.addCapegory(name);
	 		}
 		}
 		return null;
 	}

 	private static SqHFolderNode getFolder(final SqHCategoryNode category, final String name, final boolean force) {
 		if (category != null && !Str.isBlank(name)) {
 			final IMgcTreeNode node = category.getChild(name);
			if (node != null) {
				return (SqHFolderNode)node;
 			}
 			if (force) {
 				return category.addFolder(name);
 			}
 		}
 		return null;
	}

 	private static boolean addQuery(final SqHFolderNode folder, final String sqlQuery) {
 		if (folder != null && !Str.isBlank(sqlQuery)) {
 			final IMgcTreeNode node = folder.getChild(sqlQuery);
			if (node != null) {
				return false;
			}
			folder.addQuery(sqlQuery);
			return true;
 		}
 		return false;
 	}
 
	public boolean addItemData(final SqHAddItemData data) {
		return addQuery(getFolder(getCategory(ivisibleRoot, data.category, true), data.folder, true), data.sql);
	}

 	public boolean hasPrevious() {
		return false;
	}

	public boolean hasNext() {
		return false;
	}

	public SqHQueryNode nextItem() {
		// TODO Auto-generated method stub
		return null;
	}

	public SqHQueryNode prevItem() {
		// TODO Auto-generated method stub
		return null;
	}

}