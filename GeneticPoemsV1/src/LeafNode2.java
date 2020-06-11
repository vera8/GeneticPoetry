import java.util.ArrayList;

import rita.RiTa;

public class LeafNode2 extends TreeNode2 {
	private String word;

	public LeafNode2(String category) {
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
		try {
			this.word = RiTa.randomWord(this.category);
		} catch (rita.RiTaException re) {
			this.word = this.category;
			this.category = RiTa.getPosTags(this.word)[0];
		}
		
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	@Override
	public LeafNode2 copySubtree() {
		LeafNode2 leaf = new LeafNode2(this.category);
		leaf.setWord(this.word);
		return leaf;
	}
}
