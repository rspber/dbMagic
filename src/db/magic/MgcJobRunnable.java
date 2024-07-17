package db.magic;

@FunctionalInterface
public interface MgcJobRunnable {

	public void run(final MgcJob job) throws Exception;

}