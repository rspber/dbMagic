package db.magic.ui.views.dbs.driver;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import db.magic.MgcImg;

class DriverLabelProvider extends LabelProvider {

	final MgcImg img = MgcImg.getDefault();

	@Override
	public String getText(final Object obj) {
		return obj.toString();
	}

	@Override
	public Image getImage(final Object obj) {
		String imageKey = ISharedImages.IMG_OBJ_FILE;
		if (obj instanceof DBTypeNode) {
			return img.getImage(MgcImg.IMG_CODE_DB);
		}
		if (obj instanceof FolderNode) {
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		}
		if (obj instanceof FileNode) {
			imageKey = ISharedImages.IMG_OBJ_FILE;
		}
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}

}