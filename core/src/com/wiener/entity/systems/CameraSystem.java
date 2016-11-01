package com.wiener.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.wiener.Constants;
import com.wiener.entity.Mappers;
import com.wiener.entity.components.CameraComponent;
import com.wiener.entity.components.TransformComponent;

public class CameraSystem extends IteratingSystem
{
    public CameraSystem()
    {
        super(Family.all(CameraComponent.class).get());
    }

    public CameraSystem(int priority)
    {
        super(Family.all(CameraComponent.class).get(), priority);
    }

    public void processEntity(Entity entity, float deltaTime)
    {
        CameraComponent cc = Mappers.camera.get(entity);
        OrthographicCamera camera = cc.camera;
        TransformComponent targetTransform = Mappers.transform.get(cc.target);

        // Move camera's position to the player
        cc.actualX += (targetTransform.x - camera.position.x) * Constants.CAMERA_LERP;

        // Add offsets to the camera
        camera.position.x = cc.actualX + cc.xOffset;

        camera.update();
    }
}
