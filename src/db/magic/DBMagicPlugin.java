package db.magic;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import db.magic.db.DBManager;
import db.magic.descr.DescrManager;
import db.magic.dictios.DictiosSupplyer;
import db.magic.dictios.FDictiosSupplyer;
import db.magic.dictios.ForeignsSupplyer;
import db.magic.dictios.VariesSupplyer;
import db.magic.store.MgcPrefStoreManager;
import db.magic.ui.views.sqh.SqHManager;
import db.magic.util.Str;

public class DBMagicPlugin extends AbstractUIPlugin {

	private final static String TITLE = "DB Magic";
	
	private MgcImg img;

	private static DBMagicPlugin fDefault;
	public static DBMagicPlugin getDefault() {
		return fDefault;
	}

	public DBMagicPlugin() {
		super();
		fDefault = this;
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		img = MgcImg.getDefault();
	}

	public static final String getPluginVersion() {
		return "DB Magic 1.0.0";
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		try {
			img.clear();

			MgcPrefStoreManager.getDefault().finalization();
			DBManager.getDefault().finalization();
			DictiosSupplyer.getDefault().finalization();
			FDictiosSupplyer.getDefault().finalization();
			ForeignsSupplyer.getDefault().finalization();
			VariesSupplyer.getDefault().finalization();
			SqHManager.getDefault().finalization();
			DescrManager.getDefault().finalization();
		}
		finally {
			super.stop(context);
		}
		super.stop(context);
	}

	public static IViewPart findView(final String viewId) {
		final IWorkbenchPage page = getPage();
		if (page != null) {
			return page.findView(viewId);
		}
		else {
			return null;
		}
	}

	public static IViewPart findView(final String viewId, final String secondaryId) throws PartInitException {
		final IWorkbenchPage page = getPage();
		if (page != null) {
			return page.showView(viewId, secondaryId, IWorkbenchPage.VIEW_VISIBLE);
		}
		else {
			return null;
		}
	}

	public static IViewPart showView(final String viewId) {
		final IWorkbenchPage page = getPage();
		if (page != null) {
			try {
				return page.showView(viewId);
			}
			catch (Exception e) {
				showError(e);
			}
		}
		return null;
	}

	public static IViewPart showView(final String viewId, final String secondaryId) {
		final IWorkbenchPage page = getPage();
		if (page != null) {
			try {
				return page.showView(viewId, secondaryId, IWorkbenchPage.VIEW_ACTIVATE);
			}
			catch (Exception e) {
				showError(e);
			}
		}
		return null;
	}

	public static IWorkbenchPage getPage() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		return window.getActivePage();
	}

	public static Shell getShell() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		return window.getShell();
	}

	public static void getCloseEditors(final IEditorReference[] editorRefs) {
		getPage().closeEditors(editorRefs, true);
	}

	public static void showInfo(final String message) {
		MessageDialog.openInformation(getShell(), TITLE, Str.escAmp(message));
	}

	public static void showWarning(final String message) {
		MessageDialog.openWarning(getShell(), TITLE, Str.escAmp(message));
	}

	public static boolean confirmDialog(final String message) {
		return MessageDialog.openConfirm(getShell(), TITLE, Str.escAmp(message));
	}

	public static MessageDialogWithToggle confirmDialogWithToggle(final String message, final String toggleMessage, final boolean toggleStatus) {
		return MessageDialogWithToggle.openYesNoQuestion(getShell(), TITLE, Str.escAmp(message), Str.escAmp(toggleMessage), toggleStatus, null, null);
	}

	private static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}

	public static void showError(final Throwable throwable) {
		ErrorDialog.openError(getShell(), TITLE, "Error",
				new Status(IStatus.ERROR, getPluginId(), IStatus.OK, Str.escAmp(throwable.getMessage()), throwable));
	}

	public static void showError(final String msg, final Throwable throwable) {
		ErrorDialog.openError(getShell(), TITLE, Str.escAmp(msg),
				new Status(IStatus.ERROR, getPluginId(), IStatus.OK, Str.escAmp(throwable.getMessage()), throwable.getCause()));
	}

}
