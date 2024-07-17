package db.magic.ui.pref;

import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import db.magic.dictios.Foreign;
import db.magic.dictios.ForeignsSupplyer;
import db.magic.dictios.NamedItem;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;

public class ForeignsPrefPage extends CRUDTableNamedPrefPage<Foreign> {

	private static final String M_STORE_ID = "ForeignPrefer";
	private static final String M_Tit = "Tit";
	private static final String M_Nam = "Nam";
	private static final String M_Typ = "Typ";
	private static final String M_RefFld = "RefFld";
	private static final String M_RefTab = "RefTab";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private static String[] getColumns() {
		final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);
		return new String[] {
			msg.get(M_Nam),
			msg.get(M_Typ),
			msg.get(M_RefFld),
			msg.get(M_RefTab)
   		};
	}

	public ForeignsPrefPage() {
		super(ForeignsSupplyer.getDefault(), getColumns(),
		new ColumnLayoutData[] {
	   		new ColumnWeightData(1), 
	   		new ColumnWeightData(1), 
	   		new ColumnWeightData(1), 
	   		new ColumnWeightData(3) 
   		});
		setDescription(msg.get(M_Tit));
	}

	@Override
	protected ValuesDialog newInputDialog(final NamedItem<Foreign> named) {
		return new ForeignInputDialog(getShell(), named);
	}

	@Override
	protected LabelProvider getLabelProvider() {
		return new ThisLabelProvider();
	}

	private static class ThisLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public String getColumnText(final Object element, final int columnIndex) {
			if (element instanceof NamedItem) {
				final NamedItem<Foreign> named = (NamedItem)element; 
				final Foreign foreign = named.getItem();
				switch( columnIndex ) {
				case 0: return named.getName();
				case 1: return foreign.getRefTypeStr();
				case 2: return foreign.refField;
				case 3: return foreign.refTable;
				default: return null;
				}
			}
			return null;
		}

		@Override
		public Image getColumnImage(final Object obj, final int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(final Object obj) {
			return null;
		}

	}

}