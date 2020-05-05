import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;

import org.jfree.chart.ChartUtils;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;



public class GeneticAlgorithm {
	private static int popSize = 1000;
	private static int maxGenNum = 50;
	private static int poemsSize = 4;
	private static double mutationRate = 0.001;
	private static double crossoverRate = 0.95;
	private static int tournamentSize = 2;
	private static int eliteSize = (int) (popSize * 0.005);
	private static XYSeriesCollection fitnessData;
	private static XYSeriesCollection poemVarianceC;
	
	public static void main(String[] args) {
		geneticAlgorithm(true);
		
		EventQueue.invokeLater(() -> {
			var graph = new Graph(2);
			try {
				graph.createGraph(fitnessData, "Generational Fitness: pop size: " + popSize + "; gen num: "+ maxGenNum + 
						"; mutation rate: " + mutationRate + "; crossover rate: " + crossoverRate + "; elite size: " + eliteSize, 
						"generation number", "fitness", true, "fg");
				
				graph.createGraph(poemVarianceC, ("Poem Variance: pop size: " + popSize + " ; gen num: " + maxGenNum +
						"; mutation rate: " + mutationRate + "; crossover rate: " + crossoverRate + "; elite size: " + eliteSize), 
						"generation number", "difference", false, "pv");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			graph.initializeUI();
			graph.setVisible(true);
		});
	}
	
	private static void geneticAlgorithm(boolean elitism) {
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
		System.out.println("fittest: " + fittest.getFitness());
		System.out.println(fittest);
		System.out.println(genNum + ": " + pop.getAvrgFitness());

		XYSeries averageFitness = new XYSeries("average fitness");
		XYSeries fittestIndividual = new XYSeries("fittest individual");
		XYSeries poemVariance = new XYSeries("poem varaince");
		averageFitness.add(genNum, pop.getAvrgFitness());
		fittestIndividual.add(genNum, fittest.getFitness());
		poemVariance.add(genNum, pop.calculateFitnessVariance());
		
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
			
			//add data for the diagrams
			averageFitness.add(genNum, pop.getAvrgFitness());
			fittest = pop.getFittest();
			fittestIndividual.add(genNum, fittest.getFitness());
			poemVariance.add(genNum, pop.calculateFitnessVariance());
		}
		fittest = pop.getFittest();
		System.out.println("fittest: " + fittest.getFitness());
		System.out.println(fittest);
		System.out.println(pop.getIndividuals()[23]);
		System.out.println(pop.getIndividuals()[12]);
		fitnessData = new XYSeriesCollection();
		fitnessData.addSeries(averageFitness);
		fitnessData.addSeries(fittestIndividual);
		
		poemVarianceC = new XYSeriesCollection();
		poemVarianceC.addSeries(poemVariance);

	}
}
