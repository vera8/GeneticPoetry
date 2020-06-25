import rita.RiTa;

//implements tree node without children
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
	
	//gets word from lexicon for PoS-tag
	@Override
	public void replacePos() {
		try {
			this.word = RiTa.randomWord(this.category);
		} catch (rita.RiTaException re) {
			this.word = this.category;
		}
		
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
