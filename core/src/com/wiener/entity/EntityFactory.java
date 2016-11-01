package com.wiener.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.wiener.AssetPaths;
import com.wiener.Constants;
import com.wiener.Main;
import com.wiener.entity.components.*;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class EntityFactory
{
    public static Entity wiener()
    {
        Entity e = new Entity();

        float[] vertices = EntityFactory.createCirclePolygon(8, new Vector2(0f, 0f), 15f);

        PolygonComponent pc = new PolygonComponent(vertices);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        CircleShape polygon = new CircleShape();
        for (int i = 0; i < vertices.length; i++)
        {
            vertices[i] *= Constants.METER_TO_PIXEL;
        }
        polygon.setRadius(15f * Constants.METER_TO_PIXEL);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;
        fixtureDef.density = 10f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0.01f;
        Body body = Main.world.createBody(bodyDef);
        body.setTransform(0f, 0f, 0f);
        body.createFixture(fixtureDef);
        body.setUserData(e);
        polygon.dispose();

        BodyComponent bc = new BodyComponent(body);
        TransformComponent trc = new TransformComponent(0f, 0f, 0f, 10, 1f);
        ColorComponent cc = new ColorComponent(Color.SALMON);
        MovementComponent mc = new MovementComponent();
        TransformComponent tc = new TransformComponent(0f, 0f, 0f, 0, 1f);
        VerticalLimitComponent vlc = new VerticalLimitComponent();

        e.add(cc)
            .add(mc)
            .add(tc)
            .add(vlc)
            .add(bc)
            .add(trc)
            .add(pc);
        Main.engine.addEntity(e);

        return e;
    }

    public static Entity star(Vector2 position)
    {
        Entity e = new Entity();

        Texture texture = Main.assets.get(AssetPaths.STAR);
        TextureRegion textureRegion = new TextureRegion(texture);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(0.1f, 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;
        fixtureDef.density = 10f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0f;
        Body body = Main.world.createBody(bodyDef);
        body.setTransform(position.x * Constants.METER_TO_PIXEL, position.y * Constants.METER_TO_PIXEL, 0f);
        body.createFixture(fixtureDef);
        body.setUserData(e);
        polygon.dispose();

        TextureComponent tec = new TextureComponent(textureRegion);
        TransformComponent tmc = new TransformComponent(position.x, 360f, 0f, 0, 1f, false);
        BodyComponent bc = new BodyComponent(body);
        ObjectiveComponent oc = new ObjectiveComponent();

        e.add(tec)
            .add(tmc)
            .add(bc)
            .add(oc);
        Main.engine.addEntity(e);

        return e;
    }

    public static Entity camera(Entity target, OrthographicCamera camera, float x, float y) {
        Entity cameraEntity = new Entity();

        camera.position.set(x, y, 0f);
        CameraComponent cameraComponent = new CameraComponent(target, camera);
        cameraEntity.add(cameraComponent);
        Main.engine.addEntity(cameraEntity);

        return cameraEntity;
    }

    public static Entity level(float[] vertices)
    {
        Entity e = new Entity();

        Body body = createChainBody(vertices);
        body.setUserData(e);

        PolygonComponent pc = new PolygonComponent(vertices);
        BodyComponent bc = new BodyComponent(body);
        TransformComponent tc = new TransformComponent(0f, 0f, 0f, -10, 1f);
        ColorComponent cc = new ColorComponent(Main.gameScreen.levelColor);
        LevelComponent lc = new LevelComponent();

        e.add(pc)
            .add(bc)
            .add(tc)
            .add(lc)
            .add(cc);
        Main.engine.addEntity(e);

        return e;
    }

    public static float[] createCirclePolygon(int precision, Vector2 origin, float radius) {
        float angle = (float)(2 * Math.PI / precision);
        float[] vertices = new float[precision*2];
        for (int i = 0; i < precision; i++)
        {
            vertices[i * 2] = (float)(origin.x + radius * Math.cos(angle * i));
            vertices[i * 2 + 1] = (float)(origin.y + radius * Math.sin(angle * i));
        }

        return vertices;
    }

    public static Body createChainBody(float[] vertices)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.linearDamping = 0.5f;
        Body body = Main.world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0.01f;
        fixtureDef.density = 100f;
        ChainShape chain = new ChainShape();
        float[] verticesConverted = new float[vertices.length];
        System.arraycopy(vertices, 0, verticesConverted, 0, vertices.length);
        int i = 0;
        while (i < verticesConverted.length)
        {
            verticesConverted[i] *= Constants.METER_TO_PIXEL;
            verticesConverted[i + 1] *= Constants.METER_TO_PIXEL;
            i += 2;
        }
        chain.createLoop(verticesConverted);
        fixtureDef.shape = chain;
        body.createFixture(fixtureDef);
        chain.dispose();

        return body;
    }
}