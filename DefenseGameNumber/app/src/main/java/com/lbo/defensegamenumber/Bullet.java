package com.lbo.defensegamenumber;

// Unit으로부터 상속받아 총알를 구현하기 위한 객체
public class Bullet extends Unit {
    // 총알의 타입 0~1까지 사용한다.
    int mType = 0;
    int mIndex = 0;
    int mEnergy = 10;
    Soldier mAttackSoldier;
    Zombie mTargetZombie;
    MainGLRenderer mMainGLRenderer;
    //----------
    // 생성자
    public Bullet(int programImage, int programSolidColor){ //, MainGLRenderer mainGLRenderer) {
        super(programImage, programSolidColor);
        //mMainGLRenderer = mainGLRenderer;
        // mCount는 매번 루프를 돌때마다 호출된다. 병사의 움직임에 관여하는데 병사마다
        // 다른 시작점을 줌으로써 각기 다른 움직임을 갖도록 처리한다.
        mCount = (int) (Math.random() * 100);
    }
    // 총알의 속성을 설정한다.타입, 종류, 인덱스번호
    public void setType(int type) {
        mType = type;

        if(type == 0){
            mEnergy = 5;
        } else if(type == 1){
            mEnergy = 10;
        }
    }
    float mGapX = 0f;
    float mGapY = 0f;
    // 해당 블럭에 위치를 지정함
    public void setToBlock( int row, int col) {
        mTargetX = Map.getPosX(row, col);
        mTargetY = Map.getPosY(row, col) + this.mHeight / 3;
        mPosX = mTargetX;
        mPosY = mTargetY;
    }
    // 해당 블럭이로 이동함.
    public void moveToBlock(Soldier soldier,
                            Zombie zombie,
                            int startRow, int startCol, int endRow, int endCol,
                            MainGLRenderer mainGlRenderer) {
        mAttackSoldier = soldier;
        mTargetZombie = zombie;
        mMainGLRenderer = mainGlRenderer;
        // 이동하려는 위치중 현재 블럭을 제외함.
        mPosX = Map.getPosX(startRow, startCol);
        mPosY = Map.getPosY(startRow, startCol);
        mTargetX = Map.getPosX(endRow, endCol);
        mTargetY = Map.getPosY(endRow, endCol) + 50;
        mGapX = (mTargetX - mPosX)/10;
        mGapY = (mTargetY - mPosY)/10;
        mIsActive = true;
    }
    // 병사의 생각을 관리함.
    int mCount = 0;
    // 길찾기 알고리즘에서 사용하는 블럭개수 관리
    int mPathCount = 0;
    long mLastTime = 0;
    // 병사가 생각하도록 만드는 함수
    public void think() {
        if(mIsActive == false ){
            return;
        }

        mPosX = mPosX + mGapX;
        mPosY = mPosY + mGapY;


        // mPath배열의 마지막 인덱스까지 계산했다면 최종 목적지에 도착한 상태임
        if (Math.abs(mTargetX - mPosX) < 10 &&
                Math.abs(mTargetY - mPosY) < 10 ) {
            mAttackSoldier.mIsAttack = false;
            mTargetZombie.decreaseEnergy(mEnergy, mMainGLRenderer);
            destroy();
        }
    }
    void destroy(){
        this.setIsActive(false);
    }
}


