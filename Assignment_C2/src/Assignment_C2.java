import java.util.Scanner;

public class Assignment_C2 {
    static Scanner sc = new Scanner(System.in);
    static int checkSafe(int[][] needMat, int[] work, int[] finish, int n, int m){
        int cnt=0;

        for(int i=0;i<n;i++){
            cnt=0;
            if(finish[i]==0){
                for(int j=0;j<m;j++){
                    if(needMat[i][j]<=work[j]){
                        cnt++;
                    }
                } 
                if(cnt==m){
                    return i;
                }
            }

        }
        return -1;
    }
    public static void main(String args[]){
        int n,m;
        System.out.println("Enter number of processes");
        n = sc.nextInt();
        System.out.println("Enter number of resources");
        m = sc.nextInt();
        int allocation[][] = new int[n][m];
        int max[][] = new int[n][m];
        int avail[] = new int[m];
        int work[] = new int[m];
        int need[][] = new int[n][m];
        int finish[] = new int[n];
        for(int i=0;i<n;i++){
            finish[i]=0;
        }
        System.out.println("Enter Allocation Matrix");
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                allocation[i][j] = sc.nextInt();
            }
        }
        System.out.println("Enter Max Matrix");
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                max[i][j] = sc.nextInt();
            }
        }
        System.out.println("Enter Availability Matrix");
        for(int i=0;i<m;i++){
            avail[i] = sc.nextInt();
        }

        for(int i=0;i<m;i++){
            int sum=0;
            for(int j=0;j<n;j++){
                sum += allocation[j][i];
            }
            work[i] = avail[i]-sum;
        }
        System.out.println("Allocation Matrix:");
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                System.out.print(allocation[i][j]+"\t");
            }
            System.out.print("\n");
        }
        System.out.println("Max Matrix:");
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                System.out.print(max[i][j]+"\t");
            }
            System.out.print("\n");
        }
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }
        System.out.println("Work Matrix:");
        for(int i=0;i<m;i++){
            System.out.print(work[i]+"\t");
        }

        System.out.println("\nNeed Matrix:");
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                System.out.print(need[i][j]+"\t");
            }
            System.out.print("\n");
        }
        System.out.println("Sequence");
        for(int i=0;i<n;i++){
            int safe = checkSafe(need,work, finish,n,m);

            if(safe!=-1){
                System.out.println("Process "+safe);
                finish[safe]=1;
                for(int j=0;j<m;j++){
                    work[j]=work[j]+allocation[safe][j];
                }
            }
        }
        boolean executed=false;
        for(int i=0;i<n;i++){
            if(finish[i]!=1){
                executed=true;
            }
        }
        if(executed==true){
            System.out.println("Not Safe");
        }else{
            System.out.println("Safe");
        }
    }
}