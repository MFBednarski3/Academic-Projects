package codeReduced;

import java.util.Comparator;

public class CompareScores implements Comparator<HighScores> {
	public int compare(HighScores score1, HighScores score2) {

		int firstScore = score1.getScore();
		int secScore = score2.getScore();

		if (firstScore > secScore)
			return -1;
		else if (firstScore < secScore)
			return 1;
		else
			return 0;

	}
}
