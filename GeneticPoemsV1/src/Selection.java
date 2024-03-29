import java.util.concurrent.ThreadLocalRandom;

//class implementing tournament selection
public class Selection {

	//tournament selection; returns fittest poem out of tournament group
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
	
}
