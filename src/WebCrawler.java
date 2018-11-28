import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class WebCrawler {
    private static ArrayList<String> processedUrls = new ArrayList<>();
    private static String mainPage = "https://cs.txstate.edu";
    private static String phrase = "about_us";
    private static TreeNode<String> root = new TreeNode<String>(mainPage);
    private static int count = 1;

    public static void main(String[] args) {
        processPage(root);
    }

    private static void processPage(TreeNode<String> currURL){
        //check if the given mainURL is already in database
        boolean match = currURL.getChildren().contains(currURL.data);

        if(!match){
            try {
                Document doc = Jsoup.connect(currURL.data).get();
                if(doc.text().contains(phrase)){
                    System.out.println(String.format("Document: %s",currURL.data));
                }

                Elements questions = doc.select("a[href]");
                for(Element link: questions){
                    if(link.attr("href").contains(phrase)) {
                        String nodeURL = link.attr("abs:href");
                        if(!currURL.getChildren().contains(nodeURL)) {
                            TreeNode<String> currNode = currURL.addChild(nodeURL);
                            System.out.println(currNode.data);
                            processPage(currNode);
                        }
                    }
                }
            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            }
        }
    }



    public static class TreeNode<T> {
        T data;
        TreeNode<T> parent;
        List<TreeNode<T>> children;

        private TreeNode(T data) {
            this.data = data;
            this.children = new LinkedList<TreeNode<T>>();
        }

        private TreeNode<T> addChild(T child) {
            TreeNode<T> childNode = new TreeNode<T>(child);
            childNode.parent = this;
            this.children.add(childNode);
            return childNode;
        }

        private List<T> getChildren(){
            List<T> childrenList = new LinkedList<T>() ;
            for(TreeNode<T> child: this.children){
                childrenList.add(child.data);
            }
            return childrenList;
        }
    }
}