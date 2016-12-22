package com.winry.mahjong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainMenuScreen implements Screen {
	final Drop game;
	OrthographicCamera camera;
	private StringBuilder username = new StringBuilder();
	private MyClient myClient;

	public MainMenuScreen(final Drop gam) {
		game = gam;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		myClient = new MyClient();

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.font.draw(game.batch, "type your username and press ok to login", 250, 250);
		game.font.draw(game.batch, username.toString(), 250, 220);
		game.batch.end();

		Gdx.input.setInputProcessor(new InputAdapter() {

			@Override
			public boolean keyTyped(char character) {
				if (character == '\b') {
					if (username.length() > 0)
						username.deleteCharAt(username.length() - 1);
				} else {
					username.append(character);
				}
				return true;
			}
		});

		if (Gdx.input.isKeyPressed(Keys.ENTER)) {
			myClient.login(username.toString());
		}

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}