package com.lbo.defensegamemap;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
// 랜더링
public class MainGLRenderer implements Renderer {
    // 매트릭스
    private final float[] mMtrxProjection = new float[16];
    private final float[] mMtrxView = new float[16];
    private final float[] mMtrxProjectionAndView = new float[16];
    // 프로그램색상, 이미지
    private static int mProgramSolidColor;
    private static int mProgramImage;
    long mLastTime;
    // 디바이스의 넓이, 높이
    public static int mDeviceWidth = 0;
    public static int mDeviceHeight = 0;
    // 주 액티비티
    MainActivity mActivity;
    Context mContext;
    // 화면 설정
    ScreenConfig mScreenConfig;
    // 리소스 매니저
    ResourceManager mResourceManager;
    // 객체로 사용할 객체
    Unit mTitle;

    // 터치포인터
    private int mPointerId;
    // 생성자
    public MainGLRenderer(MainActivity activity, int width, int height) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
        mLastTime = System.currentTimeMillis() + 100;
        mDeviceWidth = width;
        mDeviceHeight = height;
    }
    // 멈춤
    public void onPause() {
    }
    // 재시작
    public void onResume() {
        mLastTime = System.currentTimeMillis();
    }
    // 서피스뷰 변경
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, (int) mDeviceWidth, (int) mDeviceHeight);
        Matrix.setIdentityM(mMtrxProjection, 0);
        Matrix.setIdentityM(mMtrxView, 0);
        Matrix.setIdentityM(mMtrxProjectionAndView, 0);
        Matrix.orthoM(mMtrxProjection, 0, 0f,2000, 0.0f, 1200, 0, 50);
        Matrix.setLookAtM(mMtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }
    // 서피스뷰 생성
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mScreenConfig = new ScreenConfig(mDeviceWidth, mDeviceHeight);
        mScreenConfig.setSize(2000, 1200);
        GLES20.glClearColor(0.0f, 0.5f, 0.0f, 1);
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vs_Image);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fs_Image);
        mProgramImage = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgramImage, vertexShader);
        GLES20.glAttachShader(mProgramImage, fragmentShader);
        GLES20.glLinkProgram(mProgramImage);
        GLES20.glUseProgram(mProgramImage);

        float scale = mContext.getResources().getDisplayMetrics().density;

        mResourceManager = new ResourceManager(mActivity, mContext, scale);

        // 유닛을 생성하고 이미지, 크기, 좌표를 설정한다.  -- 자원관리
        mTitle = new Unit(mProgramImage, mProgramSolidColor);
        Map.getMap();
        for (int i = 0; i < Map.mInfoSizeRow; i++) {
            for (int j = 0; j < Map.mInfoSizeCol; j++) {
                Map.mLand[i][j] = new Unit(mProgramImage, mProgramSolidColor);
            }
        }
        mResourceManager.loadResource(mTitle, Map.mLand);
        mTitle.setIsActive(true);
        mTitle.setPos(1000, 600);
        //ConstManager.SCREEN_MODE = ConstManager.SCREEN_GAME;

        for(int i=0; i< Map.mInfoSizeRow; i++){
            for(int j=0; j < Map.mInfoSizeRow; j++) {
                Map.mLand[i][j].setIsActive(true);
                //Map.mLand[i][j].setPos(110 + i * 200, 110);
                if(Map.mInfo[i][j] == 0) {
                    Map.mLand[i][j].setPos(Map.getPosX(i, j), Map.getPosY(i, j));
                } else {
                    Map.mLand[i][j].setPos(Map.getPosX(i, j), Map.getPosY(i, j) +20);
                }

            }
        }
    }
    // 쉐이더 이미지
    public static final String vs_Image =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    " gl_Position = uMVPMatrix * vPosition;" +
                    " v_texCoord = a_texCoord;" +
                    "}";
    public static final String fs_Image =
            "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    " gl_FragColor = texture2D( s_texture, v_texCoord );" +
                    "}";
    // 쉐이더 로딩
    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
    // 그리기 호출
    @Override
    public void onDrawFrame(GL10 unused) {
        long now = System.currentTimeMillis();
        if (mLastTime > now)
            return;
        long elapsed = now - mLastTime;
        // 그리기를 시작한다.
        if(ConstManager.SCREEN_MODE == ConstManager.SCREEN_INTRO){
            renderIntro(mMtrxProjectionAndView);
        } else if(ConstManager.SCREEN_MODE == ConstManager.SCREEN_GAME){
            renderGame(mMtrxProjectionAndView);
        }

        mLastTime = now;
    }
    private void renderIntro(float[] m){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Matrix.orthoM(mMtrxProjection, 0, 0f, 2000, 0.0f, 1200, 0, 50);
        Matrix.multiplyMM(mMtrxProjectionAndView, 0, mMtrxProjection, 0, mMtrxView, 0);
        mTitle.draw(mMtrxProjectionAndView);
    }
    private void renderGame(float[] m){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1);
        Matrix.orthoM(mMtrxProjection, 0, 0f, 2000, 0.0f, 1200, 0, 50);
        Matrix.multiplyMM(mMtrxProjectionAndView, 0, mMtrxProjection, 0, mMtrxView, 0);
        for (int i = 0; i < Map.mInfoSizeRow; i++) {
            for (int j = 0; j < Map.mInfoSizeCol; j++) {
                Map.mLand[i][j].draw(mMtrxProjectionAndView);
            }
        }
    }
    public boolean onTouchEvent(MotionEvent event){
        final int x = (int)event.getX();
        final int y = (int)event.getY();
        final int chgX = mScreenConfig.getX(x);
        final int chgY = mScreenConfig.getY(y);

        mPointerId = event.getPointerId(0);
        final int action = event.getAction();
        switch(action & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:{
                selectTouch(chgX, chgY);
            }
        }
        return true;
    }
    private void selectTouch(int x, int y){
        if(ConstManager.SCREEN_MODE == ConstManager.SCREEN_INTRO) {
            selectTouchIntro(x, y);
        } else if(ConstManager.SCREEN_MODE == ConstManager.SCREEN_GAME) {
            selectTouchGame(x, y);
        }
    }
    private void selectTouchIntro(int x, int y){
        ConstManager.SCREEN_MODE = ConstManager.SCREEN_GAME;
    }
    private void selectTouchGame(int x, int y){
        ConstManager.SCREEN_MODE = ConstManager.SCREEN_INTRO;
    }

}

