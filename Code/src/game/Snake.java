package game;

import java.util.ArrayList;
import java.util.Random;

public class Snake {
	static final int SNAKE_SIZE = 3;
	final int stuckLimit = 10;

	private static int index = 0;
	private boolean alive = true;
	private int score = 0;
	private int imStuck = 0;
	private Square[] snake = new Square[SNAKE_SIZE];
	private EColor snakeColor;
	private Direction headDir;
	private Random myRand=new Random();
	private Snake partnerSnake = null;
	private ArrayList<Direction> moveSet = new ArrayList<>();
	private ArrayList<Direction> leagalMoveSet = new ArrayList<>();

	public static void resetIndex() {
		index = 0;
	}

	public Snake(EColor color, Square[][] gameBoard) {
		snakeColor = color;
		moveSet.add(Direction.down);
		moveSet.add(Direction.up);
		moveSet.add(Direction.left);
		moveSet.add(Direction.right);
		int inc = myRand.nextInt(3);
		int x = -1;
		int y = -1;
		switch (index) {
		case 0:
			x = 3;
			y = 0 + inc;
			headDir = Direction.down;
			break;
		case 1:
			x = 3;
			y = 10 + inc;
			headDir = Direction.down;
			break;
		case 2:
			x = 9;
			y = 0 + inc;
			headDir = Direction.up;
			break;
		case 3:
			x = 9;
			y = 10 + inc;
			headDir = Direction.up;
			break;
		default:
			break;
		}
		buildSnake(color, headDir, x, y);
		index++;
	}

	public Snake(Snake targetSnake) {
		Square sq;
		for (int i = 0; i < targetSnake.getSnake().length; i++) {
			sq = targetSnake.getSnake()[i];
			snake[i] = new Square(sq.getX(), sq.getY(), sq.getColor());
		}
		this.snakeColor = targetSnake.getSnakeColor();
		this.headDir = targetSnake.getHeadDir();
		moveSet.add(Direction.down);
		moveSet.add(Direction.up);
		moveSet.add(Direction.left);
		moveSet.add(Direction.right);
		partnerSnake = targetSnake.getPartnerSnake();
	}

	public void buildSnake(EColor color, Direction dir, int x, int y) {
		int incX=0;
		switch (dir) {
		case up:
			incX=1;
			break;
		case down:
			incX=-1;
			break;
		default:
			break;
		}
		for(int i=0;i<SNAKE_SIZE;i++)
		{
			snake[i]=new Square(x+i*incX, y, color);
		}
	}

	public void leagalMove() {
		Direction illegalMove = null;
		switch (headDir) {
		case up:
			illegalMove = Direction.down;
			break;
		case down:
			illegalMove = Direction.up;
			break;
		case left:
			illegalMove = Direction.right;
			break;
		case right:
			illegalMove = Direction.left;
			break;
		default:
			break;
		}
		leagalMoveSet.clear();
		for (Direction dir : moveSet) {
			if (dir != illegalMove && !wallHit(dir) && !partnerHit(dir))
				leagalMoveSet.add(dir);
		}
	}

	public boolean partnerHit(Direction dir) {
		if (partnerSnake.isAlive()) {
			int headX = snake[0].getX();
			int headY = snake[0].getY();
			switch (dir) {
			case up:
				headX = headX - 1;
				break;
			case down:
				headX = headX + 1;
				break;
			case left:
				headY = headY - 1;
				break;
			case right:
				headY = headY + 1;
				break;
			}
			Square movedHead = new Square(headX, headY, snakeColor);
			for (Square body : partnerSnake.getSnake()) {
				if (movedHead.equals(body))
					return true;
			}
		}
		return false;
	}

	public boolean wallHit(Direction dir) {
		boolean ret = false;
		int headX = snake[0].getX();
		int headY = snake[0].getY();
		switch (dir) {
		case up:
			if (headX - 1 < 0)
				ret = true;
			break;
		case down:
			if (headX + 1 > 12)
				ret = true;
			break;
		case left:
			if (headY - 1 < 0)
				ret = true;
			break;
		case right:
			if (headY + 1 > 12)
				ret = true;
			break;
		default:
			break;
		}
		return ret;
	}

	public void move(Direction dir) {
		int[] cords = snake[0].getSquare();
		if (headDir != Direction.down && dir == Direction.up) {
			updateSnake(dir, -1, 0);
			snake[0] = new Square(--cords[0], cords[1], snakeColor);
		} else if (headDir != Direction.up && dir == Direction.down) {
			updateSnake(dir, 1, 0);
			snake[0] = new Square(++cords[0], cords[1], snakeColor);
		} else if (headDir != Direction.right && dir == Direction.left) {
			updateSnake(dir, 0, -1);
			snake[0] = new Square(cords[0], --cords[1], snakeColor);
		} else if (headDir != Direction.left && dir == Direction.right) {
			updateSnake(dir, 0, 1);
			snake[0] = new Square(cords[0], ++cords[1], snakeColor);
		} else if (dir == Direction.none) {
			System.out.println("Snake color  = " + this.getSnakeColor() + " doing none move");
		}
	}

	public void moveByAlgo(int index, Board board,int distanceFunc) {
		if (alive) {
			if (index == 0 || index == 1)// agent snake Min Max
			{
				updateByAlgo(Ai.minMax(this, board, 3, index,distanceFunc));
			}
			if (index == 2) // enemy snake A*
			{
				updateByAlgo( Ai.aStar(this, board,distanceFunc));
			}
			if (index == 3) // greedy
			{
				updateByAlgo( Ai.greedy(this, board,distanceFunc));
			}
		}

	}

	public void moveByAlgoWithConfig(int index, int Ag1Alg, int Ag2Alg, int En1Alg, int En2Alg, int depth,
			Board board,int distanceFunc) {
		if (alive) {
			switch (snakeColor) {
			case blue:
				if (Ag1Alg == Ai.RANDOM) {
					moveRandom();
				} else if (Ag1Alg == Ai.MINIMAX) {
					updateByAlgo(Ai.minMax(this, board, depth, index,distanceFunc));
				}
				break;
			case cyan:
				if (Ag2Alg == Ai.RANDOM) {
					moveRandom();
				} else if (Ag2Alg == Ai.MINIMAX) {
					updateByAlgo(Ai.minMax(this, board, depth, index,distanceFunc));
				}
				break;
			case red:
				if (En1Alg == Ai.ASTAR) {
					updateByAlgo( Ai.aStar(this, board,distanceFunc));
				} else if (En1Alg == Ai.GREEDY) {
					updateByAlgo( Ai.greedy(this, board,distanceFunc));
				} else if (En2Alg == Ai.RANDOM) {
					moveRandom();
				}
				break;
			case orange:
				if (En2Alg == Ai.ASTAR) {
					updateByAlgo( Ai.aStar(this, board,distanceFunc));
				} else if (En2Alg == Ai.GREEDY) {
					updateByAlgo( Ai.greedy(this, board,distanceFunc));
				} else if (En2Alg == Ai.RANDOM) {
					moveRandom();
				}
				break;
			default:
				break;
			}
		}

	}

	private void updateByAlgo(Direction dir) {
		if (dir == Direction.up) {
			updateSnake(dir, -1, 0);
		} else if (dir == Direction.down) {
			updateSnake(dir, 1, 0);
		} else if (dir == Direction.left) {
			updateSnake(dir, 0, -1);
		} else if (dir == Direction.right) {
			updateSnake(dir, 0, 1);
		} else if(dir==Direction.none || dir==null){
			moveRandom();
		}
	}

	public void moveRandom() {
		leagalMove();
		if (leagalMoveSet.size() > 0) {
			Direction dir = leagalMoveSet.get(myRand.nextInt(leagalMoveSet.size()));
			if (dir == Direction.up) {
				updateSnake(dir, -1, 0);
			} else if (dir == Direction.down) {
				updateSnake(dir, 1, 0);
			} else if (dir == Direction.left) {
				updateSnake(dir, 0, -1);
			} else if (dir == Direction.right) {
				updateSnake(dir, 0, 1);
			}
			imStuck = 0;
		} else {
			if (imStuck >= stuckLimit) {
				alive = false;
				System.out.println(snakeColor.StringFullName() + " Snake is STUCK so he died from boredom, RIP");
			}
			imStuck++;
		}
	}

	public void setImStuck(int imStuck) {
		this.imStuck = imStuck;
	}

	public void updateSnake(Direction dir, int xInc, int yInc) {
		for (int i = SNAKE_SIZE - 1; i > 0; i--) {
			snake[i] = snake[i - 1];
		}
		snake[0] = new Square(snake[0].getX() + xInc, snake[0].getY() + yInc, snakeColor);
		headDir = dir;
		score++;
	}

	public Square[] getSnake() {
		return snake;
	}

	public Square getHead() {
		return snake[0];
	}

	public EColor getSnakeColor() {
		return snakeColor;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public ArrayList<Direction> getLeagalMoveSet() {
		return leagalMoveSet;
	}

	public int getScore() {
		return score;
	}

	public Snake getPartnerSnake() {
		return partnerSnake;
	}

	public void setPartnerSnake(Snake partnerSnake) {
		this.partnerSnake = partnerSnake;
	}

	public Direction getHeadDir() {
		return headDir;
	}
	
	@Override
	public String toString() {
		String str = snakeColor.StringFullName() + ": ";
		for (int i = 0; i < snake.length; i++) {
			str += snake[i].toString();
		}
		return str;
	}
}
