package db.magic.ui.pref;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import db.magic.store.PrefStore;
import db.magic.util.Str;

public abstract class MapedFieldPrefPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private final Map<String, Widget> map = new HashMap<>();

	private final PrefStore prefStore;
	
	public MapedFieldPrefPage(final PrefStore prefStore) {
		super();
		this.prefStore = prefStore;
	}

	@Override
	public void init(final IWorkbench workbench) {
	}

	protected void addField(final String key, final Widget field) {
		final String value = prefStore.getString(key);
		if (field instanceof Text) {
			((Text)field).setText(Str.nn(value));
		}
		else if (field instanceof Button) {
			((Button)field).setSelection(Str.eq(value, "true"));
		}
		map.put(key, field);
	}

	@Override
	public boolean performOk() {
		map.forEach( (k,v) -> {
			if (v instanceof Text) {
				prefStore.setValue(k, ((Text)v).getText());
			}
			else if (v instanceof Button) {
				prefStore.setValue(k, ((Button)v).getSelection());
			}
		});
		return super.performOk();
	}

}