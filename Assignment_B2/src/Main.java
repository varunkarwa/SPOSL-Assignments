import java.lang.*;
import java.io.*;
public class Main {
    int roll;
    int marks;
    //Constructor
    public Main()
    {
        roll=20;
        marks=88;
    }
    public static void main(String[] args)
    {
        Main t=new Main();
        System.out.println("roll no:"+t.roll+" Marks:"+t.marks);
    }
}
