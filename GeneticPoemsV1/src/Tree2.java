import java.util.ArrayList;

public class Tree2 {
	private TreeNode2 root;
	public ArrayList<TreeNode2> preorderArray;
	
	public Tree2(TreeNode2 root) {
		this.root = root;
		preorderArray = fillPreorderArray();
	}
	
	//Copy constructor
	public Tree2(Tree2 tree) {
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
	public ArrayList<Tree2> getSubtrees(String category) {
		ArrayList<Tree2> subtrees = new ArrayList<Tree2>();
		for (TreeNode2 node : preorderArray) {
			if (node.getCategory().equals(category)) {
				subtrees.add(new Tree2(node));
			}
		}
		if(subtrees.isEmpty()) {
			//System.out.println("no subtrees " + category);
		}
		return subtrees;
	}
	
	public boolean contains(String category) {
		boolean contains = false;
		for (TreeNode2 node : preorderArray) {
			if (node.getCategory().equals(category)) {
				contains = true;
			}
		}
		return contains;
	}
	
	public ArrayList<TreeNode2> fillPreorderArray(){
		return root.fillPreorderArray();	
	}
	
	public void switchSubtree(int index, TreeNode2 newSubtree) {
		TreeNode2 originalSubtree = preorderArray.get(index);
		if (!originalSubtree.getCategory().equals(newSubtree.getCategory())) {
			System.out.println("cannot switch");
			return;
		}
		if (originalSubtree instanceof LeafNode2 && newSubtree instanceof LeafNode2) {
			((LeafNode2) originalSubtree).setWord(((LeafNode2) newSubtree).getWord());
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
	
	public ArrayList<TreeNode2> getPreorderArray(){
		return this.preorderArray;
	}
	
	public TreeNode2 getRoot() {
		return this.root;
	}
}
