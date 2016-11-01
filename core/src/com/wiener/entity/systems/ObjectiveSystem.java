package com.wiener.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.wiener.Constants;
import com.wiener.Main;
import com.wiener.entity.Mappers;
import com.wiener.entity.components.ObjectiveComponent;
import com.wiener.entity.components.TransformComponent;

public class ObjectiveSystem extends IteratingSystem
{

    public ObjectiveSystem()
    {
        super(Family.all(ObjectiveComponent.class, TransformComponent.class).get());
    }

    public ObjectiveSystem(int priority)
    {
        super(Family.all(ObjectiveComponent.class, TransformComponent.class).get(), priority);
    }

    public void processEntity(Entity entity, float deltaTime)
    {
        ObjectiveComponent oc = Mappers.objective.get(entity);
        TransformComponent tc = Mappers.transform.get(entity);

        if (!oc.touched)
        {
            return;
        }


        if (oc.expandProgress < oc.expandMax)
        {
            oc.expandProgress += oc.expandProgress * oc.expandLerp;
            tc.scale = 1f + oc.expandProgress;
        } else
        {
            Constants.levelsCompleted.put(Main.gameScreen.level, true);
            oc.touched = false;
            oc.expandProgress = 0.01f;
            tc.scale = 1f;
            Main.gameScreen.loadLevel(Main.gameScreen.level + 1);
            Main.engine.getSystem(MovementSystem.class).setProcessing(true);
            Main.engine.getSystem(CameraSystem.class).setProcessing(true);
            Main.engine.getSystem(PhysicsSystem.class).setProcessing(true);
        }
    }
}