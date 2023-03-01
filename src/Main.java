import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void menu(){
        System.out.println("_________HAL9000________");
        System.out.println("Choose exec mode:");
        System.out.println("[1] Normal mode");
        System.out.println("[2] Debug mode");
    }
    public static void main(String[] args) {
        /*
        final long start = System.currentTimeMillis();

        Interpreter interpreter = new Interpreter();
        interpreter.readFile(args[0]);
        menu();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();

        if(Objects.equals(input, "1")){
            interpreter.build();
        }
        else if (Objects.equals(input,"2")){
            interpreter.debug();
        }
        else{
            System.out.println("Invalid input");
        }

        long end = System.currentTimeMillis();
        float seconds = (end - start)/1000F;
        System.out.println("Runtime: " + seconds + " seconds");

         */
        HAL_OS h1 = new HAL_OS();
        h1.readFile("halOS.txt");
        for(Interpreter h : h1.HALs){
            h.start();
        }
    }
}
