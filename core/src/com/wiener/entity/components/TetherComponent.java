package com.wiener.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class TetherComponent implements Component
{
    public Entity entity;
    public float xOffset;
    public float yOffset;
    public float angleOffset;
    public boolean rotated;

public TetherComponent(Entity entity, float xOffset, float yOffset, float angleOffset, boolean rotated) {
    this.entity = entity;
    this.xOffset = xOffset;
    this.yOffset = yOffset;
    this.angleOffset = angleOffset;
    this.rotated = rotated;
}
}
