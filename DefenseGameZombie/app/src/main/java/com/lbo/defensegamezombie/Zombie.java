package com.lbo.defensegamezombie;

import android.opengl.GLES20;
import android.opengl.Matrix;

// Unit으로부터 상속받아 좀비를 구현하기 위한 객체
public class Zombie extends Unit {
    // 좀비의 타입 0~8까지 사용한다.
    int mType = 0;
    // 좀비의 상태 방어, 이동, 공격, 전투중을 나타낸다.
    int mState = 0;
    // 현재의 에너지를 나타낸다.
    int mCurrentEnergy = 0;
    // 좀비의 총 에너지를 나타낸다.
    int mEnergy = 100;
    // 좀비의 방어력을 나타낸다.
    int mSpeed = 10;
    // 좀비의 인덱스 값을 나타낸다. 배열로 표현할 예정이므로 자신의 배열 인덱스를 갖는다.
    int mIndex = 0;
    // 좀비의 게임맵의 블럭 위치를 나타낸다.
    int mPosBlockRow = 0;
    int mPosBlockCol = 0;
    int mDir = 0;

    public int getCurrRow() {
        return mPosBlockRow;
    }
    public int getCurrCol() {
        return mPosBlockCol;
    }

    // 생성자
    public Zombie(int programImage, int programSolidColor){ //, MainGLRenderer mainGLRenderer) {
        super(programImage, programSolidColor);
        //mMainGLRenderer = mainGLRenderer;
        // mCount는 매번 루프를 돌때마다 호출된다. 좀비의 움직임에 관여하는데 좀비마다
        // 다른 시작점을 줌으로써 각기 다른 움직임을 갖도록 처리한다.
        mCount = (int) (Math.random() * 100);
    }
    public int getPosBlockRow(){
        return mPosBlockRow;
    }
    public int getPosBlockCol(){
        return mPosBlockCol;
    }
    public void setSpeed(int speed){
        mSpeed = speed;
    }
    public void setEnerge(int energe){
        mEnergy = energe;
    }
    // 해당좌표로 이동함
    public void moveTo(int x, int y) {
        mTargetX = x;
        mTargetY = y;
    }
    // 해당좌표로 이동함
    public void moveTo(float x, float y) {
        mTargetX = x;
        mTargetY = y;
    }
    // 가로방향 확대축소를 설정함
    public void setScaleX(float scaleX) {
        this.mScaleX = scaleX;
    }
    // 현재 맵의 위치를 반환함
    public int getBlockRow() {
        return mPosBlockRow;
    }
    public int getBlockCol() {
        return mPosBlockCol;
    }
    // 해당 블럭에 위치를 지정함
    public void setToBlock(int row, int col) {
        mPosBlockRow = row;
        mPosBlockCol = col;
        mTargetX = Map.getPosX(row, col);
        mTargetY = Map.getPosY(row, col) + this.mHeight / 3;
        mPosX = mTargetX;
        mPosY = mTargetY;
    }
    // 해당 블럭이로 이동함.
    public void moveToBlock(int row, int col) {
        // 이동하려는 위치중 현재 블럭을 제외함.
        if(row == mPosBlockRow && col == mPosBlockCol){
            return;
        }
        mPosBlockRow = row;
        mPosBlockCol = col;
        mTargetX = Map.getPosX(row, col);
        mTargetY = Map.getPosY(row, col) + this.mHeight / 3;
    }
    // 좀비의 생각을 관리함.
    int mCount = 0;
    long mLastTime = 0;

    // 좀비가 생각하도록 만드는 함수
    public void think() {
        // 나중에
        if(mIsActive == false ){
            return;
        }
        long now = System.currentTimeMillis();
        long gab =  (long)((now - mLastTime )/10);
        if(gab < mSpeed){
            return;
        }
        mLastTime = now;

        mCount++;
        // 변수형 범위를 넘어설 경우를 대비해 초기화
        if (mCount > 30000) {
            mCount = 0;
        }
        if (this.mPosX > this.mTargetX + 10) {
            this.mPosX = this.mPosX - 9;
        } else if (this.mPosX < this.mTargetX - 10) {
            this.mPosX = this.mPosX + 9;
        }
        if (this.mPosY > this.mTargetY + 10) {
            this.mPosY = this.mPosY - 9;
        } else if (this.mPosY < this.mTargetY - 10) {
            this.mPosY = this.mPosY + 9;
        }

        if(mTargetX > mPosX) {
            mDir = 0;
        } else if(mTargetX < mPosX){
            mDir = 1;
        }

        if(mDir == 0) {
            if (mCount % 20 < 5) {
                this.mBitmapState = 0;
            } else if (mCount % 20 < 10) {
                this.mBitmapState = 1;
            } else if (mCount % 20 < 15) {
                this.mBitmapState = 2;
            } else {
                this.mBitmapState = 3;
            }
        } else {
            if (mCount % 20 < 5) {
                this.mBitmapState = 4;
            } else if (mCount % 20 < 10) {
                this.mBitmapState = 5;
            } else if (mCount % 20 < 15) {
                this.mBitmapState = 6;
            } else {
                this.mBitmapState = 7;
            }
        }
    }
}

