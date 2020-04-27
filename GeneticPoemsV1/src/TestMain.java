import rita.*;
import java.util.ArrayList;

public class TestMain {

	public static void main(String[] args) {
		RandomPoemGenerator poemGenerator = new RandomPoemGenerator();
		Poem firstPoem = poemGenerator.generatePoem(4);
		Poem secondPoem = poemGenerator.generatePoem(4);
		
//		String[][] poems = new String[100][];
		
//		for (int i=0; i<100; i++) {
//			poems[i] = generatePoem(4);
//		
//		}
		
//		for (String line : firstPoem) {
//			System.out.println(line);
//		}
		
//		TreeNode treenode = new TreeNode("NP");
//		System.out.println(treenode);
//		TreeNode node2 = new TreeNode("Det");
//		treenode.setLeftChild(node2);
//		System.out.println(treenode);
//		treenode.setRightChild(new TreeNode ("NN"));	
//		System.out.println(treenode);
//		System.out.println(node2);
		String[][] grammar = {
				{"S", "NP VP"},
				{"NP", "DET NN", "mom", "max"},
				{"VP", "VB ADV"},
				{"DET", "the"},
				{"NN", "cat", "dog"},
				{"VB", "meows", "farts", "chews"},
				{"ADV", "loudly", "quietly"}
		};
									
		String[] line = {"mom","farts","loudly"};
		
		RiGrammar rg = new RiGrammar();
		rg.addRule("<start>", "S");
		for (int i=0; i<grammar.length; i++) {
			for (int j=1; j<grammar[i].length; j++) {
				rg.addRule(grammar[i][0], grammar[i][j]);
			}
		}
		String str1 = rg.expand();
		String str2 = rg.expand();
		System.out.println(str1);
		System.out.println(str2);
		Tree tree1 = CYK.parseTree(RiTa.tokenize(str1), grammar);
		Tree tree2 = CYK.parseTree(RiTa.tokenize(str2), grammar);
		tree1.switchSubtree(2, tree2.preorderArray.get(2));
		System.out.println(tree1);
		for (TreeNode node : tree1.preorderArray) {
			System.out.print(node.getCategory() + " ");
			
		}
		//System.out.println(isGrammar2(line, grammar));

		//System.out.println(CYK.parseTree(line, grammar).toSentence());
		
		
//		Tree tree = new Tree(new TreeNode("NP", new TreeNode("Det", new TreeNode("the")), new TreeNode ("NN", new TreeNode("cat"))));
//		//Tree tree = new Tree(treenode);
//		String treestring = tree.toString();
//		String treesentence = tree.toSentence();
//		System.out.println(treestring);
//		System.out.println(treesentence);

		System.out.println();
		//System.out.println(firstPoem.printPoemTree());
		System.out.println(firstPoem);
		//System.out.println(secondPoem);
//		Poem[] crossedover = Recombination.lineCrossover(firstPoem, secondPoem);
//		for (Poem poem : crossedover) {
//			System.out.println(poem);
//		}
		
//		Recombination.subtreeCrossover(firstPoem, secondPoem);
//		System.out.println(firstPoem);
//		System.out.println(secondPoem);
		
		Mutation.multipleWordMutation(firstPoem);
		System.out.println(firstPoem);
		
		//System.out.println(firstPoem.getPoemTree()[0]);
		
		

		for (TreeNode treenode : firstPoem.getPoemTree()[0].preorderArray) {
			//System.out.print(treenode.getCategory() + " ");
		}
		System.out.println();

		
		//System.out.println(firstPoem.getPoemTree()[0].contains("xfg"));

		
//		ArrayList<Tree> subtrees = firstPoem.getPoemTree()[0].getSubTrees("AB");
//		for(Tree tree : subtrees) {
//			System.out.println(tree);
//		}
		
		//System.out.println("Fitness: " + FitnessCalculator.calculateFitness(firstPoem));
		
		
	}

}
