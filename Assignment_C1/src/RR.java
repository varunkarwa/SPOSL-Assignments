import java.util.*;

public class RR {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Number of Process: ");
        int n = sc.nextInt();
        System.out.println("Enter Quant Time:");
        int quant = sc.nextInt();
        int processid[] = new int[n];
        int arrival[] = new int[n];
        int burst[] = new int[n];
        int rem_time[] = new int[n];
        int completion[] = new int[n];
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
                rem_time[j+1] = burst[j];
                processid[j+1] = processid[j];
                j = j-1;
            }
            arrival[j+1] = key;
            processid[j+1] = temp2;
            burst[j+1] = temp1;
            rem_time[j+1] = temp1;
        }

        int current_time = 0;
        int arr = 0 ;

        System.out.println("\nSequence:");
        while(true){
            boolean done=true;
            for(i=0;i<n;i++){
                if(rem_time[i]>0){
                    done = false;
                    if(rem_time[i]>quant && arrival[i]<=arr){
                        current_time += quant;
                        rem_time[i] -= quant;
                        arr++;
                        System.out.println(current_time + "\t" + i);
                    }
                    else{
                        if(arrival[i]<=arr){
                            arr++;
                            current_time+=rem_time[i];
                            rem_time[i]=0;
                            completion[i] = current_time;
                            System.out.println(current_time + "\t" + i);
                        }
                    }
                }
            }
            if(done==true){
                break;
            }
        }

        System.out.println("\nId\tA\tB\tT\tW");
        for(i=0;i<n;i++){
            System.out.println(processid[i]+"\t"+arrival[i]+"\t"+burst[i]+"\t"+(completion[i]-arrival[i])+"\t"+(completion[i]-arrival[i]-burst[i]));
            avgta += completion[i]-arrival[i];
            avgw += completion[i]-arrival[i]-burst[i];
        }
        System.out.println("Average turnaround time: "+avgta/n);
        System.out.println("Average waiting time: "+avgw/n);
    }
};
