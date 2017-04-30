import java.util.Random;

import javax.swing.JFrame;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.Animator;



public class MainFrame
		extends JFrame
		implements GLEventListener
{

	private GLCanvas canvas;
	private Animator animator;

	// For specifying the positions of the clipping planes (increase/decrease the distance) modify this variable.
 	// It is used by the glOrtho method.
	private double v_size = 50.0;

	// Application main entry point
	public static void main(String args[]) 
	{
		new MainFrame();
	}

	// Default constructor
	public MainFrame()
	{
		super("Java OpenGL");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		this.setSize(1200, 500);
		
		this.initializeJogl();
		
		this.setVisible(true);
	}
	
	private void initializeJogl()
	{
		// Creating a new GL profile.
		GLProfile glprofile = GLProfile.getDefault();
		// Creating an object to manipulate OpenGL parameters.
		GLCapabilities capabilities = new GLCapabilities(glprofile);
		
		// Setting some OpenGL parameters.
		capabilities.setHardwareAccelerated(true);
		capabilities.setDoubleBuffered(true);

		// Try to enable 2x anti aliasing. It should be supported on most hardware.
	        capabilities.setNumSamples(2);
        	capabilities.setSampleBuffers(true);
		
		// Creating an OpenGL display widget -- canvas.
		this.canvas = new GLCanvas(capabilities);
		
		// Adding the canvas in the center of the frame.
		this.getContentPane().add(this.canvas);
		
		// Adding an OpenGL event listener to the canvas.
		this.canvas.addGLEventListener(this);
		
		// Creating an animator that will redraw the scene 40 times per second.
		this.animator = new Animator(this.canvas);
			
		// Starting the animator.
		this.animator.start();
	}
	
	public void init(GLAutoDrawable canvas)
	{
		// Obtaining the GL instance associated with the canvas.
		GL2 gl = canvas.getGL().getGL2();
		
		// Setting the clear color -- the color which will be used to erase the canvas.
		gl.glClearColor(0, 0, 0, 0);
		
		// Selecting the modelview matrix.
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);

	}
	
	public void house(GL2 gl){
		gl.glColor3d(0, 1, 0);
		gl.glBegin(GL.GL_LINE_LOOP);
			gl.glVertex2d(10, 10);
			gl.glVertex2d(10, 20);
			gl.glVertex2d(20, 20);
			gl.glVertex2d(20, 10);
		gl.glEnd();
	}
	
	public void roof(GL2 gl){
		
	}
	
	private void cerc(GL2 gl, double r, double cx, double cy) {		
		gl.glLineWidth(3);
		gl.glBegin(GL.GL_LINE_LOOP);
		for(int i = 0; i < 360; i++){
			gl.glColor3d(0.9, 0.3, 0.1);
			gl.glVertex2d(cx + r * Math.cos(Math.toRadians(i)), cy + r * Math.sin(Math.toRadians(i)));
		}
		gl.glEnd();
	}
	
	
	long start = System.currentTimeMillis();
	int FPS = 0;
	
	double cx = 0.5, cy = 0;
	double dirx = -1, diry = 1;
	double lenx = 1, leny = 1;
	double beta = 315;
	double alfa = 45;
	double dir1 = 1;
	double dir2 = 1;
	
	// origin for first speedometer (0, 0)
	double x0 = -20;
	double y0 = 0;
	// origin for second speedomeeter
	double x1 = 85;
	double y1 = 0;
	
	// random point
	Random rand;
	int xRand = 3;
	int yRand = 5;
	
	public void display(GLAutoDrawable canvas)
	{
		GL2 gl = canvas.getGL().getGL2();	
	
		// Erasing the canvas -- filling it with the clear color.
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// rotate on all three axis around the origin
		//this.cerc(gl, 0.25, x0 + 12 * Math.cos(Math.toRadians(beta)), y0 + 12 * Math.sin(Math.toRadians(beta)));
		//this.cerc(gl, 0.25, x0 + 2 * Math.cos(Math.toRadians(beta)), y0);
		//this.cerc(gl, 0.25, x0, y0 + 2 * Math.sin(Math.toRadians(beta)));
		//this.cerc(gl, 0.25, x0, y0);
		this.cerc(gl, 25, x0, y0);
		this.cerc(gl, 25, x1, y1);
		
		// primul ac indicator
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3d(x0, y0, 0);
			gl.glVertex3d(x0 + 22 * Math.cos(Math.toRadians(beta)), y0 + 22 * Math.sin(Math.toRadians(beta)), 0);
		gl.glEnd();
		
		beta = beta + (dir1 * 1.5);
		alfa = alfa + (dir2 * 1.5);
		
		if( beta == 315 ){
			dir1 = 1;
			
		}
		if( beta == 360)
			beta = 0;
		else if(beta == 0)
				beta = 360;
		
		if(beta == 225){
			dir1 = -1;
		}
		
		// al doilea ac indicator 
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3d(x1, y1, 0);
			gl.glVertex3d(x1 + 22 * Math.cos(Math.toRadians(alfa)), y1 + 22 * Math.sin(Math.toRadians(alfa)), 0);
		gl.glEnd();
		
		if(alfa == 225)
			dir2 = -1;
		if(alfa == 45)
			dir2 = 1;
		
		// rotate on random position
		//gl.glColor3d(0, 1, 0);
		//this.cerc(gl, 0.25, xRand + 2 * Math.cos(Math.toRadians(beta)), yRand + 2 * Math.sin(Math.toRadians(beta)));
		FPS++;
		
		if (System.currentTimeMillis() - start >= 1000) { //1000 ms = 1 sec 
			System.out.println("FPS:" + FPS);
			FPS = 0;
			start = System.currentTimeMillis();
		}
			
		// Forcing the scene to be rendered.
		gl.glFlush();
	}


	public void reshape(GLAutoDrawable canvas, int left, int top, int width, int height)
	{
		GL2 gl = canvas.getGL().getGL2();
		
		// Selecting the viewport -- the display area -- to be the entire widget.
		gl.glViewport(0, 0, width, height);
		
		// Determining the width to height ratio of the widget.
		double ratio = (double) width / (double) height;
		
		// Selecting the projection matrix.
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		
		gl.glLoadIdentity();
		
		// Selecting the view volume to be x from 0 to 1, y from 0 to 1, z from -1 to 1.
		// But we are careful to keep the aspect ratio and enlarging the width or the height.
		if (ratio < 1)
			gl.glOrtho(-v_size, v_size, -v_size, v_size / ratio, -1, 1);
		else
			gl.glOrtho(-v_size, v_size * ratio, -v_size, v_size, -1, 1);

		// Selecting the modelview matrix.
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);		
	}
	
	public void displayChanged(GLAutoDrawable canvas, boolean modeChanged, boolean deviceChanged)
	{
		return;
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
	}
}