package com.wiener.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.wiener.Constants;
import com.wiener.Extensions;
import com.wiener.entity.Mappers;
import com.wiener.entity.components.ColorComponent;
import com.wiener.entity.components.MovementComponent;
import com.wiener.entity.components.PolygonComponent;
import com.wiener.entity.components.TransformComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TopDownRenderSystem extends EntitySystem
{
    private ImmutableArray<Entity> entities = null;
    private ImmutableArray<Entity> wieners = null;
    private Family family;
    private SegmentComparator comparator = new SegmentComparator();
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    OrthographicCamera camera;
    private List<Segment> segments = new ArrayList<Segment>();
    private List<Segment> wienerSegments = new ArrayList<Segment>();
    boolean colored = true;

    public TopDownRenderSystem(OrthographicCamera camera)
    {
        this.camera = camera;
        this.family = Family.all(PolygonComponent.class, TransformComponent.class).get();
    }

    public TopDownRenderSystem(int priority, OrthographicCamera camera)
    {
        super(priority);
        this.camera = camera;
        this.family = Family.all(PolygonComponent.class, TransformComponent.class).get();
    }

    public void addedToEngine(Engine engine)
    {
        entities = engine.getEntitiesFor(family);
        wieners = engine.getEntitiesFor(Family.all(MovementComponent.class).get());
    }

    public void removedFromEngine(Engine engine)
    {
        entities = null;
        wieners = null;
    }

    public void update(float deltaTime)
    {
        segments.clear();
        wienerSegments.clear();

        for (int i = 0; i < entities.size(); i++)
        {
            extractSegments(entities.get(i));
        }

        Collections.sort(segments, comparator);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        colored = true;
        for (Segment it : segments)
        {
            drawTopDown(it);
        }
        shapeRenderer.end();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i < wieners.size(); i++)
        {
            drawWieners(entities.get(i));
        }
        shapeRenderer.end();
    }

    public void drawWieners(Entity entity)
    {
        PolygonComponent pc = Mappers.polygon.get(entity);
        TransformComponent tc = Mappers.transform.get(entity);

        ColorComponent colorComponent = Mappers.color.get(entity);
        Color color;
        if (colorComponent != null) color = colorComponent.color;
        else color = Constants.DEFAULT_SHAPE_COLOR;

        float[] vertices = new float[pc.polygon.length];
        System.arraycopy(pc.polygon, 0, vertices, 0, vertices.length);
        Extensions.translate(vertices, tc.x, tc.y, tc.angle);
        int j = 0;
        while (j < vertices.length)
        {
            if (vertices[j] < vertices[j + 2]) // normal x1 < x2
                wienerSegments.add(new Segment(new float[]{vertices[j], vertices[j + 1], vertices[j + 2], vertices[j + 3]}, color));
            else
                wienerSegments.add(new Segment(new float[]{vertices[j + 2], vertices[j + 3], vertices[j], vertices[j + 1]}, color)); // reversed

            j += 2;
            if (j > vertices.length - 3) break;
        }

        if (vertices[j] < vertices[0]) // normal x1 < x2
            wienerSegments.add(new Segment(new float[]{vertices[j], vertices[j + 1], vertices[0], vertices[1]}, color));
        else
            wienerSegments.add(new Segment(new float[]{vertices[0], vertices[1], vertices[j], vertices[j + 1]}, color)); // reversed

        for (Segment it : wienerSegments)
        {
            shapeRenderer.setColor(Color.SALMON);
            Vector3 camPosition = camera.position;
            float[] line = it.line;
            Vector2 left = new Vector2(line[0], line[1]);
            Vector2 right = new Vector2(line[2], line[3]);

            if (left.x < camPosition.x)
            {
                left.x = camPosition.x - ((camPosition.x - left.x) * (1f + left.y / 720f * Constants.RENDER_STRETCH));
            } else
            {
                left.x = camPosition.x + ((left.x - camPosition.x) * (1f + left.y / 720f * Constants.RENDER_STRETCH));
            }
            if (right.x < camPosition.x)
            {
                right.x = camPosition.x - ((camPosition.x - right.x) * (1f + right.y / 720f * Constants.RENDER_STRETCH));
            } else
            {
                right.x = camPosition.x + ((right.x - camPosition.x) * (1f + right.y / 720f * Constants.RENDER_STRETCH));
            }

            // upward sloping
            if (left.y < right.y)
            {
                shapeRenderer.rect(left.x, camPosition.y - left.y / 2, right.x - left.x, left.y);
                shapeRenderer.triangle(left.x, camPosition.y + left.y / 2,
                    right.x, camPosition.y + left.y / 2,
                    right.x, camPosition.y + right.y / 2);
                shapeRenderer.triangle(left.x, camPosition.y - left.y / 2,
                    right.x, camPosition.y - left.y / 2,
                    right.x, camPosition.y - right.y / 2);
            } else
            {
                // downward sloping
                shapeRenderer.rect(left.x, camPosition.y - right.y / 2, right.x - left.x, right.y);
                shapeRenderer.triangle(left.x, camPosition.y + right.y / 2,
                    right.x, camPosition.y + right.y / 2,
                    left.x, camPosition.y + left.y / 2);
                shapeRenderer.triangle(left.x, camPosition.y - right.y / 2,
                    right.x, camPosition.y - right.y / 2,
                    left.x, camPosition.y - left.y / 2);
            }
        }
    }

    public void extractSegments(Entity entity)
    {
        PolygonComponent pc = Mappers.polygon.get(entity);
        TransformComponent tc = Mappers.transform.get(entity);

        ColorComponent colorComponent = Mappers.color.get(entity);
        Color color;
        if (colorComponent != null) color = colorComponent.color;
        else color = Constants.DEFAULT_SHAPE_COLOR;

        float[] vertices = new float[pc.polygon.length];
        System.arraycopy(pc.polygon, 0, vertices, 0, vertices.length);
        Extensions.translate(vertices, tc.x, tc.y, tc.angle);
        int j = 0;
        while (j < vertices.length)
        {
            if (vertices[j] < vertices[j + 2]) // normal x1 < x2
                segments.add(new Segment(new float[]{vertices[j], vertices[j + 1], vertices[j + 2], vertices[j + 3]}, color));
            else
                segments.add(new Segment(new float[]{vertices[j + 2], vertices[j + 3], vertices[j], vertices[j + 1]}, color)); // reversed

            j += 2;
            if (j > vertices.length - 3) break;
        }

        if (vertices[j] < vertices[0]) // normal x1 < x2
            segments.add(new Segment(new float[]{vertices[j], vertices[j + 1], vertices[0], vertices[1]}, color));
        else
            segments.add(new Segment(new float[]{vertices[0], vertices[1], vertices[j], vertices[j + 1]}, color)); // reversed
    }

    private void drawTopDown(Segment segment)
    {
        if (colored)
            shapeRenderer.setColor(segment.color);
        else shapeRenderer.setColor(Color.WHITE);
        Vector3 camPosition = camera.position;
        float[] line = segment.line;
        Vector2 left = new Vector2(line[0], line[1]);
        Vector2 right = new Vector2(line[2], line[3]);

        if (left.x < camPosition.x)
        {
            left.x = camPosition.x - ((camPosition.x - left.x) * (1f + left.y / 720f * Constants.RENDER_STRETCH));
        } else
        {
            left.x = camPosition.x + ((left.x - camPosition.x) * (1f + left.y / 720f * Constants.RENDER_STRETCH));
        }
        if (right.x < camPosition.x)
        {
            right.x = camPosition.x - ((camPosition.x - right.x) * (1f + right.y / 720f * Constants.RENDER_STRETCH));
        } else
        {
            right.x = camPosition.x + ((right.x - camPosition.x) * (1f + right.y / 720f * Constants.RENDER_STRETCH));
        }

        // upward sloping
        if (left.y < right.y)
        {
            shapeRenderer.rect(left.x, camPosition.y - left.y / 2, right.x - left.x, left.y);
            shapeRenderer.triangle(left.x, camPosition.y + left.y / 2,
                right.x, camPosition.y + left.y / 2,
                right.x, camPosition.y + right.y / 2);
            shapeRenderer.triangle(left.x, camPosition.y - left.y / 2,
                right.x, camPosition.y - left.y / 2,
                right.x, camPosition.y - right.y / 2);
        } else
        { // downward sloping
            shapeRenderer.rect(left.x, camPosition.y - right.y / 2, right.x - left.x, right.y);
            shapeRenderer.triangle(left.x, camPosition.y + right.y / 2,
                right.x, camPosition.y + right.y / 2,
                left.x, camPosition.y + left.y / 2);
            shapeRenderer.triangle(left.x, camPosition.y - right.y / 2,
                right.x, camPosition.y - right.y / 2,
                left.x, camPosition.y - left.y / 2);
        }
    }

    private static class SegmentComparator implements Comparator<Segment>
    {
        public int compare(Segment a1, Segment a2)
        {
            float[] line1 = a1.line;
            float[] line2 = a2.line;
            float minY1 = Math.min(line1[1], line1[3]);
            float minY2 = Math.min(line2[1], line2[3]);

            if (minY1 > minY2) return 1;
            else if (minY1 == minY2) return 0;
            else return -1;
        }
    }
}
