package com.wiener.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.wiener.Constants;
import com.wiener.Main;
import com.wiener.entity.Mappers;
import com.wiener.entity.components.ColorComponent;
import com.wiener.entity.components.TextureComponent;
import com.wiener.entity.components.TransformComponent;

import java.util.*;

public class RenderSystem extends SortedIteratingSystem
{
    SpriteBatch batch = Main.batch;
    OrthographicCamera camera;
    Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

    public RenderSystem(OrthographicCamera camera)
    {
        super(Family.all(TextureComponent.class, TransformComponent.class).get(), new ZComparator());
        this.camera = camera;
    }

    public RenderSystem(int priority, OrthographicCamera camera)
    {
        super(Family.all(TextureComponent.class, TransformComponent.class).get(), new ZComparator(), priority);
        this.camera = camera;
    }

    public void update(float deltaTime)
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        super.update(deltaTime);
        batch.end();

//        BOX2D DEBUG RENDERING
//        var box2dmatrix = camera.combined.cpy();
//        box2dmatrix.scl(1 / Constants.METER_TO_PIXEL);
//        debugRenderer.render(Main.world, box2dmatrix);
    }

    public void processEntity(Entity entity, float deltaTime)
    {
        TransformComponent transform = Mappers.transform.get(entity);
        TextureComponent textureComponent = Mappers.texture.get(entity);

        if (!isInFrustum(transform, textureComponent)) return;

        if (Mappers.color.has(entity))
        {
            ColorComponent colorComponent = Mappers.color.get(entity);
            batch.setColor(colorComponent.color);
        } else
            batch.setColor(Color.WHITE);

        float scale = transform.scale;
        float width = textureComponent.region.getRegionWidth();
        float height = textureComponent.region.getRegionHeight();
        float originX = 0.5f * width;
        float originY = 0.5f * height;
        float x = camera.position.x - ((camera.position.x - (transform.x - originX)) * (1f + transform.y / 720f));


        batch.draw(textureComponent.region,
            x,
            transform.y - originY,
            originX,
            originY,
            width,
            height,
            scale,
            scale,
            transform.angle);
    }

    private boolean isInFrustum(TransformComponent transform, TextureComponent textureComponent)
    {
        float width = textureComponent.region.getRegionWidth();
        float height = textureComponent.region.getRegionHeight();
        float originX = width * 0.5f;
        float originY = height * 0.5f;
        float scale = transform.scale;
        float halfWidth = camera.viewportWidth * camera.zoom * 0.5f;
        float halfHeight = camera.viewportHeight * camera.zoom * 0.5f;

        if (transform.x + width * scale - originX < camera.position.x - halfWidth
            || transform.x - originX > camera.position.x + halfWidth)
            return false;
        if (transform.y + height * scale - originY < camera.position.y - halfHeight
            || transform.y - originY > camera.position.y + halfHeight)
            return false;

        return true;
    }

    private static class ZComparator implements Comparator<Entity>
    {
        public int compare(Entity e1, Entity e2)
        {
            return Integer.signum(Mappers.transform.get(e1).z - Mappers.transform.get(e2).z);
        }
    }
}