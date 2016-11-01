package com.wiener.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.wiener.Constants;
import com.wiener.Main;
import com.wiener.entity.Mappers;
import com.wiener.entity.components.BodyComponent;
import com.wiener.entity.components.TransformComponent;

public class PhysicsSystem extends IteratingSystem
{
    World world = Main.world;

    public PhysicsSystem()
    {
        super(Family.all(BodyComponent.class, TransformComponent.class).get());
    }

    public PhysicsSystem(int priority)
    {
        super(Family.all(BodyComponent.class, TransformComponent.class).get(), priority);
    }

    public void update(float deltaTime)
    {
        world.step(Constants.TIME_STEP,
            Constants.VELOCITY_ITERATIONS,
            Constants.POSITION_ITERATIONS);

        super.update(deltaTime);
    }

    public void processEntity(Entity entity, float deltaTime)
    {
        TransformComponent tc = Mappers.transform.get(entity);
        if (!tc.bodySync)// don't process transforms that ask not to be synced
            return;

        BodyComponent bc = Mappers.body.get(entity);
        Body body = bc.body;

        Vector2 position = body.getPosition();
        float angle = body.getAngle() / Constants.DEGTORAD;
        tc.x = position.x / Constants.METER_TO_PIXEL;
        tc.y = position.y / Constants.METER_TO_PIXEL;
        tc.angle = angle;
    }
}