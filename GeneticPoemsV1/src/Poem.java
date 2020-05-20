import rita.RiTa;

public class Poem {
	private Tree[] poemTree;
	//speichern als string und als baum könnte zu problemen bei rekombination führen 
	//-> nach jeder rekombination muss unbedingt der String geupdated werden
	//private String[] poemString;
	private double metricFitness =-1;
	private double rhymeFitness = -1;
	private double fitness = -1;
	private double[] fitnessPerLine;
	
	public Poem(Tree[] poemTree) {
		this.poemTree = poemTree;

		//this.fitnessPerLine = new double[length()];
		
//		this.poemString = new String[poemTree.length];
//		for (int i=0; i<poemString.length; i++) {
//			this.poemString[i] = poemTree[i].toSentence();
//		}
	}
	
	//copy constructor
	public Poem(Poem poem) {
		this.poemTree = new Tree[poem.length()];
		for (int i=0; i<poem.length(); i++) {
				this.poemTree[i] = new Tree(poem.poemTree[i]);
		}
		this.fitness = poem.fitness;
		this.fitnessPerLine = poem.fitnessPerLine;
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

	public double[] getFitnessPerLine() {
		return fitnessPerLine;
	}

	public void setFitnessPerLine(double[] fitnessPerLine) {
		this.fitnessPerLine = fitnessPerLine;
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
	
	public String printWithStresses() {
		String toString = "";
		for (String line : getPoemString()) {
			toString += line + "\n" + RiTa.getStresses(line) + "\n";
		}
		return toString;
	}

}
