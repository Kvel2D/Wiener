package com.wiener.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class ColorComponent implements Component
{    public Color color;

public  ColorComponent(Color color) {
    this.color = color.cpy();
}
}
