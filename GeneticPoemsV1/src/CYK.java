import java.util.HashMap;
import java.util.ArrayList;

public class CYK {	
	
	public static Tree parseTree(String[] line, String[][] grammar) {
		int n = line.length;
		
		int r = grammar.length;
	
		TreeNode[][][] p = new TreeNode[n][n][r];
		
		HashMap<String, Integer> ruleIndices = new HashMap<>();
		
		for (int i=0; i<grammar.length; i++) {
			ruleIndices.put(grammar[i][0], i);
		}
		
		ArrayList<String[]> termRules = new ArrayList<String[]>();
		
		for (String[] rule : grammar) {
			for (int i=1; i<rule.length; i++) {
				//check if lowercase
				if (rule[i].toLowerCase().equals(rule[i])) {
					String[] termRule = {rule[0], rule[i]};
					termRules.add(termRule);
				}
			}
		}
		
		ArrayList<String[]> nonTermRules = new ArrayList<String[]>();
		
		for (String[] rule : grammar) {
			for (int i=1; i<rule.length; i++) {
				//check if uppercase
				if (rule[i].toUpperCase().equals(rule[i])) {
					String[] symbols = rule[i].split("\\s+");
					String[] nonTermRule = {rule[0], symbols[0], symbols[1]};
					nonTermRules.add(nonTermRule);
				}
			}
		}
		
		for (int s=0; s<n; s++) {
			for (int i=0; i<termRules.size(); i++) {
				if (termRules.get(i)[1].equals(line[s])) {
					int index = ruleIndices.get(termRules.get(i)[0]);
					p[0][s][index] = new TreeNode(termRules.get(i)[0], new LeafNode(termRules.get(i)[1]));
				}
			}
		}
		
		for (int l=2; l<=n; l++) { //length of span
			for (int s=0; s<=(n-l); s++) { //start of span
				for (int k=1; k<=l-1; k++) { //partition of span
					for (int i=0; i<nonTermRules.size(); i++) {
						String[] nonterm = nonTermRules.get(i);
						String rb = nonterm[1];
						String rc = nonterm[2];
												
						int i1 = ruleIndices.get(rb);
						int i2 = ruleIndices.get(rc);
						int a = ruleIndices.get(nonterm[0]);
						
						if (p[k-1][s][i1] != null && p[l-k-1][s+k][i2] != null) {
							p[l-1][s][a] = new TreeNode(nonterm[0], p[k-1][s][i1], p[l-k-1][s+k][i2]);
						}						
					}
				}
			}
		}
		
		if (p[n-1][0][0] != null) {
			return new Tree(p[n-1][0][0]);
		} else {
			System.out.println("parsing failed");
			return null;
		}
	}
}
