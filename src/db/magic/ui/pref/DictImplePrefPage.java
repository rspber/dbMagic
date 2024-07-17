package db.magic.ui.pref;

import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import db.magic.dictios.DictImple;
import db.magic.dictios.DictiosSupplyer;
import db.magic.dictios.NamedItem;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;

public class DictImplePrefPage extends CRUDTableNamedPrefPage<DictImple> {

	private static final String M_STORE_ID = "DictImplePrefer";
	private static final String M_Tit = "Tit";
	private static final String M_Nam = "Nam";
	private static final String M_Typ = "Typ";
	private static final String M_NrSep = "NrSep";
	private static final String M_Key = "Key";
	private static final String M_Def = "Def";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private static String[] getColumns() {
		final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);
		return new String[] {
			msg.get(M_Nam),
			msg.get(M_Typ),
			msg.get(M_NrSep),
			msg.get(M_Key),
			msg.get(M_Def)
   		};
	}
	
	public DictImplePrefPage() {
		super(DictiosSupplyer.getDefault(), getColumns(),
		new ColumnLayoutData[] {
	   		new ColumnWeightData(3), 
	   		new ColumnWeightData(1),
	   		new ColumnWeightData(1),
	   		new ColumnWeightData(1),
	   		new ColumnWeightData(7)
   		});
		setDescription(msg.get(M_Tit));
	}

	@Override
	protected ValuesDialog newInputDialog(final NamedItem<DictImple> named) {
		return new DictImpleInputDialog(getShell(), named);
	}

	@Override
	protected LabelProvider getLabelProvider() {
		return new ThisLabelProvider();
	}

	private static class ThisLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public String getColumnText(final Object element, final int columnIndex) {
			if (element instanceof NamedItem) {
				final NamedItem<DictImple> named = (NamedItem)element; 
				final DictImple impl = named.getItem();
				switch( columnIndex ) {
				case 0: return named.getName();
				case 1: return impl.dictType;
				case 2: return impl.nrSep;
				case 3: return impl.keyStr();
				case 4: return DictImpleInputDialog.packDictImple(impl);
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