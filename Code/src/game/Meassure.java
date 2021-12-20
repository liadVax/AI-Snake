package game;

import java.util.ArrayList;

public class Meassure {

	private static ArrayList<Integer[]> scoreListRRRR = new ArrayList<>();
	private static ArrayList<Integer[]> scoreListRRAG = new ArrayList<>();
	private static ArrayList<Integer[]> scoreListMRAG = new ArrayList<>();
	private static ArrayList<Integer[]> scoreListMMAG = new ArrayList<>();
	private static ArrayList<Integer[]> scoreListMMAA = new ArrayList<>();
	private static ArrayList<Integer[]> scoreListMMGG = new ArrayList<>();
	private static int depth=3;
	private static int runningGames=50;
	private static int distanceFunc;

	public static void main(String[] args) {
		String[] configNames = { "RRRR", "RRAG", "MRAG", "MMAG", "MMAA", "MMGG" };
		int strInx = 0;
		distanceFunc = Ai.EUQLIDIAIN;
		for (int j = 1; j < 3; j++) {
			System.out.println(measureString());
			//System.out.println(configNames[strInx++]);
			for (int i = 0; i < runningGames; i++) {
				Board myBoard = new Board();
				scoreListRRRR.add(myBoard.runGame(Ai.RANDOM, Ai.RANDOM, Ai.RANDOM, Ai.RANDOM, depth, distanceFunc));
				Snake.resetIndex();
			}
			//System.out.println(configNames[strInx++]);
			for (int i = 0; i < runningGames; i++) {
				Board myBoard = new Board();
				scoreListRRAG.add(myBoard.runGame(Ai.RANDOM, Ai.RANDOM, Ai.ASTAR, Ai.GREEDY, depth, distanceFunc));
				Snake.resetIndex();
			}

			//System.out.println(configNames[strInx++]);
			for (int i = 0; i < runningGames; i++) {
				Board myBoard = new Board();
				scoreListMRAG.add(myBoard.runGame(Ai.MINIMAX, Ai.RANDOM, Ai.ASTAR, Ai.GREEDY, depth, distanceFunc));
				Snake.resetIndex();
			}
			//System.out.println(configNames[strInx++]);
			for (int i = 0; i < runningGames; i++) {
				Board myBoard = new Board();
				scoreListMMAG.add(myBoard.runGame(Ai.MINIMAX, Ai.MINIMAX, Ai.ASTAR, Ai.GREEDY, depth, distanceFunc));
				Snake.resetIndex();
			}
			//System.out.println(configNames[strInx++]);
			for (int i = 0; i < runningGames; i++) {
				Board myBoard = new Board();
				scoreListMMAA.add(myBoard.runGame(Ai.MINIMAX, Ai.MINIMAX, Ai.ASTAR, Ai.ASTAR, depth, distanceFunc));
				Snake.resetIndex();
			}
			//System.out.println(configNames[strInx++]);
			for (int i = 0; i < runningGames; i++) {
				Board myBoard = new Board();
				if (i == 29)
					myBoard.printFlag = false;
				scoreListMMGG.add(myBoard.runGame(Ai.MINIMAX, Ai.MINIMAX, Ai.GREEDY, Ai.GREEDY, depth, distanceFunc));
				Snake.resetIndex();
			}

			ArrayList<ArrayList<Integer[]>> totalScore = new ArrayList<>();
			totalScore.add(scoreListRRRR);
			totalScore.add(scoreListRRAG);
			totalScore.add(scoreListMRAG);
			totalScore.add(scoreListMMAG);
			totalScore.add(scoreListMMAA);
			totalScore.add(scoreListMMGG);
			strInx = 0;

			for (ArrayList<Integer[]> tarr : totalScore) {
				if (tarr.size() > 0) {
					int sum = 0;
					System.out.println("Scores for " + configNames[strInx]);
					//int game = 0;
					for (Integer[] arr : tarr) {
						//System.out.println("[" + game + "]" + "Score B = " + arr[0] + " , " + "Score C = " + arr[1]);
						//game++;
						sum = sum + arr[0] + arr[1];
					}
					System.out.println("AVG=" + sum / runningGames);
					System.out.println();
					strInx++;
					tarr.clear();
				}
			}
			totalScore.clear();
			distanceFunc = Ai.MANHATAN;
			strInx = 0;
		}
	}

	private static String measureString() {
		String breakString = "=================================================================";
		String distFunc = distanceFunc == 4 ? "EUQLIDIAIN" : "MANHATAN";
		return breakString + "\nStarting measure run with\n-Running games : " + runningGames + "\n-Depth : " + depth
				+ "\n-Distance Function : " + distFunc;

	}
}
