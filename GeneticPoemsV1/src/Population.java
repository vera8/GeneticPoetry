import java.util.Arrays;
import java.util.Comparator;

//class for saving the Poem-individuals as a population
public class Population {
	private int size;
	private Poem[] individuals;
	private double avrgFitness = -1;

	public Population(int size) {
		this.size = size;
		this.individuals = new Poem[size];
	}
	
	public void initialzePopulation(int lines) {
		//automatically fills Population with new Poems upon initialization
		RandomPoemGenerator generator = new RandomPoemGenerator(true, this.size);
		for (int i=0; i<size; i++) {
			individuals[i] = generator.generatePoem(lines);
		}
	}
	
	public void addIndividual(int index, Poem individual) {
		individuals[index] = individual;
	}
	
	public Poem[] getIndividuals() {
		return individuals;
	}
	
	public void setIndividuals(Poem[] individuals) {
		if(individuals.length == size) {
			this.individuals = individuals;
		}
	}
	
	public int getSize() {
		return size;
	}

	public double getAvrgFitness() {
		return avrgFitness;
	}

	public void setAvrgFitness(double avrgFitness) {
		this.avrgFitness = avrgFitness;
	}
	
	public void calculateAverageFitness() {
		double fitness = 0;
		for (int i=0; i<size; i++) {
			fitness += individuals[i].getFitness();
		}
		this.avrgFitness = fitness/(double) size;
	}
	
	public double calculateAverageMetricFitness() {
		double fitness = 0;
		for (int i=0; i<size; i++) {
			fitness += individuals[i].getMetricFitness();
		}
		fitness = fitness/(double) size;
		return fitness;
	}
	
	public double calculateAverageRhymeFitness() {
		double fitness = 0;
		for (int i=0; i<size; i++) {
			fitness += individuals[i].getRhymeFitness();
		}
		fitness = fitness/(double) size;
		return fitness;
	}
	
	public double calculateAverageEmotionFitness() {
		double fitness = 0;
		for (int i=0; i<size; i++) {
			fitness += individuals[i].getEmotionFitness();
		}
		fitness = fitness/(double) size;
		return fitness;
	}
	
	public Poem getFittest() {
		Poem fittest = individuals[0];
		for (int i=1; i<size; i++) {
			if (individuals[i].getFitness() > fittest.getFitness()) {
				fittest = individuals[i];
			}
		}
		return fittest;
	}
	
	//returns sorted copy
	public Poem[] getSortedCopy() {
		Poem[] popCopy = new Poem[this.size];
		for (int i=0; i<this.size; i++) {
			popCopy[i] = new Poem(individuals[i]);
		}
		Arrays.sort(popCopy, new Comparator<Poem>() {
			@Override
			public int compare(Poem poem1, Poem poem2) {
				if (poem1.getFitness() > poem2.getFitness()) {
					return -1;
				} else if (poem1.getFitness() < poem2.getFitness()) {
					return 1;
				}
				return 0;
			}
		});
		return popCopy;
	}
}
