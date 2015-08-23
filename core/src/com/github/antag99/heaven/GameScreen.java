package com.github.antag99.heaven;

import static com.github.antag99.heaven.component.Collision.FLAG_FLOOR;
import static com.github.antag99.heaven.component.Collision.FLAG_PLAYER;
import static com.github.antag99.heaven.component.Collision.FLAG_WALL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.github.antag99.heaven.component.Acting;
import com.github.antag99.heaven.component.Attack;
import com.github.antag99.heaven.component.CakeSpawner;
import com.github.antag99.heaven.component.Collision;
import com.github.antag99.heaven.component.Control;
import com.github.antag99.heaven.component.DevilSpawner;
import com.github.antag99.heaven.component.Direction;
import com.github.antag99.heaven.component.FairySpawner;
import com.github.antag99.heaven.component.House;
import com.github.antag99.heaven.component.Keyboard;
import com.github.antag99.heaven.component.Movement;
import com.github.antag99.heaven.component.Player;
import com.github.antag99.heaven.component.Position;
import com.github.antag99.heaven.component.Size;
import com.github.antag99.heaven.component.Velocity;
import com.github.antag99.heaven.component.Weight;
import com.github.antag99.heaven.system.ActingSystem;
import com.github.antag99.heaven.system.ActorPositioningSystem;
import com.github.antag99.heaven.system.ActorSizingSystem;
import com.github.antag99.heaven.system.AssetSystem;
import com.github.antag99.heaven.system.AttackControlSystem;
import com.github.antag99.heaven.system.AttackSystem;
import com.github.antag99.heaven.system.CakeSpawnerSystem;
import com.github.antag99.heaven.system.CakeSystem;
import com.github.antag99.heaven.system.ClearSystem;
import com.github.antag99.heaven.system.CollisionSystem;
import com.github.antag99.heaven.system.DevilSpawnerSystem;
import com.github.antag99.heaven.system.DevilSystem;
import com.github.antag99.heaven.system.DirectionSystem;
import com.github.antag99.heaven.system.FairySpawnerSystem;
import com.github.antag99.heaven.system.FairySystem;
import com.github.antag99.heaven.system.GravitySystem;
import com.github.antag99.heaven.system.KeyboardSystem;
import com.github.antag99.heaven.system.MovementControlSystem;
import com.github.antag99.heaven.system.PhysicsSystem;
import com.github.antag99.heaven.system.PlayerSystem;
import com.github.antag99.heaven.util.SpineActor;
import com.github.antag99.retinazer.DependencyConfig;
import com.github.antag99.retinazer.DependencyResolver;
import com.github.antag99.retinazer.EngineConfig;
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;

@Wire
public final class GameScreen extends HeavenScreen {
    private static final float GRAVITY = -40f;
    private static final float GRAVITY_ACCELERATION = 1f;

    private static final float SCENE_WIDTH = 64f;
    private static final float SCENE_HEIGHT = 128f;

    private static final float HOUSE_WIDTH = 16f;
    private static final float HOUSE_HEIGHT = 16f;

    private static final float FLOOR_PAD = 10f;
    private static final float FLOOR_WIDTH = SCENE_WIDTH + FLOOR_PAD * 2f;
    private static final float FLOOR_HEIGHT = 5f;
    private static final float FLOOR_X = -FLOOR_PAD;
    private static final float FLOOR_Y = 0f;
    private static final float WALL_WIDTH = 2f;
    private static final float WALL_HEIGHT = SCENE_HEIGHT;
    private static final float LEFT_WALL_X = -WALL_WIDTH;
    private static final float LEFT_WALL_Y = 0f;
    private static final float RIGHT_WALL_X = SCENE_WIDTH;
    private static final float RIGHT_WALL_Y = 0f;

    private static final float HAZE_X = 0f;
    private static final float HAZE_Y = FLOOR_HEIGHT;
    private static final float HAZE_WIDTH = SCENE_WIDTH;
    private static final float HAZE_HEIGHT = SCENE_HEIGHT;

    private static final float PLAYER_WIDTH = 10f;
    private static final float PLAYER_HEIGHT = 10f;
    private static final float PLAYER_WEIGHT = 2f;
    private static final float PLAYER_SPEED = 18f;
    private static final int PLAYER_HEALTH = 50;

    private static final int HELL_THRESHOLD = 25;

    private static final float CAKE_SPAWNER_X = 0f;
    private static final float CAKE_SPAWNER_Y = SCENE_HEIGHT;
    private static final float CAKE_SPAWNER_WIDTH = SCENE_WIDTH;
    private static final float CAKE_SPAWNER_HEIGHT = 3f;

    private static final float MONSTER_SPAWNER_WIDTH = 5f;
    private static final float MONSTER_SPAWNER_HEIGHT = 32f;

    private static final float LEFT_MONSTER_SPAWNER_X = -MONSTER_SPAWNER_WIDTH;
    private static final float LEFT_MONSTER_SPAWNER_Y = FLOOR_HEIGHT + 2f;

    private static final float RIGHT_MONSTER_SPAWNER_X = SCENE_WIDTH;
    private static final float RIGHT_MONSTER_SPAWNER_Y = FLOOR_HEIGHT + 2f;

    private static final Color SKY_COLOR = new Color(0.75f, 0.75f, 0.95f, 1f);
    private static final Color HELL_COLOR = new Color(0.4f, 0f, 0f, 1f);

    private static final Color FLOOR_COLOR = new Color(1f, 1f, 1f, 1f);
    private static final Color HELL_FLOOR_COLOR = new Color(0.5f, 0.2f, 0.2f, 1f);

    private static final float CAKE_DURATION = 10f;
    private static final float MONSTER_DURATION = 20f;

    private AssetSystem assetSystem;
    private Label scoreLabel;
    private Label lifeLabel;
    private Label noticeLabel;
    private Image sceneImage;
    private Image hazeImage;
    private Image floorImage;
    private Array<Image> houseImages = new Array<Image>();
    private Handle playerEntity;
    private Handle leftMonsterSpawner;
    private Handle rightMonsterSpawner;
    private Handle cakeSpawner;
    private Mapper<Player> mPlayer;
    private SkeletonData playerSkeletonData;
    private AnimationStateData playerAnimationStateData;

    private float time = 0f;
    private boolean isCake = false;

    public boolean isHell = false;
    public boolean isStarted = false;
    public boolean isFinished = false;

    public GameScreen(HeavenGame game) {
        super(game);

        pixelsPerMeter = 16f;
        worldStage.getDebugColor().set(1f, 0f, 0f, 1f);

        scoreLabel = new Label("", skin, "score");
        lifeLabel = new Label("", skin, "life");
        noticeLabel = new Label("", skin, "notice");
        Table labelTable = new Table();
        labelTable.add(scoreLabel).width(200f).left().padRight(2f).padTop(2f).row();
        labelTable.add(lifeLabel).width(200f).left().padRight(2f).padTop(2f).row();
        table.add(labelTable).expandX().right().top().row();
        table.add(noticeLabel).expandX().expandY().center().row();

        playerSkeletonData = new SkeletonJson(new AtlasAttachmentLoader(skin.getAtlas()) {
            @Override
            public RegionAttachment newRegionAttachment(Skin skin, String name, String path) {
                return super.newRegionAttachment(skin, name, "player/images/" + path);
            }
        }) {
            {
                setScale(1f / pixelsPerMeter * 0.4f);
            }

            @Override
            public SkeletonData readSkeletonData(FileHandle file) {
                SkeletonData skeletonData = super.readSkeletonData(file);
                if (skeletonData.getBones().size > 0) {
                    skeletonData.getBones().get(0).setPosition(PLAYER_WIDTH * 0.3f, 0f);
                }
                return skeletonData;
            }
        }.readSkeletonData(Gdx.files.internal("player/player.json"));
        playerAnimationStateData = new AnimationStateData(playerSkeletonData);
    }

    @Override
    public EngineConfig initialize() {
        return super.initialize()
                .addWireResolver(new DependencyResolver(new DependencyConfig()
                        .addDependency(skin)
                        .addDependency(Batch.class, batch)
                        .addDependency(this)))
                .addSystem(new AssetSystem())
                .addSystem(new KeyboardSystem())
                .addSystem(new MovementControlSystem())
                .addSystem(new AttackControlSystem())
                .addSystem(new CakeSpawnerSystem())
                .addSystem(new CakeSystem())
                .addSystem(new DevilSpawnerSystem())
                .addSystem(new DevilSystem())
                .addSystem(new FairySpawnerSystem())
                .addSystem(new FairySystem())
                .addSystem(new GravitySystem(GRAVITY, GRAVITY_ACCELERATION))
                .addSystem(new PhysicsSystem())
                .addSystem(new AttackSystem())
                .addSystem(new DirectionSystem())
                .addSystem(new CollisionSystem())
                .addSystem(new PlayerSystem())
                .addSystem(new ActorPositioningSystem())
                .addSystem(new ActorSizingSystem())
                .addSystem(new ClearSystem(new Color(0.95f, 0.95f, 1f, 1f)))
                .addSystem(new ActingSystem(worldStage));
    }

    private void startGame() {
        isStarted = true;
        noticeLabel.setText("");
        playerEntity.create(Control.class);
        assetSystem.heavenMusic.play();
        assetSystem.heavenMusic.setLooping(true);
        // leftMonsterSpawner.create(FairySpawner.class);
        // rightMonsterSpawner.create(FairySpawner.class);
        // cakeSpawner.create(CakeSpawner.class);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (!isStarted) {
            noticeLabel.setText("You're a monster in heaven.\n"
                    + "Press A/D to move left/right, \n"
                    + "and SPACE to eat cakes and kill pixies.\n"
                    + "Your goal is to gain the highest possible score.\n"
                    + "Click the screen or press ENTER to continue.");
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.justTouched()) {
                startGame();
            }
        }

        if (isStarted && !isFinished) {
            time += delta;

            if (time % (CAKE_DURATION + MONSTER_DURATION) < CAKE_DURATION) {
                if (!isCake) {
                    isCake = true;
                    cakeSpawner.create(CakeSpawner.class);
                    leftMonsterSpawner.remove(FairySpawner.class);
                    rightMonsterSpawner.remove(FairySpawner.class);
                    leftMonsterSpawner.remove(DevilSpawner.class);
                    rightMonsterSpawner.remove(DevilSpawner.class);
                }
            } else {
                if (isCake) {
                    isCake = false;
                    cakeSpawner.remove(CakeSpawner.class);
                    FairySpawner leftFairy = leftMonsterSpawner.create(FairySpawner.class);
                    FairySpawner rightFairy = rightMonsterSpawner.create(FairySpawner.class);
                    leftFairy.minDelay = MathUtils.random(0.2f, 0.5f);
                    leftFairy.maxDelay = MathUtils.random(leftFairy.minDelay + 0.2f, 8f);
                    rightFairy.minDelay = MathUtils.random(0.2f, 0.5f);
                    rightFairy.maxDelay = MathUtils.random(rightFairy.minDelay + 0.2f, 8f);
                    if (isHell) {
                        DevilSpawner leftDevil = leftMonsterSpawner.create(DevilSpawner.class);
                        DevilSpawner rightDevil = rightMonsterSpawner.create(DevilSpawner.class);
                        leftDevil.minDelay = MathUtils.random(5f, 8f);
                        leftDevil.maxDelay = MathUtils.random(leftDevil.minDelay + 1f, 18f);
                        rightDevil.minDelay = MathUtils.random(5f, 8f);
                        rightDevil.maxDelay = MathUtils.random(rightDevil.minDelay + 1f, 18f);
                    }
                }
            }

            Player player = mPlayer.get(playerEntity.getEntity());

            scoreLabel.setText("Score " + player.score);
            lifeLabel.setText("Life  " + (player.health < 0 ? 0 : player.health));

            float hellAlpha = 1f - MathUtils.clamp((float) player.health / (float) PLAYER_HEALTH, 0f, 1f);
            sceneImage.getColor().set(SKY_COLOR);
            sceneImage.getColor().lerp(HELL_COLOR, hellAlpha);
            hazeImage.getColor().a = 1f - hellAlpha;
            floorImage.getColor().set(FLOOR_COLOR);
            floorImage.getColor().lerp(HELL_FLOOR_COLOR, hellAlpha);

            if (player.health <= HELL_THRESHOLD && !isHell) {
                assetSystem.heavenMusic.stop();
                assetSystem.underworldMusic.play();
                assetSystem.underworldMusic.setLooping(true);
                isHell = true;
                for (Image houseImage : houseImages)
                    houseImage.setDrawable(skin, "hell_house");
                // leftMonsterSpawner.create(DevilSpawner.class);
                // rightMonsterSpawner.create(DevilSpawner.class);
            }

            if (player.health <= 0 && !isFinished) {
                assetSystem.underworldMusic.stop();
                isFinished = true;
                lifeLabel.setText("");
                leftMonsterSpawner.remove(FairySpawner.class);
                rightMonsterSpawner.remove(FairySpawner.class);
                leftMonsterSpawner.remove(DevilSpawner.class);
                rightMonsterSpawner.remove(DevilSpawner.class);
                playerEntity.remove(Control.class);
            }
        }

        if (isFinished) {
            noticeLabel.setText("Game Over! Click the screen or\npress ENTER to try again.");
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.justTouched()) {
                // Throw away this screen - it's state is messed up
                dispose();
                GameScreen newScreen = new GameScreen(game);
                game.setScreen(newScreen);
                newScreen.startGame();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        float scale = SCENE_WIDTH / width;

        worldViewport.setUnitsPerPixel(scale);
        worldViewport.update(width, height, true);
    }

    @Override
    public void reset() {
        engine.wire(this);

        // Create the scene
        sceneImage = new Image(skin, "background");
        sceneImage.setColor(SKY_COLOR);
        Handle sceneEntity = engine.createEntity();
        sceneEntity.create(Position.class).xy(0f, 0f);
        sceneEntity.create(Size.class).size(SCENE_WIDTH, SCENE_HEIGHT);
        sceneEntity.create(Acting.class).actor(sceneImage);

        // Create some pretty houses!
        int count = (int) (SCENE_WIDTH / HOUSE_WIDTH);
        float houseX = (SCENE_WIDTH - count * HOUSE_WIDTH);
        float houseY = FLOOR_HEIGHT;
        for (int i = 0; i < count; i++) {
            Image houseImage = new Image(skin, "house");
            houseImages.add(houseImage);
            Handle houseEntity = engine.createEntity();
            houseEntity.create(Position.class).xy(houseX, houseY);
            houseEntity.create(Size.class).size(HOUSE_WIDTH, HOUSE_HEIGHT);
            houseEntity.create(Acting.class).actor(houseImage);
            houseEntity.create(House.class);
            houseX += HOUSE_WIDTH;
        }

        // Create haze gradient
        hazeImage = new Image(skin, "haze");
        Handle hazeEntity = engine.createEntity();
        hazeEntity.create(Position.class).xy(HAZE_X, HAZE_Y);
        hazeEntity.create(Size.class).size(HAZE_WIDTH, HAZE_HEIGHT);
        hazeEntity.create(Acting.class).actor(hazeImage);

        // Create terrain entities
        floorImage = new Image(skin, "floor");
        Handle floorEntity = engine.createEntity();
        floorEntity.create(Acting.class).actor(floorImage);
        floorEntity.create(Position.class).xy(FLOOR_X, FLOOR_Y);
        floorEntity.create(Size.class).size(FLOOR_WIDTH, FLOOR_HEIGHT);
        floorEntity.create(Collision.class).properties(FLAG_FLOOR).collision(0);
        Handle leftWallEntity = engine.createEntity();
        leftWallEntity.create(Position.class).xy(LEFT_WALL_X, LEFT_WALL_Y);
        leftWallEntity.create(Size.class).size(WALL_WIDTH, WALL_HEIGHT);
        leftWallEntity.create(Collision.class).properties(FLAG_WALL).collision(0);
        Handle rightWallEntity = engine.createEntity();
        rightWallEntity.create(Position.class).xy(RIGHT_WALL_X, RIGHT_WALL_Y);
        rightWallEntity.create(Size.class).size(WALL_WIDTH, WALL_HEIGHT);
        rightWallEntity.create(Collision.class).properties(FLAG_WALL).collision(0);

        // Create cake spawner
        Handle cakeSpawnerEntity = engine.createEntity();
        cakeSpawnerEntity.create(Position.class).xy(CAKE_SPAWNER_X, CAKE_SPAWNER_Y);
        cakeSpawnerEntity.create(Size.class).size(CAKE_SPAWNER_WIDTH, CAKE_SPAWNER_HEIGHT);
        // cakeSpawnerEntity.create(CakeSpawner.class);
        this.cakeSpawner = cakeSpawnerEntity.duplicate();

        // Create monster spawners
        Handle leftMonsterSpawnerEntity = engine.createEntity();
        leftMonsterSpawnerEntity.create(Position.class).xy(LEFT_MONSTER_SPAWNER_X, LEFT_MONSTER_SPAWNER_Y);
        leftMonsterSpawnerEntity.create(Size.class).size(MONSTER_SPAWNER_WIDTH, MONSTER_SPAWNER_HEIGHT);
        // leftMonsterSpawnerEntity.create(FairySpawner.class);
        this.leftMonsterSpawner = leftMonsterSpawnerEntity.duplicate();

        Handle rightMonsterSpawnerEntity = engine.createEntity();
        rightMonsterSpawnerEntity.create(Position.class).xy(RIGHT_MONSTER_SPAWNER_X, RIGHT_MONSTER_SPAWNER_Y);
        rightMonsterSpawnerEntity.create(Size.class).size(MONSTER_SPAWNER_WIDTH, MONSTER_SPAWNER_HEIGHT);
        // rightMonsterSpawnerEntity.create(FairySpawner.class);
        this.rightMonsterSpawner = rightMonsterSpawnerEntity.duplicate();

        // Create the player
        SpineActor player = new SpineActor(new Skeleton(playerSkeletonData),
                new AnimationState(playerAnimationStateData));
        // player.debug();
        Handle playerEntity = engine.createEntity();
        playerEntity.create(Acting.class).actor(player);
        playerEntity.create(Position.class).xy(SCENE_WIDTH * 0.5f - PLAYER_WIDTH * 0.5f, FLOOR_HEIGHT);
        playerEntity.create(Size.class).size(PLAYER_WIDTH, PLAYER_HEIGHT);
        playerEntity.create(Collision.class).properties(FLAG_PLAYER).collision(FLAG_FLOOR | FLAG_WALL);
        playerEntity.create(Velocity.class);
        playerEntity.create(Movement.class).speed(PLAYER_SPEED);
        // playerEntity.create(Control.class);
        playerEntity.create(Keyboard.class).keyLeft(Keys.A).keyRight(Keys.D).keyAttack(Keys.SPACE);
        playerEntity.create(Weight.class).weight(PLAYER_WEIGHT);
        playerEntity.create(Player.class).health(PLAYER_HEALTH);
        playerEntity.create(Direction.class);
        playerEntity.create(Attack.class).delay(0.8f).trigger(0.65f);
        this.playerEntity = playerEntity.duplicate();
    }
}
