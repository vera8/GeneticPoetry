import rita.RiTa;

public class Poem {
	private Tree[] poemTrees;
	private double metricFitness =-1;
	private double rhymeFitness = -1;
	private double emotionFitness = -1;
	private double fitness = -1;
	private double[] fitnessPerLine;
	
	public Poem(Tree[] poemTree) {
		this.poemTrees = poemTree;

		//this.fitnessPerLine = new double[length()];
		
//		this.poemString = new String[poemTree.length];
//		for (int i=0; i<poemString.length; i++) {
//			this.poemString[i] = poemTree[i].toSentence();
//		}
	}
	
	//copy constructor
	public Poem(Poem poem) {
		this.poemTrees = new Tree[poem.length()];
		for (int i=0; i<poem.length(); i++) {
				this.poemTrees[i] = new Tree(poem.poemTrees[i]);
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
	
	public boolean paretoDominates(Poem poem2) {
		boolean dominates = false;
		double metricRhymeComb = (this.metricFitness + this.rhymeFitness)/2.0;
		double metricRhymeCombP2 = (poem2.getMetricFitness() + poem2.getRhymeFitness())/2.0;
//		if (this.getMetricFitness() > poem2.getMetricFitness()) {
//			dominates = true; 
//		} else if (this.getMetricFitness() < poem2.getMetricFitness()) {
//			return false;
//		}
//		if (this.getRhymeFitness() > poem2.getRhymeFitness()) {
//			dominates = true;
//		} else if (this.getRhymeFitness() < poem2.getRhymeFitness()) {
//			return false;
//		}
		if (metricRhymeComb > metricRhymeCombP2) {
			dominates = true;
		} else if (metricRhymeComb < metricRhymeCombP2) {
			return false;
		}
		if (this.getEmotionFitness() > poem2.getEmotionFitness()) {
			dominates = true;
		} else if (this.getEmotionFitness() < poem2.getEmotionFitness()) {
			return false;
		}
		
		return dominates;
	}
}
