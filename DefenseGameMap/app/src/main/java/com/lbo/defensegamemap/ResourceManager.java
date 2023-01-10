package com.lbo.defensegamemap;


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
    private static int mHandleLand0;
    private static int mHandleLand1;

    // 화면확대축소관리
    private float mScale = 0;
    // 리소스로딩 생성자
    public ResourceManager(Activity activity, Context context, float scale){
        mActivity = activity;
        mContext = context;
        mScale = scale;
    }
    // 리소스 로딩
    public void loadResource(Unit title, Unit[][] land){
        Bitmap bmpTitle = BitmapFactory.decodeResource(mContext.getResources(), mContext.
                getResources().getIdentifier("drawable/title", null, mContext.getPackageName()));
        mHandleTitle = getImageHandle(bmpTitle);
        bmpTitle.recycle();
        title.setBitmap(mHandleTitle,600,600);

        // Land
        Bitmap bmpLand0 = BitmapFactory.decodeResource(
                mContext.getResources(),
                mContext.getResources().getIdentifier("drawable/land0", null, mContext.getPackageName()));
        mHandleLand0 = getImageHandle(bmpLand0);
        bmpLand0.recycle();
        Bitmap bmpLand1 = BitmapFactory.decodeResource(
                mContext.getResources(),
                mContext.getResources().getIdentifier("drawable/land1", null, mContext.getPackageName()));
        mHandleLand1 = getImageHandle(bmpLand1);
        bmpLand1.recycle();
        //land.setBitmap(mHandleLand,200,120);
        for (int i = 0; i < Map.mInfoSizeRow; i++) {
            for (int j = 0; j < Map.mInfoSizeCol; j++) {
                if(Map.mInfo[i][j]==0) {
                    land[i][j].setBitmap(mHandleLand0, 100, 60);
                } else {
                    land[i][j].setBitmap(mHandleLand1, 100, 100);
                }
            }
        }
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