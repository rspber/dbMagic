package db.magic.ui.views.resu;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import db.magic.db.ResuTableElement;
import db.magic.ui.ui.UI;

public class ResuView extends EditorPart {

	ResuOutlinePage contentOutlinePage;

	public Composite main;

	private ResuTableLayer top = new ResuTableLayer(this);

	private List<ResuTableSubLayer> layers = new ArrayList<>();

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter != null && IContentOutlinePage.class.equals(adapter)) {
			if (contentOutlinePage == null) {
				contentOutlinePage = new ResuOutlinePage();
				final IEditorInput input = getEditorInput();

				if (input != null) {
					contentOutlinePage.setInput(input);
				}
			}
			return contentOutlinePage;
		}
		return super.getAdapter(adapter);
	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public void createPartControl(final Composite parent) {

		main = parent;
		UI.GridLayout(main).gridLayout(1, false).marginWH(0, 0).spacingHV(0, 0).apply();

		parent.getParent().addControlListener(new ControlAdapter() {
	        @Override
	        public void controlResized(final ControlEvent e) {
	        	resizeInternal();
	        }
	    });
	}

	public void createMainPage(final String partName, final ResuTableElement[] elements) {

		if (partName != null) {
			setPartName(partName);
		}

		top.createTableLayer(partName, elements);

		getSite().setSelectionProvider(getViewer());
	}

	public ResuTableSubLayer subTableLayer(final ResuTableLayer subLayer) {
		if( layers.size() > 0) {
			if (subLayer == top) {
				return layers.get(0);
			}
			for( int i = 0; i < layers.size(); ++i) {
				if (subLayer == layers.get(i)) {
					if (i + 1 < layers.size()) {
						return layers.get(i + 1);
					}
					break;
				}
			}
		}
		final ResuTableSubLayer layer = new ResuTableSubLayer(this);
		layers.add(layer);
		return layer;
	}

	public void resizeInternal() {
		if (layers.size() > 0) {

			Rectangle area = main.getParent().getClientArea();

			int h = 0;
			for(final ResuTableSubLayer layer: layers) {
	
				final GridData gridData = (GridData)layer.table.getLayoutData();
				if (layer.table.getVisible()) {
					h += gridData.heightHint + 22;
					gridData.widthHint = area.width - 22;
				}
			}

			final GridData gridData = (GridData)top.table.getLayoutData();
			gridData.heightHint = h > 0 ? area.height - h - 22 : -1;
			gridData.widthHint = area.width - 22;
		}
	}

	public void resizeLayers() {
		resizeInternal();
		main.layout(true);
	}

	public void setInput(final IEditorInput input) {
		super.setInput(input);
	}

	public TableViewer getViewer() {
		return top.getViewer();
	}

	@Override
	public void doSave(final IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
	}

	@Override
	public boolean isDirty() {
		return false;
	}

}