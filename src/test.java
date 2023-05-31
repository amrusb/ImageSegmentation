import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Podaj nazwe");
        String name = in.nextLine();
        String ext;
        try{
            ext = name.substring(name.indexOf('.'));
            ext = name.substring(name.indexOf('.') + 1 );
        }
        catch(StringIndexOutOfBoundsException e){
            ext = "png";
        }

        System.out.println(ext);
    }
}
