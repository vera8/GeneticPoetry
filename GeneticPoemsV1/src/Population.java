import java.util.Arrays;
import java.util.Comparator;

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
	
	public double calculateFitnessVariance() {
		double variance = 0;
		for (int i=0; i<size; i++) {
			variance += Math.pow((individuals[i].getFitness() - avrgFitness), 2);
		}
		variance = variance/size;
		return variance;
	}
	
	public int calculatePoemVariance() {
		int differenceCount = 0;
		for (int i=0; i<size; i++) {
			for (int j=i+1; j<size; j++) {
				for (int k=0; k<individuals[i].getPoemString().length; k++) {
					String line1 = individuals[i].getPoemString()[k];
					String line2 = individuals[j].getPoemString()[k];
					if (!line1.equals(line2)) {
						differenceCount++;
					} 
				}
			}
		}
		return differenceCount;
	}
}
