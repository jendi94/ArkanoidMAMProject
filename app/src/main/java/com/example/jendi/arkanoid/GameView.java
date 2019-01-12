package com.example.jendi.arkanoid;

import android.app.Activity;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
    private boolean isGameOver;
    private int gameLevel;
    private Bitmap ballBitmap;
    private Context context;
    private long gameEndTime;
    private  boolean isFirstTimeGameEndCount = true;
    private boolean isLevelFinished = false;
    private boolean isFirstTimeNextLevelCount = true;
    private long nextLevelTime;
    private String customLevelString;

    public GameView(Context context, int level) {
        super(context);
        this.context = context;
        gameLevel = level;
    }

    public GameView(Context context, String array) {
        super(context);
        this.context = context;
        this.customLevelString = array;
    }

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
                if (event.getX() - 120 > 0 && event.getX() + 120 < width) {
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
            ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
            ball = new Ball(xCenter, yCenter, ballBitmap);
            backgroundPaint = new Paint();
            backgroundPaint.setARGB(255, 0, 0, 0);
            elementPaint = new Paint();
            elementPaint.setARGB(255, 255, 255, 255);
            elementPaint.setTextSize(50);
            blockList = new ArrayList<>();
            prizeList = new ArrayList<>();
            blockTierList = new ArrayList<>();
            blockTierList.add(BitmapFactory.decodeResource(getResources(), R.drawable.yellow_block));
            blockTierList.add(BitmapFactory.decodeResource(getResources(), R.drawable.orange_block));
            blockTierList.add(BitmapFactory.decodeResource(getResources(), R.drawable.red_block));
            prizeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.prize);

            //Wczytywanie poziomu

            if (customLevelString == null) {
                InputStream inputStream = getResources().openRawResource(R.raw.levels);
                try {
                    String jsonString = IOUtils.toString(inputStream);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray array = null;
                    switch (gameLevel) {
                        case 1:
                            array= jsonObject.getJSONArray("levelOne");
                            break;
                        case 2:
                            array= jsonObject.getJSONArray("levelTwo");
                            break;
                        case 3:
                            array = jsonObject.getJSONArray("levelThree");
                            break;
                    }

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
            }
            //Tutaj czytamy level
            else {
                StringTokenizer tokenizer = new StringTokenizer(customLevelString, ",");
                List<Integer> customLevelArray = new ArrayList<>();
                while (tokenizer.hasMoreElements()) {
                    String element = tokenizer.nextToken();
                    customLevelArray.add(Integer.valueOf(element));
                }
                for (int i = 0; i < customLevelArray.size(); i++) {
                    int value = customLevelArray.get(i);
                    if (value == 0) {
                        blockList.add(null);
                    } else {
                        int x = 54 + (i % 10) * 108;
                        int y = 20 + (i / 10) * 40;
                        blockList.add(new Block(x, y, value, blockTierList.get(value - 1)));
                    }
                }
            }


            background = new Rect(0, 0, width, height);
            isSetup = false;
        }

        //Background
        canvas.drawRect(background, backgroundPaint);

        if (isGameOver) {
            if (isFirstTimeGameEndCount) {
                gameEndTime = System.currentTimeMillis();
                isFirstTimeGameEndCount = false;
            }
            if (System.currentTimeMillis() > (gameEndTime + 2000)) {
                Activity activity = (Activity) getContext();
                activity.finish();
            }
            else{
                elementPaint.setTextSize(180);
                canvas.drawText("GAME OVER", xCenter - 500, yCenter, elementPaint);
            }
        }
        else if (isLevelFinished) {
            if (isFirstTimeNextLevelCount) {
                nextLevelTime = System.currentTimeMillis();
                isFirstTimeNextLevelCount = false;
            }
            if (System.currentTimeMillis() > (nextLevelTime + 2000)) {
                Activity activity = (Activity) getContext();
                activity.finish();
            }
            else {
                elementPaint.setTextSize(180);
                canvas.drawText("YOU WON", xCenter - 450, yCenter, elementPaint);
            }
        }
        else {
            //Player
            //canvas.drawRect(player.getRect(), elementPaint);
            canvas.drawBitmap(player.getBitmap(), null, player.getRect(), null);

            //Ball
            //canvas.drawRect(ball.getRect(), elementPaint);
            canvas.drawBitmap(ball.getBitmap(), null, ball.getRect(), null);

            //Blocks
            for (Block b : blockList) {
                if (b != null) {
                    canvas.drawBitmap(b.getBitmap(), null, b.getRect(), null);
                }
            }

            //Prizes
            for (Prize prize : prizeList) {
                canvas.drawBitmap(prize.getBitmap(), null, prize.getRect(), null);
            }

            canvas.drawText("S",10, yCenter, elementPaint);
            canvas.drawText("C",10, yCenter + 50, elementPaint);
            canvas.drawText("O",10, yCenter + 100, elementPaint);
            canvas.drawText("R",10, yCenter + 150, elementPaint);
            canvas.drawText("E",10, yCenter + 200, elementPaint);

            if (player.getScore() < 10) {
                canvas.drawText(String.valueOf(player.getScore()), 10, yCenter + 300, elementPaint);
            }
            else if (player.getScore() < 100) {
                canvas.drawText(String.valueOf(player.getScore()/10), 10, yCenter + 300, elementPaint);
                canvas.drawText(String.valueOf(player.getScore()%10), 10, yCenter + 350, elementPaint);
            }
            else {
                canvas.drawText(String.valueOf(player.getScore()/100), 10, yCenter + 300, elementPaint);
                canvas.drawText(String.valueOf((player.getScore()%100)/10), 10, yCenter + 350, elementPaint);
                canvas.drawText(String.valueOf(player.getScore()%10), 10, yCenter + 400, elementPaint);
            }

            canvas.drawText("L", width - 50, yCenter, elementPaint);
            canvas.drawText("I", width - 50, yCenter + 50, elementPaint);
            canvas.drawText("V", width - 50, yCenter + 100, elementPaint);
            canvas.drawText("E", width - 50, yCenter + 150, elementPaint);
            canvas.drawText("S", width - 50, yCenter + 200, elementPaint);

            if (player.getLives() < 10) {
                canvas.drawText(String.valueOf(player.getLives()), width - 50, yCenter + 300, elementPaint);
            }
            else if (player.getLives() < 100) {
                canvas.drawText(String.valueOf(player.getLives()/10), width - 50, yCenter + 300, elementPaint);
                canvas.drawText(String.valueOf(player.getLives()%10), width - 50, yCenter + 350, elementPaint);
            }
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
                player.setLives(player.getLives()-1);
                if (player.getLives() == 0) {
                    isGameOver = true;
                }
            }
            else {
                ball.reboundHorizontally();
            }
        }
        //Kolizja z paletka
        else if (rNextBall.intersect(rPlayer)) {
            if (ball.getSpeed() < 25) {
                ball.setSpeed(ball.getSpeed() + 1);
            }
            int side = checkCollisionSide(player, ball);
            switch (side) {
                case LEFT:
                case RIGHT:
                    ball.reboundHorizontally();
                    break;
                case TOP:
                    ball.reboundVertically();
                    break;
            }
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
                            player.setScore(player.getScore()+5);
                            blockList.remove(collidedBlock);
                        }
                        else {
                            if (collidedBlock.getLives() == 3) {
                                player.setScore(player.getScore() + 15);
                                prizeList.add(new Prize(collidedBlock.getPosX(), collidedBlock.getPosY(), 0, prizeBitmap));
                            }
                            else {
                                player.setScore(player.getScore() + 10);
                            }
                            collidedBlock.tierDown();
                        }
                        //Predkosc pilki rosnie - maksymalnie 25
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
            player.setScore(player.getScore() + 50);
            player.setLives(player.getLives() + 1);
            prizeList.remove(prize);
        }
        for (Prize prize : prizeList) {
            prize.move();
        }

        boolean isLevelFinishedInvalidate = true;
        for (Block block : blockList) {
            if (block != null) {
                isLevelFinishedInvalidate = false;
            }
        }
        if (isLevelFinishedInvalidate) {
            isLevelFinished = true;
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

    private int checkCollisionSide(Player player, Ball ball) {
        //Pomocnicze prostokaty
        Rect rBall = ball.getRect();
        Rect rPlayer = player.getRect();
        int offsetValue = ball.getSpeed();

        //Lece z gory od lewej
        if ((ball.getDirX() == 1) && (ball.getDirY() == 1)) {
            rBall.offset(offsetValue, 0);
            //Odbijam sie od lewej
            if (rBall.intersect(rPlayer)) {
                return LEFT;
            }
            else {
                return TOP;
            }
        }
        //Lece z gory od prawej
        else  {
            rBall.offset(-offsetValue, 0);
            //Odbijam sie od prawej
            if (rBall.intersect(rPlayer)) {
                return RIGHT;
            }
            else {
                return TOP;
            }
        }
    }
}
