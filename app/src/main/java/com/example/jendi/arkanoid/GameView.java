package com.example.jendi.arkanoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
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
    private Rect background;

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
            height = getHeight() - 4;//wyrownanie ekrany dla wygody
            xCenter = width / 2;
            yCenter = height / 2;
            player = new Player(xCenter, height - 200);
            ball = new Ball(xCenter, yCenter);
            backgroundPaint = new Paint();
            backgroundPaint.setARGB(255, 0, 0, 0);
            elementPaint = new Paint();
            elementPaint.setARGB(255, 255, 255, 255);
            blockList = new ArrayList<>();
            for (int i = 54; i < width; i += 108) {
                for (int q = 20; q < 200; q += 40) {
                    BlockType type = BlockType.YELLOW;
                    blockList.add(new Block(i, q, type.getLives(), BitmapFactory.decodeResource(getResources(), type.getColor())));
                }
                background = new Rect(0, 0, width, height);
                isSetup = false;
            }
        }

        //Background
        canvas.drawRect(background, backgroundPaint);

        //Player
        canvas.drawRect(player.getRect(), elementPaint);

        //Ball
        canvas.drawRect(ball.getRect(), elementPaint);

        //Blocks
        for (Block b : blockList) {
            canvas.drawBitmap(b.getBitmap(), null, b.getRect(), null);
        }

        //Invalidate view
        invalidate();
    }


    @Override
    public void invalidate() {

        //Pozycja i stan, w ktorej bedzie pilka w nastepnej klatce
        int nextPosX = ball.getPosX() + (ball.getDirX() * ball.getSpeed());
        int nextPosY = ball.getPosY() + (ball.getDirY() * ball.getSpeed());
        Ball nextBall = new Ball(ball);
        nextBall.setPosX(nextPosX);
        nextBall.setPosY(nextPosY);

        //Pomocnicze prostokaty
        Rect rNextBall = nextBall.getRect();
        Rect rPlayer = player.getRect();

        //Kolizja ze scianami
        if (!background.contains(rNextBall))
        {
            if (rNextBall.top < background.top) {
                ball.reboundVertically();
            }
            else if (rNextBall.bottom > background.bottom) {
                ball.reset(xCenter, yCenter);
            }
            else {
                ball.reboundHorizontally();
            }
        }
        //Kolizja z paletka
        else if (rNextBall.intersect(rPlayer)) {
            ball.reboundVertically();
        }
        //Kolizja z bloczkami
        else {
            boolean collisionOccured = false;
            Block blockToBeRemoved = null;
            for (Block block : blockList) {
                Rect rBlock = block.getRect();
                if (rNextBall.intersect(rBlock)) {
                    collisionOccured = true;
                    blockToBeRemoved = block;
                    int side = checkCollisionSide(block, ball);
                    switch (side) {
                        case LEFT:
                        case RIGHT:
                            ball.reboundHorizontally();
                            break;
                        case TOP:
                        case BOTTOM:
                            ball.reboundVertically();
                            break;
                    }
                }
                if (collisionOccured) {
                    blockList.remove(blockToBeRemoved);
                    break;
                }
            }
        }
        ball.moveBall();
        super.invalidate();
    }

//    private int checkCollisionSide(Block block, Ball ball) {
//        Rect rBall = ball.getRect();
//        Rect rBlock = block.getRect();
//        //Pilka leci z gory
//        if (ball.getDirY() == 1) {
//            //Od lewej sciany
//            if (rBall.left < rBlock.left && rBall.right < rBlock.left) {
//                return LEFT;
//            }
//            //Od prawej
//            else if (rBall.left > rBlock.right && rBall.right > rBlock.right) {
//                return RIGHT;
//            }
//            //Od gornej
//            else {
//                return TOP;
//            }
//        }
//        //Pilka leci z dolu
//        else {
//            //Od lewej
//            if (rBall.left < rBlock.left && rBall.right < rBlock.left) {
//                return LEFT;
//            }
//            //Od prawej
//            else if (rBall.left > rBlock.right && rBall.right > rBlock.right) {
//                return RIGHT;
//            }
//            //Od dolnej
//            else {
//                return BOTTOM;
//            }
//        }
//    }

    private int checkCollisionSide(Block block, Ball ball) {
        //Pomocnicze prostokaty
        Rect rBall = ball.getRect();
        Rect rBlock = block.getRect();
        int offsetValue = ball.getSpeed();

        //Lece z gory od lewej
        if ((ball.getDirX() == 1) && (ball.getDirY() == 1)) {
            rBall.offset(offsetValue, 0);
            //Odbijam sie od lewej
            if (rBall.intersect(rBlock)) {
                return LEFT;
            }
            else {
                return TOP;
            }
        }
        //Lece z gory od prawej
        else if ((ball.getDirX() == -1) && (ball.getDirY() == 1)) {
            rBall.offset(-offsetValue, 0);
            //Odbijam sie od prawej
            if (rBall.intersect(rBlock)) {
                return RIGHT;
            }
            else {
                return TOP;
            }
        }
        //Lece od dolu z lewej
        else if ((ball.getDirX() == 1) && (ball.getDirY() == -1)) {
            rBall.offset(offsetValue, 0);
            //Odbijam sie od lewej
            if (rBall.intersect(rBlock)) {
                return LEFT;
            }
            else {
                return BOTTOM;
            }
        }
        //Lece od dolu z prawej
        else {
            rBall.offset(-offsetValue, 0);
            //Odbijam sie od prawej
            if (rBall.intersect(rBlock)) {
                return RIGHT;
            }
            else {
                return BOTTOM;
            }
        }
    }
}
