package db.magic;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

public class MgcJob extends Job {

	public static Job run(final String jobName, final boolean showDialog, final MgcJobRunnable artifact) {
		final MgcJob job = new MgcJob(jobName, artifact);
		job.setPriority(SHORT);
		job.setUser(showDialog);
		job.schedule();
		return job;
	}

	private String jobName;
	private final MgcJobRunnable artifact;

	public MgcJob(final String jobName, final MgcJobRunnable artifact) {
		super(jobName);
		setName(jobName);
		this.jobName = jobName;
		this.artifact = artifact;
	}

	public IProgressMonitor monitor;
	
	public void Over() throws MgcJobBreakException {
		if (monitor.isCanceled()) {
			throw new MgcJobBreakException();
		}
	}

	public void Cancel() throws MgcJobBreakException {
		throw new MgcJobBreakException();
	}

	protected void execute(final IProgressMonitor monitor) throws Exception {
		this.monitor = monitor;

		monitor.beginTask(jobName, 10);
		artifact.run(this);
		monitor.done();
	}
	
	@Override
	protected IStatus run(final IProgressMonitor monitor) {
		try {
			execute(monitor);
		}
		catch (MgcJobBreakException e) {
			return Status.CANCEL_STATUS;
		}
		catch (Exception e) {
			Display.getDefault().asyncExec( () -> {
				DBMagicPlugin.showError("Error", e);
			});
		}
		return Status.OK_STATUS;
	}

}