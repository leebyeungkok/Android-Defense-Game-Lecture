package com.lbo.defensegamethread;

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

    // 현재 이동중인지를 관리함
    private boolean mPathMove = false;
    // 블럭간 움직임여부를 관리함
    private boolean mPathMoveStep = false;
    // 최단거리 알고리즘에 의해 mPath 배열의 현재 인덱스를 관리함
    private int mPathStep = 0;
    // 최단거리 알고리즘에 의해 mPath 배열의 크기를 관리함
    private int mPathMax = 0;
    // 최단거리 알고리즘에 의해 관리되는 배열
    private int[][] mPath = new int[400][2];

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
        if(mThreadCalculated == false){
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
        ////////////////////////////////////////////////////
        // 좀비가 움직일 경우의 알고리즘
        if (mPathMove == true) {
            // 한칸씩 움직여 다음 블럭에 도착한 경우
            if (mPathMoveStep == true) {
                moveToBlock(mPath[mPathStep][ConstManager.ROW],
                        mPath[mPathStep][ConstManager.COL]);
                mPathMoveStep = false;
            }
            // 한칸씩 움직이고 있는 경우
            if(mPathMoveStep == false) {
                if (this.mPosX <= this.mTargetX + 10
                        && this.mPosX >= this.mTargetX - 10
                        && this.mPosY <= this.mTargetY + 10
                        && this.mPosY >= this.mTargetY - 10) {
                    this.mPosX = this.mTargetX;
                    this.mPosY = this.mTargetY;
                    mPathMoveStep = true;
                    mPathStep++;
                    // mPath배열의 마지막 인덱스까지 계산했다면 최종 목적지에 도착한 상태임
                    if (mPathStep == mPathMax) {
                        // 다시 값을 초기화 하고 도착했다고 알림
                        mPathMove = false;
                        mPathMax = 0;
                        /////////////////////////////////////////////
                        destroy();
                        //activeForceState(ConstMgr.STATE_ARRIVE); // 도착
                    }
                }
            }
        }
    }
    void destroy(){
        this.setIsActive(false);
    }

    boolean mThreadCalculated = true;
    // 목적지에 맞는 블럭 목록을 mPath에 축적함.
    public void moveToPosBlock(int row, int col) {
        mPathMove = false;
        mPathMoveStep = false;
        mPathMax = 0;
        mPathStep = 0;
        //try {
        mTargetBlockRow = row;
        mTargetBlockCol = col;
        Thread thread1 = new Thread() {
            public void run(){
                mThreadCalculated = false;
                mPath = getShortPath(row, col);
                mThreadCalculated = true;
                mPathMove = true;
                mPathMoveStep = true;
            }

        };
        thread1.start();

    }
    // 최단거리 알고리즘 (좌표를 가진 2차원배열을 반환함)
    public int[][] getShortPath(int tRow, int tCol) {
        //throws Exception {
        // 현재 좀비의 블럭을 중심으로 시작함.
        int sRow = mPosBlockRow;
        int sCol = mPosBlockCol;
        // 현재 지형을 path에 대입함. 움직일 수 없는 지형은 10000을 입력함.
        int[][] path = new int[Map.mInfoSizeRow][Map.mInfoSizeCol];
        for (int i = 0; i < Map.mInfoSizeRow; i++) {
            for (int j = 0; j < Map.mInfoSizeCol; j++) {
                if (Map.mInfo[i][j]==0 ){ // 평지일 경우 지나다닐 수 있다.
                    path[i][j] = 0;
                } else {
                    path[i][j] = 10000;
                }
            }
        }
        // 최종으로 반환할 경로 배열
        int[][] calcPath = new int[10000][2];
        // 최종으로 반환할 경로 배열에 최종적으로 관리할 인덱스 변수
        int maxNum = 0;
        // 현재의 위치를 먼저 담는다.
        calcPath[maxNum][ConstManager.ROW] = sRow;
        calcPath[maxNum][ConstManager.COL] = sCol;
        maxNum++;
        // 최종목적지에 다다랗다면 findIt을 true롤 설정한다.
        boolean findIt = false;
        // 최단거리를 구하기 위한 반복회수 5000으로 설정
        int loopCount = 10000;
        for (int curNum = 0; curNum < loopCount; curNum++) {
            // 최종 좌표를 읽어와 처리함
            int curRow = calcPath[curNum][ConstManager.ROW];
            int curCol = calcPath[curNum][ConstManager.COL];
            // 현재 위치가 이동이 불가할 경우 findIt = false 설정후 종료
            if (path[curRow][curCol] == 10000) {
                findIt = false;
                break;
            }
            // 현재 좌표와 움직이려는 좌표를 기준으로 4 방향 중
            // 가장 가까운 방향을 먼저 시작하도록 계산한다.
            float[] tempDir = new float[4];
            tempDir[0] = (float) (Math.abs(curRow - 1 - tRow) * Math.abs(curRow - 1 - tRow) +
                    Math.abs(curCol - tCol) * Math.abs(curCol - tCol));
            tempDir[1] = (float) (Math.abs(curRow + 1 - tRow) * Math.abs(curRow + 1 - tRow) +
                    Math.abs(curCol - tCol) * Math.abs(curCol - tCol));
            tempDir[2] = (float) (Math.abs(curRow - tRow) * Math.abs(curRow - tRow) +
                    Math.abs(curCol - 1 - tCol) * Math.abs(curCol - 1 - tCol));
            tempDir[3] = (float) (Math.abs(curRow - tRow) * Math.abs(curRow - tRow) +
                    Math.abs(curCol + 1 - tCol) * Math.abs(curCol + 1 - tCol));
            int[] tempDirValue = {0, 1, 2, 3};
            for (int i = 0; i < 4; i++) {
                for (int j = 1; j < 4; j++){
                    if (tempDir[j] < tempDir[i]) {
                        float temp = tempDir[i];
                        tempDir[i] = tempDir[j];
                        tempDir[j] = temp;
                        int tempValue = tempDirValue[i];
                        tempDirValue[i] = tempDirValue[j];
                        tempDirValue[j] = tempValue;
                    }
                }
            }
            int minusRow = curRow - 1;
            int plusRow = curRow + 1;
            int minusCol = curCol - 1;
            int plusCol = curCol + 1;
            if (minusRow < 0)
                minusRow = 0;
            if (plusRow > Map.mInfoSizeRow - 1)
                plusRow = Map.mInfoSizeRow - 1;
            if (minusCol < 0)
                minusCol = 0;
            if (plusCol > Map.mInfoSizeCol - 1)
                plusCol = Map.mInfoSizeCol - 1;
            // 4방향 중 목적지와 동일하다면 findIt = true가 됨
            // dirOrder : 0 윗쪽, 1 아래쪽, 2 왼쪽, 3 오른쪽
            int dirOrder = 0;
            if (curRow - 1 == tRow && curCol == tCol) {
                dirOrder = 0;
                findIt = true;
            } else if (curRow + 1 == tRow && curCol == tCol) {
                dirOrder = 1;
                findIt = true;
            } else if (curRow == tRow && curCol - 1 == tCol) {
                dirOrder = 2;
                findIt = true;
            } else if (curRow == tRow && curCol + 1 == tCol) {
                dirOrder = 3;
                findIt = true;
            } else {
                // 자신이 지나온 길은 path의 값을 아래쪽에서 1씩 증가시킨다.
                // 최종 목적지가 아닐 경우 4군데를 비교하여 가까운쪽 방향을 우선순위로 하여
                // path의 값이 낮은곳을 먼저 찾게 한다.
                if (tempDirValue[0] == 0) {
                    if (curRow != 0 &&
                            path[minusRow][curCol] <= path[minusRow][curCol] &&
                            path[minusRow][curCol] <= path[plusRow][curCol] &&
                            path[minusRow][curCol] <= path[curRow][minusCol] &&
                            path[minusRow][curCol] <= path[curRow][plusCol]) {
                        dirOrder = 0;
                    } else if (curCol != 0 &&
                            path[curRow][minusCol] <= path[minusRow][curCol] &&
                            path[curRow][minusCol] <= path[plusRow][curCol] &&
                            path[curRow][minusCol] <= path[curRow][minusCol] &&
                            path[curRow][minusCol] <= path[curRow][plusCol]) {
                        dirOrder = 2;
                    } else if (curCol != Map.mInfoSizeCol - 1 &&
                            path[curRow][plusCol] <= path[minusRow][curCol] &&
                            path[curRow][plusCol] <= path[plusRow][curCol] &&
                            path[curRow][plusCol] <= path[curRow][minusCol] &&
                            path[curRow][plusCol] <= path[curRow][plusCol]) {
                        dirOrder = 3;
                    } else if (curRow != Map.mInfoSizeRow - 1 &&
                            path[plusRow][curCol] <= path[minusRow][curCol] &&
                            path[plusRow][curCol] <= path[plusRow][curCol] &&
                            path[plusRow][curCol] <= path[curRow][minusCol] &&
                            path[plusRow][curCol] <= path[curRow][plusCol]) {
                        dirOrder = 1;
                    }
                } else if (tempDirValue[0] == 1) {
                    if (curRow != Map.mInfoSizeRow - 1 &&
                            path[plusRow][curCol] <= path[minusRow][curCol] &&
                            path[plusRow][curCol] <= path[plusRow][curCol] &&
                            path[plusRow][curCol] <= path[curRow][minusCol] &&
                            path[plusRow][curCol] <= path[curRow][plusCol]) {
                        dirOrder = 1;
                    } else if (curCol != 0 &&
                            path[curRow][minusCol] <= path[minusRow][curCol] &&
                            path[curRow][minusCol] <= path[plusRow][curCol] &&
                            path[curRow][minusCol] <= path[curRow][minusCol] &&
                            path[curRow][minusCol] <= path[curRow][plusCol]) {
                        dirOrder = 2;
                    } else if (curCol != Map.mInfoSizeCol - 1 &&
                            path[curRow][plusCol] <= path[minusRow][curCol] &&
                            path[curRow][plusCol] <= path[plusRow][curCol] &&
                            path[curRow][plusCol] <= path[curRow][minusCol] &&
                            path[curRow][plusCol] <= path[curRow][plusCol]) {
                        dirOrder = 3;
                    } else if (curRow != 0 &&
                            path[minusRow][curCol] <= path[minusRow][curCol] &&
                            path[minusRow][curCol] <= path[plusRow][curCol] &&
                            path[minusRow][curCol] <= path[curRow][minusCol] &&
                            path[minusRow][curCol] <= path[curRow][plusCol]) {
                        dirOrder = 0;
                    }
                } else if (tempDirValue[0] == 2) {
                    if (curCol != 0 &&
                            path[curRow][minusCol] <= path[minusRow][curCol] &&
                            path[curRow][minusCol] <= path[plusRow][curCol] &&
                            path[curRow][minusCol] <= path[curRow][minusCol] &&
                            path[curRow][minusCol] <= path[curRow][plusCol]) {
                        dirOrder = 2;
                    } else if (curRow != 0 &&
                            path[minusRow][curCol] <= path[minusRow][curCol] &&
                            path[minusRow][curCol] <= path[plusRow][curCol] &&
                            path[minusRow][curCol] <= path[curRow][minusCol] &&
                            path[minusRow][curCol] <= path[curRow][plusCol]) {
                        dirOrder = 0;
                    } else if (curRow != Map.mInfoSizeRow - 1 &&
                            path[plusRow][curCol] <= path[minusRow][curCol] &&
                            path[plusRow][curCol] <= path[plusRow][curCol] &&
                            path[plusRow][curCol] <= path[curRow][minusCol] &&
                            path[plusRow][curCol] <= path[curRow][plusCol]) {
                        dirOrder = 1;
                    } else if (curCol != Map.mInfoSizeCol - 1 &&
                            path[curRow][plusCol] <= path[minusRow][curCol] &&
                            path[curRow][plusCol] <= path[plusRow][curCol] &&
                            path[curRow][plusCol] <= path[curRow][minusCol] &&
                            path[curRow][plusCol] <= path[curRow][plusCol]) {
                        dirOrder = 3;
                    }
                } else if (tempDirValue[0] == 3) {
                    if (curCol != Map.mInfoSizeCol - 1 &&
                            path[curRow][plusCol] <= path[minusRow][curCol] &&
                            path[curRow][plusCol] <= path[plusRow][curCol] &&
                            path[curRow][plusCol] <= path[curRow][minusCol] &&
                            path[curRow][plusCol] <= path[curRow][plusCol]) {
                        dirOrder = 3;
                    } else if (curRow != 0 &&
                            path[minusRow][curCol] <= path[minusRow][curCol] &&
                            path[minusRow][curCol] <= path[plusRow][curCol] &&
                            path[minusRow][curCol] <= path[curRow][minusCol] &&
                            path[minusRow][curCol] <= path[curRow][plusCol]) {
                        dirOrder = 0;
                    } else if (curRow != Map.mInfoSizeRow - 1 &&
                            path[plusRow][curCol] <= path[minusRow][curCol] &&
                            path[plusRow][curCol] <= path[plusRow][curCol] &&
                            path[plusRow][curCol] <= path[curRow][minusCol] &&
                            path[plusRow][curCol] <= path[curRow][plusCol]) {
                        dirOrder = 1;
                    } else if (curCol != 0 &&
                            path[curRow][minusCol] <= path[minusRow][curCol] &&
                            path[curRow][minusCol] <= path[plusRow][curCol] &&
                            path[curRow][minusCol] <= path[curRow][minusCol] &&
                            path[curRow][minusCol] <= path[curRow][plusCol]) {
                        dirOrder = 2;
                    }
                }
            }
            // 방향이 정해졌으므로 해당 방향으로 움직인다.
            if (dirOrder == 0) {
                if (curRow - 1 == tRow && curCol == tCol) {
                    findIt = true;
                }
                if (curRow != 0 && path[curRow - 1][curCol] != 10000) {
                    // 지나온 길은 path에 1씩 증가시킨다. 이후 지나온 길은 위의 로직에 의해
                    // 우선순위에서 밀려난다.
                    path[curRow - 1][curCol]++;
                    calcPath[maxNum][ConstManager.ROW] = curRow - 1;
                    calcPath[maxNum][ConstManager.COL] = curCol;
                    maxNum++;
                }
            } else if (dirOrder == 1) {
                if (curRow + 1 == tRow && curCol == tCol) {
                    findIt = true;
                }
                if (curRow != Map.mInfoSizeRow - 1 && path[curRow + 1][curCol] != 10000) {
                    path[curRow + 1][curCol]++;
                    calcPath[maxNum][ConstManager.ROW] = curRow + 1;
                    calcPath[maxNum][ConstManager.COL] = curCol;
                    maxNum++;
                }
            } else if (dirOrder == 2) {
                if (curRow == tRow &&
                        curCol - 1 == tCol) {
                    findIt = true;
                }
                if (curCol != 0 && path[curRow][curCol - 1] != 10000) {
                    path[curRow][curCol - 1]++;
                    calcPath[maxNum][ConstManager.ROW] = curRow;
                    calcPath[maxNum][ConstManager.COL] = curCol - 1;
                    maxNum++;
                }
            } else if (dirOrder == 3) {
                if (curRow == tRow &&
                        curCol + 1 == tCol) {
                    findIt = true;
                }
                if (curCol != Map.mInfoSizeCol - 1 && path[curRow][curCol + 1] != 10000) {
                    path[curRow][curCol + 1]++;
                    calcPath[maxNum][ConstManager.ROW] = curRow;
                    calcPath[maxNum][ConstManager.COL] = curCol + 1;
                    maxNum++;
                }
            }
            // 목적지를 찾았으므로 종료한다.
            // 더 검색하고 최단거리를 계산할수도 있겠지만 타협하고 여기서 종료한다.
            if (findIt == true) {
                break;
            }
            // 10000번 반복했는데도 아직 찾지 못했으므로 findIt=false 다.
            if (curNum > 0 && curNum == loopCount - 1) {
                findIt = false;
                break;
            }
        }
        // 아직 최종 좌표의 배열을 반환 전에 중복된 길은 제거한다.
        if (findIt == true) {
            for (int i = 0; i < maxNum - 1; i++) {
                for (int j = i + 1; j < maxNum; j++) {
                    if (calcPath[i][ConstManager.ROW] == calcPath[j][ConstManager.ROW] &&
                            calcPath[i][ConstManager.COL] == calcPath[j][ConstManager.COL]) {
                        int gab = j - i;
                        for (int k = i; k < maxNum; k++) {
                            calcPath[k][ConstManager.ROW] = calcPath[k + gab][ConstManager.ROW];
                            calcPath[k][ConstManager.COL] = calcPath[k + gab][ConstManager.COL];
                        }
                        maxNum = maxNum - gab;
                        i--;
                        break;
                    }
                }
            }
        }
        // 못찾았을 경우 찾을수 없어요 문구를 띄우고 끝낸다.
        else {
            //activeForceState(ConstMgr.STATE_FIND_WAY_FAULT);// 찾을 수 없어요..
            //throw new Exception();
        }
        // 최종적으로 계산된 배열의 크기와 배열을 반환한다.
        mPathMax = maxNum;
        return calcPath;
    }
    public void decreaseEnergy(int energy, MainGLRenderer mainGlRenderer){
        this.mEnergy = this.mEnergy - energy;
        if(this.mEnergy <=0){
            mainGlRenderer.mScore +=10;
            mainGlRenderer.mScorePanel.addNumber(10);
            destroy();
        }
    }
}

