package com.wiener.entity.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class MovementComponent implements Component
{
    public float appliedForce = 0f;
    public float maxAppliedForce = 100f;
}