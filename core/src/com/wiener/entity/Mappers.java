package com.wiener.entity;

import com.badlogic.ashley.core.ComponentMapper;
import com.wiener.entity.components.*;

/**
 * Created by PoopfEAST_420 on 3/11/2016.
 */
public class Mappers
{
    public static ComponentMapper<BodyComponent> body = ComponentMapper.getFor(BodyComponent.class);
    public static ComponentMapper<CameraComponent> camera = ComponentMapper.getFor(CameraComponent.class);
    public static ComponentMapper<ColorComponent> color = ComponentMapper.getFor(ColorComponent.class);
    public static ComponentMapper<LevelComponent> level = ComponentMapper.getFor(LevelComponent.class);
    public static ComponentMapper<MovementComponent> movement = ComponentMapper.getFor(MovementComponent.class);
    public static ComponentMapper<ObjectiveComponent> objective = ComponentMapper.getFor(ObjectiveComponent.class);
    public static ComponentMapper<PolygonComponent> polygon = ComponentMapper.getFor(PolygonComponent.class);
    public static ComponentMapper<TetherComponent> tether = ComponentMapper.getFor(TetherComponent.class);
    public static ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);
    public static ComponentMapper<TransformComponent> transform = ComponentMapper.getFor(TransformComponent.class);
    public static ComponentMapper<VerticalLimitComponent> verticalLimit = ComponentMapper.getFor(VerticalLimitComponent.class);
}
