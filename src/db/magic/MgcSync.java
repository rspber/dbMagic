package db.magic;

import org.eclipse.core.runtime.jobs.Job;

public class MgcSync {

	public static void sync(final Thread thread) {
		try {
			thread.join();
		}
		catch (InterruptedException e) {
			DBMagicPlugin.showError(e);
		}
	}

	public static void sync(final Thread thread, final int timeoutsec) {
		try {
			if (timeoutsec > 0) {
				thread.join(timeoutsec * 1000);
			}
			else {
				thread.join();
			}
		}
		catch (InterruptedException e) {
			DBMagicPlugin.showError(e);
		}
	}

	public static void sync(final Job job) {
		try {
			job.join();
		}
		catch (InterruptedException e) {
			DBMagicPlugin.showError(e);
		}
	}

}