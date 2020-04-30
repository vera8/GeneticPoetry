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
		fitness = averageMetricFitness;
//		fitness = metricFitness;
		//fitness = calculateRhymeFitness(poem);
		fitnessPerLine = metricFitnessPerLine;
		
		poem.setFitness(fitness);
		poem.setFitnessPerLine(fitnessPerLine);
	}

	public static double[] calculateMetricFitness(Poem poem) {
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
	
	public static double calculateRhymeFitness(Poem poem) {
		int rhymePoints =0;
		for (int i=1; i<poem.length(); i++) {
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
			if (RiTa.isRhyme(lastWord1, lastWord2) || RiTa.isRhyme(lastWord1, lastWord3)) {
				rhymePoints++;
				//System.out.println(lastWord1 + " : " + lastWord2 + " : " + lastWord3);
			}
		}
		
		return rhymePoints;
	}
	
	public double calculateAlliterationFitness(Poem poem) {
		return 0.0;
	}
}
