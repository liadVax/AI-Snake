package game;

import java.util.ArrayList;

public class Ai {
	public static final int RANDOM = 0;
	public static final int MINIMAX = 1;
	public static final int ASTAR = 2;
	public static final int GREEDY = 3;
	public static final int EUQLIDIAIN = 4;
	public static final int MANHATAN = 5;

	public static final int RED = 2;
	public static final int ORANGE = 3;
	static private int runningIndex;
	static public ArrayList<Direction> myDirections = new ArrayList<>();
	static public int invalid = 0;

////---- Start of Greedy Algo
	// orange will be the greedy snake
	static public Direction greedy(Snake orangeSnake, Board gameBoard, int distanceFunc) {
		Snake blueSnake = gameBoard.getSnakes().get(0);
		Snake cyanSnake = gameBoard.getSnakes().get(1);
		Snake copySnake;
		myDirections.clear();
		myDirections.add(Direction.down);
		myDirections.add(Direction.up);
		myDirections.add(Direction.left);
		myDirections.add(Direction.right);
		double minDistance = 999;
		double closestDis = 999;
		Direction resDir = Direction.none;
		double dist1, dist2;

		for (Direction dir : myDirections) {
			if (!isInvalidMove(dir, orangeSnake)) {
				copySnake = new Snake(orangeSnake);
				copySnake.move(dir);
				if (blueSnake.isAlive() && cyanSnake.isAlive()) {
					dist1 = distanceFunctionsHub(distanceFunc, copySnake.getHead(), blueSnake.getHead());
					dist2 = distanceFunctionsHub(distanceFunc, copySnake.getHead(), cyanSnake.getHead());
					/*
					 * dist1 = calculateEuqlidiainDistance(copySnake.getHead(),
					 * blueSnake.getHead()); dist2 =
					 * calculateEuqlidiainDistance(copySnake.getHead(), cyanSnake.getHead());
					 */
					closestDis = Math.min(dist1, dist2);
				}
				if (!blueSnake.isAlive() && cyanSnake.isAlive()) {
					closestDis = distanceFunctionsHub(distanceFunc, copySnake.getHead(), cyanSnake.getHead());
				}
				if (blueSnake.isAlive() && !cyanSnake.isAlive()) {
					closestDis = distanceFunctionsHub(distanceFunc, copySnake.getHead(), blueSnake.getHead());
				}
				if (!blueSnake.isAlive() && !cyanSnake.isAlive()) {
//					System.out.println("All died do random");
					return Direction.none;
				}
//				System.out.println("closestDis = " + closestDis +" direction = " + dir);
				if (closestDis < minDistance) {
					minDistance = closestDis;
					resDir = dir;
				}
			}
		}
		// System.out.println("Chose = " + minDistance +" direction = " + resDir);
		return resDir;
	}

////////--- End of Greedy Algo

////--- Start of Min-Max Algo

	// wrapper function to minMax
	static public Direction minMax(Snake agentSnake, Board gameBoard, int depth, int index, int distanceFunc) {
		runningIndex = index;
		myDirections.clear();
		myDirections.add(Direction.down);
		myDirections.add(Direction.up);
		myDirections.add(Direction.left);
		myDirections.add(Direction.right);
		ArrayList<Snake> copySnakes = copySnakes(gameBoard.getSnakes());
		RecReulst sol = algoMinMax(agentSnake.getHead(), depth, true, copySnakes, 0, distanceFunc);
		return sol.dir;
	}

	// current position at the first call is the snake head
	static private RecReulst algoMinMax(Square AgentHead, int depth, boolean isMax, ArrayList<Snake> snakes, int index,
			int distanceFunc) {
		RecReulst finalResult = new RecReulst(-1, Direction.none);
		if (depth == 0 || evaluationFunction(AgentHead, snakes, distanceFunc) == 0) {
			double endEval = evaluationFunction(AgentHead, snakes, distanceFunc);
			// System.out.println("Pos="+currentPosition+"\neval= "+endEval);
			// System.out.println("eval= "+endEval );
			return new RecReulst(endEval, Direction.none);
		}
		// System.out.println("[dp = "+ depth + "] color = "+
		// snakes.get(index).getSnakeColor());
		if (isMax) { // 0 or 1
			ArrayList<RecReulst> evalArr = new ArrayList<>();
			for (Direction dir : myDirections) {
				// move all snakes
				if (!isInvalidMove(dir, snakes.get(runningIndex))) {
					// System.out.println(snakes.get(index).getSnakeColor()+" move: "+dir);
					ArrayList<Snake> copySnakes = copySnakes(snakes);
					copySnakes.get(runningIndex).move(dir);
					// System.out.println("["+depth+"] color = "+
					// snakes.get(runningIndex).getSnakeColor() +" , Direction =" + dir);
					RecReulst tempScore = algoMinMax(copySnakes.get(runningIndex).getHead(), depth - 1, false,
							copySnakes, RED, distanceFunc);
					tempScore.dir = dir;
					evalArr.add(tempScore);
				}
			}
			if (evalArr.size() == 0) {
				return new RecReulst(-1, Direction.none);
			}
			finalResult = evalArr.get(0);
			for (int i = 1; i < evalArr.size(); i++) {
				if (evalArr.get(i).value > finalResult.value) {
					finalResult = evalArr.get(i);
				}
			}
			// System.out.println(" || Final Result : " + finalResult + " ||");
		} else {
			ArrayList<RecReulst> evalArr = new ArrayList<>();
			if (index == RED) {
				for (Direction dir : myDirections) {
					// calc new pos
					if (!isInvalidMove(dir, snakes.get(index))) {
						// System.out.println(snakes.get(index).getSnakeColor()+" move: "+dir);
						ArrayList<Snake> copySnakes = copySnakes(snakes);
						copySnakes.get(index).move(dir);
//						System.out.println("["+depth+"] color = "+ snakes.get(index).getSnakeColor() +" , Direction =" + dir);
						RecReulst tempScore = algoMinMax(copySnakes.get(runningIndex).getHead(), depth - 1, false,
								copySnakes, ORANGE, distanceFunc); // orange
						tempScore.dir = dir;
						evalArr.add(tempScore);
					}
				}
				if (evalArr.size() == 0) {
					return new RecReulst(-1, Direction.none);
				}
				finalResult = evalArr.get(0);
				for (int i = 1; i < evalArr.size(); i++) {
					if (evalArr.get(i).value < finalResult.value) {
						finalResult = evalArr.get(i);
					}
				}
//				System.out.println(" || Final Result : " + finalResult + " ||");
				// finalResult = recResultEnemy;
			}

			if (index == ORANGE) {
				for (Direction dir : myDirections) {
					if (!isInvalidMove(dir, snakes.get(index))) { // calc new pos
						ArrayList<Snake> copySnakes = copySnakes(snakes);
						snakes.get(index).move(dir);
//						System.out.println("["+depth+"] color = "+ snakes.get(index).getSnakeColor() +" , Direction =" + dir);
						RecReulst tempScore = algoMinMax(copySnakes.get(runningIndex).getHead(), depth - 1, true,
								copySnakes, runningIndex, distanceFunc); // orange
						tempScore.dir = dir;
						evalArr.add(tempScore);
					}
				}
				if (evalArr.size() == 0) {
					return new RecReulst(-1, Direction.none);
				}
				finalResult = evalArr.get(0);
				for (int i = 1; i < evalArr.size(); i++) {
					if (evalArr.get(i).value < finalResult.value) {
						finalResult = evalArr.get(i);
					}
				}
//				System.out.println(" || Final Result : " + finalResult + " ||");
			}
		}
		return finalResult;
	}

//// --- End of Min-Max Algo

	static private boolean isInvalidMove(Direction dir, Snake snake) {
		return isOutOfBorder(dir, snake.getHead()) || isCanibal(snake.getHead(), dir, snake);

	}

	private static boolean isCanibal(Square head, Direction dir, Snake snake) {
		boolean ret = false;
		Square newHead = new Square(head.getX(), head.getY(), snake.getSnakeColor());
		switch (dir) {
		case up:
			newHead.setSquare(newHead.getX() - 1, newHead.getY(), newHead.getColor());
			break;
		case down:
			newHead.setSquare(newHead.getX() + 1, newHead.getY(), newHead.getColor());
			break;

		case left:
			newHead.setSquare(newHead.getX(), newHead.getY() - 1, newHead.getColor());
			break;
		case right:
			newHead.setSquare(newHead.getX(), newHead.getY() + 1, newHead.getColor());
			break;
		default:
			break;
		}
		for (int i = 0; i < snake.getSnake().length; i++) {
			if (newHead.equals(snake.getSnake()[i])
					|| (snake.getPartnerSnake().isAlive() && newHead.equals(snake.getPartnerSnake().getSnake()[i])))
				ret = true;
		}
		return ret;
	}

	static private boolean isOutOfBorder(Direction dir, Square head) {
		boolean ret = false;
		int headX = head.getX();
		int headY = head.getY();
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

	static private ArrayList<Snake> copySnakes(ArrayList<Snake> snakes) {
		ArrayList<Snake> copySnakes = new ArrayList<>();
		for (Snake snake : snakes) {
			copySnakes.add(new Snake(snake));
		}
		for (Snake snake : copySnakes) {
			snake.leagalMove();
		}

		return copySnakes;
	}

//// ~~~ Start of A* Algo

	static public Direction aStar(Snake enemySnake, Board board, int distanceFunc) {
		// System.out.println("Snake (" + enemySnake.getSnakeColor() + ") doing A*");
		ArrayList<Snake> snakes = board.getSnakes(); // agents snake are [indexes=0,1]

		if (!snakes.get(0).isAlive() && !snakes.get(1).isAlive()) {
			// System.out.println("Snake (" + enemySnake.getSnakeColor() + ") do random move
			// cuase all dead");
			return null;
		}
		Square target = null;
		double min = 10000;
		double dist;
		for (int i = 0; i < 2; ++i) {
			if (snakes.get(i).isAlive()) {
				for (int j = 0; j < Snake.SNAKE_SIZE; ++j) {
					dist = distanceFunctionsHub(distanceFunc, enemySnake.getHead(), snakes.get(i).getSnake()[j]);
					// dist = calculateEuqlidiainDistance(enemySnake.getHead(),
					// snakes.get(i).getSnake()[j]);
					if (min > dist) {
						min = dist;
						target = snakes.get(i).getSnake()[0];
					}
				}
			}
		}

		ArrayList<Square> path = algoAStar(enemySnake.getHead(), target, board, distanceFunc);
		if (path == null) {
			// System.out.println("No sulotion" + "Snake (" + enemySnake.getSnakeColor()
			// +")");
			return null;
		}
		try {
			return getDirection((Square) path.toArray()[0], (Square) path.toArray()[1]);
		} catch (Exception e) {
		}
		return null;

	}

	private static ArrayList<Square> algoAStar(Square enemySnake, Square agentSnake, Board board, int distanceFunc) {
		resetBoardFValues(board);
		ArrayList<Square> openNodes = new ArrayList<>();
		ArrayList<Square> closedNodes = new ArrayList<>();
		ArrayList<Square> neighborNodes = new ArrayList<>();
		ArrayList<Square> path = new ArrayList<>();
		Square currentNode;
		double newPath;

		openNodes.add(enemySnake);

		while (!openNodes.isEmpty()) {
			currentNode = getLowestFCostNode(openNodes, enemySnake, agentSnake);
			path.add(currentNode);
			openNodes.remove(currentNode);
			closedNodes.add(currentNode);
			// reach for target
			if ((currentNode.getX() == agentSnake.getX()) && (currentNode.getY() == agentSnake.getY())) {
				return path;
			}
			// set the neighborNodes of currnetNode
			updateNeighbors(neighborNodes, currentNode, board, closedNodes, openNodes, agentSnake);

			for (Square node : neighborNodes) {
				newPath = calcF(currentNode, agentSnake, node, distanceFunc);
				if (!openNodes.contains(node) || node.getfCost() > newPath) {
					node.setfCost(newPath);
					if (!openNodes.contains(node) && !closedNodes.contains(node)) {
						openNodes.add(node);
					}
				}
			} // end forEach
		} // end while
		return null;
	}

	private static Direction getDirection(Square start, Square next) {
		if (start.getX() - 1 == next.getX())
			return Direction.up;
		if (start.getX() + 1 == next.getX())
			return Direction.down;

		if (start.getY() - 1 == next.getY())
			return Direction.left;
		if (start.getY() + 1 == next.getY())
			return Direction.right;

		return Direction.down;

	}

	// reset all previous calculated FCosts
	private static void resetBoardFValues(Board board) {
		for (int i = 0; i < 13; ++i) {
			for (int j = 0; i < 13; ++i) {
				board.getBoard()[i][j].setfCost(10000);
			}
		}
	}

	private static void updateNeighbors(ArrayList<Square> neighborNodes, Square currentNode, Board board,
			ArrayList<Square> closedNodes, ArrayList<Square> openNodes, Square targetHead) {
		neighborNodes.clear();
		// check borders
		if (currentNode.getX() - 1 >= 0
				&& isValidSquare(board.getBoard()[currentNode.getX() - 1][currentNode.getY()], board, closedNodes)) {
			neighborNodes.add(board.getBoard()[currentNode.getX() - 1][currentNode.getY()]);
		}
		if (currentNode.getX() + 1 < 13
				&& isValidSquare(board.getBoard()[currentNode.getX() + 1][currentNode.getY()], board, closedNodes)) {
			neighborNodes.add(board.getBoard()[currentNode.getX() + 1][currentNode.getY()]);
		}
		if (currentNode.getY() - 1 >= 0
				&& isValidSquare(board.getBoard()[currentNode.getX()][currentNode.getY() - 1], board, closedNodes)) {
			neighborNodes.add(board.getBoard()[currentNode.getX()][currentNode.getY() - 1]);
		}
		if (currentNode.getY() + 1 < 13
				&& isValidSquare(board.getBoard()[currentNode.getX()][currentNode.getY() + 1], board, closedNodes)) {
			neighborNodes.add(board.getBoard()[currentNode.getX()][currentNode.getY() + 1]);
		}

	}

	private static boolean isValidSquare(Square sq, Board board, ArrayList<Square> closedNodes) {
		if (closedNodes.contains(sq)) {
			return false;
		}
		if (sq.getColor() == EColor.red || sq.getColor() == EColor.orange) {
			return false;
		}
		if (sq.getColor() == EColor.head && isHeadOfEnemy(sq, board)) {

			return false;
		}
		return true;
	}

	private static boolean isHeadOfEnemy(Square sq, Board board) {
		Square snakeHead;
		for (int i = 0; i < 4; ++i) {
			if (board.getSnakes().get(i).getSnakeColor() == EColor.orange
					|| board.getSnakes().get(i).getSnakeColor() == EColor.red) {
				snakeHead = board.getSnakes().get(i).getHead();
				if (sq.getX() == snakeHead.getX() && sq.getY() == snakeHead.getY()) {
					// System.out.println("found enemy head at (" + sq.getX() + "," + sq.getY()
					// +")");
					return true;
				}
			}
		}
		return false;
	}

//search for the lowest FCost
	private static Square getLowestFCostNode(ArrayList<Square> nodes, Square start, Square target) {
		Square res = nodes.get(0);
		double min = nodes.get(0).getfCost();
		for (Square node : nodes) {
			if (min > node.getfCost()) {
				res = node;
				min = node.getfCost();
			}
		}
		return res;
	}

	private static double calcF(Square start, Square target, Square currentPosition, int distanceFunc)// total value =
																										// G+H
	{
		return calcG(currentPosition, start, distanceFunc) + calcH(currentPosition, target, distanceFunc);
	}

	private static double calcH(Square currentPosition, Square target, int distanceFunc)// distance to the target point
	{
		return distanceFunctionsHub(distanceFunc, currentPosition, target);
	}

	private static double calcG(Square currentPosition, Square start, int distanceFunc) // distance from the starting
																						// point
	{
		return distanceFunctionsHub(distanceFunc, currentPosition, start);
	}

////~~~ End of A* Algo

	// support functions
	private static double evaluationFunction(Square currnetPosition, ArrayList<Snake> snakes, int distanceFunc) {
		Snake enemy1 = snakes.get(RED);
		Snake enemy2 = snakes.get(ORANGE);
		double dist1 = calculateAverageDistance(currnetPosition, enemy1.getSnake(), distanceFunc);
		double dist2 = calculateAverageDistance(currnetPosition, enemy2.getSnake(), distanceFunc);
		double distEnemies = distanceFunctionsHub(distanceFunc, enemy1.getHead(), enemy2.getHead());
		if (distEnemies < 4) {
			return (dist1 + dist2) / 2;
		}
		return Math.min(dist1, dist2);
	}

	private static double calculateAverageDistance(Square currnetPosition, Square[] snake, int distanceFunc) {
		double sum = 0;
		for (int i = 0; i < snake.length; i++)
			sum += distanceFunctionsHub(distanceFunc, currnetPosition, snake[i]);
		return sum;
	}

	private static double calculateEuqlidiainDistance(Square sq1, Square sq2) {
		double distanceX = Math.abs(sq1.getX() - sq2.getX());
		double distanceY = Math.abs(sq1.getY() - sq2.getY());
		double ret = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
		return ret;
	}

	private static double calcManhatanDistance(Square sq1, Square sq2) {
		return Math.abs(sq1.getX() - sq2.getX()) + Math.abs(sq1.getY() - sq2.getY());
	}

	private static double distanceFunctionsHub(int evalFunc, Square sq1, Square sq2) {
		if (evalFunc == EUQLIDIAIN) {
			return calculateEuqlidiainDistance(sq1, sq2);
		} else if (evalFunc == MANHATAN) {
			return calcManhatanDistance(sq1, sq2);
		}
		return -1;
	}
}
