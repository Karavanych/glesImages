package draziw.example.glesimages;


import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

public class TextureLoader {
	
	public ArrayList<Texture> textures=new ArrayList<Texture>();
	//public int[] texturesId;
	private Context context;	
	
	public TextureLoader(Context cc) {
		context=cc;
	}	
			
	
	public void loadSingleTexture(int resId,int slotId) {
		
		int[] texturesId = new int[1];
		
		GLES20.glGenTextures(texturesId.length, texturesId, 0);					
		
		GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
		
		Texture mTexture = 	new Texture(GLES20.GL_TEXTURE_2D,
				texturesId[0],
				slotId,
				resId);						        
		mTexture.load(context);
		textures.add(mTexture);
		
	}
	
	public void loadSingleTexture(Bitmap bitmap,int slotId) {
		
		int[] texturesId = new int[1];
		
		GLES20.glGenTextures(texturesId.length, texturesId, 0);					
		
		GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
		
		Texture mTexture = 	new Texture(GLES20.GL_TEXTURE_2D,
				texturesId[0],
				slotId,
				bitmap);						        
		mTexture.load(context);
		textures.add(mTexture);
		
	}
	
	public void confirmTextures() {	
		
		if (textures!=null && textures.size()>0) {
			
			for (Texture each:textures) {
				if (!GLES20.glIsTexture(each.id)) {
					
					int[] texturesId = new int[]{each.id};					
					GLES20.glDeleteTextures(1,texturesId,0);					
					GLES20.glGenTextures(1, texturesId,0);
					
					each.reload(context, texturesId[0]);					
				}
			}
		}										
	}

	public Texture getTexture(int i) {		
		return textures.get(i);
	}

}
