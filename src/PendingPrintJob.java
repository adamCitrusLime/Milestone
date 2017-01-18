import java.util.Date;
import java.util.List;
import java.util.*;

public class PendingPrintJob {

	private int jobID;
	private List<String> associatedJobs;
	private Date timeCreated;
	
	public PendingPrintJob(int JobID,List<String>ReceiptList,Date created){
		jobID = JobID;
		associatedJobs = ReceiptList;
		timeCreated = created;
	}
	
	public int getJobId(){
		return jobID;
	}
	
	public List<String> getAssociatedReceipts(){
		return associatedJobs;
	}
	
	public boolean hasFailed(Date Compare){

		long minuteMill = 60000;
		
		if((timeCreated.getTime() + (10 * minuteMill)) < Compare.getTime()){
			System.out.println("Has failed");
			return true;
		}
		return false;
	}
	
	public boolean jobContainsItem(String receiptCheck){
		
		if(associatedJobs.contains(receiptCheck)){
			return true;			
		}
		return false;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof PendingPrintJob){
			PendingPrintJob test = (PendingPrintJob)o;
			if(test.jobID == this.jobID){
				System.out.println("ReceiptJobMatch");
				return true;
			}
			//System.out.println("ReceiptJobNoMatch");
			return false;
		}
		//System.out.println("ReceiptJobNoMatch");
		return false;
	}
	
	@Override
	public int hashCode(){
		return this.jobID;
	}
}
