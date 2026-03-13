package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.Random;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    SpriteBatch spriteBatch;
    FitViewport viewport;

    //new sprite variable for head
    Sprite headSprite;

    //load in textures
    private Texture apple;
    private Texture body;
    private Texture head;
    private Texture background;
    public String lastKeyPressed; //chefck last key pressed for rotation

    //multiple apples array
    Array<Sprite> appleSprites;
    Array<Sprite> snakeBody;

    //grid settings
    private static final int tileSize = 8; //8 pixels
    private static final int worldHeight = 17; //17 grid
    private static final int worldWidth = 17;

    //constant movement
    float moveTimer = 0f;
    float moveDelay = 0.2f;
    int dirX = 0;
    int dirY = -1; //starts by moving downwards

    //gameover
    boolean gameOver = false;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();

        //grid units
        viewport = new FitViewport(worldWidth, worldHeight);

        apple = new Texture("apple.png");
        body = new Texture("body.png");
        head = new Texture("head.png");
        background = new Texture("background (1).png");

        appleSprites = new Array<>();
        snakeBody = new Array<>();

        headSprite = new Sprite(head);
        headSprite.setSize(1, 1); //1 grid tile
        headSprite.setPosition(8,8); //center
        headSprite.setOrigin(headSprite.getWidth() / 2, headSprite.getHeight() / 2); //set the origin of the head

        createApple();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
    }

    @Override
    public void render() {
        // organize code into three methods
        input();
        logic();
        draw();
    }

    private void input() {

        if(gameOver) return; //stops if game is over

        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime(); //curret delta for consistenycy

        //when press right key
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && dirX != 1) {
            dirY = 0;
            dirX = 1;
            headSprite.setRotation(90);
            moveTimer = moveDelay; //prevents pause when changing direction
        } //end of RIGHT

        else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && dirX != -1) {
            dirY = 0;
            dirX = -1;
            headSprite.setRotation(-90);
            moveTimer = moveDelay;
        } //end of LEFT

        else if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && dirY != 1) {
            dirY = 1;
            dirX = 0;
            headSprite.setRotation(180);
            moveTimer = moveDelay;
        } //end of UP

        else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && dirY != -1) {
            dirY = -1;
            dirX = 0;
            headSprite.setRotation(0);
            moveTimer = moveDelay;
        } //end of DOWN
    }

    private void logic() {

        if(gameOver) return;

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float headWidth = headSprite.getWidth();
        float headHeight = headSprite.getHeight();

        float delta = Gdx.graphics.getDeltaTime();
        moveTimer += delta;

        if(moveTimer >= moveDelay) {

            //store previous posisionts of the head for the body
            float prevX = headSprite.getX();
            float prevY = headSprite.getY();

            headSprite.translateX(dirX);
            headSprite.translateY(dirY);

            //make body follow the head
            for (int i = 0; i < snakeBody.size; i++) {
                Sprite segement = snakeBody.get(i);
                float tempX = segement.getX();
                float tempY = segement.getY();

                segement.setPosition(prevX, prevY);
                prevX = tempX;
                prevY = tempY;
            }

            for( Sprite segment : snakeBody) {
                if (headSprite.getX() == segment.getX() && headSprite.getY() == segment.getY()) {
                    gameOver = true;
                    break;
                }
            }
            moveTimer = 0f;
        }

        //collision detection with the border
        float x = headSprite.getX();
        float y = headSprite.getY();

        if (x < 0 || x >=worldWidth || y < 0 || y>=worldHeight) {
            gameOver = true;
            System.out.println("Game Over.");
            //add in game over screen
        }

        //if X and Y coords of current apple and head match then remove the apple and create a new one
        for (int i = appleSprites.size - 1; i >= 0; i--) {
            Sprite apple = appleSprites.get(i);

            if(headSprite.getX() == apple.getX() && headSprite.getY() == apple.getY()) {
                appleSprites.removeIndex(i); //remove eaten apple
                addBodySegment(); // grow snake
                createApple(); //create new

                break;
            } // end of if
        } //end of for
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        //draw each body sprite
        for (Sprite segment : snakeBody) {
            segment.draw(spriteBatch);
        }

        headSprite.draw(spriteBatch);

        //draw each apple sprite
        for (Sprite apple : appleSprites) {
            apple.draw(spriteBatch);
        }
        spriteBatch.end();
    }

    private void createApple() {

        //create random
        Random random = new Random();
        int randomY = 0;
        int randomX = 0;
        boolean validPosition = false;

        //before adding to the list make sure the coords are valid
        while (!validPosition) {
            randomX = random.nextInt(worldWidth);
            randomY = random.nextInt(worldHeight);

            validPosition = true;

            //check the head position
            if (randomX == headSprite.getX() && randomY == headSprite.getY()) {
                validPosition = false;
            }

            //check the body of snake positions
            for(Sprite segment : snakeBody) {
                if(segment.getX() == randomX && segment.getY() == randomY) {
                    validPosition = false;
                    break;
                } //end of if
            } //end of for
        } //end of while

        //create apple
        Sprite appleSprite = new Sprite(apple);
        appleSprite.setSize(1, 1);

        appleSprite.setX(randomX);
        appleSprite.setY(randomY);

        appleSprites.add(appleSprite);
    }

    private void addBodySegment() {

        Sprite segment = new Sprite(body);
        segment.setSize(1, 1);

        if (snakeBody.size == 0) {
            segment.setPosition(headSprite.getX(), headSprite.getY());
        }
        else {
            Sprite last = snakeBody.peek();
            segment.setPosition(last.getX(), last.getY());
        }
        snakeBody.add(segment);
    }

    @Override
    public void dispose() {

    }
}
