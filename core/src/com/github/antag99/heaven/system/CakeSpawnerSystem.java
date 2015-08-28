package com.github.antag99.heaven.system;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.github.antag99.heaven.component.Collision.FLAG_CAKE;
import static com.github.antag99.heaven.component.Collision.FLAG_FLOOR;
import static com.github.antag99.heaven.component.Collision.FLAG_PLAYER;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.antag99.heaven.component.Acting;
import com.github.antag99.heaven.component.Cake;
import com.github.antag99.heaven.component.CakeSpawner;
import com.github.antag99.heaven.component.Collision;
import com.github.antag99.heaven.component.Position;
import com.github.antag99.heaven.component.Size;
import com.github.antag99.heaven.component.Velocity;
import com.github.antag99.heaven.component.Weight;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;

@Wire
public final class CakeSpawnerSystem extends EntityProcessorSystem {
    private static final float CAKE_WIDTH = 2f;
    private static final float CAKE_HEIGHT = 1.75f;
    private static final float CAKE_WEIGHT = 0.5f;
    private static final float LIGHT_WIDTH = 1f;
    private static final float LIGHT_HEIGHT = 1f;
    private static final float LIGHT_X = CAKE_WIDTH * 0.5f - LIGHT_WIDTH * 0.5f;
    private static final float LIGHT_Y = CAKE_HEIGHT - LIGHT_HEIGHT * 0.5f;

    private Engine engine;
    private AssetSystem assetSystem;
    private DeltaSystem deltaSystem;
    private Mapper<CakeSpawner> mCakeSpawner;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;

    @SuppressWarnings("unchecked")
    public CakeSpawnerSystem() {
        super(Family.with(CakeSpawner.class));
    }

    @Override
    protected void process(int entity) {
        float deltaTime = deltaSystem.getDeltaTime();
        CakeSpawner cakeSpawner = mCakeSpawner.get(entity);
        Position position = mPosition.get(entity);
        Size size = mSize.get(entity);
        if ((cakeSpawner.time -= deltaTime) <= 0f) {
            cakeSpawner.time += MathUtils.random(cakeSpawner.minDelay, cakeSpawner.maxDelay);

            float cakeScale = MathUtils.random(cakeSpawner.minScale, cakeSpawner.maxScale);

            float cakeX = MathUtils.random(position.x, position.x + size.width - CAKE_WIDTH * cakeScale);
            float cakeY = MathUtils.random(position.y, position.y + size.height - CAKE_HEIGHT * cakeScale);

            Handle cakeEntity = engine.createEntity();
            cakeEntity.create(Position.class).xy(cakeX, cakeY);
            cakeEntity.create(Velocity.class);
            cakeEntity.create(Size.class).size(CAKE_WIDTH * cakeScale, CAKE_HEIGHT * cakeScale);
            cakeEntity.create(Collision.class).properties(FLAG_CAKE).collision(FLAG_PLAYER | FLAG_FLOOR);
            cakeEntity.create(Weight.class).weight(CAKE_WEIGHT * cakeScale);
            cakeEntity.create(Cake.class).value = MathUtils.random(cakeSpawner.minValue, cakeSpawner.maxValue);

            Group group = new Group();
            // group.debug();
            Image cakeImage = new Image(assetSystem.skin, "cake");
            cakeImage.setWidth(CAKE_WIDTH * cakeScale);
            cakeImage.setHeight(CAKE_HEIGHT * cakeScale);
            group.addActor(cakeImage);
            Image lightImage = new Image(assetSystem.skin, "light");
            lightImage.setX(LIGHT_X * cakeScale);
            lightImage.setY(LIGHT_Y * cakeScale);
            lightImage.setWidth(LIGHT_WIDTH * cakeScale);
            lightImage.setHeight(LIGHT_HEIGHT * cakeScale);
            /* @off */ lightImage.addAction(forever(sequence(
                alpha(0.2f, 0.5f, Interpolation.fade),
                alpha(1.0f, 0.5f, Interpolation.fade)
            ))); /* @on */
            group.addActor(lightImage);
            cakeEntity.create(Acting.class).actor(group);
        }
    }
}
