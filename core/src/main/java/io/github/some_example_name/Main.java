package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private Texture apple;
    private Texture body;
    private Texture head;

    @Override
    public void create() {
        apple = new Texture("apple.png");
        body = new Texture("body.png");
        head = new Texture("head.png");
    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {

    }
}
