package com.lbo.defensegamesoldier;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

// 서피스뷰 클래스
public class MainGLSurfaceView extends GLSurfaceView {
    // 랜더러
    private final MainGLRenderer mGLRenderer;
    //생성자
    public MainGLSurfaceView(MainActivity activity, int width, int height) {
        super(activity.getApplicationContext());
        // OpenGL ES 2.0 context를 생성한다.
        setEGLContextClientVersion(2);
        // GLSerfaceView를 사용하기 위해 Context를 이용해 랜더러를 생성한다.
        mGLRenderer = new MainGLRenderer(activity, width, height);
        setRenderer(mGLRenderer);
        // 랜더모드를 변경될 경우 그린다.
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
    @Override
    public void onPause() {
        super.onPause();
        mGLRenderer.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        mGLRenderer.onResume();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        mGLRenderer.onTouchEvent(event);
        return true;
    }
}