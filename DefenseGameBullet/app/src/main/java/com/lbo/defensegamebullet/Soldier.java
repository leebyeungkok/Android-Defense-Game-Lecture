package com.lbo.defensegamebullet;


// Unit으로부터 상속받아 병사를 구현하기 위한 객체
public class Soldier extends Unit {
    // 병사의 타입 0~8까지 사용한다.
    int mType = 0;
    int mAttackPoint = 5; //
    // 1:아군, 2:적군을 나타낸다.
    int mIndex = 0;
    // 병사의 게임맵의 블럭 위치를 나타낸다.
    int mPosBlockRow = 0;
    int mPosBlockCol = 0;
    int mDir = 0;
    int mWidth = 150;
    int mHeight = 150;
    boolean mIsAttack = false;
    // MainGLRenderer를 참조한다.
    //private MainGLRenderer mMainGLRenderer;
    // 생성자
    public Soldier(int programImage, int programSolidColor){ //, MainGLRenderer mainGLRenderer) {
        super(programImage, programSolidColor);
        //mMainGLRenderer = mainGLRenderer;
        // mCount는 매번 루프를 돌때마다 호출된다. 병사의 움직임에 관여하는데 병사마다
        // 다른 시작점을 줌으로써 각기 다른 움직임을 갖도록 처리한다.
        mCount = (int) (Math.random() * 100);
    }
    // 병사의 속성을 설정한다.타입, 종류, 인덱스번호
    public void setProperty(int type, int index) {
        mType = type;
        mIndex = index;
        mBitmapState = 0;
        setType(type);
    }
    // 병사의 타입을 설정한다.
    public void setType(int type) {
        mType = type;
        if(type == 0) {
            mAttackPoint = 5;
        } else if(type == 1){
            mAttackPoint = 20;
        }
    }
    // 현재 맵의 위치를 반환함
    public int getBlockRow() {
        return mPosBlockRow;
    }
    public int getBlockCol() {
        return mPosBlockCol;
    }
    public int getCurrRow() {
        return mPosBlockRow;
    }
    public int getCurrCol() {
        return mPosBlockCol;
    }
    // 해당 블럭에 위치를 지정함
    public void setToBlock(int row, int col) {
        mPosBlockRow = row;
        mPosBlockCol = col;
        mTargetX = Map.getPosX(row, col);
        mTargetY = Map.getPosY(row, col) + this.mHeight / 2;
        mPosX = mTargetX;
        mPosY = mTargetY;
    }
    public void think() {
        if(mIsActive == false ){
            return;
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
    }
}

