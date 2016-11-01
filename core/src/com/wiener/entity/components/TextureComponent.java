package com.wiener.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class TextureComponent implements Component
{
    public TextureRegion region;

public  TextureComponent(TextureRegion region) {
    this.region = region;
}
}