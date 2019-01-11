package com.example.jendi.arkanoid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.StrictMath.abs;

class GameView extends View {
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int TOP = 2;
    private static final int BOTTOM = 3;
    private boolean isSetup = true;
    private int width;
    private int height;
    private Player player;
    private Ball ball;
    private int xCenter;
    private int yCenter;
    private Paint backgroundPaint;
    private Paint elementPaint;
    private List<Block> blockList;

    public GameView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int pointerIndex = event.getActionIndex();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getX() - 100 > 0 && event.getX() + 100 < width) {
                    player.setPosX((int) event.getX(pointerIndex));
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        if (isSetup) {
            width = getWidth();
            height = getHeight();
            xCenter = width/2;
            yCenter = height/2;
            player = new Player(xCenter, height - 200);
            ball = new Ball(xCenter, yCenter);
            backgroundPaint = new Paint();
            backgroundPaint.setARGB(255,0,0,0);
            elementPaint = new Paint();
            elementPaint.setARGB(255, 255, 255, 255);
            blockList = new ArrayList<>();
            for (int i = 27; i < width; i += 54) {
                for(int q = 20; q < 200; q += 40) {
                    blockList.add(new Block(i, q));
                }
            }
            isSetup = false;
        }

        //Background
        canvas.drawRect(0, 0, width, height, backgroundPaint);

        //Player
        canvas.drawRect(player.getRect(), elementPaint);

        //Ball
        canvas.drawRect(ball.getRect(), elementPaint);

        //Blocks
        for (Block b : blockList) {
            canvas.drawRect(b.getRect(), b.getPaint());
        }

        //Invalidate view
        invalidate();
    }

    @Override
    public void invalidate() {

        boolean blockCollisionFound = false;
        Block collidedBlock = null;

        //Pilka ze stanem, w ktorym znajduje sie w momencie kolizji
        Ball nextBall = new Ball(ball);
        int newPosX = ball.getPosX() + ball.getSpeed()*ball.getDirX();
        int newPosY = ball.getPosY() + ball.getSpeed()*ball.getDirY();
        nextBall.setPosX(newPosX);
        nextBall.setPosY(newPosY);

        //Sprawdzenie wszystkich blokow
        for (Block block : blockList) {
            if (nextBall.getRect().intersect(block.getRect())) {
                blockCollisionFound = true;
                collidedBlock = block;
                int side = checkCollisionSide(block, ball);
                switch (side) {
                    case LEFT:
                    case RIGHT:
                        ball.reboundHorizontally();
                        break;
                    case TOP:
                    case BOTTOM:
                        ball.reboundVertically();
                        ball.setPosY(ball.getPosY() + ball.getSpeed()*ball.getDirY());
                        break;
                }
            }
            if (blockCollisionFound) break;
        }

        //Jesli byla kolizja to usuwam blok z tablicy
        if (blockCollisionFound) {
            blockList.remove(collidedBlock);
        }

        //Obsluga X
        if (newPosX > width || newPosX < 0) {
            ball.reboundHorizontally();
        }
        ball.setPosX(newPosX);

        //Obsluga Y
        if (ball.getRect().intersect(player.getRect()) || newPosY < 0) {
            ball.reboundVertically();
            ball.setPosY(ball.getPosY() + ball.getSpeed()*ball.getDirY());
        }
        else if (newPosY > height) {
            ball.resetBall(width, height);
        }
        else {
            ball.setPosY(newPosY);
        }

        super.invalidate();
    }

    private int checkCollisionSide(Block block, Ball ball) {
        Rect rBall = ball.getRect();
        Rect rBlock = block.getRect();
        //Pilka leci z gory
        if (ball.getDirY() == 1) {
            //Od lewej sciany
            if (rBall.left < rBlock.left && rBall.right < rBlock.left) {
                return LEFT;
            }
            //Od prawej
            else if (rBall.left > rBlock.right && rBall.right > rBlock.right) {
                return RIGHT;
            }
            //Od gornej
            else {
                return TOP;
            }
        }
        //Pilka leci z dolu
        else {
            //Od lewej
            if (rBall.left < rBlock.left && rBall.right < rBlock.left) {
                return LEFT;
            }
            //Od prawej
            else if (rBall.left > rBlock.right && rBall.right > rBlock.right) {
                return RIGHT;
            }
            //Od dolnej
            else {
                return BOTTOM;
            }
        }
    }
}
