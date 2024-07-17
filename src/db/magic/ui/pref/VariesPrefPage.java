package db.magic.ui.pref;

import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import db.magic.dictios.NamedItem;
import db.magic.dictios.VariesSupplyer;
import db.magic.dictios.Vario;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;

public class VariesPrefPage extends CRUDTableNamedPrefPage<Vario> {

	private static final String M_STORE_ID = "VarioPrefer";
	private static final String M_Tit = "Tit";
	private static final String M_Nam = "Nam";
//	private static final String M_Typ = "Typ";
	private static final String M_Val = "Val";
//	private static final String M_Arg = "Arg";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private static String[] getColumns() {
		final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);
		return new String[] {
			msg.get(M_Nam),
//			msg.get(M_Typ),
			msg.get(M_Val),
//			msg.get(M_Arg)
		};
	}

	public VariesPrefPage() {
		super(VariesSupplyer.getDefault(), getColumns(),
		new ColumnLayoutData[] {
	   		new ColumnWeightData(2), 
//	   		new ColumnWeightData(1), 
	   		new ColumnWeightData(4), 
//	   		new ColumnWeightData(1) 
   		});
		setDescription(msg.get(M_Tit));
	}

	@Override
	protected ValuesDialog newInputDialog(final NamedItem<Vario> named) {
		return new VarioInputDialog(getShell(), named);
	}

	@Override
	protected LabelProvider getLabelProvider() {
		return new ThisLabelProvider();
	}

	private static class ThisLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public String getColumnText(final Object element, final int columnIndex) {
			if (element instanceof NamedItem) {
				final NamedItem<Vario> named = (NamedItem)element; 
				final Vario vario = named.getItem();
				switch( columnIndex ) {
				case 0: return named.getName();
//				case 1: return ""; //vario.getVarTypeStr();
				case 1: return vario.value;
//				case 3: return vario.arg;
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