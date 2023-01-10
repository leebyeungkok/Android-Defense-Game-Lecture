package com.lbo.defensegamepinchmove;

// 버튼 클래스
public class Button extends Unit {
    // 버튼 생성자
    public Button(int programImage, int programSolidColor) {
        super(programImage, programSolidColor);
    }
    public void setDisable(boolean b) {
        if(b == false)
            mBitmapState = 0;
        else
            mBitmapState = 1;
    }
}