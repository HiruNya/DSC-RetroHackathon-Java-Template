package demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.scene.shape.Circle;

/**
 * The player fires these bullets and tries to hit the blocks to destroy them.
 */
public class Bullet {
	private static double VELOCITY = 10.0;
	private static double RADIUS = 5.0;
	private final Entity entity;

	public Bullet(double x, double y) {
		entity = FXGL.entityBuilder()
				.at(x, y)
				.viewWithBBox(new Circle(0, 0, RADIUS)) // Is drawn as a circle
				.with(new CollidableComponent(true)) // Make this bullet collide able
				.type(EntityType.Bullet)
				.buildAndAttach();
	}

	public boolean update() {
		if (entity.getY() < 0) {
			entity.removeFromWorld(); // Destroy itself if it goes out of bounds
			return false; // Tells the game to remove this bullet from its list
		}
		entity.translate(0.0, -VELOCITY); // Bullets go up
		return true;
	}

	public void destroy() {
		entity.removeFromWorld();
	}
}
