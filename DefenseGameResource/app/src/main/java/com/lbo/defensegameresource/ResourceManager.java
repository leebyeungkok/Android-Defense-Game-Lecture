package com.lbo.defensegameresource;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
// 리소스 관리
public class ResourceManager {
    private Context mContext;
    private Activity mActivity;

    private static int mHandleTitle;
    // 화면확대축소관리
    private float mScale = 0;
    // 리소스로딩 생성자
    public ResourceManager(Activity activity, Context context, float scale){
        mActivity = activity;
        mContext = context;
        mScale = scale;
    }
    // 리소스 로딩
    public void loadResource(Unit title){
        Bitmap bmpTitle = BitmapFactory.decodeResource(mContext.getResources(), mContext.
                getResources().getIdentifier("drawable/title", null, mContext.getPackageName()));
        mHandleTitle = getImageHandle(bmpTitle);
        bmpTitle.recycle();
        title.setBitmap(mHandleTitle,600,600);
    }
    // 이미지 핸들 반환
    private int getImageHandle(Bitmap bitmap){
        int[] texturenames = new int[1];
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glGenTextures(1, texturenames, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        return texturenames[0];
    }
}