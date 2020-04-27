import rita.*;
import java.io.BufferedReader;
import java.io.FileReader;


public class RandomPoemGenerator 
{

	private RiGrammar cfg;
	private String file;
	private String[][] grammar = { 
			{"S", "NP VP"},
			{"NP", "DET NN", "PRP$ NN"}, 
			{"VP", "vbz", "MD VB", "VBZ RB", "VBD PP"},
			{"PP", "IN NP"},
			{"DET", "dt"},
			{"NN", "nn"},
			{"PRP$", "prp$"},
			{"VBD", "vbd"},
			{"VBZ", "vbz"},
			{"VB", "vb"},
			{"MD", "md"},
			{"IN", "in"},
			{"RB", "rb"}
	};
	
	public RandomPoemGenerator() {
		createRiGrammar();
		file = "C:\\Users\\Vera\\Documents\\Uni\\Bachelorarbeit\\Algorithm\\GeneticPoemsV1\\src\\grammar_files\\grammar1.json";
	}
	
	private void createRiGrammar() {
		cfg = new RiGrammar();
		cfg.addRule("<start>", "S");
		for (int i=0; i<grammar.length; i++) {
			for (int j=1; j<grammar[i].length; j++) {
				cfg.addRule(grammar[i][0], grammar[i][j]);
			}
		}
		
		//cfg.loadFrom("C:\\Users\\Vera\\Documents\\Uni\\Bachelorarbeit\\Algorithm\\GeneticPoemsV1\\src\\grammar_files\\grammar1.json");
	}
	
	public Poem generatePoem(int lines) {
		String[] poemString = new String[lines];
		Tree[] poemTree = new Tree[lines];
		for (int i=0; i<lines; i++) {
			poemString[i] = cfg.expand();
			String[] temp = RiTa.tokenize(poemString[i], " ");
			
			//RiTa macht aus lowercase z teilweise uppercase Z -> workaround finden, wie zb. diese Lösing hier
			for (int j=0; j<temp.length; j++) {
				temp[j] = temp[j].toLowerCase();
			}
			poemTree[i] = CYK.parseTree(temp, grammar);
			poemTree[i].replacePos();
			
			poemString[i] = RiTa.untokenize(temp);
		}
		Poem poem = new Poem(poemTree);
		
		return poem;
	}
	
	//does not work
	public Tree expandGrammarFrom(String symbol) {
		String[] sentencePart = RiTa.tokenize(cfg.expandFrom(symbol), " ");
		for (int i=0; i<sentencePart.length; i++) {
			sentencePart[i] = sentencePart[i].toLowerCase();
		}
		String[][] alteredGrammar = grammar;

		Tree tree = CYK.parseTree(sentencePart, grammar);
		tree.replacePos();
		return tree;
	}
	
	public String[][] getGrammar(){	
		return grammar;
	}
}
