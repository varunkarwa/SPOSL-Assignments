import java.io.*;
import java.util.*;

class DefaultParam{
    String mnt_Name;
    String index;
    String value;

    DefaultParam(String name, String i, String v){
        mnt_Name = name;
        index = i;
        value = v;
    }

    public String getMnt_Name(){
        return mnt_Name;
    }
    public String getIndex(){
        return index;
    }
    public String getValue(){
        return value;
    }
}

class MNT{
    int index;
    String name;
    int no_of_parameters;
    int start_ptr;

    MNT(int i, String n, int no, int ptr){
        index = i;
        name = n;
        no_of_parameters = no;
        start_ptr = ptr;
    }

    public int getIndex(){ return index; }
    public String getName(){
        return name;
    }
    public int getNo_of_parameters(){
        return no_of_parameters;
    }
    public int getStart_ptr(){
        return start_ptr;
    }
}

class MDT{
    String instruction;
    String index_arg;
    int mdt_ptr;

    MDT(int ptr, String inst){
        instruction = inst;
        mdt_ptr = ptr;
    }

    public String getInstruction(){
        return instruction;
    }
}

class Macro{
    public ArrayList<MNT> mnt = new ArrayList<MNT>();
    public ArrayList<MDT> mdt = new ArrayList<MDT>();
    public ArrayList<DefaultParam> defaultParams = new ArrayList<DefaultParam>();
    public LinkedHashMap<String, String> ALA = new LinkedHashMap<String, String>();
    int mdtptr=0,mntptr=0;
    boolean def=false, name=false;
    String[] output= new String[100];

    public void processArgList(String a){
        ALA.clear();
        String arg_list[] = a.split(",");

        for(int i=1;i<=arg_list.length;i++){
            if(arg_list[i-1].contains("=")){
                int ind = arg_list[i-1].indexOf('=');
                int l = arg_list[i-1].length();
                ALA.put(arg_list[i-1].substring(0,ind),"#"+i);
                defaultParams.add(new DefaultParam(mnt.get(mnt.size()-1).getName(),("#"+i),arg_list[i-1].substring(ind+1,l)));
            }
            else{
                ALA.put(arg_list[i-1],"#"+i);
            }
        }
    }

    public String processArg(String a){
        String sp[] = a.split(",");
        for(int i=1;i<=sp.length;i++){
            String ind = ALA.get(sp[i-1]);
            a = a.replaceAll(sp[i-1],ind);
        }
        return a;
    }

    public void processIntermediateCode() throws FileNotFoundException {
        int k=0;
        String path="C:\\Users\\varun karwa\\Desktop\\31172_Assignments\\SPOSL_31172\\Assignment_A3\\src\\input.txt";
        File file = new File(path);
        Scanner reader = new Scanner(file);
        while(reader.hasNextLine()){
            String data = reader.nextLine();
            String split_words[] = data.split("\t");
            if(split_words[0].equalsIgnoreCase("MACRO")){
                def=true;
                name=true;
            }
            else if(def==true){
                if(split_words[0].equalsIgnoreCase("MEND")){
                    mdt.add(new MDT(mdtptr++,data));
                    def = false;
                }
                if(name==true){
                    mnt.add(new MNT(mntptr,split_words[0],split_words[1].split(",").length,mdtptr));
                    mntptr++;
                    mdt.add(new MDT(mdtptr++,data));
                    name = false;
                    processArgList(split_words[1]);
                }
                else if(name == false && def == true){
                    int flag=1;
                    for(MNT i: mnt){
                        if(i.getName().equals(split_words[0])){
                            flag=0;
                            break;
                        }
                    }
                    if(flag==1) {
                        String a =processArg(split_words[1]);
                        mdt.add(new MDT(mdtptr++,split_words[0]+"\t"+a));
                    }
                    else{
                        mdt.add(new MDT(mdtptr++,data));
                    }
                }
            }
            else{
                output[k] = data;
                k++;
            }
        }
        reader.close();
    }

    public void print_MDT() throws IOException {
        FileWriter write = new FileWriter("MDT.txt");
        System.out.println("Macro Definition Table:");
        System.out.println("MDT_Ptr\tInstruction");
        for(int i=0;i<mdtptr;i++){
            System.out.println(mdt.get(i).mdt_ptr+"\t\t"+mdt.get(i).getInstruction());
            write.write(mdt.get(i).mdt_ptr+"\t"+mdt.get(i).getInstruction());
            if(i!=mdtptr-1){
                write.write("\n");
            }
        }
        write.close();
    }

    public void print_MNT() throws IOException {
        FileWriter write = new FileWriter("MNT.txt");
        System.out.println("\nMacro Name Table:");
        System.out.println("Index\tName\tNo_of_Parameters\tStart");
        for(int i=0;i<mntptr;i++){
            write.write(mnt.get(i).getIndex()+"\t"+mnt.get(i).getName()+"\t"+mnt.get(i).getNo_of_parameters()+"\t"+mnt.get(i).getStart_ptr());
            if(i!=mntptr-1){
                write.write("\n");
            }
            System.out.println(mnt.get(i).getIndex()+"\t\t"+mnt.get(i).getName()+"\t"+mnt.get(i).getNo_of_parameters()+"\t\t\t\t\t"+mnt.get(i).getStart_ptr());
        }
        write.close();
    }

    public void print_Default() throws IOException {
        FileWriter write = new FileWriter("Default.txt");
        System.out.println("\nDefault Parameters:");
        System.out.println("MNT_Ptr\tIndex\tValue");
        for(int i=0;i<defaultParams.size();i++){
            write.write(defaultParams.get(i).getMnt_Name()+"\t"+defaultParams.get(i).getIndex()+"\t"+defaultParams.get(i).getValue());
            if(i!=defaultParams.size()-1){
                write.write("\n");
            }
            System.out.println(defaultParams.get(i).getMnt_Name()+"\t\t"+defaultParams.get(i).getIndex()+"\t\t"+defaultParams.get(i).getValue());
        }
        write.close();
    }
    
    public void print_Ouptut() throws IOException {
        FileWriter write = new FileWriter("IC.txt");
        System.out.println("\nIntermediate Code");
        for(int i=0;i<output.length&&output[i]!=null;i++){
            System.out.println(output[i]);
            write.write(output[i]);
            if(output[i+1]!=null){
                write.write("\n");
            }
        }
        write.close();
    }
}

public class Asssignment_A3 {
    public static void main(String args[]) throws IOException {
        Macro m = new Macro();
        m.processIntermediateCode();
        m.print_MDT();
        m.print_MNT();
        m.print_Default();
        m.print_Ouptut();
    }
}
