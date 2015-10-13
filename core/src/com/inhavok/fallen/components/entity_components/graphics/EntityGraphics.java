package com.inhavok.fallen.components.entity_components.graphics;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.inhavok.fallen.Application;
import com.inhavok.fallen.commands.Command;
import com.inhavok.fallen.commands.component_commands.entity.entity_graphics.*;
import com.inhavok.fallen.components.entity_components.EntityComponent;
import com.inhavok.fallen.components.entity_components.graphics.layers.Layer;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class EntityGraphics extends EntityComponent {
	private final HashMap<Enum, Layer> layers;
	private final float width;
	private final float height;
	private float x;
	private float y;
	private float angle;
	public EntityGraphics() {
		this.layers = addLayers();
		final ArrayList<Layer> layers = new ArrayList<Layer>();
		layers.addAll(this.layers.values());
		width = layers.get(0).getSprite().getWidth() / Application.PIXELS_PER_METER;
		height = layers.get(0).getSprite().getHeight() / Application.PIXELS_PER_METER;
	}
	abstract HashMap<Enum, Layer> addLayers();
	@Override
	public void handleCommand(Command command) {
		if (command.getMessage() == Message.DRAW) {
			draw(((GraphicsDraw) command).getSpriteBatch());
		} else if (command.getMessage() == Message.ANIMATE) {
			animate(((GraphicsAnimate) command).getDelta());
		} else if (command.getMessage() == Message.GET_X) {
			((GraphicsGetX) command).setData(getX());
		} else if (command.getMessage() == Message.GET_Y) {
			((GraphicsGetY) command).setData(getY());
		} else if (command.getMessage() == Message.GET_ROTATION) {
			((GraphicsGetRotation) command).setData(getRotation());
		} else if (command.getMessage() == Message.SET_X) {
			setX(((GraphicsSetX) command).getX());
		} else if (command.getMessage() == Message.SET_Y) {
			setY(((GraphicsSetY) command).getY());
		} else if (command.getMessage() == Message.SET_ROTATION) {
			setAngle(((GraphicsSetRotation) command).getAngle());
		} else if (command.getMessage() == Message.SET_ANIMATION) {
			final GraphicsSetAnimation graphicsSetAnimation = (GraphicsSetAnimation) command;
			layers.get(graphicsSetAnimation.getLayer()).setAnimation(graphicsSetAnimation.getAnimation());
		}
	}
	private void animate(final float delta) {
		for (Layer layer : layers.values()) {
			layer.animate(delta);
		}
	}
	public static Animation createAnimation(final float frameDuration, final Array<AtlasRegion> atlasRegions, PlayMode playMode) {
		final Array<TextureRegion> textureRegions = new Array<TextureRegion>();
		textureRegions.addAll(atlasRegions);
		return new Animation(frameDuration, textureRegions, playMode);
	}
	public static Animation createAnimation(final AtlasRegion atlasRegion) {
		final Array<TextureRegion> textureRegions = new Array<TextureRegion>();
		textureRegions.add(atlasRegion);
		return new Animation(1, textureRegions, PlayMode.LOOP);
	}
	private void draw(final SpriteBatch spriteBatch) {
		for (Layer layer : layers.values()) {
			final Sprite sprite = layer.getSprite();
			spriteBatch.draw(new TextureRegion(sprite.getTexture(), sprite.getRegionX(), sprite.getRegionY(), sprite.getRegionWidth(), sprite.getRegionHeight()), x, y, sprite.getOriginX(), sprite.getOriginY(), width, height, sprite.getScaleX(), sprite.getScaleY(), angle);
		}
	}
	public final float getWidth() {
		return width;
	}
	public final float getHeight() {
		return height;
	}
	private float getX() {
		return x + width / 2;
	}
	private float getY() {
		return y + height / 2;
	}
	private float getRotation() {
		return angle;
	}
	private void setX(final float x) {
		this.x = x - width / 2;
	}
	private void setY(final float y) {
		this.y = y - height / 2;
	}
	private void setAngle(final float angle) {
		this.angle = angle;
	}
	public enum Message {
		ANIMATE, DRAW, GET_X, GET_Y, GET_ROTATION, SET_X, SET_Y, SET_ROTATION, SET_ANIMATION
	}
}