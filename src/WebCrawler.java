import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class WebCrawler {
    private String phraseToSearchFor;
    private Set<String> pagesVisited = new HashSet<>();
    private List<TreeNode> pagesToVisit = new ArrayList<>();
    private int MAX_DEPTH;
    private Tree tree;

    public WebCrawler(String homePage, String searchingFor, int MAX_DEPTH){
        TreeNode homepage = new TreeNode(homePage);
        this.pagesToVisit.add(homepage);
        tree = new Tree(homepage);
        this.phraseToSearchFor = searchingFor;
        this.MAX_DEPTH = MAX_DEPTH;
    }

    public void search(){
        while(pagesToVisit.size() != 0){
            if(pagesToVisit.get(0).getDepth() <= MAX_DEPTH) {
                processPage(pagesToVisit.get(0));
                System.out.println(String.format("Now have visited %s links! %s links left to visit", pagesVisited.size(), pagesToVisit.size()));
            }
        }
    }

    private void processPage(TreeNode currURL){
        boolean match = currURL.getChildrenData().contains(currURL.getData());   // Stops cyclical dependencies

        if(!match){
            try {
                Document doc = Jsoup.connect(currURL.getData()).get();

                Elements questions = doc.select("a[href]");                     // gets all links

                for(Element link: questions){
                    String nodeURL = link.attr("abs:href");
                    if(nodeURL.contains(this.phraseToSearchFor) && !currURL.getChildrenData().contains(nodeURL)) {
                        currURL.addChild(nodeURL);
                    }

                }
            }
            catch(IOException ex){}
        }


        System.out.println(currURL);
        for (TreeNode child: currURL.getChildren()){
            if(canAddToPagesToVisit(child) && canAddToTree(child)){
                pagesToVisit.add(child);
                tree.addNode(child);
            }
        }

        pagesToVisit.remove(currURL);
        pagesVisited.add(currURL.getData());
    }

    private boolean canAddToPagesToVisit(TreeNode node){
        for(TreeNode curr: pagesToVisit){
            if(curr.getParent() != null && curr.getParent().getData().equals(node.getParent().getData()) && curr.getData().equals(node.getData())){
                return false;
            }
        }
        return true;
    }

    private boolean canAddToTree(TreeNode node){
        for(TreeNode curr: tree.getTree()){
            if(curr.getParent() != null && curr.getParent().getData().equals(node.getParent().getData()) && curr.getData().equals(node.getData())){
                return false;
            }
        }
        return true;
    }

    public Tree getTree(){
        return tree;
    }


    public class Tree {
        private List<TreeNode> tree;

        private Tree(TreeNode root){
            tree = new LinkedList<>();
            tree.add(root);
        }

        private List<TreeNode> getTree() {
            return tree;
        }

        private void addNode(TreeNode node){
            tree.add(node);
        }

        protected void printTree(){
            int depth = -1;
            for(TreeNode currNode: tree){
                if (currNode.getDepth() != depth){
                    depth = currNode.getDepth();
                    System.out.println(String.format("============ Depth: %s", currNode.getDepth()));
                }
                System.out.println(currNode);
            }
        }
    }

    private class TreeNode {
        String data;
        TreeNode parent;
        Set<TreeNode> children;
        ArrayList<String> childrenData;
        int depth = 0;


        private TreeNode(String data) {
            this.data = data;
            this.parent = null;
            this.children = new HashSet<>();
            this.childrenData = new ArrayList<>();
        }

        private TreeNode addChild(String child) {
            TreeNode childNode = new TreeNode(child);
            childNode.setDepth(this.depth+1);
            childNode.setParent(this);
            this.children.add(childNode);
            this.childrenData.add(child);
            return childNode;
        }

        private void removeChild(TreeNode child){
            this.children.remove(child);
            this.childrenData.remove(child.getData());
        }

        @Override
        public String toString(){
            String output = String.format("Depth %s -> ", this.getDepth());
            if(this.getParent() != null) {
                output += String.format("%s -> ", this.getParent().getData());
            }
            output += String.format("%s -> %s", this.getData(), this.getChildrenData());
            return output;
        }

        private Set<TreeNode> getChildren() {
            return children;
        }

        private String getData() {
            return data;
        }

        private void setData(String data) {
            this.data = data;
        }

        private TreeNode getParent() {
            return parent;
        }

        private void setParent(TreeNode parent) {
            this.parent = parent;
        }

        private ArrayList<String> getChildrenData() {
            return childrenData;
        }

        private int getDepth() {
            return depth;
        }

        private void setDepth(int depth) {
            this.depth = depth;
        }

        public boolean nodesEqual(Object other){
            if(!other.getClass().equals(this.getClass())){
                return false;
            }
            TreeNode otherNode = (TreeNode) other;
            if(!this.getData().equals(otherNode.getData())){
                return false;
            }
            if(this.getParent() != null && !this.getParent().getData().equals(otherNode.getParent().getData())){
                return false;
            }
            return this.getChildrenData().equals(otherNode.getChildrenData());
        }

    }
}