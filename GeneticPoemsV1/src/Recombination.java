import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Recombination {
	//number of different recombination methods
	private static int rNum = 4;
	
	public static Poem[] crossover(double crossoverRate, Poem poem1, Poem poem2) {
		double p = ThreadLocalRandom.current().nextDouble();

		if (p<=crossoverRate) {
			Poem child1 = new Poem(poem1);
			Poem child2 = new Poem(poem2);
			//children = onePointLineCrossover(child1, child2);
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

	//this crossover function randomly selects one subtree from poem1 and searches for a subtree of the same category in poem2
	public static Poem[] subtreeCrossover(Poem poem1, Poem poem2) {
		int poemLength = poem1.length();
		double p = 1.0/poemLength;
		//int crossoverPointsNum = ThreadLocalRandom.current().nextInt(1, poemLength+1);
		for (int i=0; i<poemLength; i++) {
			if (p>=ThreadLocalRandom.current().nextDouble()) {
				Tree currentLineP1 = poem1.getPoemTrees()[i];
				Tree currentLineP2 = poem2.getPoemTrees()[i];
				if (currentLineP1.toSentence().equals(currentLineP2.toSentence())) {
					continue;
				}
				int index = ThreadLocalRandom.current().nextInt(1, currentLineP1.getPreorderArray().size());
				String category = currentLineP1.getPreorderArray().get(index).getCategory();
				//System.out.println(category);
				//ArrayList<Tree> subtrees = currentLineP2.getSubtrees(category);
				TreeNode subtree = null;
				//int index2 = 0;
				
				//find a way to pick the subtree form poem2 in a more random manner (not always the first one that is found)
				//idea: save all possible subtrees in an array, randomly pick a subtree from that array
				ArrayList<Integer> switchableSubtrees = new ArrayList<Integer>();
				for (int j=0; j<currentLineP2.getPreorderArray().size(); j++) {
					TreeNode node = currentLineP2.getPreorderArray().get(j);
					if (node.getCategory().equals(category)) {
						switchableSubtrees.add(j);
						//subtree = node;
						//index2 = j;
					}
				}
				if(!switchableSubtrees.isEmpty()) {
					int randIndex = ThreadLocalRandom.current().nextInt(0, switchableSubtrees.size());
					int indexP2 = switchableSubtrees.get(randIndex);
					TreeNode subtreeP2Copy = currentLineP2.getPreorderArray().get(indexP2).copySubtree();
					currentLineP2.switchSubtree(indexP2, currentLineP1.getPreorderArray().get(index));
					currentLineP1.switchSubtree(index, subtreeP2Copy);
				}
//				if (subtree != null) {
//					TreeNode subtreeCopy = subtree.copySubtree();
//					currentLineP2.switchSubtree(index2, currentLineP1.getPreorderArray().get(index));
//					currentLineP1.switchSubtree(index, subtreeCopy);
//				}
			}
		}
		return new Poem[]{poem1, poem2};
	}
}
