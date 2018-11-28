import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class WebCrawler {
    private String phraseToSearchFor;
    private TreeNode homepage;
    private Set<String> pagesVisited = new HashSet<>();
    private List<TreeNode> pagesToVisit = new ArrayList<>();
    private ArrayList< ArrayList<TreeNode>> depths = new ArrayList<>();
    private int currDepth;
    private int MAX_DEPTH;

    public WebCrawler(String homePage, String searchingFor, int MAX_DEPTH){
        this.homepage = new TreeNode(homePage);
        this.pagesToVisit.add(this.homepage);
        depths.add(new ArrayList<TreeNode>(){});
        depths.get(this.homepage.getDepth()).add(this.homepage);
        this.phraseToSearchFor = searchingFor;
        this.MAX_DEPTH = MAX_DEPTH;
    }

    public void search(){
        do{
            processPage(pagesToVisit.get(0));
        }
        while(pagesToVisit.get(0).getDepth() <= MAX_DEPTH && pagesToVisit.size() != 0);

        System.out.println(String.format("Now have visited %s links! %s links left to visit", pagesVisited.size(), (pagesToVisit.size()-pagesVisited.size())));
        processPage(pagesToVisit.get(0));
        System.out.println(depths);


        currDepth = -1;
        printTree();
    }

    private void processPage(TreeNode currURL){
        if(!currURL.equals(this.homepage)) {
            System.out.println(String.format("parent -> %s link -> %s depth -> %s", currURL.getParent().getData(), currURL.getData(), currURL.getDepth()));
        }
        boolean match = currURL.getChildrenData().contains(currURL.getData());   // Stops cyclical dependencies

        if(!match){
            try {
                Document doc = Jsoup.connect(currURL.getData()).get();

                Elements questions = doc.select("a[href]");                     // gets all links

                for(Element link: questions){
                    String nodeURL = link.attr("abs:href");
                    if(nodeURL.contains(this.phraseToSearchFor) && !currURL.getChildrenData().contains(nodeURL)) {                                   // If the link contains the current searchingFor
                        currURL.addChild(nodeURL);     // Makes the current link into a Tree Node
                    }
                }
            }
            catch(IOException ex){
//                System.out.println(ex.getMessage());
            }
        }
        pagesToVisit.addAll(currURL.getChildren());
        pagesToVisit.remove(currURL);
        pagesVisited.add(currURL.getData());
    }


    private void printTree(){
        for(int i = 0; i < depths.size(); i++){
            System.out.println("====> Depth" + i);
            for(TreeNode currNode: depths.get(i)){
                System.out.println(String.format("%s -> %s -> %s", (currNode.getParent() == null) ? "no parent" : currNode.getParent().getData(), currNode.getData(), currNode.getChildrenData()));
            }
        }
    }


    private ArrayList<TreeNode> getAllFromDepth(int depth){
        TreeNode temp = this.homepage;
        while(temp.getDepth() != depth){
            temp = temp.get
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
            this.children = new HashSet<>();
            this.childrenData = new ArrayList<>();
        }

        private TreeNode addChild(String child) {
            TreeNode childNode = new TreeNode(child);
            childNode.setDepth(this.depth+1);
            childNode.setParent(this);
            if(childNode.getDepth() == depths.size()){
                depths.add(new ArrayList<TreeNode>(){});
            }

            depths.get(childNode.getDepth()).add(childNode);
            this.children.add(childNode);
            this.childrenData.add(child);
            return childNode;
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

    }
}