package com.wiener.menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.wiener.Constants;

public class LevelButton extends Button
{
    public int levelNumber = 0;

    public LevelButton(int levelNumber, float x, float y, int align, GlyphLayout layout, BitmapFont font)
    {
        super(Integer.toString(levelNumber), x, y, align, layout, font);
        this.levelNumber = levelNumber;
    }

    public void draw(SpriteBatch batch, Vector3 touch, BitmapFont fontInactive, BitmapFont fontActive)
    {
        if (bounds.contains(touch.x, touch.y))
        {
            if (0 <= levelNumber && levelNumber <= 2)
            {
                fontActive.setColor(Constants.TUTORIAL_LEVEL_COLOR);
            } else if (3 <= levelNumber && levelNumber <= 6)
            {
                fontActive.setColor(Constants.RAMP_LEVEL_COLOR);
            } else if (7 <= levelNumber && levelNumber <= 8)
            {
                fontActive.setColor(Constants.LOOP_LEVEL_COLOR);
            } else if (9 <= levelNumber && levelNumber <= 10)
            {
                fontActive.setColor(Constants.MIX_LEVEL_COLOR);
            } else if (11 <= levelNumber && levelNumber <= 13)
            {
                fontActive.setColor(Constants.END_LEVEL_COLOR);
            }
            fontActive.draw(batch, buttonText, bounds.x, bounds.y + bounds.height);
        } else
        {
            if (Constants.levelsCompleted.get(levelNumber))
                fontInactive.setColor(Color.YELLOW);
            else fontInactive.setColor(Constants.BUTTON_INACTIVE_COLOR);

            fontInactive.draw(batch, buttonText, bounds.x, bounds.y + bounds.height);
        }
    }
}