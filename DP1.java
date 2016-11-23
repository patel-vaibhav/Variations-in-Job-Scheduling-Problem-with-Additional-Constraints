package JobScheduling;
import java.util.*;

//Scheduling Jobs With Deadlines, Profits, and Durations
//http://www.cs.mun.ca/~kol/courses/2711-f13/dynprog.pdf

public class DP1 {
	public static void main(String[] args) {
		DP1 dp=new DP1();
		//Job J=dp.new Job(id,d,p,t);
		Job J1=dp.new Job(1,5,10,5);   // 2
		Job J2=dp.new Job(2,10,8,3);   // 3
		Job J3=dp.new Job(3,15,13,4);  // 4
		Job J4=dp.new Job(4,5,16,4);   // 1   
		Job[] jobArray={J1,J2,J3,J4};
		Arrays.sort(jobArray);
		ArrayList<Job> jobListInput=new ArrayList<Job>(Arrays.asList(jobArray));
		int noOfProcessors=2;
		
		System.out.println("Maximum Profit that can be obtained --> "+DPCalculateMaxSubset(jobListInput,2));
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
	
	public static int DPCalculate(ArrayList<Job> jobArray,ArrayList<Job> toRemove)
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
				if(t_<0)
					A[i][j]=A[i-1][j];
				else
					A[i][j]=Math.max(A[i-1][j],jobArray.get(i-1).p+A[i-1][t_]);	
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
	
	public static int DPCalculateMaxSubset(ArrayList<Job> jobArray,int noOfProcessors)
	{
		int maxProfit=0;
		ArrayList<Job> toRemove=new ArrayList<Job>();
		for (int i = 0; i < noOfProcessors; i++) {
			System.out.println("Processor"+ (i+1));
			maxProfit+=DPCalculate(jobArray,toRemove);
			jobArray.removeAll(toRemove);
			System.out.println("\n\n\n");
		}
		return maxProfit;
	}
	
	/*
	public static int DPCalculate(Job[] jobArray,int noOfProcessors)
	{
		//Sorting the jobArray based on custom sort implemented in Job class which sorts based on deadlines ascending.
		Arrays.sort(jobArray);
		int divide=(int) Math.ceil(jobArray.length/noOfProcessors);
		Job[][] newJobDistribution=new Job[noOfProcessors][divide];
		int processorIndex=0,sequenceIndex=0;
		for(int j=0;j<jobArray.length;j++)
		{
			if(processorIndex==noOfProcessors)
			{
				processorIndex=0;
				sequenceIndex++;
			}
			newJobDistribution[processorIndex][sequenceIndex]=jobArray[j];
		}
		
		for (int i = 0; i < newJobDistribution.length; i++) {
			ArrayList<Job> jobArraylist=new ArrayList<Job>();
			for (int j = 0; j < newJobDistribution[0].length; j++) {
				if(newJobDistribution[i][j]!=null)
				{
					jobArraylist.add(newJobDistribution[i][j]);
				}
				else
					break;
			}
			Job[] jobArrayHelper=new Job[jobArraylist.size()];
			jobArrayHelper=jobArraylist.toArray(jobArrayHelper);
			
		//	DPCalculate(jobArrayHelper);
		}
		
		return 0;
	}
	*/
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
