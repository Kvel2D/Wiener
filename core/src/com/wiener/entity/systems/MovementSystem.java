package com.wiener.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.wiener.Controls;
import com.wiener.entity.Mappers;
import com.wiener.entity.components.BodyComponent;
import com.wiener.entity.components.MovementComponent;

public class MovementSystem extends IteratingSystem
{
    boolean left = false;
    boolean right = false;

    public MovementSystem()
    {
        super(Family.all(MovementComponent.class, BodyComponent.class).get());
    }

    public MovementSystem(int priority)
    {
        super(Family.all(MovementComponent.class, BodyComponent.class).get(), priority);
    }

    public void update(float deltaTime)
    {
        left = Gdx.input.isKeyPressed(Controls.MOVE_LEFT) || Gdx.input.isKeyPressed(Controls.MOVE_LEFT_2);
        right = Gdx.input.isKeyPressed(Controls.MOVE_RIGHT) || Gdx.input.isKeyPressed(Controls.MOVE_RIGHT_2);

        super.update(deltaTime);
    }

    public void processEntity(Entity entity, float deltaTime)
    {
        BodyComponent bc = Mappers.body.get(entity);
        MovementComponent mc = Mappers.movement.get(entity);
        Body body = bc.body;

        if (left && !right)
        {
            if (mc.appliedForce < mc.maxAppliedForce)
            {
                body.applyForceToCenter(new Vector2(-5f, 0f), true);
                mc.appliedForce += 5f;
            }
            mc.appliedForce -= 5f;
        } else if (right && !left)
        {
            if (mc.appliedForce < mc.maxAppliedForce)
            {
                body.applyForceToCenter(new Vector2(5f, 0f), true);
                mc.appliedForce += 5f;
            }
            mc.appliedForce -= 5f;
        }
    }
}