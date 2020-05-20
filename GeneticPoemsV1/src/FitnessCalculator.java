import java.util.ArrayList;
import java.util.Arrays;

import rita.*;

//Possible enhancement: make different criteria and their weights adjustable by via user input 
//(different metric scheme, emotion, long lines vs. short lines)

public class FitnessCalculator {
	public static String metre = "0101010101010101010101010101010101";
	public static double metreWeight = 1.0;
	
	//add more criteria with weights over time; adjust weights by experimentation
	//syllable count
	//rhyme
	//emotion (using Emotion Lexicon)
	
	public static void calculateFitness(Poem poem) {
		double fitness;
		double[] fitnessPerLine;
		
		double[] metricFitnessPerLine = calculateMetricFitness(poem);
//		boolean penalisePoem = false;
		double averageMetricFitness = 0.0;
		for (int i=0; i<metricFitnessPerLine.length; i++) {
			averageMetricFitness += metricFitnessPerLine[i];
//			if (metricFitnessPerLine[i] < 0.5) {
//				penalisePoem = true;
//			}
		}
		averageMetricFitness = (averageMetricFitness/ (double) metricFitnessPerLine.length);
		
//		if (penalisePoem && averageMetricFitness > 0.2) {
//			averageMetricFitness -= 0.2;
//		}
		
		//double metricFitness = calculateMetricFitness(poem);
		poem.setMetricFitness(averageMetricFitness);
		
		fitnessPerLine = metricFitnessPerLine;
		
		double rhymeFitness = calculateRhymeFitness(poem);
		
		poem.setRhymeFitness(rhymeFitness);
		
		fitness = (averageMetricFitness + rhymeFitness)/2.0;
		
		poem.setFitness(fitness);
		poem.setFitnessPerLine(fitnessPerLine);
	}

	private static double[] calculateMetricFitness(Poem poem) {
		double distance = 0.0;
		double maxDistance = 0.0;
		double[] fitnessPerLine = new double[poem.length()];
		for (int i=0; i<poem.length(); i++) {
			String line = poem.getPoemString()[i];
			String stresses = RiTa.getStresses(line);
			stresses = stresses.replace("[", "").replace("]", "").replace("/", "").replace(" ", "");
			
			maxDistance += stresses.length();
			int syllableNum = stresses.length();
			String schemeTmp = metre.substring(0, syllableNum);
			fitnessPerLine[i] = 1.0 - RiTa.minEditDistance(schemeTmp, stresses, true);
//			System.out.println(schemeTmp);
//			System.out.println(stresses);
//			System.out.println(RiTa.minEditDistance(schemeTmp, stresses, true));
			distance += RiTa.minEditDistance(schemeTmp, stresses);
			//System.out.println(distance);
		}

		double normalizedDistance = distance/maxDistance;
		double fitness = 1.0 - normalizedDistance;
		//return fitness;
		return fitnessPerLine;
	}
	
	private static double calculateRhymeFitness(Poem poem) {
		int rhymePoints =0;
		for (int i=1; i<poem.length(); i+=2) {
			String[] line1 = RiTa.tokenize(poem.getPoemString()[i-1]);
			String[] line2 = RiTa.tokenize(poem.getPoemString()[i]);
			String[] line3 = null;
			if(i<poem.length()-1){
				line3 = RiTa.tokenize(poem.getPoemString()[i+1]);
			}
			String lastWord1 = line1[line1.length-1];
			String lastWord2 = line2[line2.length-1];
			String lastWord3 = "";
			if (line3 != null) {
				lastWord3 = line3[line3.length-1];
			}
			
//			String[] rhymes = RiTa.rhymes(lastWord1);
//			for (int j=0; j<rhymes.length; j++) {
//				if(rhymes[j].equals(lastWord2) || rhymes[j].equals(lastWord3)) {
//					rhymePoints++;
//				}
//			}
			if (isRhyme(lastWord1, lastWord2) ) {
				if (!lastWord1.equals(lastWord2)) {
					rhymePoints++;
				}
				//System.out.println(lastWord1 + " : " + lastWord2 + " : " + lastWord3);
			}
//			if(lastWord3 != "") {
//				if (isRhyme(lastWord1, lastWord3)) {
//					if (!lastWord1.equals(lastWord3)) {
//						rhymePoints++;
//					}
//				}
//			}
		}
		//number of rhyme points that is sufficient for perfect fitness
		int maxRhymePoints = poem.length()/2;
		double rhymeFitness = 0.0;
		if (rhymePoints >= maxRhymePoints){
			rhymeFitness = 1.0;
		} else {
			rhymeFitness = rhymePoints/ (double) maxRhymePoints;
		}
		
		return rhymeFitness;
	}
	
	private double calculateAlliterationFitness(Poem poem) {
		return 0.0;
	}
	
	public static boolean isRhyme(String word1, String word2) {
		if (word1.equals(word2)) {
			return false;
		}		
		String[] phonemesW1 = RiTa.tokenize(RiTa.getPhonemes(word1), "-");
		String[] phonemesW2 = RiTa.tokenize(RiTa.getPhonemes(word2), "-");
		
		int syllablesW1 = RiTa.tokenize(RiTa.getSyllables(word1), "/").length;
		int syllablesW2 = RiTa.tokenize(RiTa.getSyllables(word2), "/").length;

		int j=0;

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
		
//		if(phonemesW1.length<phonemesW2.length && syllablesW1!=syllablesW2) {
////			if(phonemesW1.length > 2) {
//			if(syllablesW1 == 1) {
//				j = phonemesW1.length;
//			} else {
//				j = phonemesW1.length-1;
//			}
//		} else if (phonemesW1.length>phonemesW2.length && syllablesW1!=syllablesW2) {
////			if(phonemesW2.length > 2) {
//			if(syllablesW2 == 1) {
//				j = phonemesW2.length;
//			} else {
//				j = phonemesW2.length-1;
//			}
//		} else  /*if (phonemesW1.length==phonemesW2.length)*/{
//			if(phonemesW1.length<=2) {
//				j = phonemesW1.length-1;
//			} else if(phonemesW1.length == phonemesW2.length){
//				j = phonemesW1.length-2;
//			} else {
//				
//			}
//		}
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
