package com.wiener.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.wiener.Constants;
import com.wiener.EndScreen;
import com.wiener.Main;
import com.wiener.entity.Mappers;
import com.wiener.entity.components.BodyComponent;
import com.wiener.entity.components.VerticalLimitComponent;

public class LimitSystem extends IteratingSystem
{

    public LimitSystem()
    {
        super(Family.all(VerticalLimitComponent.class, BodyComponent.class).get());
    }

    public LimitSystem(int priority)
    {
        super(Family.all(VerticalLimitComponent.class, BodyComponent.class).get(), priority);
    }

    public void processEntity(Entity entity, float deltaTime)
    {
        VerticalLimitComponent vlc = Mappers.verticalLimit.get(entity);
        BodyComponent bc = Mappers.body.get(entity);

        if (bc.body.getPosition().y / Constants.METER_TO_PIXEL < vlc.y)
        {
            Main.game.setScreen(Main.endScreen);
        }
    }
}