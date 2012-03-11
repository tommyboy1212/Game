package com.flipflop.game.input;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Logger;

import com.flipflop.game.daemon.Daemon;

public class InputDaemon extends Daemon implements MouseListener, MouseMotionListener{

	private static final Logger logger = Logger.getLogger(InputDaemon.class.getName());
	private boolean initSuccess = false;
	private int mouseX;
	private int mouseY;
	private int mouseDx;
	private int mouseDy;
	private boolean mouseClicked;
	private boolean mouseRelease;
	private long mouseTimeDown;
	
	
	public InputDaemon (Component listenOn){
		super("InputDaemon");
		super.timeSync.setTargetTPS(30);
		listenOn.addMouseListener(this);
		listenOn.addMouseMotionListener(this);
	}
	
	public void init() {
			initSuccess = true;
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		System.out.println("Mouse clicked");
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		System.out.println("Mouse entered");		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		System.out.println("Mouse exited");
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		System.out.println("Mouse pressed: "+arg0.paramString());
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		System.out.println("Mouse released: "+arg0.paramString());
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		System.out.println("Mouse dragged: "+arg0.paramString());
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		System.out.println("Mouse moved: "+arg0.paramString());
	}

	public boolean isInitSuccess() {
		return this.initSuccess;
	}

	public int getMouseX() {
		return this.mouseX;
	}

	public int getMouseY() {
		return this.mouseY;
	}

	public int getMouseDx() {
		return this.mouseDx;
	}

	public int getMouseDy() {
		return this.mouseDy;
	}

	public boolean isMouseClicked() {
		return this.mouseClicked;
	}

	public boolean isMouseRelease() {
		return this.mouseRelease;
	}

	public long getMouseTimeDown() {
		return this.mouseTimeDown;
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		
	}
	
	
}
