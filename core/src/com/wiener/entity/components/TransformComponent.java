package com.wiener.entity.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class TransformComponent implements Component
{
    public float x;
    public float y;
    public float angle;
    public int z;
    public float scale;
    public boolean bodySync = true;

    public TransformComponent(float x, float y, float angle, int z, float scale) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
        this.scale = scale;
    }

    public TransformComponent(float x, float y, float angle, int z, float scale, boolean bodySync) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
        this.scale = scale;
        this.bodySync = bodySync;
    }
}