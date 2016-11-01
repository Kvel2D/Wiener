package com.wiener.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.wiener.Main;
import com.wiener.entity.Mappers;

public class MyContactListener implements ContactListener
{
    public void beginContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Entity entityA = (Entity) fixtureA.getBody().getUserData();
        Entity entityB = (Entity) fixtureB.getBody().getUserData();

        if (Mappers.objective.has(entityA) || Mappers.objective.has(entityB))
        {
            if (Mappers.movement.has(entityA))
            {
                Mappers.objective.get(entityB).touched = true;
                Main.engine.getSystem(MovementSystem.class).setProcessing(false);
                Main.engine.getSystem(CameraSystem.class).setProcessing(false);
                Main.engine.getSystem(PhysicsSystem.class).setProcessing(false);
            } else
            {
                Mappers.objective.get(entityA).touched = true;
                Main.engine.getSystem(MovementSystem.class).setProcessing(false);
                Main.engine.getSystem(CameraSystem.class).setProcessing(false);
                Main.engine.getSystem(PhysicsSystem.class).setProcessing(false);
            }
        }
    }

    public void endContact(Contact contact)
    {
    }

    public void preSolve(Contact contact, Manifold manifold)
    {
    }

    public void postSolve(Contact contact, ContactImpulse impulse)
    {
    }
}
