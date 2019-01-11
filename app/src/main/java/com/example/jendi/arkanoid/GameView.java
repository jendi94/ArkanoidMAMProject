package com.example.jendi.arkanoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
    public static List<Bitmap> blockTierList;
    private Rect background;
    private ArrayList<Prize> prizeList;
    private Bitmap prizeBitmap;

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
            height = getHeight() - 4;//wyrownanie ekrany dla wygody, wspanialy przyklad antywzorca MAGIC NUMBER :)
            xCenter = width / 2;
            yCenter = height / 2;
            player = new Player(xCenter, height - 200, BitmapFactory.decodeResource(getResources(), R.drawable.player));
            ball = new Ball(xCenter, yCenter);
            backgroundPaint = new Paint();
            backgroundPaint.setARGB(255, 0, 0, 0);
            elementPaint = new Paint();
            elementPaint.setARGB(255, 255, 255, 255);
            blockList = new ArrayList<>();
            prizeList = new ArrayList<>();
            blockTierList = new ArrayList<>();
            blockTierList.add(BitmapFactory.decodeResource(getResources(), R.drawable.yellow_block));
            blockTierList.add(BitmapFactory.decodeResource(getResources(), R.drawable.orange_block));
            blockTierList.add(BitmapFactory.decodeResource(getResources(), R.drawable.red_block));
            prizeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.prize);
            InputStream inputStream = getResources().openRawResource(R.raw.level1);
            try {
                String jsonString = IOUtils.toString(inputStream);
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray array= jsonObject.getJSONArray("blocks");
                for (int i =0; i < array.length(); i++) {
                    int value = array.getInt(i);
                    if (value == 0) {
                        blockList.add(null);
                    }
                    else {
                        int x = 54 + (i%10)*108;
                        int y = 20 + (i/10)*40;
                        blockList.add(new Block(x,y,value,blockTierList.get(value-1)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            IOUtils.closeQuietly(inputStream);

            background = new Rect(0, 0, width, height);
            isSetup = false;
        }

        //Background
        canvas.drawRect(background, backgroundPaint);

        //Player
        canvas.drawRect(player.getRect(), elementPaint);
        //canvas.drawBitmap(player.getBitmap(), null, player.getRect(), null);

        //Ball
        canvas.drawRect(ball.getRect(), elementPaint);

        //Blocks
        for (Block b : blockList) {
            if (b != null) {
                canvas.drawBitmap(b.getBitmap(), null, b.getRect(), null);
            }
        }

        for (Prize prize : prizeList) {
            canvas.drawBitmap(prize.getBitmap(), null, prize.getRect(), null);
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
            Block collidedBlock = null;
            for (Block block : blockList) {
                if (block != null) {
                    Rect rBlock = block.getRect();
                    if (rNextBall.intersect(rBlock)) {
                        collisionOccured = true;
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
                                break;
                        }
                    }
                    if (collisionOccured) {
                        //Traci ostatnie zycie
                        if (collidedBlock.getLives() == 1) {
                            blockList.remove(collidedBlock);
                        }
                        else {
                            if (collidedBlock.getLives() == 3) {
                                prizeList.add(new Prize(collidedBlock.getPosX(), collidedBlock.getPosY(), 0, prizeBitmap));
                            }
                            collidedBlock.tierDown();
                        }
                        //Predkosc pilki rosnie
                        ball.setSpeed(ball.getSpeed()+1);
                        break;
                    }
                }
            }
        }
        ball.moveBall();

        //Obsluga nagrod

        //Zderzenie z paletka
        List<Prize> caughtPrizes = new ArrayList<>();
        for (Prize prize : prizeList) {
            Rect rPrize = prize.getRect();
            if (rPrize.intersect(rPlayer)) {
                caughtPrizes.add(prize);
            }
        }
        for (Prize prize : caughtPrizes) {
            prizeList.remove(prize);
        }
        for (Prize prize : prizeList) {
            prize.move();
        }
        super.invalidate();
    }

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
