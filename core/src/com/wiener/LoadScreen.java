package com.wiener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class LoadScreen extends ScreenAdapter
{
    SpriteBatch batch = Main.batch;
    GlyphLayout layout = new GlyphLayout();
    BitmapFont font;
    float minimumShowTime = 120f;

    public LoadScreen()
    {
        for(String s : AssetPaths.textures)
        {
            Main.assets.load(s, Texture.class);
        }
        for(String s : AssetPaths.fonts)
        {
            Main.assets.load(s, BitmapFont.class);
        }
        Main.assets.finishLoading();

        font = Main.assets.get(AssetPaths.NOTO_BIG);

        Main.gameScreen = new GameScreen();
        Main.selectScreen = new SelectScreen();
        Main.endScreen = new EndScreen();
    }

    public void render(float deltaTime)
    {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.begin();
        layout.setText(font, "CLICK");
        font.draw(batch, "CLICK", 640f - layout.width / 2, 360f + layout.height);
        batch.end();

        minimumShowTime -= deltaTime;
        if (Gdx.input.justTouched())
            minimumShowTime = 0f;
        if (minimumShowTime <= 0 && Main.assets.update())
            Main.game.setScreen(Main.selectScreen);
    }
}
