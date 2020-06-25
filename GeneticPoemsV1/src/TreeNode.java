import java.util.ArrayList;

//implements a tree node with at least one child
public class TreeNode {
	//sentence structure category or PoS-tag
	protected String category;
	
	private TreeNode leftChild;
	private TreeNode rightChild;
	
	public TreeNode(String category) {
		this.category = category;
		this.leftChild = null;
		this.rightChild = null;
	}
	
	public TreeNode(String category, TreeNode leftchild) {
		this.category = category;
		this.leftChild = leftchild;
		this.rightChild = null;
	}
	
	public TreeNode(String category, TreeNode leftchild, TreeNode rightchild) {
		this.category = category;
		this.leftChild = leftchild;
		this.rightChild = rightchild; 
	}
	
	public boolean hasChildren() {
		if (leftChild == null && rightChild == null) {
			return false;
		}
		return true;
	}
	
	public TreeNode getLeftChild() {
		return this.leftChild;
	}
	
	public TreeNode getRightChild() {
		return this.rightChild;
	}
	
	public void setLeftChild(TreeNode leftChild) {
		this.leftChild = leftChild;
	}
	
	public void setRightChild(TreeNode rightChild) {
		this.rightChild = rightChild;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String toString() {
		String toString = "";
		toString += category + " ";
		if (leftChild != null) {
			toString += leftChild.toString();
		}
		if (rightChild != null) {
			toString += rightChild.toString();
		}
		return toString;
	}
	
	public String toSentence() {
		String toSentence = "";
		toSentence += leftChild.toSentence();
		if(rightChild!=null) {
			toSentence += rightChild.toSentence();
		}
		return toSentence;
	}
	
	public void replacePos() {
		leftChild.replacePos();
		if(rightChild!=null) {
			rightChild.replacePos();
		}
	}
	
	public ArrayList<Tree> getSubTrees(String category) {
		ArrayList<Tree> subtrees = new ArrayList<Tree>();
		if (category.equals(this.category)) {
			subtrees.add(new Tree(this));
		}
		if (hasChildren()) {
			subtrees.addAll(leftChild.getSubTrees(category));
			if(rightChild!=null) {
				subtrees.addAll(rightChild.getSubTrees(category));
			}
		}
		return subtrees;
	}
	
	public boolean contains(String category, boolean contains) {
		if(category.equals(this.category)) {
			System.out.println("contains " + category);
			contains = true;
		} else {
			if(leftChild!=null) {
				contains =  leftChild.contains(category, contains);
			}
			if(rightChild!=null) {
				contains = rightChild.contains(category, contains);
			}
		}
		return contains;
	}
	
	public ArrayList<TreeNode> fillPreorderArray(){
		ArrayList<TreeNode> preorderArray = new ArrayList<>();
		preorderArray.add(this);
		if (leftChild!=null && rightChild!=null) {
			preorderArray.addAll(leftChild.fillPreorderArray());
			preorderArray.addAll(rightChild.fillPreorderArray());
		}
		return preorderArray;	
	}
	
	//return deep copy of subtree
	public TreeNode copySubtree() {
		TreeNode subtree = new TreeNode(this.category);
		subtree.leftChild = this.leftChild.copySubtree();

		if (this.rightChild != null) {
			subtree.rightChild = this.rightChild.copySubtree();
		}
		return subtree;
	}
}
