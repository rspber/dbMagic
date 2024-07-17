package db.magic;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

public class MgcImg {

	public static final String IMG_CODE_CLEAR           = "clear.gif";
	public static final String IMG_CODE_COLLAPSE_ALL    = "collapse_all.gif";
	public static final String IMG_CODE_COLUMN          = "column.gif";
	public static final String IMG_CODE_CONNECTED_DB    = "connected_db.png";
	public static final String IMG_CODE_DB_ROOT         = "db_root.png";
	public static final String IMG_CODE_DB              = "db.png";
//	public static final String IMG_CODE_DB              = "db.gif";		// plugin.xml
	public static final String IMG_CODE_DB_ADD          = "db_add.gif";
	public static final String IMG_CODE_DB_EDIT         = "db_edit.gif";
	public static final String IMG_CODE_DB_COPY         = "db_copy.gif";
	public static final String IMG_CODE_DB_DELETE       = "db_delete.gif";
	public static final String IMG_CODE_EXECUTE         = "execute.gif";	// plugin.xml
	public static final String IMG_CODE_NEXT            = "next.gif";
	public static final String IMG_CODE_PREVIOUS        = "previous.gif";
	public static final String IMG_CODE_SAMPLE          = "sample.gif";
//	public static final String IMG_CODE_SCRIPT          = "script.gif";		// plugin.xml
	public static final String IMG_CODE_SQL             = "sql.gif";
	public static final String IMG_CODE_REFRESH         = "refresh.gif";
	public static final String IMG_CODE_SCHEMA          = "schema.png";
	public static final String IMG_CODE_TABLE           = "table.gif";
	public static final String IMG_CODE_WARNING         = "warning.gif";

	private final ImageRegistry imageRegistry = DBMagicPlugin.getDefault().getImageRegistry();
	private final Map<String, Image> map = new HashMap<>();

	private static MgcImg fDefault;
	public static synchronized MgcImg getDefault() {
		if (MgcImg.fDefault == null) {
			MgcImg.fDefault = new MgcImg();
		}
		return MgcImg.fDefault;
	}

	private MgcImg() {
	}

	public Image getImage(final String imageCode) {
		final Image image = map.get(imageCode); 
		return image != null ? image : createImage(imageCode);
	}

	private Image createImage(final String imageCode) {
		final ImageDescriptor id = getImageDescriptor(imageCode);
		if (id != null) {
			final Image image = id.createImage();
			map.put(imageCode, image);
			return image;
		}
		else {
			return null;
		}
	}

	public ImageDescriptor getImageDescriptor(final String imageCode) {
		final ImageDescriptor id = imageRegistry.getDescriptor(imageCode);
		return id != null ? id : registerImage(imageCode);
	}

	private ImageDescriptor registerImage( final String fileName) {
		try {
			final IPath path = new Path("icons/" + fileName);
			final URL url = DBMagicPlugin.getDefault().find(path);
			if (url != null) {
				final ImageDescriptor id = ImageDescriptor.createFromURL(url);
				imageRegistry.put(fileName, id);
				return id;
			}
		}
		catch (Exception e) {
			DBMagicPlugin.showError(e);
		}
		return null;
	}

	public void clear() {
		map.forEach( (k,v) -> {
			v.dispose();
		});
		map.clear();
	}
}
