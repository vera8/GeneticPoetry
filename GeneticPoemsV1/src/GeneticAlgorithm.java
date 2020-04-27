import java.util.concurrent.ThreadLocalRandom;


public class GeneticAlgorithm {
	private static int popSize = 1000;
	private static int maxGenNum = 20;
	private static int poemsSize = 4;
	private static double mutationRate = 0.3;
	private static double crossoverRate = 0.70;
	private static int tournamentSize = 2;
	
	public static void main(String[] args) {
		geneticAlgorithm();
	}
	
	private static void geneticAlgorithm() {
		int genNum;
		Population pop = new Population(popSize);
		pop.initialzePopulation(poemsSize);
		
		//calculate fitness
		for (int i=0; i<popSize; i++) {
			FitnessCalculator.calculateFitness(pop.getIndividuals()[i]);
		}
		pop.calculateAverageFitness();
		System.out.println(pop.getAvrgFitness());
		
		Poem fittest = pop.getFittest();
		System.out.println("fittest: " + fittest.getFitness());
		System.out.println(fittest);
		System.out.println(pop.getIndividuals()[23]);
		System.out.println(pop.getIndividuals()[12]);
		
		for (genNum=0; genNum<maxGenNum; genNum++) {
			Poem[] selected = new Poem[popSize];
			for (int i=0; i<popSize; i++) {
				selected[i] = Selection.tournamentSelection(tournamentSize, pop);
			}
			for (int i=0; i<popSize; i+=2) {
				Recombination.crossover(crossoverRate, selected[i], selected[i+1]);
			}
			for (int i=0; i<popSize; i++) {
				Mutation.mutate(mutationRate, selected[i]);
			}
			pop.setIndividuals(selected);
			pop.calculateAverageFitness();
			System.out.println(pop.getAvrgFitness());
		}
		fittest = pop.getFittest();
		System.out.println("fittest: " + fittest.getFitness());
		System.out.println(fittest);
		System.out.println(pop.getIndividuals()[23]);
		System.out.println(pop.getIndividuals()[12]);
		
		
	}
}
