package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;

public class GameScreen implements Screen {
    private MyGdxGame game;
    private Texture pikaImage;
    private Texture pokeImage;
    private Texture backGround;
    private Music pokeMusic;
    private OrthographicCamera cam;
    private Rectangle pikachu;
    private Array<Rectangle> pokeballs;
    private long lastBallDrop;
    private Viewport port;
    private HUD hud;

    public GameScreen (MyGdxGame game) {
        this.game = game;
        pikaImage = new Texture(Gdx.files.internal("pikachu.png"));
        pokeImage = new Texture(Gdx.files.internal("pokeball.png"));
        backGround = new Texture(Gdx.files.internal("background.png"));
        pokeMusic = Gdx.audio.newMusic(Gdx.files.internal("Verdanturf Town.mp3"));
        pokeMusic.setLooping(true);
        pokeMusic.play();

        hud = new HUD(game.batch);

        cam = new OrthographicCamera();
        port = new FitViewport(800, 480, cam);

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
                hud.incrementMiss();
                if(hud.getMiss() > 2){
                    game.setScreen(new GameOver(game));
                    dispose();
                }
            }
            if(raindrop.overlaps(pikachu)) {
                iter.remove();
                hud.incrementHit();
            }
        }
    }

    public void render (float delta) {
        gameLogic();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(backGround, 0, 0);
        game.batch.draw(pikaImage, pikachu.x, pikachu.y);
        for(Rectangle pokeball: pokeballs){
            game.batch.draw(pokeImage, pokeball.x, pokeball.y);
        }
        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public void resize(int width, int height) {
        port.update(width, height);
    }

    @Override
    public void show(){

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
