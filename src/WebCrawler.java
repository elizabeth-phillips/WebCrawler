import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class WebCrawler {
    private String phraseToSearchFor;
    private int pagesVisited = 0;
    private List<Node> pagesToVisit = new ArrayList<>();
    private int MAX_DEPTH;
    private Graph graph;

    public WebCrawler(String homePage, String searchingFor, int MAX_DEPTH){
        Node homepage = new Node(homePage);
        this.pagesToVisit.add(homepage);
        graph = new Graph(homepage);
        this.phraseToSearchFor = searchingFor;
        this.MAX_DEPTH = MAX_DEPTH;
    }

    public void search(){
        while(pagesToVisit.size() != 0){
            if(pagesToVisit.get(0).getDepth() <= MAX_DEPTH) {
                processPage(pagesToVisit.get(0));
                System.out.println(String.format("Now have visited %s links! %s links left to visit", pagesVisited, pagesToVisit.size()));
            }
        }
    }

    private void processPage(Node currURL){
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
        for (Node child: currURL.getChildren()){
            if(canAddToPagesToVisit(child) && canAddToGraph(child)){
                pagesToVisit.add(child);
                graph.addNode(child);
            }
        }

        pagesToVisit.remove(currURL);
        pagesVisited++;
    }

    private boolean canAddToPagesToVisit(Node node){
        for(Node curr: pagesToVisit){
            if(curr.getParent() != null && curr.getParent().getData().equals(node.getParent().getData()) && curr.getData().equals(node.getData())){
                return false;
            }
        }
        return true;
    }

    private boolean canAddToGraph(Node node){
        for(Node curr: graph.getGraph()){
            if(curr.getParent() != null && curr.getParent().getData().equals(node.getParent().getData()) && curr.getData().equals(node.getData())){
                return false;
            }
        }
        return true;
    }

    public Graph getGraph(){
        return graph;
    }

    private Map<Node, Boolean> vis = new HashMap<>();

    private Map<Node, Node> prev = new HashMap<>();

    public int compDist(Node start, Node finish){
        List<Node> directions = new LinkedList<>();
        Queue<Node> q = new LinkedList<>();
        Node current = start;
        q.add(current);
        vis.put(current, true);
        while(!q.isEmpty()){
            current = q.remove();
            if (current.equals(finish)){
                break;
            }else{
                for(Node node : current.getChildren()){
                    if(!vis.containsKey(node)){
                        q.add(node);
                        vis.put(node, true);
                        prev.put(node, current);
                    }
                }
            }
        }
        if (!current.equals(finish)){
            return -1;
        }
        for(Node node = finish; node != null; node = prev.get(node)) {
            directions.add(node);
        }

        return directions.size();
    }


    public class Graph {
        private List<Node> graph;

        private Graph(Node root){
            graph = new LinkedList<>();
            graph.add(root);
        }

        private List<Node> getGraph() {
            return graph;
        }

        public Node getNode(int index){
            return graph.get(index);
        }

        protected int getSize(){
            return graph.size();
        }

        private void addNode(Node node){
            graph.add(node);
        }

        protected void printGraph(){
            int depth = -1;
            for(Node currNode: graph){
                if (currNode.getDepth() != depth){
                    depth = currNode.getDepth();
                    System.out.println(String.format("============ Depth: %s", currNode.getDepth()));
                }
                System.out.println(currNode);
            }
        }
    }

    private class Node {
        String data;
        Node parent;
        Set<Node> children;
        ArrayList<String> childrenData;
        int depth = 0;


        private Node(String data) {
            this.data = data;
            this.parent = null;
            this.children = new HashSet<>();
            this.childrenData = new ArrayList<>();
        }

        private Node addChild(String child) {
            Node childNode = new Node(child);
            childNode.setDepth(this.depth+1);
            childNode.setParent(this);
            this.children.add(childNode);
            this.childrenData.add(child);
            return childNode;
        }

        private void removeChild(Node child){
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

        private Set<Node> getChildren() {
            return children;
        }

        private String getData() {
            return data;
        }

        private void setData(String data) {
            this.data = data;
        }

        private Node getParent() {
            return parent;
        }

        private void setParent(Node parent) {
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
            Node otherNode = (Node) other;
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