package demo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Demo extends GameApplication {
	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;

	private Game game;
	private Game.State state;
	private final Entity[] message = new Entity[]{null, null};

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	protected void initSettings(GameSettings gameSettings) {
		gameSettings.setTitle("Retro Game");
		gameSettings.setHeight(HEIGHT);
		gameSettings.setWidth(WIDTH);
		gameSettings.setVersion("1.0");
	}

	@Override
	protected void initGame() {
		game = new Game(WIDTH, HEIGHT);
	}

	@Override
	protected void onUpdate(double time) {
		if (state == null || state == Game.State.Running) {
			state = game.update();
			if (state != Game.State.Running) { // The game just ended
				Text text = new Text(0, 0, (state == Game.State.Won)? "You won!": "You lost.");
				text.setFont(new Font(30));
				text.setFill((state == Game.State.Won)? Color.GREENYELLOW: Color.RED);
				message[0] = FXGL.entityBuilder()
						.at(50.0, 50.0)
						.view(text)
						.buildAndAttach();

				Text text2 = new Text(0, 0, "Press ENTER to retry...");
				text2.setFont(new Font(20));
				message[1] = FXGL.entityBuilder()
						.at(50.0, 80.0)
						.view(text2)
						.buildAndAttach();
			}
		}
	}

	@Override
	protected void initInput() {
		Input input = FXGL.getInput();

		input.addAction(new MoveAction("Left", -2.5, 0.0), KeyCode.LEFT);
		input.addAction(new MoveAction("Right", 2.5, 0.0), KeyCode.RIGHT);
		input.addAction(new MoveAction("Up", 0.0, -2.5), KeyCode.UP);
		input.addAction(new MoveAction("Down", 0.0, +2.5), KeyCode.DOWN);
		input.addAction(new UserAction("Shoot") { // Shoot a bullet when the user presses SPACE
			@Override
			protected void onAction() {
				game.shoot();
			}
		}, KeyCode.SPACE);
		input.addAction(new UserAction("Retry") {
			@Override
			protected void onAction() {
				if (state != Game.State.Running) {
					state = null;
					game.destroy();
					game = new Game(WIDTH, HEIGHT);
					for (Entity m: message) {
						if (m != null) {
							m.removeFromWorld();
						}
					}
				}
			}
		}, KeyCode.ENTER);
	}

	@Override
	protected void initPhysics() {
		FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Bullet, EntityType.Block) {
			/**
			 * When a block and bullet collide with each other, both entities are destroyed.
			 */
			@Override
			protected void onCollision(Entity a, Entity b) {
				a.removeFromWorld();
				b.removeFromWorld();
			}
		});
	}

	private class MoveAction extends UserAction {
		private final double deltaX;
		private final double deltaY;

		public MoveAction(String direction, double deltaX, double deltaY) {
			super("Move " + direction);
			this.deltaX = deltaX;
			this.deltaY = deltaY;
		}

		@Override
		protected void onAction() {
			game.movePlayer(deltaX, deltaY);
		}
	}
}
