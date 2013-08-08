package com.cloudmunch.pmd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.json.JSONException;
import org.json.JSONObject; 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class PMDErrorClassifier {
  
	private String inputfilepath;
	private String errors_str;
	private String warnings_str;
	private String information_str;
	private int num_errors=0;
	private int num_warnings=0;
	private int num_informations=0;
	private String [] errorClassificationArray;
	private String [] warningClassificationArray ;
	private String [] informationClassificationArray;
	private boolean isClassificationProvided() {
		
		if (this.getErrors_str().isEmpty()==true && this.getWarnings_str().isEmpty()==true && this.getInformation_str().isEmpty() == true) {
			return false;
		}
		return true;
	}
	private void classifyIssues() throws Exception {
		try {
			 File fXmlFile = new File(this.getInputfilepath());
	       
			 
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(fXmlFile);
	         doc.getDocumentElement().normalize();
	         NodeList nList = doc.getChildNodes();
	         
	         
	         nList = nList.item(0).getChildNodes();
	         
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	        	 	
	                 org.w3c.dom.Node nNode = nList.item(temp);
	                
	                 if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
	                         Element eElement = (Element) nNode;
	                         NodeList violationList = eElement.getChildNodes();
	                         for (int l = 0; l < violationList.getLength(); l++) {

	                                Node violationNode = violationList.item(l);
	                                
	                                if (violationNode.getNodeType() == Node.ELEMENT_NODE) {
	                                		
	                                		Element violationElement = (Element) violationNode;
	                                		System.out.println(violationElement.getAttribute("priority"));
	                                        if (this.isViolationError(violationElement)==true) {
	                                        	this.incrementNum_errors();
	                                        }
	                                        if ( this.isViolationWarning(violationElement)==true) {
	                                        	this.incrementNum_warnings();
	                                        }
	                                        if (this.isViolationInformation(violationElement)==true) {
	                                        	this.incrementNum_informations();
	                                        }
	                                        
	                                } 
	                        }

	                         
	                 }
	         }
	         
		 } catch (Exception e) {
	         e.printStackTrace();
		 }

	}
	private boolean isPriorityMatched(String [] priorityArray, Element violationElement) 
	{
		
		String priority = violationElement.getAttribute("priority");
		
		if (null == priorityArray) {
			return false;
		}
		if (null == priority) {
			return false;
		}

		for (int i=0; i < priorityArray.length;i++) {
			String priorityRec=priorityArray[i];
			if (priorityRec == null) {
				continue;
			}
			
			if (priorityRec.equalsIgnoreCase(priority)==true) {
				
				return true;
			}
			
		}
		
		return false;
	}
	
	private boolean isViolationError(Element violationElement) 
	{
		
		return isPriorityMatched(this.errorClassificationArray,violationElement);
	}
	
	private boolean isViolationWarning(Element violationElement) 
	{
		
		return isPriorityMatched(this.warningClassificationArray,violationElement);
	}
	
	private boolean isViolationInformation(Element violationElement) 
	{
		
		return isPriorityMatched(this.informationClassificationArray,violationElement);
	}
	private void convertStringToArray(String [] stringsArray, String inputString ){
		int i=0;
		
		if (inputString != null && inputString.isEmpty()==false) {
			for (String retval: inputString.split(",")){
				stringsArray[i]=retval;
				i++;
				
		      }
		}
		return;
	}
	private void printJsonreport(String outputFileName) throws JSONException, IOException {
		
		int actual = this.getNum_errors();
		int total = this.getNum_errors()+this.getNum_informations()+ this.getNum_warnings();
		String content =  "{\"summary\":{\"total\":\""+total+"\",\"actual\":\""+actual+"\"}}";
		JSONObject reportJson =  new JSONObject(content);
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
		bw.write(reportJson.toString(1));
		bw.close();
		
	}
	public void createJSONOutput(String outputFileName) throws Exception {
		this.errorClassificationArray = new String[5];
		this.warningClassificationArray = new String[5];
		this.informationClassificationArray = new String[5];
		
		if (this.getInputfilepath().isEmpty()== true) {
			throw new Exception ("No Input File is Specified");
		}
		if (this.isClassificationProvided() == true) {
			this.convertStringToArray(this.errorClassificationArray,this.getErrors_str());
			this.convertStringToArray(this.warningClassificationArray,this.getWarnings_str());
			this.convertStringToArray(this.informationClassificationArray,this.getInformation_str());
			
		}
		else {
			this.errorClassificationArray[0]="1";
			this.errorClassificationArray[1]="2";
			this.errorClassificationArray[2]="3";
			this.errorClassificationArray[3]="4";
			this.errorClassificationArray[4]="5";
		}
		
		this.classifyIssues();
		try {
			this.printJsonreport(outputFileName);
		}
		catch (Exception e) {
			System.out.println("Unable to  classify violations");
			e.printStackTrace();
		}
			
	}
	
	public PMDErrorClassifier(String XMLFilePath,String errors, String warnings, String information) {
		this.setInputfilepath(XMLFilePath);
		this.setErrors_str(errors);
		this.setWarnings_str(warnings);
		this.setInformation_str(information);
	}
	
	private void incrementNum_errors() {
		this.num_errors++;
		return;
	}
	private void incrementNum_warnings() {
		this.num_warnings++;
		return;
	}
	private void incrementNum_informations() {
		this.num_informations++;
		return;
	}
	public String getInputfilepath() {
		return inputfilepath;
	}
	public void setInputfilepath(String inputfilepath) {
		this.inputfilepath = inputfilepath;
	}
	public String getErrors_str() {
		return errors_str;
	}
	public void setErrors_str(String errors_str) {
		this.errors_str = errors_str;
	}
	public String getWarnings_str() {
		return warnings_str;
	}
	public void setWarnings_str(String warnings_str) {
		this.warnings_str = warnings_str;
	}
	public String getInformation_str() {
		return information_str;
	}
	public void setInformation_str(String information_str) {
		this.information_str = information_str;
	}
	public int getNum_errors() {
		return num_errors;
	}
	
	public void setNum_errors(int num_errors) {
		this.num_errors = num_errors;
	}
	public int getNum_warnings() {
		return num_warnings;
	}
	public void setNum_warnings(int num_warnings) {
		this.num_warnings = num_warnings;
	}
	public int getNum_informations() {
		return num_informations;
	}
	public void setNum_informations(int num_informations) {
		this.num_informations = num_informations;
	}
	

}
