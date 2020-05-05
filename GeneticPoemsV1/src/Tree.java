import java.util.ArrayList;

public class Tree {
	private TreeNode root;
	public ArrayList<TreeNode> preorderArray;
	
	public Tree(TreeNode root) {
		this.root = root;
		//preorderArray = RiTa.tokenize(toString(), " ");
		preorderArray = fillPreorderArray();
	}
	
	//Copy constructor
	public Tree(Tree tree) {
		this.root = tree.root.copySubtree();
		this.preorderArray = fillPreorderArray();
	}
	
	public String toString() {
		String toString = root.toString();
		
		return toString;
	}
	
	public String toSentence() {
		String sentence = root.toSentence();
		
		return sentence;
	}
	
	public void replacePos() {
		root.replacePos();
	}
	
	//get all subtrees from certain category (~search for category)
	public ArrayList<Tree> getSubtrees(String category) {
		ArrayList<Tree> subtrees = root.getSubTrees(category);
		
		if(subtrees.isEmpty()) {
			System.out.println("no subtrees " + category);
		}
		return subtrees;
	}
	
	public int numOfSubtrees() {
		int numOfSubtrees = 0;
		numOfSubtrees += root.numOfSubtrees();
		return numOfSubtrees;
	}
	
	public int nodeCount() {
		int nodeCount = 0;
		return nodeCount += root.nodeCount();
	}
	
	public boolean contains(String category) {
		boolean contains = root.contains(category, false);
		return contains;
	}
	
	public ArrayList<TreeNode> fillPreorderArray(){
		return root.fillPreorderArray();	
	}
	
	public void switchSubtree(int index, TreeNode newSubtree) {
		TreeNode originalSubtree = preorderArray.get(index);
		if (!originalSubtree.getCategory().equals(newSubtree.getCategory())) {
			System.out.println("cannot switch");
			return;
		}
		if (originalSubtree instanceof LeafNode && newSubtree instanceof LeafNode) {
			((LeafNode) originalSubtree).setWord(((LeafNode) newSubtree).getWord());
		} else {
			originalSubtree.setLeftChild(newSubtree.getLeftChild());
			if(newSubtree.getRightChild() != null) {
				originalSubtree.setRightChild(newSubtree.getRightChild());
			} else {
				originalSubtree.setRightChild(null);
				
			}
		}
		this.preorderArray = fillPreorderArray();
		
	}
	
	public ArrayList<TreeNode> getPreorderArray(){
		return this.preorderArray;
	}
	
	public TreeNode getRoot() {
		return this.root;
	}
}
