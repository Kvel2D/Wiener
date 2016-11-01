package com.wiener;

import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class Constants
{
    public static int VIEWPORT_WIDTH = 16;
    public static int VIEWPORT_HEIGHT = 9;
    public static float METER_TO_PIXEL = 1 / 80f;
    public static float DEGTORAD = 0.0175f;
    public static float TIME_STEP = 1 / 60f;
    public static int VELOCITY_ITERATIONS = 6;
    public static int POSITION_ITERATIONS = 2;
    public static float CAMERA_LERP = 0.1f;
    public static float RENDER_STRETCH = 2f;

    public static Color DEFAULT_SHAPE_COLOR = new Color(0.5f, 0.5f, 0.5f, 1f);
    public static Color BUTTON_INACTIVE_COLOR = new Color(1f, 1f, 238f / 256f, 1f);
    public static Color BUTTON_ACTIVE_COLOR = new Color(205f / 255f, 255f / 255f, 159f / 255f, 1f);

    public static Color TUTORIAL_LEVEL_COLOR = new Color(205f / 255f, 255f / 255f, 159f / 255f, 1f);
    public static Color TUTORIAL_BACK_COLOR = new Color(223f / 255f, 224f / 255f, 226f / 255f, 1f);
    public static Color RAMP_LEVEL_COLOR = new Color(1f, 25f / 255f, 25f / 255f, 1f);
    public static Color RAMP_BACK_COLOR = new Color(214f / 255f, 213f / 255f, 201f / 255f, 1f);
    public static Color LOOP_LEVEL_COLOR = new Color(102f / 256f, 153f / 256f, 153f / 256f, 1f);
    public static Color LOOP_BACK_COLOR = new Color(205f / 255f, 223f / 255f, 160f / 255f, 1f);
    public static Color MIX_LEVEL_COLOR = new Color(162f / 256f, 160f / 256f, 121f / 256f, 1f);
    public static Color MIX_BACK_COLOR = new Color(255f / 255f, 245f / 255f, 224f / 255f, 1f);
    public static Color END_LEVEL_COLOR = new Color(1f, 1f, 1f, 1f);
    public static Color END_BACK_COLOR = new Color(0f, 0f, 0f, 1f);

    public static HashMap<Integer, Boolean> levelsCompleted = new HashMap<Integer, Boolean>(){{
            put(0, false);
            put(1,false);
            put(2,false);
            put(3,false);
            put(4,false);
            put(5,false);
            put(6,false);
            put(7,false);
            put(8,false);
            put(9,false);
            put(10,false);
            put(11,false);
            put(12,false);
            put(13,false);
            put(14,false);
            put(15,false);
    }};
}
