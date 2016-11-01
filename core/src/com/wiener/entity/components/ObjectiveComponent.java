package com.wiener.entity.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class ObjectiveComponent implements Component
{
    public boolean touched = false;
    public float expandProgress = 0.01f;
    public float expandMax = 80f;
    public float expandLerp = 0.2f;
}
