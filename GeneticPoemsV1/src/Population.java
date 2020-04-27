
public class Population {
	private int size;
	private Poem[] individuals;
	private double avrgFitness = -1;

	public Population(int size) {
		this.size = size;
		this.individuals = new Poem[size];
	}
	
	public void initialzePopulation(int lines) {
		RandomPoemGenerator generator = new RandomPoemGenerator();
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
	
//	public Poem getIndividual(int index) {
//		return individuals[index];
//	}
	
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
	
	public Poem getFittest() {
		Poem fittest = individuals[0];
		for (int i=1; i<size; i++) {
			if (individuals[i].getFitness() > fittest.getFitness()) {
				fittest = individuals[i];
			}
		}
		return fittest;
	}
}
