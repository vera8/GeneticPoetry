import java.util.ArrayList;


public class TreeNode2 {
	protected String category;
	private TreeNode2 leftChild;
	private TreeNode2 rightChild;
	
	public TreeNode2(String category) {
		this.category = category;
		this.leftChild = null;
		this.rightChild = null;
	}
	
	public TreeNode2(String category, TreeNode2 leftchild) {
		this.category = category;
		this.leftChild = leftchild;
		this.rightChild = null;
	}
	
	public TreeNode2(String category, TreeNode2 leftchild, TreeNode2 rightchild) {
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
	
	public TreeNode2 getLeftChild() {
		return this.leftChild;
	}
	
	public TreeNode2 getRightChild() {
		return this.rightChild;
	}
	
	public void setLeftChild(TreeNode2 leftChild) {
		this.leftChild = leftChild;
	}
	
	public void setRightChild(TreeNode2 rightChild) {
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
	
	public ArrayList<Tree2> getSubTrees(String category) {
		ArrayList<Tree2> subtrees = new ArrayList<Tree2>();
		if (category.equals(this.category)) {
			subtrees.add(new Tree2(this));
			//System.out.println("found subtree " + category);
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
		//boolean contains;
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
	
	public ArrayList<TreeNode2> fillPreorderArray(){
		ArrayList<TreeNode2> preorderArray = new ArrayList<>();
		preorderArray.add(this);
		if (leftChild!=null && rightChild!=null) {
			preorderArray.addAll(leftChild.fillPreorderArray());
			preorderArray.addAll(rightChild.fillPreorderArray());
		}
		return preorderArray;	
	}
	
	public TreeNode2 copySubtree() {
		TreeNode2 subtree = new TreeNode2(this.category);
		subtree.leftChild = this.leftChild.copySubtree();

		if (this.rightChild != null) {
			subtree.rightChild = this.rightChild.copySubtree();
		}
		return subtree;
	}
}
