package newCode;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class New21 {
	static ArrayList<Integer> cards = new ArrayList<Integer>(52);
	static ArrayList<Integer> currentCards = new ArrayList<Integer>();

	static int playerCardTotal;
	static int CPUCardTotal;
	static ArrayList<Integer> playerCards = new ArrayList<Integer>();
	static ArrayList<Integer> CPUCards = new ArrayList<Integer>();

	static int playerScore;
	static int CPUScore;

	JLabel label1 = new JLabel();
	static JLabel label2 = new JLabel();
	static JLabel label3 = new JLabel();
	static JLabel label4 = new JLabel();
	static JButton button1 = new JButton("Hit");
	static JButton button2 = new JButton("Stand");
	static JButton button3 = new JButton("Next Round");
	JButton button4 = new JButton("End");
	JFrame frame = new JFrame("New 21");

	String diff;

	public New21() {
		playerCardTotal = 0;
		CPUCardTotal = 0;
		playerScore = 0;
		CPUScore = 0;
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 300);
		frame.setLayout(new GridLayout(8, 1));

		label1.setText("Try to beat your opponent without going over 21. Busts lose 1 point. Wins gain 1 point");
		label1.setHorizontalTextPosition(JLabel.CENTER);
		label1.setHorizontalAlignment(JLabel.CENTER);

		label2.setText("");
		label2.setHorizontalTextPosition(JLabel.CENTER);
		label2.setHorizontalAlignment(JLabel.CENTER);

		label3.setText("");
		label3.setHorizontalTextPosition(JLabel.CENTER);
		label3.setHorizontalAlignment(JLabel.CENTER);

		label4.setText("Make your move");
		label4.setHorizontalTextPosition(JLabel.CENTER);
		label4.setHorizontalAlignment(JLabel.CENTER);

		frame.add(label1);
		frame.add(label2);
		frame.add(label3);
		frame.add(label4);
		frame.add(button1);
		frame.add(button2);
		frame.add(button3);
		frame.add(button4);
		frame.setVisible(true);

	}

	void prepAndStart(String diff) {
		this.diff = diff;
		if (diff.equals("Easy"))
			frame.getContentPane().setBackground(Color.GREEN);
		else
			frame.getContentPane().setBackground(Color.RED);

		startGame();

		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Random randomNum = new Random();
				int cardSpot = randomNum.nextInt(currentCards.size());
				playerCards.add(currentCards.get(cardSpot));
				updateCardScore("Player");
				currentCards.remove(cardSpot);
				label2.setText("Your Card Score: " + playerCardTotal + ". Your Cards: " + playerCards
						+ ". Your Overall Score: " + playerScore);
				if (playerCardTotal > 21) {
					playerCardTotal = -1;
					playerScore--;
					if (diff.equals("Easy"))
						dumbCPU();
					else
						smartCPU();
					result();
				} else if (playerCardTotal == 21) {
					if (diff.equals("Easy"))
						dumbCPU();
					else
						smartCPU();
					result();
				}

			}

		});

		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (diff.equals("Easy"))
					dumbCPU();
				else
					smartCPU();
				result();
			}

		});

		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextGame();
			}

		});

		button4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int finalScore = playerScore - CPUScore;
				HighScores scores = new HighScores();
				try {
					scores.addScore(finalScore, diff);
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
				try {
					scores.HighScoreBox(diff);
				} catch (ClassNotFoundException | IOException | InterruptedException e1) {
					e1.printStackTrace();
				}
				frame.dispose();
			}

		});

	}

	static void startGame() {
		button3.setEnabled(false);
		setOriginalCards();
		resetCards();
		distributeCards();
		label2.setText("Your Card Score: " + playerCardTotal + ". Your Cards: " + playerCards + ". Your Overall Score: "
				+ playerScore);
		label3.setText(
				"CPU Card Score " + CPUCardTotal + ". CPU Cards: " + CPUCards + ". CPU Overall Score: " + CPUScore);
	}

	static void nextGame() {
		button3.setEnabled(false);
		button1.setEnabled(true);
		button2.setEnabled(true);
		if (currentCards.size() <= 10)
			resetCards();

		distributeCards();
		label2.setText("Your Card Score: " + playerCardTotal + ". Your Cards: " + playerCards + ". Your Overall Score: "
				+ playerScore);
		label3.setText(
				"CPU Card Score " + CPUCardTotal + ". CPU Cards: " + CPUCards + ". CPU Overall Score: " + CPUScore);
		label4.setText("Make your move");

	}

	public static void dumbCPU() {
		button1.setEnabled(false);
		button2.setEnabled(false);
		label4.setText("CPU's Turn");
		while (CPUCardTotal < 17 || CPUCardTotal < playerCardTotal) {
			Random randomNum = new Random();
			int cardSpot = randomNum.nextInt(currentCards.size());
			CPUCards.add(currentCards.get(cardSpot));
			updateCardScore("CPU");
			currentCards.remove(cardSpot);
			label3.setText(
					"CPU Card Score " + CPUCardTotal + ". CPU Cards: " + CPUCards + ". CPU Overall Score: " + CPUScore);
			if (CPUCardTotal > 21) {
				CPUCardTotal = -1;
				CPUScore--;
				break;
			} else if (CPUCardTotal == 21)
				break;

		}
	}

	public static void smartCPU() {
		button1.setEnabled(false);
		button2.setEnabled(false);
		int howManyAway;
		while (true) {
			double favorTotal = 0;
			if (CPUCardTotal > playerCardTotal)
				break;

			howManyAway = 21 - CPUCardTotal;
			for (int i = 0; i < currentCards.size(); i++) {
				if (currentCards.get(i) <= howManyAway)
					favorTotal++;

			}
			double favorProb = favorTotal / (double) currentCards.size();
			if (favorProb >= .55) {
				Random randomNum = new Random();
				int cardSpot = randomNum.nextInt(currentCards.size());
				CPUCards.add(currentCards.get(cardSpot));
				updateCardScore("CPU");
				currentCards.remove(cardSpot);
			} else
				break;

			if (CPUCardTotal > 21) {
				CPUCardTotal = -1;
				CPUScore--;
				break;
			} else if (CPUCardTotal == 21)
				break;

		}
	}

	static void setOriginalCards() {
		for (int i = 0; i < 4; i++) {
			for (int j = 1; j < 14; j++) {
				if (j == 11 || j == 12 || j == 13)
					cards.add(10);
				else
					cards.add(j);

			}
		}
	}

	static void resetCards() {
		currentCards.clear();
		for (int i = 0; i < 52; i++)
			currentCards.add(cards.get(i));

	}

	static void updateCardScore(String turn) {
		if (turn.equals("Player")) {
			playerCardTotal = 0;
			for (int i = 0; i < playerCards.size(); i++)
				playerCardTotal += playerCards.get(i);

		} else {
			CPUCardTotal = 0;
			for (int i = 0; i < CPUCards.size(); i++)
				CPUCardTotal += CPUCards.get(i);

		}
	}

	static void distributeCards() {
		playerCards.clear();
		playerCardTotal = 0;
		CPUCards.clear();
		CPUCardTotal = 0;
		Random randomNum = new Random();
		int cardSpot = randomNum.nextInt(currentCards.size());
		playerCards.add(currentCards.get(cardSpot));
		playerCardTotal = playerCards.get(0);
		currentCards.remove(cardSpot);
		cardSpot = randomNum.nextInt(currentCards.size());
		CPUCards.add(currentCards.get(cardSpot));
		CPUCardTotal = CPUCards.get(0);
		currentCards.remove(cardSpot);

	}

	public static void result() {
		if (playerCardTotal > CPUCardTotal) {
			label4.setText("Player Wins");
			playerScore++;
		} else if (playerCardTotal < CPUCardTotal) {
			label4.setText("CPU wins!");
			CPUScore++;
		} else
			label4.setText("Tie!");

		label2.setText("Your Card Score: " + playerCardTotal + ". Your Cards: " + playerCards + ". Your Overall Score: "
				+ playerScore);
		label3.setText(
				"CPU Card Score " + CPUCardTotal + ". CPU Cards: " + CPUCards + ". CPU Overall Score: " + CPUScore);
		button3.setEnabled(true);
	}
}
