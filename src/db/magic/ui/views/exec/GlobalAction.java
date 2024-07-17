package db.magic.ui.views.exec;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;

import db.magic.MgcImg;

public class GlobalAction extends Action {

	public static final int EXEC_SQL = 1011;
	public static final int NEXT_SQL = 1012;
	public static final int PREV_SQL = 1013;
	public static final int CLEAR    = 1014;

	private MgcImg img = MgcImg.getDefault();

	private final SQLExecView sqlExecView;

	private int operation;

	public GlobalAction(final SQLExecView sqlExecView, final int operation) {
		this.sqlExecView = sqlExecView;
		this.operation = operation;
		setImage(operation);
	}

	public void run() {
		if (sqlExecView.getSqlViewer().canDoOperation(operation)) {
			sqlExecView.getSqlViewer().doOperation(operation);
		}
		else {
			throw new IllegalAccessError("unknown Action.");
		}
	}

	public void setImage(final int operation) {
		switch (operation) {
		case ITextOperationTarget.UNDO:
			setImageDescriptor(platformImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
			setText("Undo");
			setActionDefinitionId(IWorkbenchActionDefinitionIds.UNDO);
			// setAccelerator(SWT.CTRL | 'Z');
			return;
		case ITextOperationTarget.REDO:
			setImageDescriptor(platformImageDescriptor(ISharedImages.IMG_TOOL_REDO));
			setText("Redo");
			setActionDefinitionId(IWorkbenchActionDefinitionIds.REDO);
			// setAccelerator(SWT.CTRL | 'Y');
			return;
		case ITextOperationTarget.COPY:
			setImageDescriptor(platformImageDescriptor(ISharedImages.IMG_TOOL_COPY));
			setText("Copy");
			setActionDefinitionId(IWorkbenchActionDefinitionIds.COPY);
			// setAccelerator(SWT.CTRL | 'C');
			return;
		case ITextOperationTarget.CUT:
			setImageDescriptor(platformImageDescriptor(ISharedImages.IMG_TOOL_CUT));
			setText("Cut");
			setActionDefinitionId(IWorkbenchActionDefinitionIds.CUT);
			// setAccelerator(SWT.CTRL | 'X');
			return;
		case ITextOperationTarget.PASTE:
			setImageDescriptor(platformImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
			setText("Paste");
			setActionDefinitionId(IWorkbenchActionDefinitionIds.PASTE);
			// setAccelerator(SWT.CTRL | 'V');
			return;
		case ITextOperationTarget.DELETE:
			setImageDescriptor(platformImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
			setText("Delete");
			setActionDefinitionId(IWorkbenchActionDefinitionIds.DELETE);
			return;
		case EXEC_SQL:
			setText("Ececute");
			setImageDescriptor(img.getImageDescriptor(MgcImg.IMG_CODE_EXECUTE));
			return;
		case NEXT_SQL:
			setText("Next");
			setImageDescriptor(img.getImageDescriptor(MgcImg.IMG_CODE_NEXT));
			return;
		case PREV_SQL:
			setText("Prev");
			setImageDescriptor(img.getImageDescriptor(MgcImg.IMG_CODE_PREVIOUS));
			return;
		case CLEAR:
			setText("Clear");
			setImageDescriptor(img.getImageDescriptor(MgcImg.IMG_CODE_CLEAR));
			return;
		default:
			break;
		}
	}

	private ImageDescriptor platformImageDescriptor(final String imageName) {
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(imageName);
	}

}