import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

public class Recombination {
	//number of different recombination methods
	private static int rNum = 2;
	
	public static Poem[] crossover(double crossoverRate, Poem poem1, Poem poem2) {
		double p = ThreadLocalRandom.current().nextDouble();

		if (p<=crossoverRate) {
			Poem child1 = new Poem(poem1);
			Poem child2 = new Poem(poem2);
			//children = onePointLineCrossover(child1, child2);
			int r = ThreadLocalRandom.current().nextInt(0, rNum);
			if(r==0) {
				return onePointLineCrossover(poem1, poem2);
			} else if(r==1) {
				return subtreeCrossover(poem1, poem2);
			}
		}
		return new Poem[]{poem1, poem2};
	}

	public static Poem[] onePointLineCrossover(Poem poem1, Poem poem2) {
		int poemLength = poem1.length();
		int crossoverPoint = ThreadLocalRandom.current().nextInt(1, poemLength);
 
		for (int i=crossoverPoint; i<poemLength; i++) {
			Tree temp = poem1.getPoemTree()[i];
			poem1.changeLine(i, poem2.getPoemTree()[i]);
			poem2.changeLine(i, temp);
		}
		return new Poem[]{poem1, poem2};
	}

	//this crossover function randomly selects one subtree from poem1 and searches for a subtree of the same category in poem2
	public static Poem[] subtreeCrossover(Poem poem1, Poem poem2) {
		int poemLength = poem1.length();
		double p = 1.0/poemLength;
		//int crossoverPointsNum = ThreadLocalRandom.current().nextInt(1, poemLength+1);
		for (int i=0; i<poemLength; i++) {
			if (/*p>=ThreadLocalRandom.current().nextDouble()*/ true) {
				Tree currentLineP1 = poem1.getPoemTree()[i];
				Tree currentLineP2 = poem2.getPoemTree()[i];
				if (currentLineP1.toSentence().equals(currentLineP2.toSentence())) {
					continue;
				}
				int index = ThreadLocalRandom.current().nextInt(1, currentLineP1.getPreorderArray().size());
				String category = currentLineP1.getPreorderArray().get(index).getCategory();
				//System.out.println(category);
				//ArrayList<Tree> subtrees = currentLineP2.getSubtrees(category);
				TreeNode subtree = null;
				int index2 = 0;
				
				//find a way to pick the subtree form poem2 in a more random manner (not always the first one that is found)
				//idea: save all possible subtrees in an array, randomly pick a subtree from that array
				for (int j=0; j<currentLineP2.getPreorderArray().size(); j++) {
					TreeNode node = currentLineP2.getPreorderArray().get(j);
					if (node.getCategory().equals(category)) {
						subtree = node;
						index2 = j;
					}
				}
				if (subtree != null) {
					TreeNode subtreeCopy = subtree.copySubtree();
					currentLineP2.switchSubtree(index2, currentLineP1.getPreorderArray().get(index));
					currentLineP1.switchSubtree(index, subtreeCopy);
				}
			}
		}
		return new Poem[]{poem1, poem2};
	}
}
