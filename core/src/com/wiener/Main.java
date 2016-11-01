package com.wiener;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Main extends ApplicationAdapter {
	public static Game game = new Game() {
		public void create() {
			this.setScreen(new LoadScreen());
		}
	};
	public static AssetManager assets = new AssetManager();
	public static Engine engine = new Engine();
	public static World  world = new World(new Vector2(0f, -10f), false);
	public static SpriteBatch batch;
	public static GameScreen gameScreen;
	public static SelectScreen selectScreen;
	public static EndScreen endScreen;

	public void create() {
		batch = new SpriteBatch();
		Texture.setAssetManager(assets);
		game.create();
	}

	public void render() {
		game.render();
	}

	public void dispose() {
		assets.dispose();
		world.dispose();
		batch.dispose();
	}
}