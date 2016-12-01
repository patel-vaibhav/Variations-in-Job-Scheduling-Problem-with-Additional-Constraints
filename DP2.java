package JobScheduling;
import java.util.*;

/*Dynamic programming algorithm for single processor and function to print schedule is derived from high level pseudo-code located at: 
Scheduling Jobs With Deadlines, Profits, and Durations
http://www.cs.mun.ca/~kol/courses/2711-f13/dynprog.pdf */

public class DP2 {
	public static void main(String[] args) {
		DP2 dp=new DP2();
		//Job J=dp.new Job(id,d,p,t);
		Job J1=dp.new Job(1,8,10,5);   // 2
		Job J2=dp.new Job(2,10,8,3);   // 3
		Job J3=dp.new Job(3,15,13,4);  // 4
		Job J4=dp.new Job(4,5,16,4);   // 1   
		Job[] jobArray={J1,J2,J3,J4};
		Arrays.sort(jobArray);
		ArrayList<Job> jobListInput=new ArrayList<Job>(Arrays.asList(jobArray));
		int noOfProcessors=2;
		int processorTravelCost[]={3,4};
		System.out.println("Maximum Profit that can be obtained --> "+DPCalculateMaxSubset(jobListInput,processorTravelCost));
	}
	public class Job implements Comparable<Job>
	{
		int id;
		int d;
		int p;
		int t;
		Job(int id,int d,int p,int t)
		{
			this.id=id;
			this.d=d;
			this.p=p;
			this.t=t;
		}
		
		//Arrange jobs in increasing value of their deadlines as primary sorting criteria
		public int compareTo(Job that)
		{
			if(this.d>that.d)
				return 1;
			else if(this.d<that.d)
				return -1;
			// if deadlines are equal than sort based on ratio of Profit/Processing Time. Take Job with higher ratio ahead.
			else
			{
				if(((double)this.p/(double)this.t)<((double)that.p/(double)that.t))
					return 1;
				else 
					return -1;		
			}
		}
	}
	
	public static int DPCalculate(ArrayList<Job> jobArray,ArrayList<Job> toRemove,int transportOverhead)
	{
		//Sorting the jobArray based on custom sort implemented in Job class which sorts based on deadlines ascending.
		
		int[][] A=new int[jobArray.size()+1][jobArray.get(jobArray.size()-1).d+1];
		for (int i = 0; i < A.length; i++) {
			A[i][0]=0;
		}
		for (int i = 0; i < A[0].length; i++) {
			A[0][i]=0;
		}
		for (int  i= 1;  i< A.length; i++) {
			for (int j = 1; j < A[0].length; j++) {
				int t_=Math.min(j,jobArray.get(i-1).d)-jobArray.get(i-1).t;
				t_-=transportOverhead;					//Including transport overhead time if it is greater than 0
				if(t_<0)
					A[i][j]=A[i-1][j];
				else
				{
					transportOverhead-=jobArray.get(i-1).t;
					transportOverhead=transportOverhead<0?0:transportOverhead;	//Make value of transport overhead to zero if it less than 0
					A[i][j]=Math.max(A[i-1][j],jobArray.get(i-1).p+A[i-1][t_]);	
				}
			}
		}
		
		for (int  i= 0;  i< A.length; i++) {
			for (int j = 0; j < A[0].length; j++){
				System.out.print(A[i][j]+"\t");
			}
			System.out.print("\n");
			}
		printOptimalJobSchedule(jobArray,A,A.length-1,A[0].length-1,toRemove);
		return A[A.length-1][A[0].length-1];
	}
	
	public static int DPCalculateMaxSubset(ArrayList<Job> jobArray,int[] processorTravelCost)
	{
		Arrays.sort(processorTravelCost);
		
		int maxProfit=0;
		ArrayList<Job> toRemove=new ArrayList<Job>();
		
		for (int i = 0; i < processorTravelCost.length; i++) {
			System.out.println("Processor"+ (i+1));
			maxProfit+=DPCalculate(jobArray,toRemove,processorTravelCost[i]);
			jobArray.removeAll(toRemove);
			System.out.println("\n\n\n");
		}
		return maxProfit;
	}
	
	public static void printOptimalJobSchedule(ArrayList<Job> jobArray,int[][] A,int i,int t,ArrayList<Job> toRemove)
	{
		if(i==0)
			return;
		if(A[i][t]==A[i-1][t])
			printOptimalJobSchedule(jobArray,A,i-1,t,toRemove);
		else
		{
			int t_=Math.min(t,jobArray.get(i-1).d)-jobArray.get(i-1).t;
			printOptimalJobSchedule(jobArray,A,i-1,t_,toRemove);
			System.out.println("Job: "+jobArray.get(i-1).id+ " with profit : "+ jobArray.get(i-1).p+" and Deadline : "+jobArray.get(i-1).d+" and Processing time "+jobArray.get(i-1).t+" --> Scheduled at: "+t_);
			toRemove.add(jobArray.get(i-1));
		}
	}
}
