package newCode;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class HighScores implements Serializable {

	private static final long serialVersionUID = 1L;
	String name;
	int score;

	public ArrayList<HighScores> highScoreList;
	ArrayList<HighScores> sortedScores;

	JFrame scoreFrame = new JFrame();
	JLabel firstPlace = new JLabel();
	JLabel secondPlace = new JLabel();
	JLabel thirdPlace = new JLabel();
	JLabel scoringNote = new JLabel("Note: Scoring is the (Your score - CPU Score)");

	String hsFile;

	public HighScores() {
		highScoreList = new ArrayList<HighScores>();
	}

	public HighScores(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public void addScore(int finalScore, String diff) throws ClassNotFoundException, IOException {
		if (diff.equals("Easy"))
			hsFile = "N21scoresE.txt";
		else
			hsFile = "N21scoresH.txt";
		if (this.newScore(finalScore)) {
			String name = JOptionPane.showInputDialog("You got a high score! Enter your name below (No Spaces)");

			highScoreList.add(new HighScores(name, finalScore));
			addScoreToFile(new HighScores(name, finalScore));
		}
	}

	private boolean newScore(int score) throws IOException, ClassNotFoundException {
		readFile();
		if (highScoreList.size() < 10)
			return true;

		sortedScores = getSortedScores();
		HighScores lowScore = sortedScores.get(9);
		if (score >= lowScore.score)
			return true;

		return false;
	}

	public void showHighScoreBox(String diff) throws IOException, ClassNotFoundException, InterruptedException {
		if (diff.equals("Easy"))
			hsFile = "N21scoresE.txt";
		else
			hsFile = "N21scoresH.txt";
		readFile();
		sortedScores = getSortedScores();
		HighScoreBox(diff);
	}

	public void HighScoreBox(String diff) throws ClassNotFoundException, IOException, InterruptedException {
		scoreFrame.setTitle("High Scores");
		scoreFrame.setLayout(new GridLayout(12, 1));
		scoreFrame.setSize(315, 315);

		if (diff.equals("Easy")) {
			scoreFrame.getContentPane().setBackground(Color.GREEN);
			scoringNote.setForeground(Color.RED);
		} else {
			scoreFrame.getContentPane().setBackground(Color.RED);
			scoringNote.setForeground(Color.YELLOW);
		}

		scoreFrame.add(scoringNote);

		sortedScores = getSortedScores();

		if (sortedScores.size() < 10) {
			for (int i = 0; i < sortedScores.size(); i++) {
				if (i == 0) {
					firstPlace.setText(
							1 + ". " + sortedScores.get(i).getName() + "    " + sortedScores.get(i).getScore());
					firstPlace.setForeground(new Color(255, 255, 153));
					scoreFrame.add(firstPlace);
				} else if (i == 1) {
					secondPlace.setText(
							2 + ". " + sortedScores.get(i).getName() + "    " + sortedScores.get(i).getScore());
					secondPlace.setForeground(Color.GRAY);
					scoreFrame.add(secondPlace);
				} else if (i == 2) {
					thirdPlace.setText(
							3 + ". " + sortedScores.get(i).getName() + "    " + sortedScores.get(i).getScore());
					thirdPlace.setForeground(new Color(205, 127, 50));
					scoreFrame.add(thirdPlace);
				} else {
					scoreFrame.add(new JLabel(
							(i + 1) + ". " + sortedScores.get(i).getName() + "    " + sortedScores.get(i).getScore()));
				}
			}
		} else {
			for (int i = 0; i < 10; i++) {
				if (i == 0) {
					firstPlace.setText(
							1 + ". " + sortedScores.get(i).getName() + "    " + sortedScores.get(i).getScore());
					firstPlace.setForeground(new Color(255, 215, 0));
					scoreFrame.add(firstPlace);
				} else if (i == 1) {
					secondPlace.setText(
							2 + ". " + sortedScores.get(i).getName() + "    " + sortedScores.get(i).getScore());
					secondPlace.setForeground(Color.GRAY);
					scoreFrame.add(secondPlace);
				} else if (i == 2) {
					thirdPlace.setText(
							3 + ". " + sortedScores.get(i).getName() + "    " + sortedScores.get(i).getScore());
					thirdPlace.setForeground(new Color(205, 127, 50));
					scoreFrame.add(thirdPlace);
				} else {
					scoreFrame.add(new JLabel(
							(i + 1) + ". " + sortedScores.get(i).getName() + "    " + sortedScores.get(i).getScore()));
				}
			}
		}
		JButton close = new JButton("Close");

		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scoreFrame.dispose();
			}
		});

		scoreFrame.add(close);
		scoreFrame.setVisible(true);

	}

	private String getName() {
		return name;
	}

	private ArrayList<HighScores> getSortedScores() throws ClassNotFoundException, IOException {
		CompareScores sorter = new CompareScores();
		Collections.sort(highScoreList, sorter);
		return highScoreList;
	}

	public String toString() {
		return name + " " + score;
	}

	public void readFile() throws IOException {
		File file = new File(hsFile);
		Scanner input = new Scanner(file);
		while (input.hasNext()) {
			String name = input.next();
			int score = input.nextInt();
			highScoreList.add(new HighScores(name, score));
		}
		input.close();

	}

	public void addScoreToFile(HighScores newScore) throws IOException {
		FileWriter fw = new FileWriter(hsFile, true);
		PrintWriter pw = new PrintWriter(fw);
		pw.print(newScore.name + " ");
		pw.println(newScore.score);
		pw.close();
	}
}