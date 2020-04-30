import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class GeneticAlgorithm {
	private static int popSize = 1000;
	private static int maxGenNum = 50;
	private static int poemsSize = 4;
	private static double mutationRate = 0.0;
	private static double crossoverRate = 0.95;
	private static int tournamentSize = 2;
	private static int eliteSize = popSize/100;
	
	public static void main(String[] args) {
		geneticAlgorithm(true);
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
//		System.out.println(pop.getIndividuals()[23]);
//		System.out.println(pop.getIndividuals()[12]);
		
		for (genNum=1; genNum<maxGenNum; genNum++) {			

			Poem[] newGeneration = new Poem[popSize];
			
			int i = 0;
			if (elitism) {
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
			System.out.println(genNum + ": " + pop.getAvrgFitness());
		}
		fittest = pop.getFittest();
		System.out.println("fittest: " + fittest.getFitness());
		System.out.println(fittest);
		System.out.println(pop.getIndividuals()[23]);
		System.out.println(pop.getIndividuals()[12]);
		
		
	}
}
