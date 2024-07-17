package db.magic;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;

public class MgcExclJob extends MgcJob {

	private static Object globalLock = new Object();

	public static Job async(final Object lock, final String jobName, final boolean showDialog, final MgcJobRunnable artifact) {
		final MgcExclJob job = new MgcExclJob(lock, jobName, artifact);
		job.setPriority(SHORT);
		job.setUser(showDialog);
		job.schedule();
		return job;
	}

	public static Job async(final String jobName, final boolean showDialog, final MgcJobRunnable artifact) {
		return async(globalLock, jobName, showDialog, artifact);
	}

	private final Object lock;
	
	public MgcExclJob(final Object lock, final String jobName, final MgcJobRunnable artifact) {
		super(jobName, artifact);
		this.lock = lock;
	}

	@Override
	protected void execute(final IProgressMonitor monitor) throws Exception {
		synchronized (lock) {
			super.execute(monitor);
		}
	}
	
}