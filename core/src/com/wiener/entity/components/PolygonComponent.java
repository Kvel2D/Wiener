package com.wiener.entity.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class PolygonComponent implements Component
{
    public float[] polygon;
    public float lowY;
    public float highY;
    public float leftX;
    public float rightX;

    public PolygonComponent(float[] polygon)
    {
        this.polygon = new float[polygon.length];
        System.arraycopy(polygon, 0, this.polygon, 0, polygon.length);
        float lowY = polygon[1];
        float highY = polygon[1];
        float leftX = polygon[0];
        float rightX = polygon[0];
        int i = 0;
        while (i < polygon.length)
        {
            if (polygon[i] < leftX) leftX = polygon[i];
            if (polygon[i] > rightX) rightX = polygon[i];
            if (polygon[i + 1] < lowY) lowY = polygon[i + 1];
            if (polygon[i + 1] > highY) highY = polygon[i + 1];
            i += 2;
        }
        this.lowY = lowY;
        this.highY = highY;
        this.leftX = leftX;
        this.rightX = rightX;
    }
}
