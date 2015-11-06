package draziw.example.glesimages;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import android.content.Context;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;

import android.util.Log;
import android.view.MotionEvent;

public class GLES20Renderer implements Renderer {

	private Context context;

	// private VideoCapture videoCapture;
	// private Rect videoViewport;

	int width = 0;
	int height = 0;

	boolean wasActionDown = false;


	public TextureLoader textureLoader;

	private ShaderManager shaderManager;


	private Sprite2D sprite;
	
	int currentTexture=0;

	public GLES20Renderer(Context cc) {
		context = cc;		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {		
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

		
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glCullFace(GLES20.GL_BACK); GLES20.glFrontFace(GLES20.GL_CCW);		 

		//GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		textureLoader= new TextureLoader(context);
		textureLoader.loadSingleTexture(R.drawable.i1, GLES20.GL_TEXTURE0);
		textureLoader.loadSingleTexture(R.drawable.i2, GLES20.GL_TEXTURE1);
		textureLoader.loadSingleTexture(R.drawable.i3, GLES20.GL_TEXTURE2);
		textureLoader.loadSingleTexture(R.drawable.i4, GLES20.GL_TEXTURE3);
		textureLoader.loadSingleTexture(R.drawable.i5, GLES20.GL_TEXTURE4);
		textureLoader.loadSingleTexture(R.drawable.i6, GLES20.GL_TEXTURE5);
		textureLoader.loadSingleTexture(R.drawable.i7, GLES20.GL_TEXTURE6);
		
		
		
		shaderManager = new ShaderManager(context);
		
		sprite=new Sprite2D(shaderManager.getShader("simple_texture"));

		
		Log.d("MyLogs", "all done");
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		

		GLES20.glViewport(0, 0, width, height);
		this.width = width;
		this.height = height;
					

	}

	@Override
	public void onDrawFrame(GL10 gl) {
		//float timer = getAnimationTime(timeAnimationInterval);		

		/*
		 * if (MainActivity.CAPTURE_VIDEO) { // если видеозахват делаем, то
		 * придется вьюпорт переключать внутри рендера кадра
		 * GLES20.glViewport(0,0,width,height); }
		 */

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		GLES20.glUseProgram(sprite.shader.programHandler);
		
		sprite.draw(textureLoader.getTexture(currentTexture));
		
		/*int error = GLES20.glGetError();
		  if (error != GLES20.GL_NO_ERROR)
	        {         	
	            Log.d("MyLogs", " on draw Error: " + GLU.gluErrorString(error));            
	        }*/
		

	}


	public void onTouchEvent(MotionEvent event) {		

		int pointerIndex = event.getActionIndex();
		int pointerId = event.getPointerId(pointerIndex);	
		

		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:// при одновременном нажатии 2х пальцев это событие
			changeTexture();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			if (!wasActionDown) {
				pointerId = event.getPointerId(0);
				wasActionDown = true;
			}					
			break;
		case MotionEvent.ACTION_POINTER_UP:		
			break;
		case MotionEvent.ACTION_UP:		
			wasActionDown = false;
			break;
		case MotionEvent.ACTION_CANCEL:		
			wasActionDown = false;
			break;
		case MotionEvent.ACTION_MOVE: // движение			
			break;
		}

	}

	private void changeTexture() {
		currentTexture++;
		if (currentTexture>=textureLoader.textures.size()) {
			currentTexture=0;
		}
		
	}
		

}
