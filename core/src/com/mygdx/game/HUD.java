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

    Label countUp;
    Label timeLabel;

    public HUD(SpriteBatch sb){

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Pokemon Hollow.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 10;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();


        timeCount = 0;

        OrthographicCamera cam = new OrthographicCamera();
        cam.setToOrtho(false, 800, 480);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        timeLabel = new Label(String.format("%03d", timeCount), new Label.LabelStyle(font, Color.BLACK));
        countUp = new Label("TIME", new Label.LabelStyle(font, Color.BLACK));

        table.add(timeLabel).expandX().top().left();
        table.row();
        table.add(countUp).expandX().top().left();

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
