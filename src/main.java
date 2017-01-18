import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.print.PrinterJob;
import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import javax.swing.text.SimpleAttributeSet;

import org.apache.http.util.EncodingUtils;
import org.cups4j.*;
import org.cups4j.PrintJob.Builder;

import javax.xml.rpc.ServiceException;
import javax.xml.ws.WebServiceRef;

import org.omg.IOP.Encoding;
import org.tempuri.*;

public class main {

	static String firmwareVersion = "2.0.1";
	static String IPAddress = "";
	static boolean finished = false;
	static SolidServicesLocator ws = new SolidServicesLocator();
	
	static CupsPrinter cReceiptPrinter = null;
	static CupsPrinter cLabelPrinter = null;

	static ArrayList<String> completedReceiptTransactions = new ArrayList<String>();
	static List<PendingPrintJob> toRemoveReceipt = new ArrayList<PendingPrintJob>();
	static List<PendingPrintJob> currentReceiptJobs = new ArrayList<PendingPrintJob>();

	static ArrayList<String> completedLabelTransactions = new ArrayList<String>();
	static List<PendingPrintJob> toRemoveLabel = new ArrayList<PendingPrintJob>();
	static List<PendingPrintJob> currentLabelJobs = new ArrayList<PendingPrintJob>();

	static int currService = 1;

	static int printerNumber = 0;

	static boolean zplReset = false;
	static boolean zplModeSet = false;

	public static void main(String[] args) throws Exception {

		getPrinterNumber();

		System.out.println("#####".toString().replace('#', (char) 158));

		PrintService[] printTest = PrinterJob.lookupPrintServices();
		PrintService receiptPrinter = null;
		
		InetAddress iAddress = getCurrentIp();
		if(iAddress != null){
			IPAddress = iAddress.getHostAddress();
		}
		
		testReceiptLoop();

		StringBuilder LabelTest = new StringBuilder();

		// LabelTest.append("\nN\n"
		// + "q609"
		// + "Q203,26"
		// + "B26,26,UA0,2,2,152,B,\"" + "123456789" + "\"\n"
		// + "P1,1\n");

		for (int i = 0; i <= 9; i++) {
			LabelTest.append("^XA\n");
			LabelTest
					.append("\r^FO025,20\n\r^AF,35\n\r^FDAll Condition Armadill 700\n\r^FS\n^"); // Text
			LabelTest
					.append("\r^FO025,50\n\r^BC,100,N,N\n\r^FD00014-4105\n\r^FS\n"); // Barcode
			LabelTest.append("\r^FO25,155\n\r^AD\n\r^FD00014-4105\n\r^FS\n^"); // Barcode
																				// value
			LabelTest
					.append("\r^FO250,155\n\r^AD\n\r^CI2,156,36\n\r^FH_15\n\r^FD$35.00\n\r^FS\n^"); // Price
																									// -
																									// CI2
																									// -
																									// 21
																									// =
																									// euro,
																									// 156
																									// =
																									// pound
			LabelTest
					.append("\r^FO75,235\n\r^AD\n\r^FDCitrus Lime Test Environment\n\r^FS\n^");
			LabelTest.append("^XZ \n ");

		}
		// LabelTest.append("^XA\n\r^MNM\n\r^FO050,50\n\r^B8N,100,Y,N\n\r^FD1234567\n\r^FS\n\r^PQ1\n\r^XZ");

		// "^XA^FO20,10^AD^FDZEBRA^FS^FO20,60^B3^FDAAA001^FS^XZ"

		// asciiTest.append(new String(cut));

	}

	public static InetAddress getCurrentIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) networkInterfaces
                        .nextElement();
                Enumeration<InetAddress> nias = ni.getInetAddresses();
                while(nias.hasMoreElements()) {
                    InetAddress ia= (InetAddress) nias.nextElement();
                    if (!ia.isLinkLocalAddress() 
                     && !ia.isLoopbackAddress()
                     && ia instanceof Inet4Address) {
                        return ia;
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println("unable to get current IP " + e.getMessage());
        }
        return null;
    }
	
	private static void asciiTest() throws Exception {

		StringBuilder asciiTest = new StringBuilder();
		char[] fontA = new char[] { 0x1b, 'R', 3 };

		asciiTest.append(fontA);
		asciiTest.append("\n");
		// asciiTest.append("Milestone " + printerNumber + " is now active!");

		// for(int i = 0; i<= 255; i++){
		// asciiTest.append(i + " - " + (char)i + "|");
		// }

		// for(int i = 0; i < 12; i++){
		// asciiTest.append("\n");
		// }

		if (cReceiptPrinter != null) {

			// SerialPort port = new SerialPort("COM3");
			// try{
			// port.openPort();
			//
			// port.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,
			// SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			// port.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN|SerialPort.FLOWCONTROL_RTSCTS_OUT);
			//
			// port.writeString(testStr.toString());
			// port.closePort();
			//
			// }catch(Exception e){
			//
			// System.out.println(e.getLocalizedMessage());
			// }
			InputStream is;
			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			DocFlavor flv = DocFlavor.INPUT_STREAM.AUTOSENSE;
			is = new ByteArrayInputStream(asciiTest.toString().getBytes());
			PrintJob cJob = new PrintJob.Builder(is).build();
			PrintRequestResult pResult = cReceiptPrinter.print(cJob);

		}
	}

	private static void testReceiptLoop() throws RemoteException {

		int pound = 158;

		SolidServicesSoap wsClient = null;
		try {
			wsClient = ws.getSolidServicesSoap();
			org.apache.axis.client.Stub s = (SolidServicesSoapStub) wsClient;
			s.setTimeout(3000); // 5 second, in miliseconds

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			System.out.println(new Date().toString());

			CupsClient cClient = new CupsClient();

			List<CupsPrinter> printerList = cClient.getPrinters();

			for (CupsPrinter curr : printerList) {
				if (curr.getName().equals("ReceiptPrinter")) {
					cReceiptPrinter = curr;
					System.out.println("FOUND RECEIPT PRINTER");
				}
				if (curr.getName().equals("LabelPrinter")) {
					cLabelPrinter = curr;
					System.out.println("FOUND LABEL PRINTER");
				}

				if (cReceiptPrinter != null && cLabelPrinter != null) {
					break;
				}
			}

			try {
				for (PrintJobAttributes curr : cClient.getJobs(cReceiptPrinter,
						WhichJobsEnum.NOT_COMPLETED, "root", false)) {

					System.out.println("Receipt Job " + curr.getJobID()
							+ " is being cancelled");
					cClient.cancelJob(curr.getJobID());
				}

				for (PrintJobAttributes curr : cClient.getJobs(cLabelPrinter,
						WhichJobsEnum.NOT_COMPLETED, "root", false)) {
					System.out.println("Label Job " + curr.getJobID()
							+ " is being cancelled");
					cClient.cancelJob(curr.getJobID());
				}

			} catch (Exception e) {
				System.out.println("Printer purge failed!");
			}

			// start loop here

			asciiTest();

			Date nextPoll = new Date();
			nextPoll.setSeconds(new Date().getSeconds() + 2);

			int rebootcheck=0;
			int labelcheck=0;
			
			while (true) {
				
				try {
					boolean serviceAvalible = true;

					if (new Date().after(nextPoll)) {
						
						if (rebootcheck>=10){ // only check for reboots one time in ten
							try {
							    
								if (wsClient.logStatus_CheckReboot(IPAddress, firmwareVersion, printerNumber)) {
									break;
								}
							} catch (Exception ex) {
								// Here so live units do not break before update
								// goes out.
							} 
							rebootcheck=0;
						}
						// if(serviceAvalible){
						DateFormat dateFormat = new SimpleDateFormat(
								"yyyy/MM/dd HH:mm:ss");
						System.out.println("Receipt Start : "
								+ dateFormat.format(new Date()));
						doReceiptJobs(wsClient);
						System.out.println("Receipt End : "
								+ dateFormat.format(new Date()));
						
						if (labelcheck>=3){ // only check for labels one time in three
							System.out.println("Label Start : "
									+ dateFormat.format(new Date()));
							
							doLabelJobs(wsClient);
							
							System.out.println("Label End: "
									+ dateFormat.format(new Date()));
							labelcheck=0;
						}
						
					

						nextPoll = new Date();
						nextPoll.setSeconds(new Date().getSeconds() + 2);
						// }
					}
				} catch (Exception ex) {
					labelcheck=0; // do not reset reboot request as this may present a faulty unit retarting
					ex.printStackTrace();
				}
				
				// increment our counters
				rebootcheck++;
				labelcheck++;
			}
		} catch (Exception ex) {
			System.out.println(ex.getLocalizedMessage());
			ex.printStackTrace();
		}

	}

	private static void doReceiptJobs(SolidServicesSoap wsClient)
			throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		if (cReceiptPrinter != null) {
			InputStream is;
			StringBuilder receiptStr = new StringBuilder();
			// String cut = 0x1d + "V1";
			char[] cut = new char[] { 0x1d, 'V', '1' };
			char[] fontA = new char[] { 0x1b, 'R', 3 };
			char[] drawerKick = new char[] { 0x1b, 'p', '0', '5', '5' };

			String[] receipts = new String[] {};
			try {
				System.out.println("GetOutstandingReceiptsBegin : "
						+ dateFormat.format(new Date()));
				receipts = wsClient.getOutStandingReceipts(printerNumber)
						.split("---NEWRECEIPT---");
				System.out.println("GetOutstandingReceiptsSuccessful");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Awaaaaa");
			}

			System.out.println("ShowingReceipts");
			System.out.println(receipts);

			toRemoveReceipt = new ArrayList<PendingPrintJob>();

			for (PendingPrintJob curr : currentReceiptJobs) {
				if (curr.hasFailed(new Date())) {
					toRemoveReceipt.add(curr);
				}
			}

			currentReceiptJobs.removeAll(toRemoveReceipt);

			List<PendingPrintJob> receiptJobsToAdd = new ArrayList<PendingPrintJob>();
			boolean notPresent = true;

			for (String curr : receipts) {
				if (!curr.equals("")) {

					String[] splitStr = curr.split("\\|\\|");
					String receiptIDStr = splitStr[0];

					if (!currentReceiptJobs.isEmpty()) {
						for (PendingPrintJob currReceipt : currentReceiptJobs) {
							if (currReceipt.jobContainsItem(receiptIDStr)) {
								notPresent = false;
							} else {

							}
						}
						if (notPresent) {
							completedReceiptTransactions.add(splitStr[0]);
							receiptStr.append(fontA + "\n");
							receiptStr.append("\n");
							receiptStr.append(splitStr[1]);
							for (int i = 0; i < 8; i++) {
								receiptStr.append("\n");
							}

							receiptStr.append(cut);
							receiptStr.append("\n");
							receiptStr.append(drawerKick);

							is = new ByteArrayInputStream(receiptStr.toString()
									.replace('#', (char) 36).getBytes());

							if (!receiptStr.equals("")) {
								PrintJob cJob = new PrintJob.Builder(is)
										.build();
								PrintRequestResult pResult = cReceiptPrinter
										.print(cJob);
								if (pResult.getJobId() != -1) {
									List<String> newReceipt = new ArrayList<String>();
									newReceipt.add(splitStr[0]);
									receiptJobsToAdd.add(new PendingPrintJob(
											pResult.getJobId(), newReceipt,
											new Date()));
									System.out.println("Job added - "
											+ pResult.getJobId()
											+ " - Receipt -" + splitStr[0]);
									receiptStr = new StringBuilder();
								}
							}

						}
					}

					else {
						completedReceiptTransactions.add(splitStr[0]);
						receiptStr.append(splitStr[1]);
						for (int i = 0; i < 8; i++) {
							receiptStr.append("\n");
						}
						receiptStr.append(cut);
						receiptStr.append("\n");
						receiptStr.append(drawerKick);

						is = new ByteArrayInputStream(receiptStr.toString()
								.getBytes());

						if (!receiptStr.equals("")) {
							PrintJob cJob = new PrintJob.Builder(is).build();
							PrintRequestResult pResult = cReceiptPrinter
									.print(cJob);
							if (pResult.getJobId() != -1) {
								List<String> newReceipt = new ArrayList<String>();
								newReceipt.add(splitStr[0]);
								receiptJobsToAdd.add(new PendingPrintJob(
										pResult.getJobId(), newReceipt,
										new Date()));
								System.out.println("Job added - "
										+ pResult.getJobId() + " - Receipt -"
										+ splitStr[0]);
								receiptStr = new StringBuilder();
							}
						}

					}

				}

			}

			if (!receiptJobsToAdd.isEmpty()) {

				for (PendingPrintJob currJob : receiptJobsToAdd) {
					currentReceiptJobs.add(currJob);
				}

			}

			// is = new ByteArrayInputStream(receiptStr.toString().getBytes());
			//
			// System.out.println("receiptStr: " + receiptStr.toString());
			//
			// if(!receiptStr.equals("")){
			// PrintJob cJob = new PrintJob.Builder(is).build();
			// PrintRequestResult pResult = cReceiptPrinter.print(cJob);
			// if(pResult.getJobId() != -1){
			// currentReceiptJobs.add(new
			// PendingPrintJob(pResult.getJobId(),completedReceiptTransactions,new
			// Date()));
			// System.out.println("Job added - " + pResult.getJobId());
			// }

			// }
		}
		toRemoveReceipt = new ArrayList<PendingPrintJob>();

		List<Integer> completedJobs = new ArrayList<Integer>();
		
		for (PendingPrintJob curr : currentReceiptJobs) {
			if (cReceiptPrinter.getJobStatus(curr.getJobId()) == JobStateEnum.ABORTED
					|| cReceiptPrinter.getJobStatus(curr.getJobId()) == JobStateEnum.CANCELED) {
				System.out
						.println("Print failed - Job ID : " + curr.getJobId());
				toRemoveReceipt.add(curr);
			}

			if (cReceiptPrinter.getJobStatus(curr.getJobId()) == JobStateEnum.COMPLETED) {
				System.out.println("Print Success - Job ID : "
						+ curr.getJobId());

				for (String currID : curr.getAssociatedReceipts()) {
					completedJobs.add(Integer.parseInt(currID));
					//wsClient.markReceiptStatus(Integer.parseInt(currID), true);
				}
				toRemoveReceipt.add(curr);
			}

		}

		if (completedJobs.size() > 0) {
			try {
				int[] ids = new int[completedJobs.size()];
				for (int i = 0; i < completedJobs.size(); i++) {
					ids[i] = completedJobs.get(i);
				}
				wsClient.bulkSetReceiptStatus(ids);
			} catch (Exception ex) {
				System.out.println("Error occurred when attempting to mark receipt's status");
				System.out.println("EX MSG: " + ex.getMessage());
				ex.printStackTrace();
				toRemoveReceipt = new ArrayList<PendingPrintJob>();
				TimeUnit.MILLISECONDS.sleep(1000);
			}
		}
		
		if (!toRemoveReceipt.isEmpty()) {
			for (PendingPrintJob curr : toRemoveReceipt) {
				currentReceiptJobs.remove(curr);
			}
		}
	}

	private static void doLabelJobs(SolidServicesSoap wsClient) throws Exception {

		List<Integer> completedJobs = new ArrayList<Integer>();

		toRemoveLabel = new ArrayList<PendingPrintJob>();

		if (cLabelPrinter != null) {
			InputStream is;
			StringBuilder labelStr = new StringBuilder();

			String[] labels = new String[] {};
			try {
				labels = wsClient.getOutStandingLabels(printerNumber).split(
						"---NEWLABELJOB---");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println(labels);

			toRemoveLabel = new ArrayList<PendingPrintJob>();

			for (PendingPrintJob curr : currentLabelJobs) {
				if (curr.hasFailed(new Date())) {
					toRemoveLabel.add(curr);
				}
			}

			currentLabelJobs.removeAll(toRemoveLabel);

			// zplReset

			// if(!zplReset){
			// labelStr.append("~JR");
			// zplReset = true;
			// }

			for (String curr : labels) {

				// if(!zplModeSet){
				// labelStr.append("^SZ1");
				// zplModeSet = true;
				// }

				int labelID = 0;
				if (!curr.equals("")) {

					String[] splitStr = curr.split("\\|\\|");
					String receiptIDStr = splitStr[0];

					if (!currentLabelJobs.isEmpty()) {
						for (PendingPrintJob currReceipt : currentLabelJobs) {
							if (currReceipt.jobContainsItem(receiptIDStr)) {
								// Don't add, receipt already pending
							} else {
								completedLabelTransactions.add(splitStr[0]);
								labelStr.append(splitStr[1]);
							}
						}
					} else {
						completedLabelTransactions.add(splitStr[0]);
						labelStr.append(splitStr[1]);
					}

				}

			}

			is = new ByteArrayInputStream(labelStr.toString().getBytes());

			System.out.println("LabelStr: " + labelStr.toString());
			int TotalLabels = 0;
			if (!labelStr.equals("")) {
				PrintJob cJob = new PrintJob.Builder(is).build();
				PrintRequestResult pResult = cLabelPrinter.print(cJob);
				if (pResult.getJobId() != -1) {
					PendingPrintJob insert = new PendingPrintJob(
							pResult.getJobId(), completedLabelTransactions,
							new Date());
					currentLabelJobs.add(insert);
					TotalLabels = insert.getAssociatedReceipts().size();
					for (String currID : insert.getAssociatedReceipts()) {
						try {
							// wsClient.markLabelStatus(Integer.parseInt(currID),
							// true);
							// TimeUnit.MILLISECONDS.sleep(1000);
							completedJobs.add(Integer.parseInt(currID));
							toRemoveLabel.add(insert);
						} catch (Exception ex) {
							System.out
									.println("Error occurred when attempting to mark label's status");
							System.out.println("EX MSG: " + ex.getMessage());
							ex.printStackTrace();
							TimeUnit.MILLISECONDS.sleep(1000);
						}
					}

					System.out.println("Job added - " + pResult.getJobId());

					// if(TotalLabels > 0){
					// TimeUnit.MILLISECONDS.sleep(5000 + (250 * TotalLabels));
					// }

				}

			}
		}

		// for(PendingPrintJob curr: currentLabelJobs){
		//
		// if(cLabelPrinter.getJobStatus(curr.getJobId()) ==
		// JobStateEnum.ABORTED || cLabelPrinter.getJobStatus(curr.getJobId())
		// == JobStateEnum.CANCELED){
		// System.out.println("Print failed - Job ID : " + curr.getJobId());
		// toRemoveLabel.add(curr);
		// }
		//
		//
		// if(cLabelPrinter.getJobStatus(curr.getJobId()) ==
		// JobStateEnum.COMPLETED){
		// System.out.println("Print Success - Job ID : " + curr.getJobId());
		// System.out.println(cLabelPrinter.getJobStatus(curr.getJobId()).toString());
		// for(String currID : curr.getAssociatedReceipts()){
		// try{
		// //wsClient.markLabelStatus(Integer.parseInt(currID), true);
		// //TimeUnit.MILLISECONDS.sleep(1000);
		// completedJobs.add(Integer.parseInt(currID));
		// toRemoveLabel.add(curr);
		// }catch(Exception ex){
		// System.out.println("Error occurred when attempting to mark label's status");
		// System.out.println("EX MSG: " + ex.getMessage());
		// ex.printStackTrace();
		// TimeUnit.MILLISECONDS.sleep(1000);
		// }
		// }
		// }
		//
		//
		//
		// }

		if (completedJobs.size() > 0) {
			try {
				int[] ids = new int[completedJobs.size()];
				for (int i = 0; i < completedJobs.size(); i++) {
					ids[i] = completedJobs.get(i);
				}
				wsClient.bulkSetLabelStatus(ids);
			} catch (Exception ex) {
				System.out
						.println("Error occurred when attempting to mark label's status");
				System.out.println("EX MSG: " + ex.getMessage());
				ex.printStackTrace();
				toRemoveLabel = new ArrayList<PendingPrintJob>();
				TimeUnit.MILLISECONDS.sleep(1000);
			}
		}

		if (!toRemoveLabel.isEmpty()) {
			for (PendingPrintJob curr : toRemoveLabel) {
				currentLabelJobs.remove(curr);
			}
		}

	}

	private static void getPrinterNumber() {
		try {
			List<String> lines = java.nio.file.Files.readAllLines(
					Paths.get("/home/debian/Downloads/printerNumber.txt"),
					StandardCharsets.UTF_8);
			printerNumber = Integer.parseInt(lines.get(0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(printerNumber);
	}

	static class PrintJobWatcher {
		// true iff it is safe to close the print job's input stream
		boolean done = false;

		PrintJobWatcher(DocPrintJob job) {
			// Add a listener to the print job
			job.addPrintJobListener(new PrintJobAdapter() {
				public void printJobCanceled(PrintJobEvent pje) {
					allDone();
				}

				public void printJobCompleted(PrintJobEvent pje) {
					allDone();
				}

				public void printJobFailed(PrintJobEvent pje) {
					allDone();
				}

				public void printJobNoMoreEvents(PrintJobEvent pje) {
					allDone();
				}

				void allDone() {
					synchronized (PrintJobWatcher.this) {
						done = true;
						PrintJobWatcher.this.notify();
					}
				}
			});
		}

		public synchronized void waitForDone() {
			try {
				while (!done) {
					wait();
				}
			} catch (InterruptedException e) {
			}
		}
	}

}
