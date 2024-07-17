package db.magic.ui.views.sqh;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import db.magic.MgcImg;

public class SqHLabelProvider extends LabelProvider {

	private final MgcImg img = MgcImg.getDefault();

	@Override
	public String getText(final Object obj) {
		return obj.toString();
	}

	@Override
	public Image getImage(Object obj) {

		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;

		if (obj instanceof SqHQueryNode) {
			return img.getImage(MgcImg.IMG_CODE_SQL);
		}
		else if (obj instanceof SqHFolderNode) {
			imageKey = ISharedImages.IMG_OBJ_FOLDER;

		}
		else if (obj instanceof SqHCategoryNode) {
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		}

		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}

}