package com.wiener.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class CameraComponent implements Component
{
    public Entity target;
    public OrthographicCamera camera;
    public float actualX;
    public float actualY;
    public float xOffset = 0f;
    public float yOffset = 0f;

    public  CameraComponent(Entity target, OrthographicCamera camera) {
        this.target = target;
        this.camera = camera;
        this.actualX = camera.position.x;
        this.actualY = camera.position.y;
    }
}
