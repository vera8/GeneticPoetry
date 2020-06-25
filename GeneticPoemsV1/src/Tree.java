import java.util.ArrayList;

//implements a tree structure
public class Tree {
	private TreeNode root;
	
	//saves nodes in preorder for access via index
	public ArrayList<TreeNode> preorderArray;
	
	public Tree(TreeNode root) {
		this.root = root;
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
	
	//replace all of the PoS-tags in the leaf nodes with random words from lexicon
	public void replacePos() {
		root.replacePos();
	}
	
	//get all subtrees from certain category
	public ArrayList<Tree> getSubtrees(String category) {
		ArrayList<Tree> subtrees = root.getSubTrees(category);
		return subtrees;
	}
	
	//check if tree contains given category
	public boolean contains(String category) {
		boolean contains = root.contains(category, false);
		return contains;
	}
	
	public ArrayList<TreeNode> fillPreorderArray(){
		return root.fillPreorderArray();	
	}
	
	//switch subtree at a given index in the preorder array with a given subtree (if types match)
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
