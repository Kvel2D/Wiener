package com.wiener.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.wiener.Extensions;
import com.wiener.entity.Mappers;
import com.wiener.entity.components.TetherComponent;
import com.wiener.entity.components.TransformComponent;

public class TetherSystem extends IteratingSystem
{
    public TetherSystem()
    {
        super(Family.all(TetherComponent.class, TransformComponent.class).get());
    }

    public TetherSystem(int priority)
    {
        super(Family.all(TetherComponent.class, TransformComponent.class).get(), priority);
    }

    public void processEntity(Entity entity, float deltaTime)
    {
        TetherComponent tether = Mappers.tether.get(entity);

        TransformComponent tc = Mappers.transform.get(entity);
        TransformComponent tetherTransform = Mappers.transform.get(tether.entity);

        if (tether.rotated)
        {
            Vector2 position = new Vector2(tetherTransform.x + tether.xOffset,
                tetherTransform.y + tether.yOffset);
            Extensions.rotateAround(position, tetherTransform.x, tetherTransform.y, tetherTransform.angle);
            tc.x = position.x;
            tc.y = position.y;
            tc.angle = tetherTransform.angle + tether.angleOffset;
        } else
        {
            tc.x = tetherTransform.x + tether.xOffset;
            tc.y = tetherTransform.y + tether.yOffset;
            tc.angle = tetherTransform.angle + tether.angleOffset;
        }
    }
}