
public class WebCrawlerTester {

    public static void main(String[] args) {
        String mainPage = "https://cs.txstate.edu";
        String searchingFor = "https://cs.txstate.edu/about_us/";
        WebCrawler search = new WebCrawler(mainPage, searchingFor, 10);
        search.search();
        search.getTree().printTree();
    }
}
