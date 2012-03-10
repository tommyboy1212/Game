package com.flipflop.game.daemon;

import com.flipflop.utility.concurrency.AtomicFloat;

public class TimeSync {
	public static final long NANO_IN_SECOND = 1000 * 1000 * 1000;
	public static final long NANO_IN_MILLI = 1000 * 1000;
	public static final long NANO_IN_MICRO = 1000;
	public static final long MILLI_IN_SECOND= 1000;
	private long lastTickAtNano = System.nanoTime();
	private float targetTPS = 60.0f;
	private AtomicFloat actualTPS = new AtomicFloat(0.0f);
	private long ticks = 0;
	private float runTime = 0;
	private long nanoPassed = 0;
	private long nanoToWait = 0;
	private boolean isSlow = false;
	private long nanoErrorFeedback = 0;
	private long lastNano = System.nanoTime();

	public TimeSync() {
		setTargetTPS(this.targetTPS);
	}

	public TimeSync(float tps) {
		setTargetTPS(tps);
	}

	public void syncTPS(float tps) {
		setTargetTPS(tps);
		this.sleep();
	}

	public void syncTPS() {
		this.sleep();
	}

	private void sleep() {
		long currNano = System.nanoTime();
		long alarmSet = currNano + this.nanoToWait;
		long anticipatedExecution = currNano - this.lastNano;
		try {
			while ((currNano=(currNano+this.nanoErrorFeedback)) < alarmSet) {
				Thread.sleep(1);
				currNano = System.nanoTime();
			}
		} catch (InterruptedException e) {}
		finally {
			this.nanoErrorFeedback = currNano - alarmSet + anticipatedExecution ;
			this.setActualTPS();
			this.lastNano = System.nanoTime();
		}
		this.isSlow = false;
	}

	public boolean isSlow() {
		return this.isSlow;
	}

	public void setTargetTPS(float tps) {
		this.targetTPS = tps;
		this.nanoToWait = (long) (((float) 1 / this.targetTPS) * NANO_IN_SECOND);
	}

	public float getActualTPS() {
		return this.actualTPS.get();
	}

	public void setActualTPS() {
		this.ticks++;
		this.nanoPassed += System.nanoTime() - this.lastTickAtNano;
		if (this.nanoPassed >= NANO_IN_SECOND) {
			this.runTime += (float) this.nanoPassed / NANO_IN_SECOND;
			this.actualTPS.set((float) this.ticks / this.nanoPassed * NANO_IN_SECOND);
			this.nanoPassed = 0;
			this.ticks = 0;
		}
		this.lastTickAtNano = System.nanoTime();
	}
	
	public float getRunTime() {
		return this.runTime;
	}
}
