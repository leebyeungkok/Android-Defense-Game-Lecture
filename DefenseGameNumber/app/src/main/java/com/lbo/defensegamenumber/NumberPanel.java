package com.lbo.defensegamenumber;

public class NumberPanel {
    private boolean mIsActive = true;

    private int mNumber=0;
    private int mNumberHeight = 0;
    private int mNumberWidth = 0;
    private int[] mHandleNumber;
    private Unit[] mPanel = new Unit[5]; // 99999까지 가능
    // 숫자패널 생성자 (숫자를 나타낼 패널을 전달받음)
    public NumberPanel(int programImage, int programSolidColor) {
        for (int i = 0; i < 5; i++)
            mPanel[i] = new Unit(programImage, programSolidColor);
    }
    // 패널의 숫자크기 설정
    public void setNumberSize(int width, int height){
        mNumberWidth = width;
        mNumberHeight = height;
    }
    // 핸들설정
    public void setBitmap(int[] handleNumber){

        mHandleNumber = handleNumber;
    }
    // 숫자반환
    public int getNumber(){
        return mNumber;
    }
    // 숫자감소
    public void addNumber(int number){
        mNumber += number;
        setNumber(mNumber);
    }
    // 숫자설정 - 자리수별로 계산하여 표현할 숫자 이미지를 설정한다.
    public void setNumber(int number){
        mNumber = number;
        int i =0;
        i = (int)((mNumber % 100000)/10000);
        mPanel[0].setBitmap(mHandleNumber[i], mNumberWidth, mNumberHeight);
        i = (int)((mNumber % 10000)/1000);
        mPanel[1].setBitmap(mHandleNumber[i], mNumberWidth, mNumberHeight);
        i = (int)((mNumber % 1000)/100);
        mPanel[2].setBitmap(mHandleNumber[i], mNumberWidth, mNumberHeight);
        i = (int)((mNumber % 100)/10);
        mPanel[3].setBitmap(mHandleNumber[i], mNumberWidth, mNumberHeight);
        i = (int)(mNumber % 10);
        mPanel[4].setBitmap(mHandleNumber[i], mNumberWidth,mNumberHeight);
    }
    // 숫자 위치
    public void setPos(int x, int y){
        for(int i=0; i< 5; i++)
            mPanel[i].setPos(x + (i * 50), y);
    }
    // 활성화
    public void setIsActive(boolean isActive){
        mIsActive = isActive;
        for(int i=0; i<5; i++)
            mPanel[i].setIsActive(true);
    }
    // 그리기
    void draw(float[] m) {
        for(int i=0; i<5; i++)
            mPanel[i].draw(m);
    }
}