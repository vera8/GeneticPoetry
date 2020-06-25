import rita.*;
import java.util.ArrayList;
import java.util.HashMap;




public class RandomPoemGenerator 
{
	private RiGrammar cfg;
	private String[][] grammar = { 
			{"S", "NP VP", "NP VBZ", "NP AP"},
			{"NP", "DT NN", "PRP$ NN"}, 
			{"VP", "MD VBB", "VBZ RB", "VBD PP"},
			{"AP", "LV JJ"},
			{"PP", "IN NP"},
			{"DT", "dt"},
			{"NN", "nn"},
			{"PRP$", "prp$"},
			{"VBB", "vb"},
			{"VBD", "vbd"},
			{"VBZ", "vbz"},
			{"MD", "md"},
			{"IN", "in"},
			{"RB", "rb"},
			{"LV", "is", "looks", "seems", "feels", "remains"},
			{"JJ", "jj"}
	};
	public static HashMap<String, ArrayList<String>> rhymeWords;
	private int totalPoemNum = 0;
	
	//constructor; boolean parameter to set whether or not rhyme words should be added to the grammar
	public RandomPoemGenerator(boolean addRhymes) {
		createRiGrammar(addRhymes);
	}
	
	//constructor with popSize parameter
	public RandomPoemGenerator(boolean addRhymes, int popSize) {
		this.totalPoemNum = popSize;
		createRiGrammar(addRhymes);
	}
	
	private void createRiGrammar(boolean addRhymes) {
		RiTa.SILENT = true;
		cfg = new RiGrammar();
		if (addRhymes)
		{
			addRhymesToGrammar();
		}
		cfg.addRule("<start>", "S");
		for (int i=0; i<grammar.length; i++) {
			for (int j=1; j<grammar[i].length; j++) {
				cfg.addRule(grammar[i][0], grammar[i][j]);
			}
		}
	}
	
	public Poem generatePoem(int lines) {
		String[] poemString = new String[lines];
		Tree[] poemTree = new Tree[lines];
		for (int i=0; i<lines; i++) {
			poemString[i] = cfg.expand();
			String[] temp = RiTa.tokenize(poemString[i], " ");
			poemTree[i] = CYK.parseTree(temp, grammar);
			poemTree[i].replacePos();
			
			poemString[i] = RiTa.untokenize(temp);
		}
		Poem poem = new Poem(poemTree);
		
		return poem;
	}
	
	
	public Tree expandGrammarWith(String symbol) {
		String part = cfg.expandFrom(symbol);
		String sentence = cfg.expandWith(part, symbol);
		String[] sentenceTkn = RiTa.tokenize(sentence, " ");
		Tree tree = CYK.parseTree(sentenceTkn, grammar);
		tree.replacePos();
		ArrayList<Tree> subtrees = tree.getSubtrees(symbol);
		if (!subtrees.isEmpty()) {
			return subtrees.get(0);
		} else {
			return null;
		}
	}
	
	private void addRhymesToGrammar() {
		int rhymeWordListSize = totalPoemNum*2;
		RandomPoemGenerator.rhymeWords = writeRhymeWordList(rhymeWordListSize);
		for (int i=0; i<grammar.length; i++) {
			String pos = grammar[i][0].toLowerCase();
			if (rhymeWords.containsKey(pos)) {
				String[] rhymeWordArray = new String[rhymeWords.get(pos).size()];
				rhymeWordArray = rhymeWords.get(pos).toArray(rhymeWordArray);
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
		rhymeWords.put("jj", new ArrayList<String>());
		
		while (rhymeWords.get("nn").size() <= length || rhymeWords.get("vb").size() <= length || rhymeWords.get("vbz").size() <= length ||
				rhymeWords.get("vbd").size() <= length || rhymeWords.get("rb").size() <= length || rhymeWords.get("jj").size() <= length) {
			for (String pos : rhymeWords.keySet()) {
				String rhymeWord = RiTa.randomWord(pos);
				String[] rhymeList = RiTa.rhymes(rhymeWord);
				if (rhymeList.length != 0) {
					rhymeWords.get(pos).add(rhymeWord);
				}
				
				for (int i=0; i<rhymeList.length; i++) {
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
