import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Arrays;

import org.jfree.chart.ChartUtils;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;



public class GeneticAlgorithm {
	//private static int popSize = 1000;
	//private static int maxGenNum = 50;
	//private static int poemsSize = 4;
	//private static double initialMutationRate = 0.005;
	//private static double crossoverRate = 0.95;
	private static int tournamentSize = 2;
	private static int eliteSize;
	private static XYSeriesCollection fitnessData;
	private static XYSeriesCollection poemVarianceC;
	private static FitnessCalculator fitnessCalculator;
	private static int numOfRuns;
	private static double[] fittestVariance;
	
	private static Poem bestPoem;
	
	//public static void main(String[] args) {
	public static Poem runGeneticAlgorithm(int poemSize, String metre, String emotion, int popSize, int maxGenNum, 
			double crossoverRate, double mutationRate, int runs, boolean showGraphs) {
		UI.progressBar.setValue(0);
		long startTime = System.nanoTime();
		fitnessCalculator = new FitnessCalculator(metre, emotion);
		//int runs = 1;
		HashMap<String, double[]> averageValues = new HashMap<String, double[]>();
		averageValues.put("popFitness", new double[maxGenNum]);
		averageValues.put("fittest", new double[maxGenNum]);
		averageValues.put("popFitnessMetric", new double[maxGenNum]);
		averageValues.put("popFitnessRhyme", new double[maxGenNum]);
		averageValues.put("popFitnessEmotion", new double[maxGenNum]);
		
		for (String data : averageValues.keySet()) {
			Arrays.fill(averageValues.get(data), 0);
		}
		
		eliteSize = (int) (popSize * 0.005);
		boolean elitism = true;
		
		fittestVariance = new double[runs];
		double fittestVarianceAvrg = 0.0;
		numOfRuns = runs;
		for (int i=0; i<runs; i++) {
			geneticAlgorithm(i, elitism, averageValues, poemSize, popSize, maxGenNum, crossoverRate, mutationRate);
			fittestVarianceAvrg += fittestVariance[i]; 
		}
		fittestVarianceAvrg = fittestVarianceAvrg/(double)runs;
		System.out.println("Best Poem after " + runs + " runs (fitness:" + bestPoem.getFitness() + ", metric: "+ bestPoem.getMetricFitness()+
				", rhyme: " + bestPoem.getRhymeFitness() + ", emotion: " + bestPoem.getEmotionFitness() + "):");
		System.out.println(bestPoem);
		System.out.println("avrg fitness: " + averageValues.get("popFitness")[maxGenNum-1]/(double)runs);
		System.out.println("avrg fittest: " + averageValues.get("fittest")[maxGenNum-1]/(double)runs);
		System.out.println("metric fitness: " + averageValues.get("popFitnessMetric")[maxGenNum-1]/(double)runs);
		System.out.println("rhyme fitness: " + averageValues.get("popFitnessRhyme")[maxGenNum-1]/(double)runs);
		System.out.println("emotion fitness: " + averageValues.get("popFitnessEmotion")[maxGenNum-1]/(double)runs);
		System.out.println("fittest variance: " + fittestVarianceAvrg);
		
		XYSeries averageFitness = new XYSeries("overall fitness");
		XYSeries fittestIndividual = new XYSeries("fittest individual");
		XYSeries averageMetricFitness = new XYSeries("metre fitness");
		XYSeries averageRhymeFitness = new XYSeries("rhyme fitness");
		XYSeries averageEmotionFitness = new XYSeries("emotion fitness");
		for (int i=0; i<maxGenNum; i++) {
			averageValues.get("popFitness")[i] = averageValues.get("popFitness")[i]/(double)runs;	
			averageFitness.add(i, averageValues.get("popFitness")[i]);
			averageValues.get("fittest")[i] = averageValues.get("fittest")[i]/(double)runs;	
			fittestIndividual.add(i, averageValues.get("fittest")[i]);
			averageValues.get("popFitnessMetric")[i] = averageValues.get("popFitnessMetric")[i]/(double)runs;	
			averageMetricFitness.add(i, averageValues.get("popFitnessMetric")[i]);
			averageValues.get("popFitnessRhyme")[i] = averageValues.get("popFitnessRhyme")[i]/(double)runs;	
			averageRhymeFitness.add(i, averageValues.get("popFitnessRhyme")[i]);
			averageValues.get("popFitnessEmotion")[i] = averageValues.get("popFitnessEmotion")[i]/(double)runs;	
			averageEmotionFitness.add(i, averageValues.get("popFitnessEmotion")[i]);
	
		}
		fitnessData = new XYSeriesCollection();
		fitnessData.addSeries(averageFitness);
		fitnessData.addSeries(fittestIndividual);
		fitnessData.addSeries(averageMetricFitness);
		fitnessData.addSeries(averageRhymeFitness);
		fitnessData.addSeries(averageEmotionFitness);
		
		if (showGraphs) {
			EventQueue.invokeLater(() -> {
				var graph = new Graph(1);
				try {
					graph.createGraph(fitnessData, "Generational Fitness: pop size: " + popSize + "; gen num: "+ maxGenNum + 
							"; mutation rate: " + mutationRate + "; crossover rate: " + crossoverRate + "; elite size: " + eliteSize + "; runs: " + runs, 
							"generation number", "fitness", true, "fg");
					
	//				graph.createGraph(poemVarianceC, ("Poem Variance: pop size: " + popSize + " ; gen num: " + maxGenNum +
	//						"; mutation rate: " + mutationRate + "; crossover rate: " + crossoverRate + "; elite size: " + eliteSize), 
	//						"generation number", "difference", false, "pv");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				graph.initializeUI();
				graph.setVisible(true);
			});
		}


		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println("Running time: " + totalTime);
		return bestPoem;
	}
	
	private static void geneticAlgorithm(int run, boolean elitism, HashMap<String, double[]> savedValues, 
			int poemSize, int popSize, int maxGenNum, double crossoverRate, double initialMutationRate) {

		if(!elitism) {
			eliteSize = 0;
		}
		double mutationRate = initialMutationRate;
		int genNum = 0;
		
		Population pop = new Population(popSize);
		pop.initialzePopulation(poemSize);
		
		//calculate fitness
		for (int i=0; i<popSize; i++) {
			fitnessCalculator.calculateFitness(pop.getIndividuals()[i]);
		}
		pop.calculateAverageFitness();
		
		Poem fittest = pop.getFittest();
		if (bestPoem == null) {
			bestPoem = fittest;
		}
		System.out.println("fittest: " + fittest.getFitness());
		System.out.println(fittest.printWithStresses());
		System.out.println(genNum + ": " + pop.getAvrgFitness());

		double avrgMetricFitness = pop.calculateAverageMetricFitness();
		double avrgRhymeFitness = pop.calculateAverageRhymeFitness();
		double avrgEmotionFitness = pop.calculateAverageEmotionFitness();
		
		//fill HashMap for average values
		savedValues.get("popFitness")[genNum] += pop.getAvrgFitness();
		savedValues.get("fittest")[genNum] += fittest.getFitness();
		savedValues.get("popFitnessMetric")[genNum] += avrgMetricFitness;
		savedValues.get("popFitnessRhyme")[genNum] += avrgRhymeFitness;
		savedValues.get("popFitnessEmotion")[genNum] += avrgEmotionFitness;
		
		for (genNum=1; genNum<maxGenNum; genNum++) {			
			UI.progressBar.setValue(genNum+((maxGenNum-1)*run));
			
			Poem[] newGeneration = new Poem[popSize];
			
			int i = 0;
			if (elitism) {
				if (eliteSize%2 != 0) {
					eliteSize--;
				}
				Poem[] sortedPop = pop.getSortedCopy();
				Poem[] elite = Arrays.copyOfRange(sortedPop, 0, eliteSize);
				while (i<elite.length) {
					newGeneration[i] = elite[i];
					i++;
				}
			}
			
			while (i<popSize) {
				Poem parent1 = Selection.tournamentSelection(tournamentSize, pop);
				Poem parent2 = Selection.tournamentSelection(tournamentSize, pop);
				Poem[] children = Recombination.crossover(crossoverRate, parent1, parent2);
				Poem child1 = children[0];
				Mutation.mutate(mutationRate, child1);
				Poem child2 = children[1];
				Mutation.mutate(mutationRate, child2);
				newGeneration[i] = child1;
				newGeneration[i+1] = child2;
				i+=2;
			}
			
			pop.setIndividuals(newGeneration);
			
			for (int j=0; j<popSize; j++) {
				fitnessCalculator.calculateFitness(pop.getIndividuals()[j]);
			}
			pop.calculateAverageFitness();
			System.out.println(genNum + ": " + pop.getAvrgFitness());
			
			//lower mutation rate to help convergence
			if(mutationRate>0.0) {
				mutationRate-=(initialMutationRate/((double)maxGenNum * 0.75));
				if (mutationRate<0.0) {
					mutationRate = 0.0;
				}
			}
			
			fittest = pop.getFittest();
			avrgMetricFitness = pop.calculateAverageMetricFitness();
			avrgRhymeFitness = pop.calculateAverageRhymeFitness();
			avrgEmotionFitness = pop.calculateAverageEmotionFitness();
			
			//fill HashMap for average values
			savedValues.get("popFitness")[genNum] += pop.getAvrgFitness();
			savedValues.get("fittest")[genNum] += fittest.getFitness();
			savedValues.get("popFitnessMetric")[genNum] += avrgMetricFitness;
			savedValues.get("popFitnessRhyme")[genNum] += avrgRhymeFitness;
			savedValues.get("popFitnessEmotion")[genNum] += avrgEmotionFitness;
		}
		fittest = pop.getFittest();
		if (fittest.getFitness() > bestPoem.getFitness()) {
			bestPoem = fittest;
		}
		System.out.println("fittest: " + fittest.getFitness());
		System.out.println(fittest.printWithStresses());
		System.out.println(pop.getIndividuals()[23]);
		System.out.println(pop.getIndividuals()[12]);
		for (int i=0; i<popSize; i++) {
			fittestVariance[run] += Math.pow((pop.getIndividuals()[i].getFitness() - fittest.getFitness()), 2);			
		}
		fittestVariance[run] = fittestVariance[run]/(double)popSize;
		System.out.println("mutation rate" + mutationRate);
		System.out.println("fittest variance: " + fittestVariance[run]);
	}
}
