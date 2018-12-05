import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class WebCrawler {
    private String phraseToSearchFor;
    private Set<String> pagesVisited = new HashSet<>();
    private List<Node> pagesToVisit = new ArrayList<>();
    private Graph graph;

    public WebCrawler(String homePage, String searchingFor){
        Node homepage = new Node(homePage);
        this.pagesToVisit.add(homepage);
        graph = new Graph(homepage);
        this.phraseToSearchFor = searchingFor;
    }

    public void search(){
        while(pagesToVisit.size() != 0){
            graph.doesContain(pagesToVisit.get(0).getData());
            processPage(pagesToVisit.get(0));
            System.out.println(String.format("Now have visited %s links! %s links left to visit", pagesVisited.size(), pagesToVisit.size()));
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
        pagesVisited.add(currURL.data);
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
        if(!pagesVisited.contains(node.getData())) {
            for (Node curr : graph.getGraph()) {
                if (curr.getParent() != null && curr.getParent().getData().equals(node.getParent().getData()) && curr.getData().equals(node.getData())) {
                    return false;
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    public Graph getGraph(){
        return graph;
    }

    public void printAllDist(){
        int maxDist = -1;
        Node maxStart = new Node(getGraph().getNode(0).getData());
        Node maxFinish = new Node(getGraph().getNode(0).getData());
        int currDist = 0;
        for(Node i: graph.getGraph()){
            for(Node j: graph.getGraph()){
                currDist = compDist(i,j);
//                if(currDist >= 0) System.out.println(String.format("Start: %s  Finish: %s  -  Distance: %s", i.getData(), j.getData(), currDist));
                if(currDist > maxDist){
                    maxDist = currDist;
                    maxStart = i;
                    maxFinish = j;
                }
            }
        }
        System.out.println(String.format("%s   Start: %s  Finish: %s", maxDist, maxStart.getData(), maxFinish.getData()));
    }


    public int compDist(Node start, Node finish){
        List<Node> directions = new LinkedList<>();
        Queue<Node> q = new LinkedList<>();
        Map<Node, Boolean> vis = new HashMap<>();
        Map<Node, Node> prev = new HashMap<>();
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

        return directions.size()-1;
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

        public Node getNode(String url){
            for(Node n: graph){
                if(n.getData().equals(url)){
                    return n;
                }
            }
            return null;
        }

        public boolean doesContain(String url){
            if(getNode(url) != null){
//                System.out.println("already here" + pagesToVisit.toString());
                return true;
            }
            return false;
        }

        protected int getSize(){
            return graph.size();
        }

        private void addNode(Node node){
            graph.add(node);
        }

        protected void printGraph(){
            for(Node currNode: graph){
                System.out.println(currNode);
            }
        }
    }

    public class Node {
        String data;
        Node parent;
        Set<Node> children;
        ArrayList<String> childrenData;


        private Node(String data) {
            this.data = data;
            this.parent = null;
            this.children = new HashSet<>();
            this.childrenData = new ArrayList<>();
        }

        private Node addChild(String child) {
            Node childNode = new Node(child);
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
            String output = ""; //= String.format("Depth %s -> ", this.getDepth());
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