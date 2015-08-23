package com.github.antag99.heaven;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;

public abstract class HeavenScreen implements Screen {
    public float worldArea = 500000f;
    public float pixelsPerMeter = 16f;
    public HeavenGame game;
    public Engine engine;
    public Stage worldStage;
    public ScreenViewport worldViewport;
    public Stage uiStage;
    public ScreenViewport uiViewport;
    public Batch batch;
    public Table table;
    public Skin skin;
    public InputMultiplexer inputMultiplexer;

    public HeavenScreen(HeavenGame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.worldViewport = new ScreenViewport();
        this.worldStage = new Stage(worldViewport, batch);
        this.uiViewport = new ScreenViewport();
        this.uiStage = new Stage(uiViewport, batch);
        this.table = new Table();
        this.table.setFillParent(true);
        this.uiStage.addActor(table);
        this.inputMultiplexer = new InputMultiplexer();
        this.inputMultiplexer.addProcessor(worldStage);
        this.inputMultiplexer.addProcessor(uiStage);
        this.skin = new Skin(Gdx.files.internal("skin.json"));
        // Images such as "tooth_0", "tooth_1" are handled specially by TextureAtlas; revert this.
        for (AtlasRegion region : this.skin.getAtlas().getRegions().<AtlasRegion> toArray(AtlasRegion.class)) {
            if (region.index != -1) {
                this.skin.getAtlas().addRegion(region.name + "_" + region.index, region);
            }
        }
        this.table.setSkin(skin);
        Gdx.input.setInputProcessor(inputMultiplexer);
        this.engine = new Engine(initialize());
    }

    /**
     * Initializes this screen, and returns an {@link EngineConfig} instance to
     * create an {@link Engine} with.
     *
     * @return The engine configuration.
     */
    public EngineConfig initialize() {
        return new EngineConfig();
    }

    /**
     * Restarts this screen; called before it shown.
     */
    public void reset() {
    }

    @Override
    public void render(float delta) {
        engine.update();
        worldStage.act(delta);
        worldStage.draw();
        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        float scale = (float) Math.sqrt(worldArea / (width * height));

        worldViewport.setUnitsPerPixel((1f / pixelsPerMeter) * scale);
        worldViewport.update(width, height, true);

        uiViewport.setUnitsPerPixel(scale);
        uiViewport.update(width, height, true);
    }

    @Override
    public void show() {
        reset();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        skin.dispose();
        worldStage.dispose();
    }

    @Override
    public final void pause() {
    }

    @Override
    public final void resume() {
    }
}
