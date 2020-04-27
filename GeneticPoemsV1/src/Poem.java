
public class Poem {
	private Tree[] poemTree;
	//speichern als string und als baum könnte zu problemen bei rekombination führen 
	//-> nach jeder rekombination muss unbedingt der String geupdated werden
	//private String[] poemString;
	private double fitness = -1;
	
	public Poem(Tree[] poemTree) {
		this.poemTree = poemTree;

//		this.poemString = new String[poemTree.length];
//		for (int i=0; i<poemString.length; i++) {
//			this.poemString[i] = poemTree[i].toSentence();
//		}
	}
	
	public String toString() {
		String toString = "";
		for (String line : getPoemString()) {
			toString += line + "\n";
		}
		return toString;
	}
	
	public String printPoemTree() {
		String treeString = "";
		for (Tree line : poemTree) {
			treeString += line + "\n";
		}
		return treeString;
	}
	
	
	public String[] getPoemString() {
		String[] poemString = new String[poemTree.length];
		for (int i=0; i<poemString.length; i++) {
			poemString[i] = poemTree[i].toSentence();
		}
		return poemString;
	}
	
	public void changeLine(int index, Tree line) {
		poemTree[index] = line;
	}
	
	public Tree[] getPoemTree() {
		return poemTree;
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public int length() {
		return poemTree.length;
	}

}
