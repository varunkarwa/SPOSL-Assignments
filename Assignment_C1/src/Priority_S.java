import java.util.*;

public class Priority_S {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of processes");
        int n = sc.nextInt();
        int priority[] = new int[n];
        int burst[] = new int[n];
        int arrival[] = new int[n];
        int complete[] = new int[n];
        int process[] = new int[n];
        int executed[] = new int[n];
        int i,j,key,temp1,temp2,temp3,temp4;
        float avgta = 0, avgw = 0;
        for(i=0;i<n;i++){
            System.out.println("Enter arrival time for process "+(i+1));
            arrival[i] = sc.nextInt();
            System.out.println("Enter burst time for process "+(i+1));
            burst[i] = sc.nextInt();
            System.out.println("Enter priority for process "+(i+1));
            priority[i] = sc.nextInt();
            process[i] = i+1;
            executed[i]=0;
            key = arrival[i];
            temp1 = burst[i];
            temp2 = process[i];
            temp3 = priority[i];
            temp4 = executed[i];
            j = i-1;
            while(j>=0 && arrival[j] > key){
                arrival[j+1] = arrival[j];
                burst[j+1] = burst[j];
                process[j+1] = process[j];
                priority[j+1] = priority[j];
                executed[j+1] = executed[j];
                j = j-1;
            }
            arrival[j+1] = key;
            burst[j+1] = temp1;
            process[j+1] = temp2;
            priority[j+1] = temp3;
            executed[j+1] = temp4;
        }

        int current = 0;
        System.out.print("Sequence: ");
        for(i=0;i<n;i++){
            int p=n+1;
            int index=-1;
            for(j=0;j<n;j++){
                if(executed[j]!=1&&current>=arrival[j]&&priority[j]<p){
                    p=priority[j];
                    index=j;
                }
            }
            if(index!=-1){
                System.out.print(index+1+"\t");
                complete[index] = current+burst[index];
                current = complete[index];
                executed[index]=1;
            }
        }
        System.out.println("\nId\tA\tB\tT\tW\tP");
        for(i=0;i<n;i++){
            System.out.println(process[i]+"\t"+arrival[i]+"\t"+burst[i]+"\t"+(complete[i]-arrival[i])+"\t"+(complete[i]-arrival[i]-burst[i])+"\t"+priority[i]);
            avgta += complete[i]-arrival[i];
            avgw += complete[i]-arrival[i]-burst[i];
        }
        System.out.println("Average turnaround time: "+avgta/n);
        System.out.println("Average waiting time: "+avgw/n);
    }
}
