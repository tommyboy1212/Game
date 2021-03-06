package com.flipflop.game.daemon;

public class TimeSync {
	public static final long NANO_IN_SECOND = 1000 * 1000 * 1000;
	public static final long NANO_IN_MILLI = 1000 * 1000;
	public static final long NANO_IN_MICRO = 1000;
	public static final long MILLI_IN_SECOND= 1000;
	private long lastTickAtNano = System.nanoTime();
	private float targetTPS = 60.0f;
	private float actualTPS = 0.0f;
	private long ticks = 0;
	private float runTime = 0;
	private long nanoSinceSecond = 0;
	private long nanoToWait = 0;
	private boolean isSlow = false;
	private long nanoSlowTime = 0;

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
		long timeToBe = currNano + this.nanoToWait;
		try {
			while (timeToBe > (currNano+this.nanoSlowTime)) {
				Thread.sleep((long)(1.0f/this.targetTPS*MILLI_IN_SECOND));
				currNano = System.nanoTime();
			}
			this.nanoSlowTime = currNano - timeToBe;
		} catch (InterruptedException e) {}
		finally {
			this.setActualTPS();
		}
		this.isSlow = false;
	}

	public boolean isSlow() {
		return this.isSlow;
	}

	public void setTargetTPS(float tps) {
		this.targetTPS = tps;
		this.nanoToWait = (long) (((float) 1 / this.targetTPS) * NANO_IN_MILLI);
	}

	public float getActualTPS() {
		return this.actualTPS;
	}

	public void setActualTPS() {
		this.ticks++;
		this.nanoSinceSecond += System.nanoTime() - this.lastTickAtNano;
		if (this.nanoSinceSecond >= NANO_IN_SECOND) {
			this.runTime += (float) this.nanoSinceSecond / NANO_IN_SECOND;
			this.actualTPS = (float) this.ticks / (this.nanoSinceSecond * NANO_IN_SECOND);
			this.nanoSinceSecond = 0;
		}
	}
	
	public float getRunTime() {
		return this.runTime;
	}
}
