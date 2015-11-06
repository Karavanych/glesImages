package draziw.example.glesimages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

public class Sprite2D {
	
	public static float BASE_SPRITE_WIDTH=2f; // это исходя из начальних вертексов спрайта
	public static float BASE_SPRITE_HEIGHT=2f; // см. pointVFA 
	
	public boolean isGUI=false;
		
	FloatBuffer vertextBuffer;
	FloatBuffer textureCoordBuffer;
	ShortBuffer verticesIndex;
	
	
	public ShaderProgram shader;
	
	
	public Sprite2D(ShaderProgram shader,float[] leftTopRightBottomTextureCoords) {
		this.shader=shader;
		
		
		// по умолчанию координаты на весь экран, нужно будет реализовать сдвиг и скалирование
				float[] pointVFA = {
						 1f,-1f,0.0f,
						 -1f,-1f,0.0f,
						 1f,1f,0.0f,
						-1f,1f,0.0f
						};
						ByteBuffer pointVBB = ByteBuffer.allocateDirect(pointVFA.length * 4);
						pointVBB.order(ByteOrder.nativeOrder());
						vertextBuffer = pointVBB.asFloatBuffer();
						vertextBuffer.put(pointVFA);
						vertextBuffer.position(0); 				
												
						// по умолчанию вся текстура на полигон, нужен вектор смещения, вектор определяющий размер текстуры и вектор анимации
				setTextureCoord(leftTopRightBottomTextureCoords);
						
				//зададим индексы, предидущие массивы определяли просто координаты вершин и текстурные координаты, соответствующие вершинам
				// а здесь мы определим порядок следования вершин через индексы					
						
				short[] planeISA = {
								2,3,1,
								0,2,1,
						};
				
				ByteBuffer planeIBB = ByteBuffer.allocateDirect(planeISA.length * 2);
				planeIBB.order(ByteOrder.nativeOrder());
				verticesIndex = planeIBB.asShortBuffer();
				verticesIndex.put(planeISA);
				verticesIndex.position(0);
		
	}
	
	public Sprite2D(ShaderProgram shader) {
		 this(shader,new float[]{0f,0f,1f,1f});		
	}
	
	// устанавливаем текстурные координаты для спрайтовой текструры состоящей их scaleX кадров по горизонтали и scaleY кадров по вертикали
	public void setRelativeTextureBounds(float scaleX,float scaleY,int offsetFrameX,int offsetFrameY) {
		setTextureBounds(new float[] {1/scaleX,1/scaleY},new float[]{offsetFrameX/scaleX,offsetFrameY/scaleY});
	}
	
	public void setTextureCoord(float[] leftTopRightBottom) {
		float[] planeTFA = {
				// 1f,1f, 0,1f, 1f,0,0,0
				leftTopRightBottom[2],leftTopRightBottom[3],
				leftTopRightBottom[0],leftTopRightBottom[3],
				leftTopRightBottom[2],leftTopRightBottom[1],
				leftTopRightBottom[0],leftTopRightBottom[1]
		};

		ByteBuffer planeTBB = ByteBuffer.allocateDirect(planeTFA.length * 4);
		planeTBB.order(ByteOrder.nativeOrder());
		textureCoordBuffer = planeTBB.asFloatBuffer();
		textureCoordBuffer.put(planeTFA);
		textureCoordBuffer.position(0);	
		
	}
	
	//устанавливаем границы текстуры как сумму векторов вектора отступа и вектора текстуры vec2
	public void setTextureBounds(float[] textureVector,	float[] offsetTextureVector) {			
		
		if (textureVector!=null && textureVector.length != 2) {try {
			throw new Exception("texture vector have to be length=2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}}
		
		if (offsetTextureVector!=null && offsetTextureVector.length != 2) {try {
			throw new Exception("offset vector have to be length=2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}}				
		
		float s=textureVector[0];
		float t=textureVector[1];
		
		float sOf=0;				
		float tOf=0;
		
		if (offsetTextureVector!=null) {
			sOf=offsetTextureVector[0];
			tOf=offsetTextureVector[1];
		}
		
		float[] planeTFA = {
					// 1f,1f, 0,1f, 1f,0,0,0
			s+sOf,t+tOf,
			0+sOf,t+tOf,
			s+sOf,0+tOf,
			0+sOf,0+tOf
			};
	
			ByteBuffer planeTBB = ByteBuffer.allocateDirect(planeTFA.length * 4);
			planeTBB.order(ByteOrder.nativeOrder());
			textureCoordBuffer = planeTBB.asFloatBuffer();
			textureCoordBuffer.put(planeTFA);
			textureCoordBuffer.position(0);								
	}
	
	public void draw(Texture mTexture) {
		
			
		 mTexture.use(shader.uBaseMap);
		 
		 GLES20.glVertexAttribPointer(shader.aPosition, 3, GLES20.GL_FLOAT, false, 0, vertextBuffer);
		 GLES20.glEnableVertexAttribArray(shader.aPosition);	
		 
		 GLES20.glVertexAttribPointer(shader.aTextureCoord, 2, GLES20.GL_FLOAT, false, 8, textureCoordBuffer);
	     GLES20.glEnableVertexAttribArray(shader.aTextureCoord);
		 		
	     GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, verticesIndex);
		
	}
	
}
