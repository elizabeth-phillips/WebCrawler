import java.util.Scanner;

public class WebCrawlerTester {

    public static void main(String[] args) {
        String mainPage = "https://cs.txstate.edu";
        String searchingFor = "https://cs.txstate.edu/about_us/";
        WebCrawler search = new WebCrawler(mainPage, searchingFor, 10);
        search.search();
        Scanner input = new Scanner(System.in);
        System.out.println("Would you like to print the resulting link nodes? 1 <- Yes, Any other key <- No");


        int choice = input.nextInt();
        if(choice == 1){
            search.getGraph().printGraph();
        }

        System.out.println(search.compDist(search.getGraph().getNode(0), search.getGraph().getNode(50)));

    }
}
