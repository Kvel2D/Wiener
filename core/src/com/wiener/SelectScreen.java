package com.wiener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.wiener.menus.LevelButton;

public class SelectScreen  extends ScreenAdapter
{
    SpriteBatch batch = Main.batch;
    BitmapFont fontBig = Main.assets.get(AssetPaths.NOTO_BIG);
    BitmapFont fontBigger = Main.assets.get(AssetPaths.NOTO_BIGGER);
    GlyphLayout layout = new GlyphLayout();
    OrthographicCamera camera;
    LevelButton level0 = new LevelButton(0, 50f, 50f, 0, layout, fontBig);
    LevelButton level1 = new LevelButton(1, 100f, 110f, 0, layout, fontBig);
    LevelButton level2 = new LevelButton(2, 300f, 250f, 0, layout, fontBig);
    LevelButton level3 = new LevelButton(3, 400f, 200f, 0, layout, fontBig);
    LevelButton level4 = new LevelButton(4, 330f, 450f, 0, layout, fontBig);
    LevelButton level5 = new LevelButton(5, 350f, 600f, 0, layout, fontBig);
    LevelButton level6 = new LevelButton(6, 650f, 600f, 0, layout, fontBig);
    LevelButton level7 = new LevelButton(7, 850f, 600f, 0, layout, fontBig);
    LevelButton level8 = new LevelButton(8, 850f, 100f, 0, layout, fontBig);
    LevelButton level9 = new LevelButton(9, 150f, 650f, 0, layout, fontBig);
    LevelButton level10 = new LevelButton(10, 850f, 650f, 0, layout, fontBig);
    LevelButton level11 = new LevelButton(11, 850f, 200f, 0, layout, fontBig);
    LevelButton level12 = new LevelButton(12, 1100f, 150f, 0, layout, fontBig);
    LevelButton level13 = new LevelButton(13, 1000f, 150f, 0, layout, fontBig);
    LevelButton[] buttons = new LevelButton[]{level0, level1, level2, level3, level4, level5, level6, level7, level8, level9, level10, level11, level12, level13};

    public SelectScreen()
    {
        float camWidth = Gdx.graphics.getWidth();
        float camHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(camWidth, camHeight);
        camera.position.set(camWidth / 2, camHeight / 2, 0f);
        camera.update();
    }

    public void render(float deltaTime)
    {
        draw();
        update();
    }

    public void draw()
    {
        Gdx.gl.glClearColor(200f / 255f, 237f / 255f, 197f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
        camera.unproject(touch);

        for (LevelButton b : buttons)
        {
            b.draw(batch, touch, fontBig, fontBigger);
        }

        batch.end();
    }

    public void update()
    {
        if (Gdx.input.isTouched())
        {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
            camera.unproject(touch);

            for (LevelButton b : buttons) {
            if (b.bounds.contains(touch.x, touch.y))
            {
                Main.gameScreen.loadLevel(b.levelNumber);
                Main.game.setScreen(Main.gameScreen);
            }
        }
        }
    }
}
