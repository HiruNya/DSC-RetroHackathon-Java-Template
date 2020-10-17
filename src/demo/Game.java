package demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private final Entity player;
	private final List<Bullet> bullets = new ArrayList<>();
	private final List<Block> blocks = new ArrayList<>();
	private int height;
	private State state = State.Running;

	public Game(int width, int height) {
		this.height = height;

		Polygon triangle = new Polygon(); // Make a triangle by specifying their points
		triangle.getPoints().addAll(
				0.0, 0.0,
				-10.0, 20.0,
				10.0, 20.0
		);

		player = FXGL.entityBuilder() // Create the player at position 100, 100
				.at(300, 300)
				.view(triangle)
				.buildAndAttach();

		for (int x = 0; x < width; x += Block.LENGTH * 2) {
			blocks.add(new Block(x, 0));
		}
	}

	public State update() {
		if (state != State.Running) { // If the game isn't running then we'll just return if we won or lost
			return state;
		}

		List<Bullet> bulletsToRemove = new ArrayList<>();
		for (Bullet b: bullets) {
			if (!b.update()) { // All bullets that returns false should be removed
				bulletsToRemove.add(b);
			}
		}
		for (Bullet b: bulletsToRemove) {
			bullets.remove(b);
		}

		List<Block> blocksToRemove = new ArrayList<>();
		for (Block block: blocks) {
			if (!block.update()) { // All blocks that return false should be removed
				blocksToRemove.add(block);
			}
			if (block.getY() >= this.height) {
				state = State.Lost; // If a block hits the bottom, we lose!
			}
		}
		for (Block block: blocksToRemove) {
			blocks.remove(block);
		}
		if (blocks.isEmpty()) { // If all the blocks are destroyed, the player wins!
			state = State.Won;
		}
		return state;
	}

	public void destroy() {
		for (Bullet bullet: bullets) {
			bullet.destroy();
		}
		for (Block block: blocks) {
			block.destroy();
		}
		player.removeFromWorld();
	}

	public void movePlayer(double x, double y) {
		if (state != State.Running) {
			return;
		}
		player.translate(x, y);
	}

	public void shoot() {
		bullets.add(new Bullet(player.getX(), player.getY()));
	}

	/**
	 * The state of the game
	 */
	public enum State {
		Running,
		Won,
		Lost,
	}
}
