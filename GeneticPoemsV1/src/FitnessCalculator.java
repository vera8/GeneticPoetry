import rita.*;

//Possible enhancement: make different criteria and their weights adjustable by via user input 
//(different metric scheme, emotion, long lines vs. short lines)

public class FitnessCalculator {
	public static String metricScheme = "0101010101010101010101010101010101";
	public static double metricSchemeWeight = 1.0;
	
	//add more criteria with weights over time; adjust weights by experimentation
	//syllable count
	//rhyme
	//emotion (using Emotion Lexicon)
	
	public static void calculateFitness(Poem poem) {
		double fitness;
		
		double metricFitness = calculateMetricFitness(poem);
		
		fitness = metricFitness;
		
		poem.setFitness(fitness);;
	}

	public static double calculateMetricFitness(Poem poem) {
		double distance = 0.0;
		double maxDistance = 0.0;
		double[] fitnessPerLine = new double[poem.length()];
		for (String line : poem.getPoemString()) {
			String stresses = RiTa.getStresses(line);
			stresses = stresses.replace("[", "").replace("]", "").replace("/", "").replace(" ", "");
			
			maxDistance += stresses.length();
			int syllableNum = stresses.length();
			String schemeTmp = metricScheme.substring(0, syllableNum);
			//fitnessPerLine
//			System.out.println(schemeTmp);
//			System.out.println(stresses);
//			System.out.println(RiTa.minEditDistance(schemeTmp, stresses, true));
			distance += RiTa.minEditDistance(schemeTmp, stresses);
			//System.out.println(distance);
		}

		double normalizedDistance = distance/maxDistance;
		double fitness = 1.0 - normalizedDistance;
		return fitness;
	}
}
