package db.magic.ui.views.sqh;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import db.magic.DBMagicPlugin;
import db.magic.ui.pref.ValuesDialog;
import db.magic.ui.ui.UI;
import db.magic.util.Str;

public class SqHAddDialog extends ValuesDialog {

	private Text categoryText;
	private Text folderText;
	private Text sqlText;

	private final SqHAddItemData data;

	public SqHAddDialog(final SqHAddItemData data) {
		super(DBMagicPlugin.getShell());
		this.data = data;
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Add");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 300);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite group = (Composite)super.createDialogArea(parent);

		final Composite div = UI.compositeGrid(group).gridLayout(1, false).eol().apply();

		{
			final Composite span = UI.compositeGrid(div).gridLayout(2, false).eol().apply();
			UI.label(span).text("Category").width(100).apply(); 
			categoryText = UI.text(span, SWT.BORDER).text(data.category).eol().apply();
		}

		{
			final Composite span = UI.compositeGrid(div).gridLayout(2, false).eol().apply();
			UI.label(span).text("Folder").width(100).apply();  
			folderText = UI.text(span, SWT.BORDER).text(data.folder).eol().apply();
		}

		{
			final Composite span = UI.compositeGrid(div).gridLayout(1, false).client().apply();
			UI.label(span).text("Sql").apply(); 
			sqlText = UI.text(span, SWT.BORDER).text(data.sql).eol().apply();
		}

		return group;
	}

	@Override
	protected boolean save() {
		data.category = categoryText.getText();
		data.folder = folderText.getText();
		data.sql = sqlText.getText();
				
		return !Str.isBlank(data.category) && (Str.isBlank(data.sql) || !Str.isBlank(data.folder));
	}

}