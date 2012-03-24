package com.flipflop.game;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Renderable;

public class World implements Renderable {

	// protected ArrayList<Actors> actors = new ArrayList<Actors>();
	// protected Player player;

	private void renderEnvironment() {
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		GL11.glColor3f(0.0f, 1.0f, 0.0f);
		GL11.glBegin(GL11.GL_LINE);
		{
//			for (float i = -100; i <= 100; i++) {
//				for (float j = -100; j <= 100; j++) {
//					for (float k = -100; k <= 100; k++) {
//						GL11.glVertex3f(i, j, 1.0f);
//						GL11.glVertex3f(i, j, -1.0f);
//						GL11.glVertex3f(i, 1.0f, k);
//						GL11.glVertex3f(i, -1.0f, k);
//						GL11.glVertex3f(1.0f, j, k);
//						GL11.glVertex3f(-1.0f, j, k);
//					}
//				}
//			}
		}
		GL11.glEnd();

		GL11.glPopMatrix();
	}

	@Override
	public void render() {
		renderEnvironment();
	}

}
