import java.util.concurrent.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.HashMap;

//class containing implementations of different mutation methods
public class Mutation {
	//number of different mutation methods implemented
	private static int mNum = 4;
	
	//chooses a mutation method from the four implemented methods randomly
	public static void mutate(double mutationRate, Poem poem) {
		double p = ThreadLocalRandom.current().nextDouble();
		if (p<=mutationRate) {

			int m = ThreadLocalRandom.current().nextInt(0, mNum);
			if(m==0) {
				singleWordMutation(poem);
			} else if(m==1) {
				multipleWordMutation(poem);
			} else if(m==2) {
				if (poem.getRhymeFitness() == 1.0) {
					return;
				}
				endWordMutation(poem);
			} else if (m==3) {
				subtreeMutation(poem);
			}
		}	
	}
	
	//randomly changes one word into another word of the same POS
	public static void singleWordMutation(Poem poem) {
		int lineIndex =  ThreadLocalRandom.current().nextInt(0, poem.length());
		ArrayList<TreeNode> chosenLine = poem.getPoemTrees()[lineIndex].getPreorderArray();
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
		ArrayList<TreeNode> chosenLine = poem.getPoemTrees()[lineIndex].getPreorderArray();
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
		HashMap<String, ArrayList<String>> rhymeWords = RandomPoemGenerator.rhymeWords;
		for (int i=0; i<poem.length(); i++) {
			double p = ThreadLocalRandom.current().nextDouble();
			if (p<0.5) {				
				ArrayList<TreeNode> chosenLine = poem.getPoemTrees()[i].getPreorderArray();
				LeafNode leafNode = (LeafNode) chosenLine.get(chosenLine.size()-1).getLeftChild();
				String targetPos = leafNode.getCategory();
				if (rhymeWords.get(targetPos) != null) {
					int wordIndex = ThreadLocalRandom.current().nextInt(0, rhymeWords.get(targetPos).size());
					String newWord = rhymeWords.get(targetPos).get(wordIndex);
					leafNode.setWord(newWord);
				}
			}
		}
	}
	
	//randomly changes one subtree into another randomly created subtree that fits the same category
	public static void subtreeMutation(Poem poem) {
		int lineIndex =  ThreadLocalRandom.current().nextInt(0, poem.length());
		ArrayList<TreeNode> chosenLine = poem.getPoemTrees()[lineIndex].getPreorderArray();

		int subtreeIndex = ThreadLocalRandom.current().nextInt(1, chosenLine.size());
		String category = chosenLine.get(subtreeIndex).getCategory();
		
		Tree newTree = new RandomPoemGenerator(false).expandGrammarWith(category);
		if (newTree != null) {
			poem.getPoemTrees()[lineIndex].switchSubtree(subtreeIndex, newTree.getRoot());
		}
	}

}
