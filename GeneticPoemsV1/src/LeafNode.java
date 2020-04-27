import java.util.ArrayList;

import rita.RiTa;

public class LeafNode extends TreeNode {
	private String word;

	public LeafNode(String category) {
		super(category);
		this.word = "";
	}
	
	@Override
	public String toString() {
		String toString = category + ":" + word + " ";
		return toString;
	}
	
	@Override
	public String toSentence() {
		String toSentence = word + " ";
		return toSentence;
	}
	
	@Override
	public void replacePos() {
		
		this.word = RiTa.randomWord(this.category);
		
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	@Override
	public LeafNode copySubtree() {
		LeafNode leaf = new LeafNode(this.category);
		leaf.setWord(this.word);
		return leaf;
	}
}
