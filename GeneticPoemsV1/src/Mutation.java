import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

public class Mutation {
	//number of different mutation methods
	private static int mNum = 3;
	
	public static void mutate(double mutationRate, Poem poem) {
		double p = ThreadLocalRandom.current().nextDouble();
		if (p<=mutationRate) {
			int m = ThreadLocalRandom.current().nextInt(0, mNum);
			if(m==0) {
				singleWordMutation(poem);
			} else if(m==1) {
				multipleWordMutation(poem);
			} else if(m==3) {
				if (poem.getRhymeFitness() == 1.0) {
					return;
				}
				endWordMutation(poem);
			}
		}	
	}
	
	//randomly change one word into another word of the same POS
	//take full poem as parameter or rather just one line??
	public static void singleWordMutation(Poem poem) {
		int lineIndex =  ThreadLocalRandom.current().nextInt(0, poem.length());
		ArrayList<TreeNode> chosenLine = poem.getPoemTree()[lineIndex].getPreorderArray();
		ArrayList<LeafNode> wordNodes = new ArrayList<LeafNode>();
		for (int i=0; i<chosenLine.size(); i++) {
			if(chosenLine.get(i).getRightChild() == null) {
				wordNodes.add((LeafNode) chosenLine.get(i).getLeftChild());
			}
		}
		int wordIndex = ThreadLocalRandom.current().nextInt(0, wordNodes.size());
		wordNodes.get(wordIndex).replacePos();
	}
	
	//mutates multiple words most of the time
	public static void multipleWordMutation(Poem poem) {
		int lineIndex =  ThreadLocalRandom.current().nextInt(0, poem.length());
		ArrayList<TreeNode> chosenLine = poem.getPoemTree()[lineIndex].getPreorderArray();
		ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
		for (int i=0; i<chosenLine.size(); i++) {
			if(chosenLine.get(i).getRightChild() != null) {
				nodes.add(chosenLine.get(i));
			}
		}
		
		int subtreeIndex = ThreadLocalRandom.current().nextInt(1, nodes.size());
		nodes.get(subtreeIndex).replacePos();
	}
	
	//mutates the last word of a line
	public static void endWordMutation(Poem poem) {
		int lineIndex =  ThreadLocalRandom.current().nextInt(0, poem.length());
		ArrayList<TreeNode> chosenLine = poem.getPoemTree()[lineIndex].getPreorderArray();
		chosenLine.get(chosenLine.size()-1).replacePos();
	}
	
	//randomly change one subtree into another randomly creates subtree that fits the same category
	//does not work because CYK algorithm cannot parse subtrees, only trees from complete sentences
//	public static void subtreeMutation(Poem poem) {
//		int lineIndex =  ThreadLocalRandom.current().nextInt(0, poem.length());
//		ArrayList<TreeNode> chosenLine = poem.getPoemTree()[lineIndex].getPreorderArray();
//
//		int subtreeIndex = ThreadLocalRandom.current().nextInt(1, chosenLine.size());
//		String category = chosenLine.get(subtreeIndex).getCategory();
//		
//		Tree newTree = new RandomPoemGenerator().expandGrammarFrom(category);
//		poem.getPoemTree()[lineIndex].switchSubtree(subtreeIndex, newTree.getRoot());
//	}

}
