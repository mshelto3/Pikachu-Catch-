package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

public class HUD implements Disposable {
    public Stage stage;

    private Integer timeCount;

    private Viewport port;

    private int pokeHit;
    private int pokeMiss;

    Label hitLabel;
    Label missLabel;

    public HUD(SpriteBatch sb){

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Pokemon Solid.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        timeCount = 0;

        port = new FitViewport(800, 480, new OrthographicCamera());
        stage = new Stage(port, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        hitLabel = new Label(String.format("Hits: %d", pokeHit), new Label.LabelStyle(font, Color.WHITE));
        missLabel = new Label(String.format("Miss: %d", pokeMiss), new Label.LabelStyle(font, Color.WHITE));

        table.add(hitLabel).expandX().top().left();
        table.row();
        table.add(missLabel).expandX().top().left();

        stage.addActor(table);
    }

    public void incrementHit(){
        pokeHit++;
        hitLabel.setText(String.format("Hits: %d", pokeHit));
    }

    public void incrementMiss(){
        pokeMiss++;
        missLabel.setText(String.format("Miss: %d", pokeMiss));
    }

    public int getMiss(){
        return pokeMiss;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
