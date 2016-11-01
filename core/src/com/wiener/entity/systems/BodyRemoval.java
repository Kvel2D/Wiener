package com.wiener.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.wiener.Main;
import com.wiener.entity.Mappers;
import com.wiener.entity.components.BodyComponent;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class BodyRemoval implements EntityListener
{
    public void entityAdded(Entity entity) {}

    public void  entityRemoved(Entity entity) {
    BodyComponent bc = Mappers.body.get(entity);
    Main.world.destroyBody(bc.body);
}
}
