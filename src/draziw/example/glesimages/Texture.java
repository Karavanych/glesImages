package draziw.example.glesimages;


import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Texture {
	private Bitmap img1;
	private int resId;
	protected int id;
	private int slot;
	
	private int type;
	private int sizeX,sizeY = 0;	
	
	public Texture(int mType,int mId,int mSlot,int mResId) {
		this.id=mId;
		this.slot=mSlot;
		this.type=mType;
		this.resId=mResId;
	}
	
	public Texture(int mType,int mId,int mSlot, Bitmap mBitmap) {
		this.id=mId;
		this.slot=mSlot;
		this.type=mType;
		this.img1=mBitmap;
	}

	public void setSize(int mSizeX, int mSizeY) {
		this.sizeX=mSizeX;
		this.sizeY=mSizeY;
	}
	
	public void use(int uSampler) {
		/* Active and Bind texture not needed if you dont replace it or change�
		 we active and bind it in TextureLoader, all your texture in memory and shader can
		 get in by index from uSampler
		
		 ��� ������������� ������������ � ������� �������� ������ ��� ���� �� �� �� ������.
		 �� ������ ������ ��� �������� ��������� � textureLoadere.
		 ������ ����� �� ���� ������ ����� ������ ���������� � uSampler
		 �.�. ���������� ������ �������� ����� ����� � �� ����� ������������ ��������, ������� � ��� 
		 ����������.
		
		 ��� ����� �� ��� ��������� ������ ������� ����� ������������ �������� 8 ���������� ������,
		 ����� ��� ����� ����� ����������������� ���������� �����.
		 � ���� ����� �� �������� ������� ���������� �����, ������ ��� ������� ���� �������� � 2 ������ ����� ������ 
		 */
		
		//GLES20.glActiveTexture(this.slot);
	    //GLES20.glBindTexture(this.type, this.id);
		GLES20.glUniform1i(uSampler,this.slot-GLES20.GL_TEXTURE0);
	    
	}
	
	public void reload(Context mContext,int mTextureId){
		this.id=mTextureId;
		this.load(mContext);
	}
	
	
	public void load(Context mContext) {				
			loadTexture2D(mContext);														 	
	}
	
	
	public void loadTexture2D(Context mContext) {
		
		GLES20.glActiveTexture(this.slot);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.id);
        
        // ��������� ��������
     	GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST); // GL_LINEAR
     	GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
     	GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
     	GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);		
		
		if (img1==null) {
	        InputStream is1 = mContext.getResources().openRawResource(this.resId);
	        
	        try {
	        	img1 = BitmapFactory.decodeStream(is1);
	        } finally {
	        	try {
	        		is1.close();
	        	} catch(IOException e) {
	        		//e.printStackTrace();
	        	}
	        }
		}
            
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, img1, 0);
		
		//textures.add(new Texture(GLES20.GL_TEXTURE_2D,mTextureId,mTextureSlot,img1.getWidth(),img1.getHeight(),resId));		// add (id, index, sizeX,SizeY
		this.setSize(img1.getWidth(),img1.getHeight());		
		img1.recycle();
		
	}
	
}
