package db.magic.descr;

import java.util.List;
import java.util.Map;

import db.magic.tree.IMgcTreeNode;
import db.magic.util.Obj;
import db.magic.util.Str;
import db.magic.yaml.YamlIn;
import db.magic.yaml.YamlOut;
import db.magic.yaml.Yamlet;

public class DescrManager extends Yamlet {

	private static final String DESCRIPTIONS_YAML = "descriptions.yaml";

	private static final String tables = "tables";
	
	private static final String name = "name";
	private static final String column = "column";
	private static final String columns = "columns";
	private static final String descr = "descr";

	public final DescrRootNode ivisibleRoot = new DescrRootNode("ivisible");

	private static DescrManager fDefault;
	public static DescrManager getDefault() {
		if( fDefault == null ) {
			fDefault = new DescrManager();
			fDefault.initialization(DESCRIPTIONS_YAML);
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
		YamlIn.loadSections(yamlObject, (categoryName, tablesMap) -> {
			final DescrCategoryNode categoryNode = new DescrCategoryNode((String)categoryName);
			tablesMap.forEach( (tableId, tableMap) -> {
				categoryNode.addChild( yamlGetDescrTableNode(tableId, Obj.ofStringMap(tableMap)) );
			});
			ivisibleRoot.addChild(categoryNode);
		});
	}

	private static DescrTableNode yamlGetDescrTableNode(final String tableId, final Map<String, Object> tableMap) {
		final DescrTableNode tableNode = new DescrTableNode(tableId);
		tableNode.setFullName(Obj.ofString(tableMap.get(name)));
		tableNode.setDescription(Obj.ofString(tableMap.get(descr)));
		final List<Object> columnsList = Obj.ofList(tableMap.get(columns));
		if (columnsList != null) {
			columnsList.forEach( columnMap -> {
				tableNode.addChild(yamlGetDescrColumnNode(Obj.ofStringMap(columnMap)));
			});
		}
		return tableNode;
	}

	private static DescrColumnNode yamlGetDescrColumnNode(final Map<String, Object> columnMap) {
		final DescrColumnNode columnNode = new DescrColumnNode(Obj.ofString(columnMap.get(column)));
		columnNode.setFullName(Obj.ofString(columnMap.get(name)));
		columnNode.setDescription(Obj.ofString(columnMap.get(descr)));
		return columnNode;
	}

	@Override
	public String toYaml() {
		final StringBuilder sb = new StringBuilder();
		ivisibleRoot.forEach( categoryNode -> {
			YamlOut.yaml(sb, 0, categoryNode.getName(), null);
			switch (categoryNode.getName()) {
			case tables:
				categoryNode.forEach( tableNode -> {
					YamlOut.yaml(sb, 2, tableNode.getName(), null);
					YamlOut.yaml(sb, 4, name, ((DescrTableNode)tableNode).getFullName());
					YamlOut.text(sb, 4, descr, ((DescrTableNode)tableNode).getDescription());
					YamlOut.yaml(sb, 4, columns, null);
					tableNode.forEach( columnNode -> {
						YamlOut.liyaml(sb, 6, column, columnNode.getName());
						YamlOut.yaml(sb, 6, name, ((DescrColumnNode)columnNode).getFullName());
						YamlOut.text(sb, 6, descr, ((DescrColumnNode)columnNode).getDescription());
					});
				});
				break;
			}
		});
		return sb.toString();
	}

	private static DescrCategoryNode getCategory(final DescrRootNode root, final String name, final boolean force) {
 		if (root != null && !Str.isBlank(name)) {
	 		final IMgcTreeNode node = root.getChild(name);
 			if (node != null) {
 				return (DescrCategoryNode)node;
	 		}
	 		if (force) {
	 	 		root.addCategory(name);
	 		}
 		}
 		return null;
 	}

 	private static DescrTableNode getTable(final DescrCategoryNode category, final String tableId, final boolean force) {
 		if (category != null && !Str.isBlank(tableId)) {
 			final IMgcTreeNode node = category.getChild(tableId);
 			if (node != null) {
				return (DescrTableNode)node;
 			}
 			if (force) {
 				return category.addTable(tableId);
 			}
 		}
 		return null;
	}

 	private static DescrColumnNode getColumn(final DescrTableNode table, final String columnId, final boolean force) {
 		if (table != null && !Str.isBlank(columnId)) {
	 		final IMgcTreeNode node = table.getChild(columnId);
			if (node != null) {
				return (DescrColumnNode)node;
			}
	 		if (force) {
	 			return table.addColumn(columnId);
	 		}
 		}
 		return null;
 	}
 
	public void addItemData(final DescrItemData data) {
		final DescrCategoryNode categoryNode = getCategory(ivisibleRoot, data.category, true);
		final DescrTableNode tableNode = getTable(categoryNode, data.tableId, true);
		getColumn(tableNode, data.columnId, true);
	}

	public void updItemData(final DescrItemData old, final DescrItemData data) {
		if (Str.eq(old.category, data.category)) {
			final DescrCategoryNode categoryNode = getCategory(ivisibleRoot, old.category, false);
			if (categoryNode.getName().equals(tables)) {
				updTables(categoryNode, old, data);
			}
		}
		else {
			final DescrCategoryNode categoryNode = getCategory(ivisibleRoot, data.category, true);
			if (categoryNode.getName().equals(tables)) {
				final DescrTableNode tableNode = getTable(categoryNode, data.tableId, true);
				getColumn(tableNode, data.columnId, true);
			}
		}
	}

	private void updTables(final DescrCategoryNode categoryNode, final DescrItemData old, final DescrItemData data) {
		if (Str.eq(old.tableId, data.tableId)) {
			final DescrTableNode tableNode = getTable(categoryNode, old.tableId, false);
			if (Str.eq(old.columnId, data.columnId)) {
				// no changes
			}
			else {
				if (Str.isBlank(old.columnId)) {
					getColumn(tableNode, data.columnId, true);
				}
				else {
					if (!Str.isBlank(data.columnId)) {
						getColumn(tableNode, old.columnId, false).setName(data.columnId);
					}
				}
			}
		}
		else {
			if (Str.isBlank(data.columnId)) {
				if (Str.isBlank(old.tableId)) {
					categoryNode.addTable(data.tableId);
				}
				else {
					if (!Str.isBlank(data.tableId)) {
						getTable(categoryNode, old.tableId, false).setName(data.tableId);
					}
				}
			}
			else {
				final DescrTableNode tableNode = getTable(categoryNode, data.tableId, true);
				getColumn(tableNode, data.columnId, true);
			}
		}
	}

}