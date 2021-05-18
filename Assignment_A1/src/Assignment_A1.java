import java.io.*;
import java.util.*;
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

class Opcode {
    public HashMap<String,String> data = new HashMap<>();
    public HashMap<String,String> mnemonic = new HashMap<>();
    public HashMap<String,String> directive = new HashMap<>();
    public HashMap<String,String> condition = new HashMap<>();
    public HashMap<String,String> register = new HashMap<>();
    public LinkedHashMap Symbol = new LinkedHashMap();
    public ArrayList<Literal> lit_table = new ArrayList<Literal>();
    public ArrayList<String> pool_table = new ArrayList();
    public ArrayList<String>  S_index= new ArrayList();
    public ArrayList<String>  L_index= new ArrayList();
    String [][] opcode = new String[50][4];
    int line_count=0;

    Opcode(){

        //DS Table
        data.put("DC","01");
        data.put("DS","02");

        //IS Table
        mnemonic.put("STOP","00");
        mnemonic.put("ADD","01");
        mnemonic.put("SUB","02");
        mnemonic.put("MULT","03");
        mnemonic.put("MOVER","04");
        mnemonic.put("MOVEM","05");
        mnemonic.put("COMP","06");
        mnemonic.put("BC","07");
        mnemonic.put("DIV","08");
        mnemonic.put("READ","09");
        mnemonic.put("PRINT","10");

        //AD Table
        directive.put("START","01");
        directive.put("END","02");
        directive.put("ORIGIN","03");
        directive.put("EQU","04");
        directive.put("LTORG","05");

        //Condition Table
        condition.put("LT","01");
        condition.put("LE","02");
        condition.put("EQ","03");
        condition.put("GT","04");
        condition.put("GE","05");
        condition.put("ANY","06");
        condition.put("NE","07");

        //Register Table
        register.put("AREG","01");
        register.put("BREG","02");
        register.put("CREG","03");
        register.put("DREG","04");
    }

    public void convertToOpcode(Scanner br) throws IOException {
        String st;
        int LC = 0;
        while(br.hasNextLine()){
            st = br.nextLine();
            String[] split_words = st.split("\t");

            if(line_count==0){
                LC = Integer.parseInt(split_words[2]);
                opcode[line_count][0] = "-";
                opcode[line_count][1] = "(AD,01)";
                opcode[line_count][2] = "(C," + LC + ")";
                line_count ++;
            }
            else{
                if(split_words[0]!=null && !split_words[0].isEmpty()){
                    if(!Symbol.containsKey(split_words[0])) {
                        Symbol.put(split_words[0],LC);
                        S_index.add(split_words[0]);
                    }else{
                        Symbol.replace(split_words[0],LC);
                    }
                }
                if(mnemonic.containsKey(split_words[1])){
                    opcode[line_count][0] = String.valueOf(LC);
                    opcode[line_count][1] = "(IS," + mnemonic.get(split_words[1]) + ")";
                    if(split_words[1].equals("STOP")){
                        opcode[line_count][1] = "(IS," + mnemonic.get(split_words[1]) + ")";
                    }else if(register.containsKey(split_words[2])||condition.containsKey(split_words[2])){
                        if(register.containsKey(split_words[2]))
                        {
                            opcode[line_count][2] = "(" + register.get(split_words[2]) + ")";
                        }
                        else
                        {
                            opcode[line_count][2] = "(" + condition.get(split_words[2]) + ")";
                        }
                       if(split_words[3].charAt(0) == '='){
                           lit_table.add(new Literal(lit_table.size(),split_words[3],999));
                           L_index.add(split_words[3]);
                           opcode[line_count][3] = "(L,"+(L_index.lastIndexOf(split_words[3])+1) + ")";
                       }else{
                           if(Symbol.containsKey(split_words[3])){
                               opcode[line_count][3] = "(S,"+(S_index.indexOf(split_words[3])+1)+")";
                           }else{
                               S_index.add(split_words[3]);
                               Symbol.put(split_words[3],999);
                               opcode[line_count][3] = "(S,"+(S_index.indexOf(split_words[3])+1)+")";
                           }
                       }
                    }
                    else if(split_words[1].equals("PRINT")||split_words[1].equals("READ")){
                        if(Symbol.containsKey(split_words[2])){
                            opcode[line_count][2] = "(S," + (S_index.indexOf(split_words[2])+1)+")";
                        }else{
                            S_index.add(split_words[2]);
                            Symbol.put(split_words[2],999);
                            opcode[line_count][2] = "(S," + (S_index.indexOf(split_words[2])+1)+")";
                        }
                    }
                    LC=LC+1;
                }
                else if(data.containsKey(split_words[1])){
                    opcode[line_count][0] = String.valueOf(LC);
                    opcode[line_count][1] = "(DL," + data.get(split_words[1]) +")";
                    if(split_words[1].equals("DS"))
                    {
                        opcode[line_count][2] = "(C," + split_words[2] + ")";
                        LC+=Integer.parseInt(split_words[2]);
                    }
                    else{
                        opcode[line_count][2] = "(C,"+split_words[2]+")";
                        LC+=1;
                    }
                }
                else if(directive.containsKey(split_words[1])){
                    opcode[line_count][0] = "-";
                    opcode[line_count][1] = "(AD," + directive.get(split_words[1]) +")";
                    if(split_words[1].equals("ORIGIN")){
                        try{
                            LC = Integer.parseInt(split_words[2]);
                            opcode[line_count][2] = ("(S," + (S_index.indexOf(split_words[2])+1)+")");
                        }catch (NumberFormatException err){
                            if(split_words[2].contains("+")){
                                String[] sp = split_words[2].split(Pattern.quote("+"));
                                LC = (int)Symbol.get(sp[0]) + Integer.parseInt(sp[1]);
                                opcode[line_count][2] = ("(S," + (S_index.indexOf(sp[0])+1)+")+"+sp[1]);
                            }
                            else{
                                String[] sp = split_words[2].split(Pattern.quote("-"));
                                LC = (int)Symbol.get(sp[0]) + Integer.parseInt(sp[1]);
                                opcode[line_count][2] = ("(S," + (S_index.indexOf(sp[0])+1)+")-"+sp[1]);
                            }
                        }
                    }
                    else if(split_words[1].equals("LTORG")||split_words[1].equals("END")){
                        int flag=0;
                        for(Literal i:lit_table){
                            if(i.get_Address()==999){
                                i.set_Address(LC);
                                opcode[line_count][0] = String.valueOf(LC);
                                opcode[line_count][1] = "(AD,"+directive.get(split_words[1])+")";
                                opcode[line_count][2] = "-";
                                opcode[line_count][3] = i.get_Literal().replace("=","");
                                line_count++;
                                LC++;
                                if(flag==0){
                                    int ind = L_index.indexOf(i.get_Literal());
                                    pool_table.add("#"+(ind+1));
                                    flag=1;
                                }
                            }
                        }
                        /*Iterator lit = lit_table.iterator();
                          while(lit.hasNext()){
                            Map.Entry litelement = (Map.Entry)lit.next();
                            int value = (int)litelement.getValue();
                            String key = (String) litelement.getKey();
                            if(value==999){
                                if(flag==0){
                                    pool_table.add("#"+(L_index.indexOf(key)+1));
                                    flag=1;
                                }
                                lit_table.replace(litelement.getKey(),LC);
                                opcode[line_count][0] = String.valueOf(LC);
                                opcode[line_count][1] = "(AD," + directive.get(split_words[1])+")";
                                line_count++;
                                LC++;
                            }
                        }*/
                        line_count--;
                    }else if(split_words[1].equals("EQU")){
                        if(!S_index.contains(split_words[0]))
                            S_index.add(split_words[0]);
                        int l;
                        if(split_words[2].contains("+")){
                            String[] wo = (split_words[2]).split(Pattern.quote("+"));
                            l = (int)Symbol.get(wo[0]);
                            l = l+ Integer.parseInt(wo[1]);
                            opcode[line_count][2] = ("(S," + (S_index.indexOf(wo[0])+1)+")+" + wo[1]);
                        }else if(split_words[2].contains("-")){
                            String[] wo = (split_words[2]).split(Pattern.quote("-"));
                            l = (int)Symbol.get(wo[0]);
                            l = l - Integer.parseInt(wo[1]);
                            opcode[line_count][2] = ("(S," + (S_index.indexOf(wo[0])+1)+")-" + wo[1]);
                        } else{
                            l = (int) Symbol.get(split_words[2]);
                        }
                        Symbol.put(split_words[0],l);
                    }
                }
                line_count++;
            }
        }
    }
    /*public void printS_index(){
        for(int i=0;i<S_index.size();i++){
            System.out.println(S_index.get(i));
        }
    }*/
    public void writeOpcode(FileWriter bw) throws IOException {
        System.out.println("Opcode Table");
        for(int i=0;i<line_count;i++){
            for(int j=0;j<opcode[i].length;j++){
                if(opcode[i][j]!=null){
                    System.out.print(opcode[i][j]+"\t");
                    bw.write(opcode[i][j] + "\t");
                }
            }
            System.out.print("\n");
            if(i!=line_count-1)
                bw.write("\n");
        }
    }

    public void writeSymbolTable(FileWriter bw) throws IOException {
        System.out.println("\nSYMBOL TABLE:");
        Iterator sym = Symbol.entrySet().iterator();
        while(sym.hasNext()){
            Map.Entry elem = (Map.Entry)sym.next();
            System.out.println(elem.getKey()+"\t"+elem.getValue());
            bw.write(elem.getKey()+"\t"+elem.getValue());
            if(sym.hasNext()){
                bw.write("\n");
            }
        }
    }

    public void printLitTable(FileWriter bw) throws IOException {
        System.out.println("\nLITERAL TABLE:");
        for(Literal i:lit_table){
            System.out.println(i.get_Literal()+"\t"+i.get_Address());
            bw.write(i.get_Literal()+"\t"+i.get_Address()+"\n");
        }
        /*Iterator lit = lit_table.iterator();
        while(lit.hasNext()){
            Map.Entry elem = (Map.Entry)lit.next();
            System.out.println(elem.getKey()+"\t"+elem.getValue());
            bw.write(elem.getKey()+"\t"+elem.getValue());
            if(lit.hasNext()){
                bw.write("\n");
            }
        }*/
    }

    public void printPool(FileWriter bw) throws IOException {
        System.out.println("\nPOOL TABLE:");
        for(int i=0;i<pool_table.size();i++){
            bw.write(pool_table.get(i) + "\n");
            System.out.println(pool_table.get(i));
        }
    }
}


public class Assignment_A1 {
    public static void main(String []args) throws IOException {
        Opcode op = new Opcode();
        String path="C:\\Users\\varun karwa\\Desktop\\31172_Assignments\\SPOSL_31172\\Assignment_A1\\src\\input.txt";
        File file = new File(path);
        Scanner reader = new Scanner(file);
        op.convertToOpcode(reader);
        reader.close();
        //op.printS_index();
        FileWriter writer = new FileWriter("opcode.txt");
        op.writeOpcode(writer);
        writer.close();

        writer = new FileWriter("symbol.txt");
        op.writeSymbolTable(writer);
        writer.close();

        writer = new FileWriter("literal.txt");
        op.printLitTable(writer);
        writer.close();

        writer = new FileWriter("pool.txt");
        op.printPool(writer);
        writer.close();
    }
}
