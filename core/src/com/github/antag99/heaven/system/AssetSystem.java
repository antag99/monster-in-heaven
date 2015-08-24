package com.github.antag99.heaven.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Wire;

public final class AssetSystem extends EntitySystem {
    public @Wire Skin skin;
    public ParticleEffect eatCake;
    public ParticleEffect dieCake;
    public SkeletonData devilSkeletonData;
    public AnimationStateData devilAnimationStateData;
    public Sound tridentSound;
    public SkeletonData fairySkeletonData;
    public AnimationStateData fairyAnimationStateData;
    public SkeletonData playerSkeletonData;
    public AnimationStateData playerAnimationStateData;
    public ParticleEffect fairyDie;
    public ParticleEffect hellFairyDie;
    public ParticleEffect devilDie;
    public Sound biteSound;
    public Sound devilSound;
    public Sound eatSound;
    public Music heavenMusic;
    public Music underworldMusic;
    public Sound fairySound;
    public Sound playerSound;
    public Sound ringSound;

    @Override
    protected void initialize() {
        eatCake = new ParticleEffect();
        eatCake.load(Gdx.files.internal("cake_eat.txt"), skin.getAtlas());
        eatCake.scaleEffect(1f / 16f); // XXX: Duplicated constant
        dieCake = new ParticleEffect();
        dieCake.load(Gdx.files.internal("cake_die.txt"), skin.getAtlas());
        dieCake.scaleEffect(1f / 16f); // XXX: Duplicated constant
        skin.add("cake_eat", eatCake, ParticleEffect.class);
        skin.add("cake_die", dieCake, ParticleEffect.class);

        this.devilSkeletonData = new SkeletonJson(new AtlasAttachmentLoader(skin.getAtlas()) {
            @Override
            public RegionAttachment newRegionAttachment(com.esotericsoftware.spine.Skin skin, String name, String path) {
                return super.newRegionAttachment(skin, name, "devil/images/" + path);
            }
        }) {
            {
                setScale(1f / 16f); // XXX: Duplicated constant
            }
        }.readSkeletonData(Gdx.files.internal("devil/devil.json"));
        this.devilAnimationStateData = new AnimationStateData(devilSkeletonData);

        tridentSound = Gdx.audio.newSound(Gdx.files.internal("trident.wav"));
        skin.add("trident", tridentSound, Sound.class);

        this.fairySkeletonData = new SkeletonJson(new AtlasAttachmentLoader(skin.getAtlas()) {
            @Override
            public RegionAttachment newRegionAttachment(com.esotericsoftware.spine.Skin skin, String name, String path) {
                return super.newRegionAttachment(skin, name, "fairy/images/" + path);
            }
        }) {
            {
                setScale(1f / 16f * 0.25f); // XXX: Duplicated constant
            }
        }.readSkeletonData(Gdx.files.internal("fairy/fairy.json"));
        this.fairyAnimationStateData = new AnimationStateData(fairySkeletonData);

        playerSkeletonData = new SkeletonJson(new AtlasAttachmentLoader(skin.getAtlas()) {
            @Override
            public RegionAttachment newRegionAttachment(com.esotericsoftware.spine.Skin skin, String name, String path) {
                return super.newRegionAttachment(skin, name, "player/images/" + path);
            }
        }) {
            {
                setScale(1f / 16f * 0.4f); // XXX: Duplicated constant
            }
        }.readSkeletonData(Gdx.files.internal("player/player.json"));
        playerAnimationStateData = new AnimationStateData(playerSkeletonData);

        fairyDie = new ParticleEffect();
        fairyDie.load(Gdx.files.internal("fairy_die.txt"), skin.getAtlas());
        fairyDie.scaleEffect(1f / 16f); // XXX: Duplicated constant
        skin.add("fairy_die", fairyDie, ParticleEffect.class);

        hellFairyDie = new ParticleEffect();
        hellFairyDie.load(Gdx.files.internal("hell_fairy_die.txt"), skin.getAtlas());
        hellFairyDie.scaleEffect(1f / 16f); // XXX: Duplicated constant
        skin.add("hell_fairy_die", hellFairyDie, ParticleEffect.class);

        devilDie = new ParticleEffect();
        devilDie.load(Gdx.files.internal("devil_die.txt"), skin.getAtlas());
        devilDie.scaleEffect(1f / 16f); // XXX: Duplicated constant
        skin.add("devil_die", devilDie, ParticleEffect.class);

        biteSound = Gdx.audio.newSound(Gdx.files.internal("bite.wav"));
        skin.add("bite", biteSound, Sound.class);
        devilSound = Gdx.audio.newSound(Gdx.files.internal("devil.wav"));
        skin.add("devil", devilSound, Sound.class);
        eatSound = Gdx.audio.newSound(Gdx.files.internal(("eat.wav")));
        skin.add("eat", eatSound, Sound.class);
        heavenMusic = Gdx.audio.newMusic(Gdx.files.internal("heaven.ogg"));
        skin.add("heaven", heavenMusic, Music.class);
        underworldMusic = Gdx.audio.newMusic(Gdx.files.internal("underworld.ogg"));
        skin.add("underworld", underworldMusic, Music.class);
        fairySound = Gdx.audio.newSound(Gdx.files.internal("pixie.wav"));
        skin.add("fairy", fairySound, Sound.class);
        playerSound = Gdx.audio.newSound(Gdx.files.internal("player.wav"));
        skin.add("player", playerSound, Sound.class);
        ringSound = Gdx.audio.newSound(Gdx.files.internal("ring.wav"));
        skin.add("ring", ringSound, Sound.class);
    }
}
