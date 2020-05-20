import rita.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;




public class RandomPoemGenerator 
{
	private RiGrammar cfg;
	private String file;
	private String[][] grammar = { 
			{"S", "NP VP"},
			{"NP", "DET NN", "PRP$ NN"}, 
			{"VP", "vbz", "MD VBB", "VBZ RB", "VBD PP"},
			{"PP", "IN NP"},
			{"DET", "dt"},
			{"NN", "nn"},
			//{"NN", "nn", "snow", "state", "slate", "plate", "hand", "band", "sand"},
			{"PRP$", "prp$"},
			{"VBD", "vbd"},
			{"VBZ", "vbz"},
			//{"VBZ", "tries", "wakes", "takes", "makes", "goes", "shows", "snows", "flows", "glows", "grows"},
			{"VBB", "vb"},
			//{"VBB", "stand", "land", "know", "show", "go", "flow", "snow", "grow", "hate", "wait"},
			{"MD", "md"},
			{"IN", "in"},
			{"RB", "rb"},
			//{"RB", "gladly", "badly", "sadly", "slowly", "wisely", "precisely", "widely"}
	};
	
	public RandomPoemGenerator() {
		createRiGrammar();
		file = "C:\\Users\\Vera\\Documents\\Uni\\Bachelorarbeit\\Algorithm\\GeneticPoemsV1\\src\\grammar_files\\grammar1.json";
	}
	
	private void createRiGrammar() {
		cfg = new RiGrammar();
		addRhymesToGrammar();
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
			
			//RiTa macht aus lowercase z teilweise uppercase Z, scheint ein Fehler zu sein -> workaround finden, wie zb. diese Lösing hier
//			for (int j=0; j<temp.length; j++) {
//				temp[j] = temp[j].toLowerCase();
//			}
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
	
	private void addRhymesToGrammar() {
		HashMap<String, ArrayList<String>> rhymeWordList = writeRhymeWordList(1000);
		for (int i=0; i<grammar.length; i++) {
			String pos = grammar[i][0].toLowerCase();
			if (rhymeWordList.containsKey(pos)) {
				String[] rhymeWordArray = new String[rhymeWordList.get(pos).size()];
				rhymeWordArray = rhymeWordList.get(pos).toArray(rhymeWordArray);
				String[] ruleArray = new String[rhymeWordArray.length+1];
				ruleArray[0] = pos.toUpperCase();
				System.arraycopy(rhymeWordArray, 0, ruleArray, 1, rhymeWordArray.length);
				grammar[i] = ruleArray;
				
			}
		}
	}
	
	private HashMap<String, ArrayList<String>> writeRhymeWordList(int length) {
		HashMap<String, ArrayList<String>> rhymeWords = new HashMap<>();
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
	
	public String[][] getGrammar(){	
		return grammar;
	}
	
}
