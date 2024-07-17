package db.magic.ui.pref.descr;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import db.magic.MgcImg;
import db.magic.descr.DescrCategoryNode;
import db.magic.descr.DescrColumnNode;
import db.magic.descr.DescrTableNode;

public class DescrLabelProvider extends LabelProvider {

	private final MgcImg img = MgcImg.getDefault();

	@Override
	public String getText(final Object obj) {
		return obj.toString();
	}

	@Override
	public Image getImage(Object obj) {

		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;

		if (obj instanceof DescrColumnNode) {
			return img.getImage(MgcImg.IMG_CODE_COLUMN);
		}
		else if (obj instanceof DescrTableNode) {
			return img.getImage(MgcImg.IMG_CODE_TABLE);
		}
		else if (obj instanceof DescrCategoryNode) {
			return img.getImage(MgcImg.IMG_CODE_SAMPLE);
		}

		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}

}