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
            headSprite.translateX(dirX);
            headSprite.translateY(dirY);
            moveTimer = 0f;
        }

        //collision detection with the border
        float x = headSprite.getX();
        float y = headSprite.getY();

        if (x < 0 || x >=worldWidth || y < 0 || y>=worldHeight) {
            gameOver = true;
            System.out.println("Game Over.");
            //add in game over screen


        createApple();

        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

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
        int randomInt = random.nextInt(worldHeight); //height and widtrh are the same
        float appleHeight = 1;
        float appleWidth = 1;

        //create apple
        Sprite appleSprite = new Sprite(apple);
        appleSprite.setSize(1, 1);

        appleSprite.setX(randomInt);
        appleSprite.setY(randomInt);

        //if apple is not in the coordinates of head then add to array
        if (appleSprite.getX() != headSprite.getX() && appleSprite.getY() != headSprite.getY()) {
            appleSprites.add(appleSprite);
        }
    }

    @Override
    public void dispose() {

    }
}
