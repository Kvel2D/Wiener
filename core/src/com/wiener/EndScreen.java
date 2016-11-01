package com.wiener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.wiener.entity.systems.LimitSystem;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class EndScreen extends ScreenAdapter
{
    float timer = 0f;

    public void render(float deltaTime)
    {
        Gdx.gl.glClearColor(102f / 256f, 102f / 256f, 51f / 256f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Main.engine.update(deltaTime);

        timer += deltaTime;

        if (timer >= 0.5f && Gdx.input.isKeyPressed(Input.Keys.ANY_KEY))
        {
            Main.gameScreen.loadLevel(Main.gameScreen.level);
            Main.engine.getSystem(LimitSystem.class).setProcessing(true);
            Main.game.setScreen(Main.gameScreen);
        }
    }

    public void show()
    {
        Main.engine.getSystem(LimitSystem.class).setProcessing(false);
        timer = 0f;
    }
}
