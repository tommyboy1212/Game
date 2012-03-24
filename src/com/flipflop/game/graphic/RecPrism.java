package com.flipflop.game.graphic;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Vector3f;

public class RecPrism implements Drawable {

	// Indices of v to create faces.  Dependent on the order in which v is instantiated.
	private static int[][] faces = {{2,1,5,6}, {3,0,4,7}, {7,3,2,6}, {0,4,5,1}, {3,0,1,2}, {4,7,6,5}};
	protected Vector3f[] v;
	protected Vector3f[] faceColor = new Vector3f[6];
	protected Vector3f[] indexColor = new Vector3f[8];
	protected Vector3f center;
	protected Vector3f rot = new Vector3f(0.0f, 0.0f, 0.0f);
	protected float degree = 0.0f;
	protected FloatBuffer vertexBuffer;


	static {
	}

	public RecPrism(Vector3f center, Vector3f rot, float width, float height, float depth, float degree) {
		this.center = center;
		this.rot = rot;
		this.degree = degree;
		initVertices(width, height, depth);
	}
	
	public RecPrism(Vector3f center, float width, float height, float depth) {
		this.center = center;
		initVertices(width, height, depth);
	}
	
	private void initVertices(float width, float height, float depth) {
		float depthBy2 = depth / 2;
		float widthBy2 = width / 2;
		float heightBy2 = height / 2;
		v = new Vector3f[8];
		v[0] = new Vector3f(( + widthBy2), ( - heightBy2), ( + depthBy2));
		v[1] = new Vector3f(( + widthBy2), ( + heightBy2), ( + depthBy2));
		v[2] = new Vector3f(( - widthBy2), ( + heightBy2), ( + depthBy2));
		v[3] = new Vector3f(( - widthBy2), ( - heightBy2), ( + depthBy2));
		v[4] = new Vector3f(( + widthBy2), ( - heightBy2), ( - depthBy2));
		v[5] = new Vector3f(( + widthBy2), ( + heightBy2), ( - depthBy2));
		v[6] = new Vector3f(( - widthBy2), ( + heightBy2), ( - depthBy2));
		v[7] = new Vector3f(( - widthBy2), ( - heightBy2), ( - depthBy2));
		
		this.vertexBuffer = BufferUtils.createFloatBuffer(8*3);
		for (Vector3f vec : v) {
			this.vertexBuffer.put(vec.x);
			this.vertexBuffer.put(vec.y);
			this.vertexBuffer.put(vec.z);
		}
	}

	@Override
	public void render() {
		glPushMatrix();
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		glLineWidth(2.0f);
		glColor3f(0.5f,0.5f,1.0f);
		glPolygonMode(GL_FRONT, GL_FILL);
		glTranslatef(center.x, center.y, center.z);
		glRotatef(this.degree, rot.x, rot.y, rot.z);
		glEnable(GL_POLYGON_OFFSET_FILL);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glPolygonOffset(1.0f, 1.0f);
		//glEnableClientState(GL_VERTEX_ARRAY);
		//glVertexPointer(3, 0, this.vertexBuffer);
		
//		glBegin(GL_QUADS);
//		{
//			for(int i=0; i<faces.length; i++) {
//				for(int j=0; j<faces[i].length; j++) {
//					glArrayElement(faces[i][j]);
//				}
//			}
//		}
//		glEnd();

		glBegin(GL_QUADS);
		for (int i=0; i<faces.length; i++) {
			for (int j=0; j<faces[i].length; j++) {
				glVertex3f(v[faces[i][j]].x, v[faces[i][j]].y, v[faces[i][j]].z);
			}
		}
		glEnd();
		glDisable(GL_LIGHTING);
		glColor3f(0.0f,1.0f,0.0f);
		for (int i=0; i<faces.length; i++) {
			glBegin(GL_LINE_LOOP);
			for (int j=0; j<faces[i].length; j++) {
				glVertex3f(v[faces[i][j]].x, v[faces[i][j]].y, v[faces[i][j]].z);
			}
			glEnd();
		}

		glPopMatrix();
	}
	
	public void rotate(Vector3f rot, float degree) {
		this.rot = rot;
		this.degree = degree;
	}
}
