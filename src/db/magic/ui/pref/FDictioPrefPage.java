package db.magic.ui.pref;

import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import db.magic.dictios.FDictio;
import db.magic.dictios.FDictiosSupplyer;
import db.magic.dictios.NamedItem;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;

public class FDictioPrefPage extends CRUDTableNamedPrefPage<FDictio> {

	private static final String M_STORE_ID = "DictioPrefer";
	private static final String M_Tit = "Tit";
	private static final String M_Nam = "Nam";
	private static final String M_Mod = "Mod";
	private static final String M_Dic = "Dic";
	private static final String M_Ran = "Ran";
	private static final String M_Arg = "Arg";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);
	
	private static String[] getColumns() {
		final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);
		return new String[] {
			msg.get(M_Nam),
			msg.get(M_Mod),
			msg.get(M_Dic),
			msg.get(M_Ran),
			msg.get(M_Arg)
   		};
	}

	public FDictioPrefPage() {
		super(FDictiosSupplyer.getDefault(), getColumns(),
		new ColumnLayoutData[] {
	   		new ColumnWeightData(3), 
	   		new ColumnWeightData(1), 
	   		new ColumnWeightData(3), 
	   		new ColumnWeightData(1),
	   		new ColumnWeightData(1)
   		});
		setDescription(msg.get(M_Tit));
	}

	@Override
	protected ValuesDialog newInputDialog(final NamedItem<FDictio>named) {
		return new FDictioInputDialog(getShell(), named);
	}

	@Override
	protected LabelProvider getLabelProvider() {
		return new ThisLabelProvider();
	}

	private static class ThisLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public String getColumnText(final Object element, final int columnIndex) {
			if (element instanceof NamedItem) {
				final NamedItem<FDictio> named = (NamedItem)element; 
				final FDictio dictio = named.getItem();
				switch( columnIndex ) {
				case 0: return named.getName();
				case 1: return dictio.modeStr();
				case 2: return dictio.dictImple() != null ? dictio.dict.getName() : "?";
				case 3: return dictio.range > 0 ? String.valueOf(dictio.range) : "";
				case 4: return FDictioInputDialog.packArgs(dictio.args);
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