import rita.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class TestMain {

	public static void main(String[] args) {
		RandomPoemGenerator poemGenerator = new RandomPoemGenerator(false);
		Poem firstPoem = poemGenerator.generatePoem(4);
		Poem secondPoem = poemGenerator.generatePoem(4);
		FitnessCalculator fitnessCalculator = new FitnessCalculator();
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
				{"VB", "meows", "farts", "chews", "eats", "purrs", "screams", "laughs", "claps", "jumps"},
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
//		double[] fitnessPL = FitnessCalculator.calculateMetricFitness(firstPoem);
//		for (int i= 0; i<firstPoem.length(); i++) {
//			System.out.println(firstPoem.getPoemString()[i]);
//			System.out.println(RiTa.getStresses(firstPoem.getPoemString()[i]));
//			System.out.println(fitnessPL[i]);
//		}
		System.out.println(firstPoem);
		fitnessCalculator.calculateFitness(firstPoem);
		System.out.println(firstPoem.getFitness());
		System.out.println(secondPoem);
//		Poem[] crossedover = Recombination.lineCrossover(firstPoem, secondPoem);
//		for (Poem poem : crossedover) {
//			System.out.println(poem);
//		}
		
//		Recombination.subtreeCrossover(firstPoem, secondPoem);
//		System.out.println(firstPoem);
//		System.out.println(secondPoem);
		
//		Mutation.multipleWordMutation(firstPoem);
//		System.out.println(firstPoem);
		
		//System.out.println(firstPoem.getPoemTree()[0]);
		
		

		for (TreeNode treenode : firstPoem.getPoemTrees()[0].preorderArray) {
			//System.out.print(treenode.getCategory() + " ");
		}
		System.out.println();

		
		//System.out.println(firstPoem.getPoemTree()[0].contains("xfg"));

		
//		ArrayList<Tree> subtrees = firstPoem.getPoemTree()[0].getSubTrees("AB");
//		for(Tree tree : subtrees) {
//			System.out.println(tree);
//		}
		
		//System.out.println("Fitness: " + FitnessCalculator.calculateFitness(firstPoem));

//		System.out.println(RiTa.isRhyme("shyly", "operationally"));
//		
//		System.out.println();
//		FitnessCalculator.calculateFitness(firstPoem);
//		System.out.println("fitness:" + firstPoem.getFitness());
//		FitnessCalculator.calculateFitness(firstPoem);
//		System.out.println("fitness:" + firstPoem.getFitness());
//		FitnessCalculator.calculateFitness(firstPoem);
//		System.out.println("fitness:" + firstPoem.getFitness());
		
//		for (int i=0; i<poemGenerator.getGrammar().length; i++) {
//			for (int j=0; j<poemGenerator.getGrammar()[i].length; j++) {
//				System.out.print(poemGenerator.getGrammar()[i][j] + ", ");
//				
//			}
//			System.out.println();
//		}
		
		System.out.println(fitnessCalculator.isRhyme("must", "sadist"));
		System.out.println(fitnessCalculator.isRhyme("charitably ", "warningly"));
		System.out.println(RiTa.getPhonemes("shows") + " : " + RiTa.getPhonemes("snows"));
				
//		TreeMap<String, ArrayList<String>> rhymeList = writeRhymeWordList(100);
//		for (String pos : rhymeList.keySet()) {
//			System.out.println(pos + ": " + rhymeList.get(pos));
//		}
		
		System.out.println(firstPoem.printWithStresses());
		double[] fitness = fitnessCalculator.calculateMetricFitness(firstPoem);
		for(int i=0; i<fitness.length; i++) {
			System.out.println(fitness[i]);
		}
		
		System.out.println();
//		"their folk snipes"
//		"these folk can scorch"
		
		System.out.println(firstPoem);
		for (int i=0; i<300; i++) {
			Mutation.subtreeMutation(firstPoem);
		}
		//System.out.println(firstPoem);
		System.out.println(RiTa.stem("feels"));
		System.out.println(RiTa.containsWord("looks"));
		System.out.println(RiTa.getStresses("looks"));

	}
	
	public static TreeMap<String, ArrayList<String>> writeRhymeWordList(int length) {
		TreeMap<String, ArrayList<String>> rhymeWords = new TreeMap<>();
		rhymeWords.put("nn", new ArrayList<String>());
		rhymeWords.put("vb", new ArrayList<String>());
		rhymeWords.put("vbz", new ArrayList<String>());
		rhymeWords.put("vbd", new ArrayList<String>());
		rhymeWords.put("rb", new ArrayList<String>());
		
		while (rhymeWords.get("nn").size() <= length || rhymeWords.get("vb").size() <= length || rhymeWords.get("vbz").size() <= length ||
				rhymeWords.get("vbd").size() <= length || rhymeWords.get("rb").size() <= length) {
			for (String pos : rhymeWords.keySet()) {
				String rhymeWord = RiTa.randomWord(pos);
				String[] rhymeList = RiTa.rhymes(rhymeWord);
				if (rhymeList.length != 0) {
					rhymeWords.get(pos).add(rhymeWord);
				}
				
				for (int i=0; i<rhymeList.length; i++) {
					//System.out.println(i);
					String rwPos = RiTa.getPosTags(rhymeList[i])[0];
					if (rhymeWords.get(rwPos)!=null) {	
						if (rhymeWords.get(rwPos).size() <= length) {
							rhymeWords.get(rwPos).add(rhymeList[i]);
						}
					}
				}
			}
		}
		return rhymeWords;
	}
}


