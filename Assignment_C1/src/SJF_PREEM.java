import java.util.*;

public class SJF_PREEM {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Number of Process: ");
        int n = sc.nextInt();
        int processid[] = new int[n];
        int arrival[] = new int[n];
        int burst[] = new int[n];
        int waittime[] = new int[n];
        int remaintime[] = new int[n];
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
                remaintime[j+1] = burst[j];
                processid[j+1] = processid[j];
                j = j-1;
            }
            arrival[j+1] = key;
            processid[j+1] = temp2;
            burst[j+1] = temp1;
            remaintime[j+1] = temp1;
        }

        int complete = 0, current = 0, minm = Integer.MAX_VALUE;
        int shortest = 0, finish_time;
        boolean check = false;
        System.out.println("\nSequence:");
        while(complete!=n){
            for(j=0;j<n;j++){
                if(arrival[j]<=current && remaintime[j]<minm && remaintime[j]>0){
                    minm=remaintime[j];
                    shortest=j;
                    check=true;
                }
            }
            if(check == false){
                current++;
                continue;
            }

            remaintime[shortest]--;
            System.out.print((shortest+1)+"\t");
            minm = remaintime[shortest];

            if(remaintime[shortest]==0){
                minm=Integer.MAX_VALUE;
                complete++;
                check=false;
                finish_time = current+1;
                waittime[shortest] = finish_time - arrival[shortest] - burst[shortest];
                if(waittime[shortest]<0){
                    waittime[shortest]=0;
                }
            }
            current++;
        }

        System.out.println("\nId\tA\tB\tT\tW");
        for(i=0;i<n;i++){
            System.out.println(processid[i]+"\t"+arrival[i]+"\t"+burst[i]+"\t"+(waittime[i] + burst[i])+"\t"+waittime[i]);
            avgta += waittime[i] + burst[i];
            avgw += waittime[i];
        }
        System.out.println("Average turnaround time: "+avgta/n);
        System.out.println("Average waiting time: "+avgw/n);
    }
};
