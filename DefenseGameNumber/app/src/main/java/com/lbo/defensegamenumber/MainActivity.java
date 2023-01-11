package com.lbo.defensegamenumber;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
// 메인 액티비티
public class MainActivity extends Activity {
    // GLSurfaceView
    private GLSurfaceView mGLSurfaceView;

    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 타이틀바를 제거함
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        // 화면 최대화
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 서피스뷰 생성을 위한 매트릭스
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        mGLSurfaceView = new MainGLSurfaceView(this, width, height);
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }
}