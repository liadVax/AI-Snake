package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Board {
	private Square[][] gameBoard = new Square[13][13];
	private ArrayList<Snake> snakes = new ArrayList<>();
	public Integer[] score = { 0, 0 };
	public static boolean printFlag = false;

	public Board() {
		initBorad();
		ArrayList<Snake> Ag_snakes = new ArrayList<>();
		ArrayList<Snake> En_snakes = new ArrayList<>();
		EColor[] colArr = { EColor.blue, EColor.cyan, EColor.orange, EColor.red };
		List<EColor> intList = Arrays.asList(colArr);
		Collections.shuffle(intList);
		intList.toArray(colArr);
		for (int i = 0; i < 4; i++) {
			if (colArr[i].ordinal() <= 1) {
				Ag_snakes.add(new Snake(colArr[i], gameBoard));
			} else {
				En_snakes.add(new Snake(colArr[i], gameBoard));
			}
		}

		Ag_snakes.get(0).setPartnerSnake(Ag_snakes.get(1));
		Ag_snakes.get(1).setPartnerSnake(Ag_snakes.get(0));
		En_snakes.get(0).setPartnerSnake(En_snakes.get(1));
		En_snakes.get(1).setPartnerSnake(En_snakes.get(0));
		if (Ag_snakes.get(0).getSnakeColor() == EColor.blue) {
			snakes.add(Ag_snakes.get(0));
			snakes.add(Ag_snakes.get(1));
		} else {
			snakes.add(Ag_snakes.get(1));
			snakes.add(Ag_snakes.get(0));
		}
		if (En_snakes.get(0).getSnakeColor() == EColor.red) {
			snakes.add(En_snakes.get(0));
			snakes.add(En_snakes.get(1));
		} else {
			snakes.add(En_snakes.get(1));
			snakes.add(En_snakes.get(0));
		}
		 //snakes.get(1).setAlive(false);
		 //snakes.get(3).setAlive(false);
	}

	public void initBorad() {
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				gameBoard[i][j] = new Square(i, j, EColor.white);
			}
		}
	}

	public void addSnakeToBoard(Snake snake) {
		boolean head = true;
		for (Square s : snake.getSnake()) {
			if (head) {
				gameBoard[s.getX()][s.getY()].setColor(EColor.head);
				head = false;
			} else {
				gameBoard[s.getX()][s.getY()].setColor(s.getColor());
			}
		}
	}

	public void updateBorad() {
		initBorad();
		for (Snake s : snakes) {
			if (s.isAlive()) {
				addSnakeToBoard(s);
			}
		}
	}

	public boolean isBite(Snake enemy) {
		Square EnemyHead = enemy.getHead();
		for (int i = 0; i < 2; i++) {
			Snake agSnake = snakes.get(i);
			Square agHead = agSnake.getHead();
			if (agSnake.isAlive()) {
				for (Square body : agSnake.getSnake()) {
					if (EnemyHead.equals(body)) {
//						System.out.println("Enemy bite:" + agSnake);
						agSnake.setAlive(false);
						// removeSnakeFromBoard(agSnake);
						return true;
					}
				}
				for (Square body : enemy.getSnake()) {
					if (agHead.equals(body)) {
//						System.out.println("Agent bite: " + agSnake);
						agSnake.setAlive(false);
						// removeSnakeFromBoard(agSnake);
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean enemyWon() {
		return (!snakes.get(0).isAlive() && !snakes.get(1).isAlive());
	}
	/*
	 * public void removeSnakeFromBoard(Snake snake) { for (Square body :
	 * snake.getSnake()) {
	 * gameBoard[body.getX()][body.getY()].setColor(EColor.white); } }
	 */

	public Square[][] getBoard() {
		return gameBoard;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		EColor col;
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				col = gameBoard[i][j].getColor();
				sb.append("|" + col + "|");
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	public String getSnakeScore(int i) {
		Snake s = snakes.get(i);
		return s.getSnakeColor().StringFullName() + " Snake:" + s.getScore();
	}

	public ArrayList<Snake> getSnakes() {
		return snakes;
	}

	public Integer[] runGame(int Ag1Alg, int Ag2Alg, int En1Alg, int En2Alg, int depth,int distanceFunc) {
		// System.out.println("game number = "+gameNumber);
		// Board board = new Board();
		this.updateBorad();
		if (printFlag) {
			System.out.println(this);
		}
		int index = 0;
		while (!this.enemyWon()) {
			index = 0;
			for (Snake s : snakes) {
				if (s.isAlive()) {
					if (Ag1Alg == -1)
						s.moveByAlgo(index, this,distanceFunc);
					else
						s.moveByAlgoWithConfig(index, Ag1Alg, Ag2Alg, En1Alg, En2Alg, depth, this,distanceFunc);

					if (s.getSnakeColor() == EColor.red || s.getSnakeColor() == EColor.orange) {
						this.isBite(s);
					}
				}
				++index;
				this.updateBorad();
			}
			if (printFlag)
				System.out.println(this);

		}

		score[0] = snakes.get(0).getScore();
		score[1] = snakes.get(1).getScore();
		return score;
	}
}
