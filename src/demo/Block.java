package demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.shape.Rectangle;

/**
 * Blocks fall down and when they hit the bottom, you lose!
 */
public class Block {
	private final Entity entity;

	public final static int LENGTH = 10;
	private final static double VELOCITY = 0.5;

	public Block(double x, double y) {
		entity = FXGL.entityBuilder()
				.at(x, y)
				.viewWithBBox(new Rectangle(0, 0, LENGTH, LENGTH))
				.collidable()
				.type(EntityType.Block)
				.buildAndAttach();
	}

	public boolean update() {
		entity.translateY(VELOCITY);
		return entity.isActive();
	}

	public double getY() {
		return entity.getY();
	}

	public void destroy() {
		entity.removeFromWorld();
	}
}
