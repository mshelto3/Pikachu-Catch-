package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;

public class MyGdxGame extends ApplicationAdapter {
	private Texture pikaImage;
	private Texture pokeImage;
	private Texture backGround;
	private Music pokeMusic;
	private SpriteBatch batch;
	private OrthographicCamera cam;
	private Rectangle pikachu;
	private Array<Rectangle> pokeballs;
	private long lastBallDrop;
	private int pokeHit;
	private int pokeMiss;
	private String Hit;
	private String Miss;
	private BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		pikaImage = new Texture(Gdx.files.internal("pikachu.png"));
		pokeImage = new Texture(Gdx.files.internal("pokeball.png"));
		backGround = new Texture(Gdx.files.internal("background.png"));
		pokeMusic = Gdx.audio.newMusic(Gdx.files.internal("Verdanturf Town.mp3"));
		pokeMusic.setLooping(true);
		pokeMusic.play();

		pokeHit = 0;
		pokeMiss = 0;
		Hit = "Hit: 0";
		Miss = "Miss: 0";

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Pokemon Hollow.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 10;
		font = generator.generateFont(parameter);

		cam = new OrthographicCamera();
		cam.setToOrtho(false, 800, 480);

		pikachu = new Rectangle();
		pikachu.x = 800 / 2 - 64 / 2; // center the bucket horizontally
		pikachu.y = 20; // bottom left corner of the bucket is 20 pixels above the bottom screen edge
		pikachu.width = 64;
		pikachu.height = 64;

		pokeballs = new Array<Rectangle>();
		spawnPokeball();
	}

	private void spawnPokeball(){
		Rectangle pokeball = new Rectangle();
		pokeball.x = MathUtils.random(0, 800-64);
		pokeball.y = 480;
		pokeball.width = 64;
		pokeball.height = 64;
		pokeballs.add(pokeball);
		lastBallDrop = TimeUtils.nanoTime();
	}

	public void gameLogic(){
		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			cam.unproject(touchPos);
			pikachu.x = touchPos.x - 64 / 2;
			if(pikachu.x < 0) pikachu.x = 0;
			else if(pikachu.x > 800 -64) pikachu.x = 800 - 64;
		}
		if(TimeUtils.nanoTime() - lastBallDrop > 1000000000) spawnPokeball();
		for (Iterator<Rectangle> iter = pokeballs.iterator(); iter.hasNext(); ) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 100 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0){
				iter.remove();
				pokeMiss++;
			}
			if(raindrop.overlaps(pikachu)) {
				iter.remove();
				pokeHit++;
			}
		}
	}

	@Override
	public void render () {
		gameLogic();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		batch.draw(backGround, 0, 0);
		batch.draw(pikaImage, pikachu.x, pikachu.y);
		for(Rectangle pokeball: pokeballs){
			batch.draw(pokeImage, pokeball.x, pokeball.y);
		}
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font.draw(batch, Hit, 0, 0);
		batch.end();

	}
	
	@Override
	public void dispose () {
		pokeImage.dispose();
		pokeMusic.dispose();
		pikaImage.dispose();
		batch.dispose();
	}
}
