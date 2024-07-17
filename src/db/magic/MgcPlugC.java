package db.magic;

import org.eclipse.swt.graphics.Color;

public class MgcPlugC
{
	public static int DARK_MODE = 0;	// 0 - light, 1 - dark 

	public static final Color[] COMBO_DEFAULT   = new Color[] {new Color(null, 240, 248, 255), new Color(null,  64,  72,  80)};
	public static final Color[] COMBO_READ_ONLY = new Color[] {new Color(null, 240, 240, 240), new Color(null,  64,  64,  64)};

	public static final Color SELECTED_CELL_BACKGROUND = new Color(64, 160, 255);
	public static final Color SELECTED_CELL_NOT_FOCUS_BACKGROUND = new Color(160, 208, 255);
	public static final Color SELECTED_ROW = new Color(218, 236, 255);

	public static final Color IDX_CELL = new Color(242, 248, 255);
	public static final Color DICTIO_CELL = new Color(235, 250, 255);
	public static final Color FOREIGN_CELL = new Color(235, 235, 255);

	public static final String DBsView     = "db.magic.ui.views.DBsView";
	public static final String SqHView     = "db.magic.ui.views.SqHView";
	public static final String SQLExecView = "db.magic.ui.views.SQLExecView";
	public static final String ResuView    = "db.magic.ui.views.ResuView";

	public static Color ComboColor(final boolean readOnly) {
		return readOnly ? COMBO_READ_ONLY[DARK_MODE] : COMBO_DEFAULT[DARK_MODE];
	}

}