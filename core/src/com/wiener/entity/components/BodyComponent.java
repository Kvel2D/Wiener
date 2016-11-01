package com.wiener.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class BodyComponent implements Component
{
    public Body body;

    public BodyComponent(Body body)
    {
        this.body = body;
    }
}
