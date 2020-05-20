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
	private static int popSize = 1000;
	private static int maxGenNum = 50;
	private static int poemsSize = 4;
	private static double mutationRate = 0.005;
	private static double crossoverRate = 0.95;
	private static int tournamentSize = 2;
	private static int eliteSize = (int) (popSize * 0.005);
	private static XYSeriesCollection fitnessData;
	private static XYSeriesCollection poemVarianceC;
	
	private static Poem bestPoem;
	
	public static void main(String[] args) {
		double initialMRate = mutationRate;
		int runs = 5;
		HashMap<String, double[]> averageValues = new HashMap<String, double[]>();
		averageValues.put("popFitness", new double[maxGenNum]);
		averageValues.put("fittest", new double[maxGenNum]);
		averageValues.put("popFitnessMetric", new double[maxGenNum]);
		averageValues.put("popFitnessRhyme", new double[maxGenNum]);
		
		for (String data : averageValues.keySet()) {
			Arrays.fill(averageValues.get(data), 0);
		}
		
		for (int i=0; i<runs; i++) {
			geneticAlgorithm(true, averageValues);
		}
		System.out.println("Best Poem after " + runs + " runs:");
		System.out.println(bestPoem);
		
		XYSeries averageFitness = new XYSeries("average fitness");
		XYSeries fittestIndividual = new XYSeries("fittest individual");
		XYSeries averageMetricFitness = new XYSeries("average metric fitness");
		XYSeries averageRhymeFitness = new XYSeries("average rhyme fitness");
		for (int i=0; i<maxGenNum; i++) {
			averageValues.get("popFitness")[i] = averageValues.get("popFitness")[i]/runs;	
			averageFitness.add(i, averageValues.get("popFitness")[i]);
			averageValues.get("fittest")[i] = averageValues.get("fittest")[i]/runs;	
			fittestIndividual.add(i, averageValues.get("fittest")[i]);
			averageValues.get("popFitnessMetric")[i] = averageValues.get("popFitnessMetric")[i]/runs;	
			averageMetricFitness.add(i, averageValues.get("popFitnessMetric")[i]);
			averageValues.get("popFitnessRhyme")[i] = averageValues.get("popFitnessRhyme")[i]/runs;	
			averageRhymeFitness.add(i, averageValues.get("popFitnessRhyme")[i]);
	
		}
		fitnessData = new XYSeriesCollection();
		fitnessData.addSeries(averageFitness);
		fitnessData.addSeries(fittestIndividual);
		fitnessData.addSeries(averageMetricFitness);
		fitnessData.addSeries(averageRhymeFitness);
		
		EventQueue.invokeLater(() -> {
			var graph = new Graph(1);
			try {
				graph.createGraph(fitnessData, "Generational Fitness: pop size: " + popSize + "; gen num: "+ maxGenNum + 
						"; mutation rate: " + initialMRate + "; crossover rate: " + crossoverRate + "; elite size: " + eliteSize, 
						"generation number", "fitness", false, "fg");
				
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
	
	private static void geneticAlgorithm(boolean elitism, HashMap<String, double[]> savedValues) {
		if(!elitism) {
			eliteSize = 0;
		}
		int genNum = 0;
		Population pop = new Population(popSize);
		pop.initialzePopulation(poemsSize);
		
		//calculate fitness
		for (int i=0; i<popSize; i++) {
			FitnessCalculator.calculateFitness(pop.getIndividuals()[i]);
		}
		pop.calculateAverageFitness();
		
		Poem fittest = pop.getFittest();
		bestPoem = fittest;
		System.out.println("fittest: " + fittest.getFitness());
		System.out.println(fittest.printWithStresses());
		System.out.println(genNum + ": " + pop.getAvrgFitness());

		double avrgMetricFitness = pop.calculateAverageMetricFitness();
		double avrgRhymeFitness = pop.calculateAverageRhymeFitness();
		
		//fill HashMap for average values
		savedValues.get("popFitness")[genNum] += pop.getAvrgFitness();
		savedValues.get("fittest")[genNum] += fittest.getFitness();
		savedValues.get("popFitnessMetric")[genNum] += avrgMetricFitness;
		savedValues.get("popFitnessRhyme")[genNum] += avrgRhymeFitness;
		
		for (genNum=1; genNum<maxGenNum; genNum++) {			
		
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
				FitnessCalculator.calculateFitness(pop.getIndividuals()[j]);
			}
			pop.calculateAverageFitness();
			System.out.println(genNum + ": " + pop.getAvrgFitness() /*+ 
					"; variance: " + pop.calculateFitnessVariance() + 
					"; PV: " + pop.calculatePoemVariance()*/);
			
			//lower mutation rate to help convergence
			if(mutationRate>0.0) {
				mutationRate-=(mutationRate/((double)maxGenNum * 0.5));
			}
			
			fittest = pop.getFittest();
			avrgMetricFitness = pop.calculateAverageMetricFitness();
			avrgRhymeFitness = pop.calculateAverageRhymeFitness();
			
			//fill HashMap for average values
			savedValues.get("popFitness")[genNum] += pop.getAvrgFitness();
			savedValues.get("fittest")[genNum] += fittest.getFitness();
			savedValues.get("popFitnessMetric")[genNum] += avrgMetricFitness;
			savedValues.get("popFitnessRhyme")[genNum] += avrgRhymeFitness;
		}
		fittest = pop.getFittest();
		if (fittest.getFitness() > bestPoem.getFitness()) {
			bestPoem = fittest;
		}
		System.out.println("fittest: " + fittest.getFitness());
		System.out.println(fittest.printWithStresses());
		System.out.println(pop.getIndividuals()[23]);
		System.out.println(pop.getIndividuals()[12]);
	}
}
