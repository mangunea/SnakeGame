package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    SpriteBatch spriteBatch;
    FitViewport viewport;

    //new sprite variable for head
    Sprite headSprite;

    private Texture apple;
    private Texture body;
    private Texture head;
    private Texture background;

    //grid settings
    private static final int tileSize = 8; //8 pixels
    private static final int worldHeight = 17; //17 grid
    private static final int worldWidth = 17;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        //grid units
        viewport = new FitViewport(worldWidth, worldHeight);

        apple = new Texture("apple.png");
        body = new Texture("body.png");
        head = new Texture("head.png");
        background = new Texture("background (1).png");

        headSprite = new Sprite(head);
        headSprite.setSize(1, 1); //1 grid tile
        headSprite.setPosition(8,8); //center
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
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime(); //curret delta for consistenycy

        //when press right key
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            headSprite.translateX(1);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            headSprite.translateX(-1);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            headSprite.translateY(1);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            headSprite.translateY(-1);
        }


    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float headWidth = headSprite.getWidth();
        float headHeight = headSprite.getHeight();

        //so head does not go past world border
        headSprite.setX(MathUtils.clamp(headSprite.getX(), 0, worldWidth - headWidth));
        headSprite.setY(MathUtils.clamp(headSprite.getY(), 0, worldHeight - headHeight));
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        headSprite.draw(spriteBatch);

        spriteBatch.end();
    }

    @Override
    public void dispose() {

    }
}
