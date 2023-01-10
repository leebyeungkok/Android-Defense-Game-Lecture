package com.lbo.defensegamebasic;


import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
// 유닛
public class Unit {
    // 기본적인 이미지 처리를 위한 변수
    protected static float mVertices[];
    protected static short mIndices[];
    protected static float mUvs[];
    protected static int mProgramImage;
    protected static int mProgramSolidColor;
    protected int mPositionHandle;
    protected int mTexCoordLoc;
    protected int mtrxhandle;
    protected int mSamplerLoc;
    protected FloatBuffer mVertexBuffer;
    protected ShortBuffer mDrawListBuffer;
    protected FloatBuffer mUvBuffer;
    // 매트릭스변환을 위한 변수
    protected final float[] mMVPMatrix = new float[16];
    protected final float[] mMVPMatrix2 = new float[16];
    protected float[] mRotationMatrix = new float[16];
    protected float[] mScaleMatrix = new float[16];
    protected float[] mTranslationMatrix = new float[16];
    // 비트맵 이미지 핸들관리 (여러건 처리를 위해 배열로 정의)
    protected int[] mHandleBitmap;
    protected int mBitmapCount = 0;
    protected Bitmap mBitmap[];
    // 현재의 위치정보
    protected float mPosX = 0;
    protected float mPosY = 0;
    // 이동하려는 위치정보
    protected float mTargetX;
    protected float mTargetY;
    // 게임의 맵에서 이동하려는 좌표
    protected int mTargetBlockRow;
    protected int mTargetBlockCol;
    // 이미지의 가로, 세로 설정
    protected float mWidth = 0;
    protected float mHeight = 0;
    // 이미지의 기울기 설정
    protected int mAngle = 0;
    // 이미지의 확대, 축소 설정
    protected float mScaleX = 1.0f;
    protected float mScaleY = 1.0f;
    // 유닛의 움직임을 관리하는 변수
    protected int mCount = 0;
    // 여러개의 이미지 중 화면에 표시할 인덱스번호
    protected int mBitmapState = 0;
    // 현재 객체의 활성화 여부
    protected boolean mIsActive = false;
    // 생성자
    public Unit(int programImage, int programSolidColor) {
        mProgramImage = programImage;
        mProgramSolidColor = programSolidColor;
        mPositionHandle = GLES20.glGetAttribLocation(mProgramImage, "vPosition");
        mTexCoordLoc = GLES20.glGetAttribLocation(mProgramImage, "a_texCoord");
        mtrxhandle = GLES20.glGetUniformLocation(mProgramImage, "uMVPMatrix");
        mSamplerLoc = GLES20.glGetUniformLocation(mProgramImage, "s_texture");
    }
    // 이미지핸들 배열, 가로,세로 값을 받아와 설정
    public void setBitmap(int handle[], int width, int height) {
        mBitmapCount = handle.length;
        this.mWidth = width;
        this.mHeight = height;
        setupBuffer();
        mHandleBitmap = new int[mBitmapCount];
        mHandleBitmap = handle;
        mBitmapState = 0;
    }
    // 이미지핸들, 가로, 세로 값을 받아와 설정
    public void setBitmap(int handle, int width, int height) {
        mBitmapCount = 1;
        this.mWidth = width;
        this.mHeight = height;
        setupBuffer();
        mHandleBitmap = new int[mBitmapCount];
        mHandleBitmap[0] = handle;
        mBitmapState = 0;
    }
    // 위치정보를 설정함
    public void setPos(float posX, float posY) {
        this.mPosX = posX;
        this.mPosY = posY;
        this.mTargetX = posX;
        this.mTargetY = posY;
    }
    // 현재의 X 좌표를 설정함
    public void setPosX(float posX) {
        this.mPosX = posX;
        this.mTargetX = this.mPosX;
    }
    // 현재의 Y좌표를 설정함
    public void setPosY(float posY) {
        this.mPosY = posY;
        this.mTargetY = this.mPosY;
    }
    // 객체의 높이를 반환함
    public float getHeight(){
        return this.mHeight;
    }
    // 객체의 폭을 반환함
    public float getWidth(){
        return this.mWidth;
    }
    // 객체의 크기를 계산하여 하단의 X 위치를 설정함
    public void setPosBottomX(float posX) {
        this.mPosX = posX;
        this.mTargetX = this.mPosX;
    }
    // 객체의 크기를 계산하여 하단의 Y 위치를 설정함
    public void setPosBottomY(float posY) {
        this.mPosY = posY + mHeight/3;
        this.mTargetY = this.mPosY;
    }
    // 기울기를 설정함
    public void setAngle(int angle){
        this.mAngle = angle;
    }
    // 객체의 활성화여부를 설정함
    public void setIsActive(boolean isActive){
        this.mIsActive = isActive;
    }
    // 객체의 활성화여부를 반환함
    public boolean getIsActive(){
        return this.mIsActive;
    }
    // 이미지 처리를 위한 버퍼를 설정함.
    public void setupBuffer(){
        mVertices = new float[] {
                mWidth/ (-2), mHeight/2, 0.0f,
                mWidth/(-2), mHeight/ (-2), 0.0f,
                mWidth/2, mHeight/(-2), 0.0f,
                mWidth/2, mHeight /2, 0.0f,
        };
        mIndices = new short[] {0, 1, 2, 0, 2, 3}; // The order of vertexrendering.
        ByteBuffer bb = ByteBuffer.allocateDirect(mVertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(mVertices);
        mVertexBuffer.position(0);
        ByteBuffer dlb = ByteBuffer.allocateDirect(mIndices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        mDrawListBuffer = dlb.asShortBuffer();
        mDrawListBuffer.put(mIndices);
        mDrawListBuffer.position(0);
        mUvs = new float[] {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };
        ByteBuffer bbUvs = ByteBuffer.allocateDirect(mUvs.length * 4);
        bbUvs.order(ByteOrder.nativeOrder());
        mUvBuffer = bbUvs.asFloatBuffer();
        mUvBuffer.put(mUvs);
        mUvBuffer.position(0);
    }
    // 현재 객체가 선택되었는지를 반환함
    public boolean isSelected(int x, int y){
        boolean isSelected = false;
        if(mIsActive == true) {
            if ((x >= mPosX - mWidth / 2 && x <= mPosX + mWidth / 2) &&
                    (y >= mPosY - mHeight / 2 && y <= mPosY + mHeight / 2)) {
                isSelected = true;
            }
        }
        return isSelected;
    }
    // 그리기
    void draw(float[] m) {
        // 활성화 상태가 아니라면 그리지 않는다.
        if(mIsActive == false){
            return;
        }
        // 회전, 가로/세로 확대,축소를 변환한다.
        // 변환이 없을 경우 호출하지 않도록 미리 구분했다.
        if(this.mAngle != 0) {
            Matrix.setIdentityM(mTranslationMatrix, 0);
            Matrix.setIdentityM(mRotationMatrix, 0);
            Matrix.translateM(mTranslationMatrix, 0, mPosX, mPosY, 0);
            Matrix.setRotateM(mRotationMatrix, 0, this.mAngle, 0, 0, -1.0f);
            Matrix.multiplyMM(mMVPMatrix, 0, m, 0, mTranslationMatrix, 0);
            Matrix.multiplyMM(mMVPMatrix2, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        } else if(this.mScaleX != 1.0f || this.mScaleY != 1.0f) {
            Matrix.setIdentityM(mTranslationMatrix, 0);
            Matrix.setIdentityM(mScaleMatrix, 0);
            Matrix.translateM(mTranslationMatrix, 0, mPosX, mPosY, 0);
            Matrix.scaleM(mScaleMatrix, 0, this.mScaleX, this.mScaleY, 1.0f);
            Matrix.multiplyMM(mMVPMatrix, 0, m, 0, mTranslationMatrix, 0);//
            Matrix.multiplyMM(mMVPMatrix2, 0, mMVPMatrix, 0, mScaleMatrix, 0);
        } else {
            Matrix.setIdentityM(mTranslationMatrix, 0);
            Matrix.translateM(mTranslationMatrix, 0, mPosX, mPosY, 0);
            Matrix.multiplyMM(mMVPMatrix2, 0, m, 0, mTranslationMatrix, 0);
        }
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, mUvBuffer);
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, mMVPMatrix2, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mHandleBitmap[mBitmapState]);
        GLES20.glUniform1i(mSamplerLoc, 0);
        // 투명한 배경을 처리한다.
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        // 이미지 핸들을 출력한다.
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndices.length,
                GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }
}
