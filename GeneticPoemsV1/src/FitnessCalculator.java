import java.util.Arrays;
import java.util.HashMap;

import rita.*;

//class for fitness calculations
public class FitnessCalculator {
	//default metre and emotion
	private String metre = "0101010101010101010101010101010101";
	private String emotion = "sadness";
	
	//weights for weighted sum of fitness values
	private double metreWeight = 1.0;
	private double rhymeWeight = 1.0;
	private double emotionWeight = 1.0;
	
	private EmotionLexicon emotionLexicon;
	private HashMap<String, String> opposites;
	
	public FitnessCalculator(String metre, String emotion) {
		this.emotionLexicon = new EmotionLexicon();
		opposites = new HashMap<String, String>();
		opposites.put("anger", "joy");
		opposites.put("disgust", "joy");
		opposites.put("fear", "joy");
		opposites.put("joy", "sadness");
		opposites.put("negative", "positive");
		opposites.put("positive", "negative");
		opposites.put("sadness", "joy");
		opposites.put("trust", "disgust");
		
		if (metre.equals("iambic")) {
			this.metre = "0101010101010101010101010101010101";
		} else if (metre.equals("trochaic")) {
			this.metre = "1010101010101010101010101010101010";
		} else if (metre.equals("anapestic")) {
			this.metre = "001001001001001001001001001001001";
		} else if (metre.equals("dactylic")) {
			this.metre = "100100100100100100100100100100100";
		}
		
		this.emotion = emotion;
	}
	
	//calculates overall fitness with weighted sum
	public void calculateFitness(Poem poem) {
		double fitness;		

		double metricFitness = calculateMetreFitness(poem);
		double rhymeFitness = calculateRhymeFitness(poem);
		double emotionFitness = calculateEmotionFitness(poem);
		
		poem.setMetricFitness(metricFitness);
		poem.setRhymeFitness(rhymeFitness);
		poem.setEmotionFitness(emotionFitness);
		
		double weightsSum = metreWeight + rhymeWeight + emotionWeight;
		fitness = ((metreWeight*metricFitness) + (rhymeWeight*rhymeFitness) + (emotionWeight*emotionFitness))/weightsSum;
		
		poem.setFitness(fitness);
	}

	//calculates metre fitness
	private double calculateMetreFitness(Poem poem) {
		double fitness = 0.0;
		for (int i=0; i<poem.length(); i++) {
			String line = poem.getPoemString()[i];
			String[] tokenizedLine = RiTa.tokenize(line, " ");
			String stresses = RiTa.getStresses(line);
			stresses = stresses.replace("[", " ").replace("]", " ");
			stresses = " " + stresses + " ";
			
			int fitnessPoints = 0;
			int s = 0;
			int w = 0;
			for (int j=1; j<stresses.length(); j++) {
				if (stresses.charAt(j)==('/')) { continue; }
				if (stresses.charAt(j)==' ') {
					w++;
					continue;
				}
				//compare actual stress pattern to target pattern
				if (stresses.charAt(j-1)==' ' && stresses.charAt(j+1)==' ') {
					String pos = RiTa.getPosTags(tokenizedLine[w])[0];
					//one syllable "closed class" words always fit the metric pattern
					if (pos.equals("dt") || pos.equals("prp$") || pos.equals("md") || pos.equals("in")){
						fitnessPoints++;	
					} else {
						if (stresses.charAt(j) == metre.charAt(s)) {
							fitnessPoints++;
						}
					}
				} else {
					if (stresses.charAt(j) == metre.charAt(s)) {
						fitnessPoints++;
					}
				}
				s++;
			}
			
			fitness += (double)fitnessPoints / (double) s;			
		}
		fitness = fitness/poem.length();
		return fitness;
	}
	
	//calculates rhyme fitness
	private double calculateRhymeFitness(Poem poem) {
		int rhymePoints =0;
		for (int i=1; i<poem.length(); i+=2) {
			String[] line1 = RiTa.tokenize(poem.getPoemString()[i-1]);
			String[] line2 = RiTa.tokenize(poem.getPoemString()[i]);

			String lastWord1 = line1[line1.length-1];
			String lastWord2 = line2[line2.length-1];
			
			//check for pair rhyme
			if (isRhyme(lastWord1, lastWord2) ) {
				if (!lastWord1.equals(lastWord2)) {
					rhymePoints++;
				}
			}
		}
		//number of rhyme points that is needed for perfect fitness
		int maxRhymePoints = poem.length()/2;
		double rhymeFitness = 0.0;
		if (rhymePoints >= maxRhymePoints){
			rhymeFitness = 1.0;
		} else {
			rhymeFitness = rhymePoints/ (double) maxRhymePoints;
		}
		
		return rhymeFitness;
	}
	
	//calculates emotion fitness
	private double calculateEmotionFitness(Poem poem) {
		double maxEmotionPoints = poem.length() + (poem.length()/2);
		double emotionPoints = 0;
		double[] emotionPointsPerLine = new double[poem.length()];
		for (int i=0; i<poem.length(); i++) {
			String[] tokenizedLine = RiTa.tokenize(poem.getPoemString()[i]);
			emotionPointsPerLine[i] = 0;
			for (int j=0; j<tokenizedLine.length; j++) {
				String word = tokenizedLine[j];
				String wordPos = RiTa.getPosTags(word)[0];
				
				//only check emotion for "relevant" word types
				if (wordPos != "dt" || wordPos != "prp$" || wordPos != "in") {
					//get stem word for non-baseform verbs
					if (wordPos.contentEquals("vbz") || wordPos.equals("vbd")){
						word = RiTa.stem(word);
					}
					if (emotionLexicon.isEmotion(word, emotion)) {
						emotionPointsPerLine[i]++;
						emotionPoints++;
					} else if (emotionLexicon.isEmotion(word, opposites.get(emotion))) {
						if (emotionPoints > 0) {
							emotionPointsPerLine[i]--;
							emotionPoints--;
						}
					}
				}
			}
		}
		//check for at least one emotion word in each line
		boolean emotionInEveryLine = true;
		emotionPoints = 0;
		for (int i=0; i<emotionPointsPerLine.length; i++) {
			emotionPoints += emotionPointsPerLine[i];
			if (emotionPointsPerLine[i] == 0) {
				emotionInEveryLine = false;
			}
		}
		double emotionFitness;
		if(emotionPoints>=maxEmotionPoints) {
			emotionFitness = 1.0;
		} else {
			emotionFitness = emotionPoints/maxEmotionPoints;
		}
		//penalize for lines without emotion word
		if (!emotionInEveryLine) {
			if (emotionFitness > 0.5) {
				emotionFitness -= 0.5;
			}
		}
		
		return emotionFitness;
	}
	
	//function to check two words for rhyme; returns true if the words rhyme
	private boolean isRhyme(String word1, String word2) {
		//prevent detection of equals words as rhyme
		if (word1.equals(word2)) {
			return false;
		}		
		
		String[] phonemesW1 = RiTa.tokenize(RiTa.getPhonemes(word1), "-");
		String[] phonemesW2 = RiTa.tokenize(RiTa.getPhonemes(word2), "-");

		String[] shorterWord;
		if(phonemesW1.length<phonemesW2.length) {
			shorterWord = phonemesW1;
		} else {
			shorterWord = phonemesW2;
		}
		String[] subWord = null;
		for(int i=0; i<shorterWord.length; i++) {
			char c = shorterWord[i].charAt(0);
			if(c!='a' && c!='e' && c!='i' && c!='o' && c!='u') {
				continue;
			} else {
				subWord = Arrays.copyOfRange(shorterWord, i, shorterWord.length);
				break;
			}
		}
		
		for(int i=1; i<=subWord.length; i++) {
			if(phonemesW1[phonemesW1.length-i]!=null && phonemesW2[phonemesW2.length-i]!=null) {
				if(phonemesW1[phonemesW1.length-i].equals(phonemesW2[phonemesW2.length-i])) {
					continue;
				} else {
					return false;
				}
			}
		}
		return true;
	}
}
