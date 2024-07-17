package db.magic.ui.views.dbs;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import db.magic.MgcImg;

public class DBsLabelProvider extends LabelProvider {

	final MgcImg img = MgcImg.getDefault();

	@Override
	public String getText(final Object obj) {
		if (obj instanceof TableNode) {
			final TableNode tableNode = (TableNode)obj;
			return tableNode.getName();
		}
		else if (obj instanceof SchemaNode) {
			SchemaNode schemaNode = (SchemaNode) obj;
			return schemaNode.getName();
		}
		else {
			return obj.toString();
		}
	}

	@Override
	public Image getImage(Object obj) {

		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;

		if (obj instanceof RootNode) {
			return img.getImage(MgcImg.IMG_CODE_DB_ROOT);
		}
		else if (obj instanceof DBNode) {
			DBNode dbNode = (DBNode)obj;
			if (dbNode.isConnected()) {
				return img.getImage(MgcImg.IMG_CODE_CONNECTED_DB);
			}
			else {
				return img.getImage(MgcImg.IMG_CODE_DB);
			}
		}
		else if (obj instanceof SchemaNode) {
			return img.getImage(MgcImg.IMG_CODE_SCHEMA);
		}
		else if (obj instanceof TableNode) {
			return img.getImage(MgcImg.IMG_CODE_TABLE);
		}
		else if(obj instanceof FolderNode){
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		}

		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}

}