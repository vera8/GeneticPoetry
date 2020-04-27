import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

public class Selection {

	public static Poem tournamentSelection(int tournamentSize, Population pop) {
		int randIndex = ThreadLocalRandom.current().nextInt(0, pop.getSize());
		Poem winner = pop.getIndividuals()[randIndex];
		for (int i=1; i<tournamentSize; i++) {
			randIndex = ThreadLocalRandom.current().nextInt(0, pop.getSize());

			if (pop.getIndividuals()[randIndex].getFitness() > winner.getFitness()) {
				winner = pop.getIndividuals()[randIndex];
			}
		}
		return winner;
	}
	
	public static Poem[] truncationSelection(int num, Population pop) {
		Poem[] selected = new Poem[num];
		Arrays.sort(pop.getIndividuals(), new Comparator<Poem>() {
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
		
		selected = Arrays.copyOfRange(pop.getIndividuals(), 0, num);
		return selected;
	}
}
