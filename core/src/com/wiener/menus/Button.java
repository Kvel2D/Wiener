package com.wiener.menus;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.wiener.Constants;
import org.w3c.dom.css.Rect;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class Button
{
    public String buttonText;
    public Rectangle bounds;
    public int align;

    public Button(String buttonText, float x, float y, GlyphLayout layout, BitmapFont font)
    {
        this.buttonText = buttonText;
        layout.setText(font, buttonText);
        bounds = new Rectangle(x, y, layout.width, layout.height);
        align = 0;
    }

    /*
     * 0 = te|xt
     * -1 = text|
     * 1 = |text
     */
    public Button(String buttonText, float x, float y, int align, GlyphLayout layout, BitmapFont font)
    {
        this.buttonText = buttonText;
        layout.setText(font, buttonText);
        switch (align)
        {
            case 0:
            {
                bounds = new Rectangle(x - layout.width / 2, y, layout.width, layout.height);
            }
            case 1:
            {
                bounds = new Rectangle(x, y, layout.width, layout.height);
            }
            case -1:
            {
                bounds = new Rectangle(x - layout.width, y, layout.width, layout.height);
            }
            default:
            {
                bounds = new Rectangle(x, y, layout.width, layout.height);
            }
        }
        this.align = align;
    }

    public void draw(SpriteBatch batch, Vector3 touch, BitmapFont font)
    {
        if (bounds.contains(touch.x, touch.y))
            font.setColor(Constants.BUTTON_ACTIVE_COLOR);
        else
            font.setColor(Constants.BUTTON_INACTIVE_COLOR);
        font.draw(batch, buttonText, bounds.x, bounds.y + bounds.height);
    }
}