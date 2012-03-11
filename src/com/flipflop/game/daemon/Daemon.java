package com.flipflop.game.daemon;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Abstract class to handle the unfortunate business that is thread management.
 * This class specifically handles Daemon-type threads, i.e. threads that poll
 * and sleep for extended periods of time.
 * 
 * @author joma
 * 
 */
public abstract class Daemon implements Runnable {

	protected static final Logger logger = Logger.getLogger(Daemon.class.getName());
	private String daemonName = "DaemonThread";
	private Thread daemon;
	private final AtomicBoolean running = new AtomicBoolean(true);
	protected final TimeSync timeSync = new TimeSync();

	public Daemon() {
	}

	public Daemon(String daemonName) {
		this.daemonName = daemonName;
	}

	public String getDaemonName() {
		return daemonName;
	}

	public void start() {
		this.running.set(true);
		this.daemon = new Thread(this, daemonName);
		this.daemon.start();
	}

	public void stop() {
		this.running.set(false);
	}
	
	public void join(long millis) throws InterruptedException {
		this.daemon.join(millis);
	}

	@Override
	public void run() {
		this.init();
		while (this.running.get()) {
			execute();
			this.timeSync.syncTPS();
		}
		this.cleanUp();
	}

	public abstract void execute();
	public abstract void init();
	public abstract void cleanUp();

}
