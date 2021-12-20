package game;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;

public class Gui extends Application {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 800;
	private static final int ROWS = 13;
	private static final int COLUMNS = 13;
	private static final int SQUARE_SIZE = WIDTH / ROWS;
	private GraphicsContext gc;
	private Board board;
	private boolean endGame = false;
	public  Integer[] score= {0,0};


	@Override
	public void start(Stage primaryStage) throws Exception {
		board = new Board();
		primaryStage.setTitle("Snake");

		Group root = new Group();
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

		gc = canvas.getGraphicsContext2D();
		board.updateBorad();	
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(150), e -> run(gc)));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();


	}

	private void run(GraphicsContext gc) {
		if (!endGame) {
			ArrayList<Snake> snakes = board.getSnakes();
			//System.out.println(board);
			int index=0;
			if (!board.enemyWon()) {
			//	System.out.println("snakes :  " +snakes.toString() );
				for (Snake s : snakes) {
					if (s.isAlive()) {
						s.moveByAlgoWithConfig(index,Ai.MINIMAX,Ai.MINIMAX,Ai.GREEDY,Ai.ASTAR,3,board,Ai.MANHATAN);
						//s.moveByAlgo(index,board,Ai.EUQLIDIAIN);
						if (s.getSnakeColor() == EColor.red || s.getSnakeColor() == EColor.orange) {
							board.isBite(s);
						}
					}
					++index;
					board.updateBorad();
				}
				//System.out.println(board);
				
				//drawing
				Square[][] gameBoard = board.getBoard();
				for (int i = 0; i < ROWS; i++) {
					for (int j = 0; j < COLUMNS; j++) {
						EColor col = gameBoard[i][j].getColor();
						if (col == EColor.white) {
							drawBackground(gc, j, i);
						} else {
							drawSnake(gc, col, j, i);
						}
					}
				}
				drawScore(board.getSnakeScore(0), 5, 25,1);
				drawScore(board.getSnakeScore(1), 5, 55,2);
			} else {
				endGame = true;
			}
		} else { // game over
			ArrayList<Snake> snakes = board.getSnakes();
			gc.setFill(Color.BLACK);
			gc.setFont(new Font("Digital-7", 70));
			gc.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2);			
			score[0] = snakes.get(0).getScore();
			score[1] = snakes.get(1).getScore();
			return;
		}
	}

	private void drawBackground(GraphicsContext gc, int x, int y) {
		if ((x + y) % 2 == 0) {
			gc.setFill(Color.web("AAD751"));
		} else {
			gc.setFill(Color.web("A2D149"));
		}
		gc.fillRect(x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
	}

	private void drawSnake(GraphicsContext gc, EColor color, int x, int y) {
		Paint p = Color.BLACK;
		int rad=20;
		switch (color) {
		case blue:
			p = Color.BLUE;
			break;
		case cyan:
			p = Color.	AQUA;
			break;
		case red:
			p = Color.RED;
			break;
		case orange:
			p = Color.ORANGE;
			break;
		case head:
			p = Color.GRAY;
		default:
			break;
		}
		gc.setFill(p);
		gc.fillRoundRect(x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, rad, rad);
	}

	private void drawScore(String str, int x, int y,int index) {
		gc.setFill(Color.WHITE);
		gc.setFont(new Font("Digital-7", 25));
		gc.fillText(str, x, y);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
