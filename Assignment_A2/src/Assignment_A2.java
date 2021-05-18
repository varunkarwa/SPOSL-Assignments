import javafx.scene.shape.HLineTo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

class Literal {
    public int index;
    public String literal;
    public int address;

    Literal(int i, String lit, int add){
        index = i;
        literal = lit;
        address = add;
    }

    public void set_index(int ind){
        index = ind;
    }
    public int get_index(){
        return index;
    }
    public void set_Literal(String lit){
        literal = lit;
    }
    public String get_Literal(){
        return literal;
    }
    public void set_Address(int lc){
        address = lc;
    }
    public int get_Address(){
        return address;
    }
}

class Machine_Code {
    public HashMap symbol = new HashMap();
    public ArrayList<Literal> literal = new ArrayList<Literal>();
    String[] pool = new String[10];
    String[][] mc_code = new String[30][3];
    int line_count;
    public void read_Symtab() throws FileNotFoundException {
        String path="C:\\Users\\varun karwa\\Desktop\\31172_Assignments\\SPOSL_31172\\Assignment_A1\\symbol.txt";
        File file = new File(path);
        Scanner reader = new Scanner(file);
        int counter = 1;
        while(reader.hasNextLine()){
            String st = reader.nextLine();
            String split_sym[] = st.split("\t");
            symbol.put(counter,split_sym[1]);
            counter++;
        }
        reader.close();
    }

    public void read_Littab() throws FileNotFoundException {
        String path="C:\\Users\\varun karwa\\Desktop\\31172_Assignments\\SPOSL_31172\\Assignment_A1\\literal.txt";
        File file = new File(path);
        Scanner reader = new Scanner(file);
        int counter = 1;
        while(reader.hasNextLine()){
            String st = reader.nextLine();
            String split_lit[] = st.split("\t");
            literal.add(new Literal(counter,split_lit[0],Integer.parseInt(split_lit[1])));
            counter++;
        }
        reader.close();
    }

    public void convertToMcode() throws FileNotFoundException {
        line_count=0;
        String path="C:\\Users\\varun karwa\\Desktop\\31172_Assignments\\SPOSL_31172\\Assignment_A1\\opcode.txt";
        File file = new File(path);
        Scanner reader = new Scanner(file);
        while(reader.hasNextLine()){
            String st = reader.nextLine();
            String split_words[] = st.split("\t");
            if(split_words[1].contains("IS")){
                if(split_words[1].contains("10")||split_words[1].contains("09")){
                    mc_code[line_count][0] = "+"+split_words[1].substring(4,6);
                    int sym_index = Integer.parseInt(split_words[2].substring(3,4));
                    mc_code[line_count][1] = "(00)";
                    mc_code[line_count][2] = (String) symbol.get(sym_index);
                }else if(split_words[1].contains("00")){
                    mc_code[line_count][0] = "+"+split_words[1].substring(4,6);
                }
                else{
                    mc_code[line_count][0] = "+"+split_words[1].substring(4,6);
                    mc_code[line_count][1] = split_words[2];
                    if(split_words[3].contains("L")){
                        int lit_index = Integer.parseInt(split_words[3].substring(3,4));
                        mc_code[line_count][2] = String.valueOf(literal.get(lit_index-1).get_Address());
                    }else if(split_words[3].contains("S")){
                        int sym_index = Integer.parseInt(split_words[3].substring(3,4));
                        mc_code[line_count][2] = (String) symbol.get(sym_index);
                    }
                }
                line_count++;
            }else if(split_words[1].contains("DL,02")){
                mc_code[line_count][0]="+00";
                mc_code[line_count][1]="0";
                mc_code[line_count][2]="00"+split_words[2].substring(3,4);
                line_count++;
            }else if(split_words[1].contains("AD,05")||split_words[1].contains("AD,02")){
                mc_code[line_count][0] = "+00";
                mc_code[line_count][1] = "0";
                mc_code[line_count][2] = String.format("%03d",Integer.parseInt(split_words[3]));
                line_count++;
            }
        }
    }

    public void print_MachineCode(FileWriter writer) throws IOException {
        System.out.println("MACHINE CODE:-");
        for(int i=0;i<line_count;i++){
            for(int j=0;j<mc_code[i].length&&mc_code[i][j]!=null;j++)
            {
                System.out.print(mc_code[i][j]+"\t");
                writer.write(mc_code[i][j] + "\t");
            }
            System.out.print("\n");
            if(i!=line_count-1)
                writer.write("\n");
         }
    }
}

public class Assignment_A2 {
    public static void main(String[] args) throws IOException {
        Machine_Code m1 = new Machine_Code();
        m1.read_Symtab();
        m1.read_Littab();
        m1.convertToMcode();
        FileWriter writer = new FileWriter("machine_code.txt");
        m1.print_MachineCode(writer);
        writer.close();
    }
}
