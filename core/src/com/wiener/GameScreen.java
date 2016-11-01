package com.wiener;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.wiener.entity.EntityFactory;
import com.wiener.entity.Mappers;
import com.wiener.entity.components.BodyComponent;
import com.wiener.entity.components.LevelComponent;
import com.wiener.entity.systems.*;

import java.util.ArrayList;

public class GameScreen extends ScreenAdapter
{
    public OrthographicCamera gameCamera;
    public OrthographicCamera hudCamera;
    Engine engine = Main.engine;
    public Entity wiener;
    public Entity star;
    public int level = 0;
    public Color levelColor = Color.LIME;
    Color backColor = Color.WHITE;

    public GameScreen()
    {
        float camWidth = Gdx.graphics.getWidth();
        float camHeight = Gdx.graphics.getHeight();
        gameCamera = new OrthographicCamera(camWidth, camHeight);
        gameCamera.update();
        gameCamera.zoom *= 2f;

        hudCamera = new OrthographicCamera(camWidth, camHeight);
        hudCamera.position.set(camWidth / 2, camHeight / 2, 0f);
        hudCamera.update();

        engine.addSystem(new MovementSystem(0));
        engine.addSystem(new PhysicsSystem(10));
        engine.addSystem(new TetherSystem(20));
        engine.addSystem(new CameraSystem(30));
        engine.addSystem(new TopDownRenderSystem(50, gameCamera));
        engine.addSystem(new RenderSystem(60, gameCamera));
        engine.addSystem(new ObjectiveSystem());
        engine.addSystem(new LimitSystem());

        engine.addEntityListener(Family.all(BodyComponent.class).get(), new BodyRemoval());
        Main.world.setContactListener(new MyContactListener());

        wiener = EntityFactory.wiener();
        star = EntityFactory.star(new Vector2(1000f, 360f));
        EntityFactory.camera(wiener, gameCamera, 0f, 360f);
    }

    public void render(float deltaTime)
    {
        Gdx.gl.glClearColor(backColor.r, backColor.g, backColor.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        engine.update(deltaTime);

        if (Gdx.input.isKeyPressed(Controls.MENU))
        {
            Main.game.setScreen(Main.selectScreen);
        }
        if (Gdx.input.isKeyPressed(Controls.RESTART))
        {
            loadLevel(level);
        }
    }

    public void loadLevel(int level)
    {
        this.level = level;
        if (level > 13) this.level = 0;

        if (0 <= this.level && this.level <= 2)
        {
            levelColor = Constants.TUTORIAL_LEVEL_COLOR;
            backColor = Constants.TUTORIAL_BACK_COLOR;
        } else if (3 <= this.level && this.level <= 6)
        {
            levelColor = Constants.RAMP_LEVEL_COLOR;
            backColor = Constants.RAMP_BACK_COLOR;
        } else if (7 <= this.level && this.level <= 8)
        {
            levelColor = Constants.LOOP_LEVEL_COLOR;
            backColor = Constants.LOOP_BACK_COLOR;
        } else if (9 <= this.level && this.level <= 10)
        {
            levelColor = Constants.MIX_LEVEL_COLOR;
            backColor = Constants.MIX_BACK_COLOR;
        } else if (11 <= this.level && this.level <= 13)
        {
            levelColor = Constants.END_LEVEL_COLOR;
            backColor = Constants.END_BACK_COLOR;
        }

        // Clear current level entities
        ImmutableArray<Entity> levelEntites = Main.engine.getEntitiesFor(Family.all(LevelComponent.class).get());
        int i = levelEntites.size() - 1;
        while (i >= 0)
        {
            Main.engine.removeEntity(levelEntites.get(i));
            i--;
        }
//        // Load static level entities
//        val files = Gdx.files.local(AssetPaths.LEVELS_DIR + this.level + "/static").list();
//        files.forEach {
//            val vertices = it.toVertices()
//            EntityFactory.level(vertices)
//        }
        FileHandle pathFile = Gdx.files.internal(AssetPaths.LEVELS_DIR + this.level + "/paths.txt");
        String[] paths = Extensions.toStringArray(pathFile);
        for(String it : paths)
        {
            FileHandle file = Gdx.files.internal(it);
            float[] vertices = Extensions.toVertices(file);
            EntityFactory.level(vertices);
        }
        // Load player position
        FileHandle playerFile = Gdx.files.internal(AssetPaths.LEVELS_DIR + this.level + "/player.txt");
        float[] playerPosition = Extensions.toVertices(playerFile);
        BodyComponent bcPlayer = Mappers.body.get(wiener);
        bcPlayer.body.setTransform(playerPosition[0] * Constants.METER_TO_PIXEL, playerPosition[1] * Constants.METER_TO_PIXEL, 0f);
        bcPlayer.body.setAngularVelocity(0f);
        bcPlayer.body.setLinearVelocity(0f, 0f);
        // Load star position
        FileHandle starFile = Gdx.files.internal(AssetPaths.LEVELS_DIR + this.level + "/star.txt");
        float[] starPosition = Extensions.toVertices(starFile);
        BodyComponent bcStar = Mappers.body.get(star);
        bcStar.body.setTransform(starPosition[0] * Constants.METER_TO_PIXEL, starPosition[1] * Constants.METER_TO_PIXEL, 0f);
        Mappers.transform.get(star).x = starPosition[0];
    }

    public void show()
    {
        Main.engine.getSystem(MovementSystem.class).setProcessing(true);
        Main.engine.getSystem(CameraSystem.class).setProcessing(true);
        Main.engine.getSystem(PhysicsSystem.class).setProcessing(true);
    }
}
