import rita.RiTa;

public class Poem {
	private Tree[] poemTrees;
	private double metricFitness =-1;
	private double rhymeFitness = -1;
	private double emotionFitness = -1;
	private double fitness = -1;
	
	public Poem(Tree[] poemTree) {
		this.poemTrees = poemTree;
	}
	
	//copy constructor
	public Poem(Poem poem) {
		this.poemTrees = new Tree[poem.length()];
		for (int i=0; i<poem.length(); i++) {
				this.poemTrees[i] = new Tree(poem.poemTrees[i]);
		}
		this.fitness = poem.fitness;
	}
	
	public String toString() {
		String toString = "";
		for (String line : getPoemString()) {
			toString += line + "\n";
		}
		return toString;
	}
	
	public String toHTMLString() {
		String toString = "<html>";
		for (String line : getPoemString()) {
			toString += line + "<br>";
		}
		return toString;
	}
	
	public String printPoemTrees() {
		String treeString = "";
		for (Tree line : poemTrees) {
			treeString += line + "\n";
		}
		return treeString;
	}
	
	
	public String[] getPoemString() {
		String[] poemString = new String[poemTrees.length];
		for (int i=0; i<poemString.length; i++) {
			poemString[i] = poemTrees[i].toSentence();
		}
		return poemString;
	}
	
	public void changeLine(int index, Tree line) {
		poemTrees[index] = line;
	}
	
	public Tree[] getPoemTrees() {
		return poemTrees;
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public int length() {
		return poemTrees.length;
	}

	public void setMetricFitness(double metricFitness) {
		this.metricFitness = metricFitness;
	}

	public double getMetricFitness() {
		return metricFitness;
	}
	
	public void setRhymeFitness(double rhymeFitness) {
		this.rhymeFitness = rhymeFitness;
	}

	public double getRhymeFitness() {
		return rhymeFitness;
	}
	
	public double getEmotionFitness() {
		return emotionFitness;
	}
	
	public void setEmotionFitness(double emotionFitness) {
		this.emotionFitness = emotionFitness;
	}
	
	public String printWithStresses() {
		String toString = "";
		for (String line : getPoemString()) {
			toString += line + "\n" + RiTa.getStresses(line) + "\n";
		}
		return toString;
	}
}
