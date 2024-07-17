package db.magic.ui.views.resu;

import java.util.function.Function;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import db.magic.MgcPlugC;
import db.magic.db.ResuTableElement;
import db.magic.dictios.FDictio;
import db.magic.dictios.Foreign;

public class ResuLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {

	private final Function<ResuTableElement, Boolean> isRowSelected; 

	public ResuLabelProvider(final Function<ResuTableElement, Boolean> isRowSelected) {
		this.isRowSelected = isRowSelected;
	}

	@Override
	public String getColumnText(final Object ele, final int columnIndex) {
		if (ele instanceof ResuTableElement) {
			final ResuTableElement element = (ResuTableElement)ele;
			if (columnIndex == 0) {
				return String.valueOf(element.recordNo);
			}
			else {
				final Object elem = element.getPictItem(columnIndex - 1);
				if (elem != null) {
					return elem.toString();
				}
				return null;
			}
		}
		return null;
	}

	@Override
	public Image getColumnImage(final Object ele, final int columnIndex) {
		return getImage(ele);
	}

	public Image getImage(final Object ele) {
		return null;
	}

	@Override
	public Color getForeground(Object ele, int columnIndex) {
		return null;
	}

	@Override
	public Color getBackground(Object ele, int columnIndex) {
		if (ele instanceof ResuTableElement) {
			final ResuTableElement element = (ResuTableElement)ele;
			Color color = null;
			if (columnIndex > 0) {
				final FDictio dictio = element.getDictio(columnIndex-1); 
				if (dictio != null) {
					color = dictio.isMgcDictio() ? MgcPlugC.DICTIO_CELL : MgcPlugC.IDX_CELL;
				}
				final Foreign foreign = element.getForeign(columnIndex-1);
				if (foreign != null) {
					color = MgcPlugC.FOREIGN_CELL;
				}
			}
			if (isRowSelected.apply(element) ) {
				color = MgcPlugC.SELECTED_ROW;
			}
			return color;
		}
		return null;
	}

}