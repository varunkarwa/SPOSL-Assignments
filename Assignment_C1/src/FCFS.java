import java.util.*;

public class FCFS {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Number of Process: ");
        int n = sc.nextInt();
        int processid[] = new int[n];
        int arrival[] = new int[n];
        int burst[] = new int[n];
        int complete[] = new int[n];
        int i, key, j, temp1, temp2;
        float avgta = 0, avgw = 0;
        for(i=0;i<n;i++){
            System.out.println("Enter Arrival Time for process " + (i+1));
            arrival[i] = sc.nextInt();
            System.out.println("Enter Burst Time for process " + (i+1));
            burst[i] = sc.nextInt();
            processid[i] = i+1;
            key = arrival[i];
            temp1 = burst[i];
            temp2 = processid[i];
            j = i-1;
            while(j>=0 && arrival[j] > key){
                arrival[j+1] = arrival[j];
                burst[j+1] = burst[j];
                processid[j+1] = processid[j];
                j = j-1;
            }
            arrival[j+1] = key;
            processid[j+1] = temp2;
            burst[j+1] = temp1;
        }

        System.out.println("\nSequence:");
        for(i=0;i<n;i++){
            System.out.print(processid[i]+"\t");
            if(i==0){
                complete[i] = arrival[i] + burst[i];
            }
            else{
                if(complete[i-1]<arrival[i]){
                    complete[i] = arrival[i] + burst[i];
                }
                else{
                    complete[i] = complete[i-1]+burst[i];
                }
            }
        }
        System.out.println("\nId\tA\tB\tT\tW");
        for(i=0;i<n;i++){
            System.out.println(processid[i]+"\t"+arrival[i]+"\t"+burst[i]+"\t"+(complete[i]-arrival[i])+"\t"+(complete[i]-arrival[i]-burst[i]));
            avgta += complete[i]-arrival[i];
            avgw += complete[i]-arrival[i]-burst[i];
        }
        System.out.println("Average turnaround time: "+avgta/n);
        System.out.println("Average waiting time: "+avgw/n);
    }
};