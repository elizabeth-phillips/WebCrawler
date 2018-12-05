import java.util.Scanner;

public class WebCrawlerTester {

    public static void main(String[] args) {
        String mainPage = "https://cs.txstate.edu";
        String searchingFor = "https://cs.txstate.edu/about_us/";
        WebCrawler search = new WebCrawler(mainPage, searchingFor);
        search.search();
        Scanner input = new Scanner(System.in);
        System.out.println("Would you like to print the resulting graph? 1 <- Yes, Any other key <- No");

        try {
            int choice = input.nextInt();
            if(choice == 1){
                search.getGraph().printGraph();
            }
        }
        catch (Exception ioException){

        }

        System.out.print("The diameter of this graph is: ");
        search.printAllDist();

    }
}
