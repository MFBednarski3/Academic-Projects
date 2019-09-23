package codeReduced;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.*;

public class GuessMyNumberGame {
	
	private static int tries;; 
	private static int lastGuess;
	
	//Random rand = new Random();
	private int answer;
	private int maxRange;
	
	private JLabel label1 = new JLabel();
	private JLabel label2 = new JLabel();
	private JButton button1 = new JButton("Submit");
	private JTextField text1 = new JTextField();
	private JFrame frame = new JFrame("Guess My Number Game");
	
	private String diff;

	
	public GuessMyNumberGame(){
		tries = 0;
		lastGuess = 0;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,300);
		frame.setLayout(new GridLayout(4,1));

	}
	
	void playGame(String diff) {
		this.diff = diff;
		
		if (diff.equals("Easy")) {
			maxRange = 10;
			frame.getContentPane().setBackground(Color.GREEN);
		}
		else if (diff.equals("Normal")) {
			maxRange = 100;
			frame.getContentPane().setBackground(Color.YELLOW);
		}
		else {
			maxRange = 1000;
			frame.getContentPane().setBackground(Color.RED);
		}
		
		answer = new Random().nextInt(maxRange)+1;
		
		label1.setText("Guess a number between 1 and " + maxRange);
		label1.setHorizontalTextPosition(JLabel.CENTER);
		label1.setHorizontalAlignment(JLabel.CENTER);
		
		label2.setText("Enter your guess in the text box below");
		label2.setHorizontalTextPosition(JLabel.CENTER);
		label2.setHorizontalAlignment(JLabel.CENTER);
		
		frame.add(label1);
		frame.add(label2);
		frame.add(text1);
		frame.add(button1);
		frame.setVisible(true);
		
		button1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String guessString = text1.getText(); 
				int guess = Integer.parseInt(guessString); 
				if (guess == answer){ 
					try { 
						ExactGuess();
					} catch (ClassNotFoundException | IOException | InterruptedException e1) { 
						e1.printStackTrace();
					}
				}
				else if (guess < 1 || guess > maxRange){ 
					tries++; 
					JOptionPane.showMessageDialog(new JFrame(), "Number is not between 1 and " + maxRange, "ERROR", JOptionPane.ERROR_MESSAGE); 
					label1.setText("Guess a number BETWEEN 1 and " + maxRange); 
					label2.setText("Enter your guess in the text box below");
				}
				else wrongGuess(guess);
				
			}	
		});
	}
	
	
	void wrongGuess(int guess) {
		lastGuess = guess;
		tries++;
		
		if (guess > answer) label1.setText("That number is too high. Try lower");
		else label1.setText("That number is too low. Try higher");
		
		label2.setText("Last number guessed: " + lastGuess);
	}
	
	void ExactGuess() throws ClassNotFoundException, IOException, InterruptedException{ 
		tries++;
		label1.setText("That is the exact number!");
		label2.setText("You took " + tries + " tries");
		button1.setEnabled(false); 
		
		HighScores scores = new HighScores();
		
		int finalScore = (maxRange + 1) - tries; 
		scores.addScore(finalScore, diff); 
		scores.HighScoreBox(); 
		frame.dispose(); 
	}
	
}
