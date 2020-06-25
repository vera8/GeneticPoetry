import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

//class that implements different crossover-methods
public class Recombination {
	//number of different recombination methods
	private static int rNum = 4;
	
	//randomly chooses a recombination method out of the implemented methods
	public static Poem[] crossover(double crossoverRate, Poem poem1, Poem poem2) {
		double p = ThreadLocalRandom.current().nextDouble();

		if (p<=crossoverRate) {
			//create copies of the parent poems as child poems
			Poem child1 = new Poem(poem1);
			Poem child2 = new Poem(poem2);

			int r = ThreadLocalRandom.current().nextInt(0, rNum);
			if(r==0) {
				return onePointLineCrossover(child1, child2);
			} else if(r==1) {
				return subtreeCrossover(child1, child2);
			} else if(r==2) {
				return uniformLineCrossover(child1, child2);
			} else if(r==3) {
				return pairedCrossover(child1, child2);
			}
		}
		return new Poem[]{poem1, poem2};
	}

	//crossover at one point in poems
	public static Poem[] onePointLineCrossover(Poem poem1, Poem poem2) {
		int poemLength = poem1.length();
		int crossoverPoint = ThreadLocalRandom.current().nextInt(1, poemLength);
 
		for (int i=crossoverPoint; i<poemLength; i++) {
			Tree temp = poem1.getPoemTrees()[i];
			poem1.changeLine(i, poem2.getPoemTrees()[i]);
			poem2.changeLine(i, temp);
		}
		return new Poem[]{poem1, poem2};
	}
	
	//uniform crossover
	public static Poem[] uniformLineCrossover(Poem poem1, Poem poem2) {
		int poemLength = poem1.length();
		
		for (int i=0; i<poemLength; i++) {
			double p = ThreadLocalRandom.current().nextDouble();
			if (p<0.5) {
				Tree temp = poem1.getPoemTrees()[i];
				poem1.changeLine(i, poem2.getPoemTrees()[i]);
				poem2.changeLine(i, temp);
			}
		}
		return new Poem[]{poem1, poem2};
	}
	
	//crossover is pairs
	public static Poem[] pairedCrossover(Poem poem1, Poem poem2) {
		for (int i=2; i<poem1.length(); i+=2) {
			Tree temp = poem1.getPoemTrees()[i];
			poem1.changeLine(i, poem2.getPoemTrees()[i]);
			poem2.changeLine(i, temp);
			
			temp = poem1.getPoemTrees()[i+1];
			poem1.changeLine(i+1, poem2.getPoemTrees()[i+1]);
			poem2.changeLine(i+1, temp);
		}
		return new Poem[]{poem1, poem2};
	}

	//randomly selects one subtree from poem1 and searches for a subtree of the same category in poem2
	public static Poem[] subtreeCrossover(Poem poem1, Poem poem2) {
		int poemLength = poem1.length();
		double p = 0.5;

		for (int i=0; i<poemLength; i++) {
			if (p>=ThreadLocalRandom.current().nextDouble()) {
				Tree currentLineP1 = poem1.getPoemTrees()[i];
				Tree currentLineP2 = poem2.getPoemTrees()[i];
				if (currentLineP1.toSentence().equals(currentLineP2.toSentence())) {
					continue;
				}
				int index = ThreadLocalRandom.current().nextInt(1, currentLineP1.getPreorderArray().size());
				String category = currentLineP1.getPreorderArray().get(index).getCategory();

				ArrayList<Integer> switchableSubtrees = new ArrayList<Integer>();
				for (int j=0; j<currentLineP2.getPreorderArray().size(); j++) {
					TreeNode node = currentLineP2.getPreorderArray().get(j);
					if (node.getCategory().equals(category)) {
						switchableSubtrees.add(j);
					}
				}
				if(!switchableSubtrees.isEmpty()) {
					int randIndex = ThreadLocalRandom.current().nextInt(0, switchableSubtrees.size());
					int indexP2 = switchableSubtrees.get(randIndex);
					TreeNode subtreeP2Copy = currentLineP2.getPreorderArray().get(indexP2).copySubtree();
					currentLineP2.switchSubtree(indexP2, currentLineP1.getPreorderArray().get(index));
					currentLineP1.switchSubtree(index, subtreeP2Copy);
				}
			}
		}
		return new Poem[]{poem1, poem2};
	}
}
