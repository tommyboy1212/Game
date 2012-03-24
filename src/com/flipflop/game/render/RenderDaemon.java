package com.flipflop.game.render;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Canvas;
import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Renderable;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Project;

import com.flipflop.game.DisplayUtil;
import com.flipflop.game.daemon.Daemon;
import com.flipflop.game.daemon.TimeSync;

public class RenderDaemon extends Daemon {

	private static final Logger logger = Logger.getLogger(RenderDaemon.class.getName());
	private DisplayMode currentDisplayMode;
	private DisplayMode targetDisplayMode;
	private String appName;
	private Canvas canvas;
	private Renderable renderer;
	
	private Timer fpsPrintTimer;
	
	public RenderDaemon(Canvas canvas, DisplayMode targetDisplayMode, Renderable renderer, String appName) {
		super("RenderLoop");
		this.canvas = canvas;
		this.appName = appName;
		this.targetDisplayMode = targetDisplayMode;
		this.renderer = renderer;
		super.timeSync.setTargetTPS(60.0f);
	}

	public void init() {
		while(this.canvas.isDisplayable() == false) {
			logger.info("Waiting for window to finish initializing...");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {}
		}
		try {
			initLWJGL();
			initOpenGL();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute() {
		//Display.sync(60);
		// Clear buffer for redrawing.
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// Let Renderer know it's time to draw.
		this.renderer.render();
		
		int errCode;
		String errString;
		while ((errCode = glGetError()) != GL_NO_ERROR) {
			errString = GLU.gluErrorString(errCode);
			logger.warning("GL ERROR: " + errString);
		}
		// Swap buffers. Display is by default a double-buffer
		// configuration.
		Display.update();
	}

	/**
	 * Initializes the LWJGL utilities used by this engine.
	 * 
	 * @throws LWJGLException
	 */
	private void initLWJGL() throws LWJGLException {
		Display.setParent(this.canvas);
		Display.setTitle(this.appName);
		// TODO How to do fullscreen?
		if (DisplayUtil.tryDisplayChange(targetDisplayMode, false)) {
			this.currentDisplayMode = targetDisplayMode;
		}
	}

	/**
	 * Initializes the OpenGL states and matrices. Must be called from the
	 * thread that created the OpenGL context. In this case, the GameLoop should
	 * be the only thread to call this method.
	 * 
	 * @throws LWJGLException
	 */
	private void initOpenGL() throws LWJGLException {
		logger.config("OpenGL Version: " + glGetString(GL_VERSION));
		logger.config("Renderer: " + glGetString(GL_RENDERER));
		logger.config("Extensions: " + glGetString(GL_EXTENSIONS));
		glViewport(0, 0, this.currentDisplayMode.getWidth(), this.currentDisplayMode.getHeight());
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glEnable(GL_LINE_SMOOTH);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glCullFace(GL_BACK);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		float aspect = this.currentDisplayMode.getWidth() / this.currentDisplayMode.getHeight();
		Project.gluPerspective(45.0f, aspect, 0.1f, 25.0f);
		glMatrixMode(GL_MODELVIEW);
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	@Override
	public void cleanUp() {
		this.fpsPrintTimer.cancel();
	}
	
	public float getFPS() {
		return super.timeSync.getActualTPS();
	}
	
	public void printFPS(long interval) {
		printFPS(interval, System.out);
	}
	public void printFPS(long interval, PrintStream out) {
		this.fpsPrintTimer = new Timer("FPS Timer", true);
		this.fpsPrintTimer.scheduleAtFixedRate(new FPSPrintTask(out, this.timeSync), 0, interval);
	}
	
	class FPSPrintTask extends TimerTask {
		private PrintStream out;
		private TimeSync sync;
		public FPSPrintTask(PrintStream out, TimeSync sync) {
			this.out = out;
			this.sync = sync;
		}
		@Override
		public void run() {
			out.println("FPS: "+sync.getActualTPS());
		}
	}
}
