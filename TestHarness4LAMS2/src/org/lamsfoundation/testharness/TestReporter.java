/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.testharness;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.lamsfoundation.testharness.Call.CallRecord;
import org.lamsfoundation.testharness.learner.LearnerTest;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingExecutionException;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;

/**
 * @version
 *
 * <p>
 * <a href="TestReporter.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class TestReporter {
    
	private static final Logger log = Logger.getLogger(TestReporter.class);
    private static List<CallRecord> callRecords = new LinkedList<CallRecord>();
    private static String fileName;
    private static String fileTemplate;

    public static boolean initialized(){
    	return ((fileName!=null)&&(fileTemplate!=null));
    }
    
    public static List<CallRecord> getCallRecords() {
		return callRecords;
	}


	public static void setCallRecords(List<CallRecord> callRecords) {
		TestReporter.callRecords = callRecords;
	}


	public static String getFileName() {
		return fileName;
	}


	public static void setFileName(String fileName) {
		TestReporter.fileName = fileName;
	}


	public static String getFileTemplate() {
		return fileTemplate;
	}


	public static void setFileTemplate(String fileTemplate) {
		TestReporter.fileTemplate = fileTemplate;
	}


	public static synchronized void addCallRecord(CallRecord callRecord){
        callRecords.add(callRecord);
    }

	public static void report(AbstractTestManager manager) {
		final String NEW_LINE = "\n* ";
		final String NEW_LINE_INDENT = "\n*   ";
		StringBuilder report = new StringBuilder();
		report.append("See below\n\n**********************************Brief Report*******************************************");
		
		//disclaimer
		report.append(NEW_LINE);
		report.append(NEW_LINE).append("Disclaimer:");
		report.append(NEW_LINE_INDENT).append("This program is created in the hope that it will help estimate how many concurrent");
		report.append(NEW_LINE_INDENT).append("users a LAMS 2.x server can handle, but WITHOUT ANY GARANTEE  the server can support");
		report.append(NEW_LINE_INDENT).append("that number of users in service use.");
		report.append(NEW_LINE);
		report.append(NEW_LINE_INDENT).append("This program is more a load test tool than a functional test tool, ");
		report.append(NEW_LINE_INDENT).append("so it does NOT GARANTEE there is no functional bug in the target server.");
		report.append(NEW_LINE);
		
		report.append(NEW_LINE).append("Test Result Summary:");
		report.append(NEW_LINE_INDENT).append(manager.testSuites.size()).append(" test suite(s) launched. ");
		report.append(manager.testSuites.size()-manager.countAborted()).append(" test suite(s) finished, and ").append(manager.countAborted()).append(" test suite(s) aborted.");
		for (TestSuite testSuite : manager.testSuites){
			report.append(NEW_LINE_INDENT).append("Test Suite ").append(testSuite.getSuiteIndex()).append(testSuite.isFinished()? " finished" : " aborted").append(", in which");
			report.append(NEW_LINE_INDENT);
			AbstractTest[] tests = new AbstractTest[]{testSuite.getAdminTest(),testSuite.getAuthorTest(),testSuite.getMonitorTest(),testSuite.getLearnerTest()};
			boolean first = true;
			for(AbstractTest test : tests){
				if(test!=null){
					if(!first)
						report.append(", ");
					report.append(test.getTestName()).append(test.isFinished()? " finished" : " aborted");
					first = false;
				}
			}
			report.append(NEW_LINE_INDENT);
			LearnerTest learnerTest = testSuite.getLearnerTest();
			report.append("In ").append(learnerTest.getTestName()).append(", ");
			report.append(learnerTest.countLearners()).append(" learner(s) attended, "); 
			report.append(learnerTest.countLearners()-learnerTest.countAborted()).append(" finished and ");
			report.append(learnerTest.countAborted()).append(" aborted.");
			report.append(NEW_LINE);
		}
		report.append(NEW_LINE).append("Refer to the formal test report document for the details.");
		report.append(NEW_LINE).append("\n*****************************************************************************************\n");
		log.info(report.toString());
	}
	
	public static void generateReport(AbstractTestManager manager) {
		report(manager);
		log.info("Generating the formal test report document...");
		TemplateCompiler.init(manager.testSuites, callRecords);
		String filename = generateFileName();
		BufferedWriter out = null;
		try {
			String report = TemplateCompiler.compile(TemplateCompiler.load());
	        out = new BufferedWriter(new FileWriter(filename));
	        out.write(report);
	        out.close();
			BrowserLauncher launcher = new BrowserLauncher(null);
			launcher.openURLinBrowser(new File(filename).toURL().toString());
		} catch (MalformedURLException e) {
			log.debug(e.getMessage(),e);
	    } catch (IOException e) {
	    	log.debug(e.getMessage(), e);
	    }catch (BrowserLaunchingInitializingException e) {
			log.debug(e.getMessage(),e);
		} catch (UnsupportedOperatingSystemException e) {
			log.debug(e.getMessage(),e);
		} catch (BrowserLaunchingExecutionException e) {
			log.debug(e.getMessage(),e);
		} finally{
			if( out!=null ){
				try{
					out.close();
				}catch(IOException e){
					log.debug(e.getMessage(), e);
				}
			}
		}
	}
	
	private static String generateFileName(){
		return fileName + "_" + new SimpleDateFormat("MMM-d-HH'h-'mm'm'").format(new Date())+".html";
	}

	public static void main(String[] args){
		String[] results = "testSuite.suiteIndex".split("\\.");
		System.out.println(results.length);
	}
	
	private static class TemplateCompiler{
		
		static Map<String, Object> context;
		static final int LIST_FIRST = 0;
		static final int IF_FIRST = 1;
		static final int EL_FIRST = 2;
		static final int TEXT_FIRST = 3;
		
		static final String EL_START = "${";
		static final char EL_END = '}';
		static final String LIST_START = "<#list";
		static final String LIST_AS = "as";
		//static final String LIST_GROUP_BY = "group by";
		static final String LIST_END = "</#list>";
		static final String IF_START = "<#if";
		static final String IF_ELSE = "<#else>";
		static final String IF_END = "</#if>";
		static final char TAG_END = '>';
		//static final String AVG = "avg";
		//static final String SUM = "sum";
		//static final String COUNT = "count";
		//static final char PARAM_START = '(';
		//static final char PARAM_END = ')';

		static void init(List<TestSuite> testSuites, List<CallRecord> callRecords){
			context = new HashMap<String, Object>();
			context.put("callRecords", callRecords);
			context.put("testSuites", testSuites);
			context.put("time", new SimpleDateFormat("HH:mm:ss dd MMM yyyy").format(new Date()));
		}
		
		static String load() throws IOException {
			BufferedReader bReader = null;
			try {
				StringBuilder source = new StringBuilder();
				bReader = new BufferedReader(new FileReader(
						fileTemplate));
				String line = bReader.readLine();
				while (line != null) {
					source.append(line).append('\n');
					line = bReader.readLine();
				}
				return source.toString();
			} finally {
				if (bReader != null) {
					bReader.close();
				}
			}
		}

		private static String compile(String source) {
			int listStartIndex = source.indexOf(LIST_START);
			int ifStartIndex = source.indexOf(IF_START);
			int elStartIndex = source.indexOf(EL_START);
			switch(whichFirst(convert(listStartIndex,source), convert(ifStartIndex, source), convert(elStartIndex, source), source.length())){
			case LIST_FIRST:
				int listEndIndex = source.indexOf(LIST_END, listStartIndex);
				int listStartTagIndex = source.indexOf(TAG_END, listStartIndex);
				int listAsIndex = source.indexOf(LIST_AS, listStartIndex);
				if(listStartTagIndex == -1) throw new TestHarnessException("'list' tag missing '>'");
				if(listAsIndex == -1) throw new TestHarnessException("'as' is required for 'list'");
				if(listEndIndex == -1) throw new TestHarnessException("'list' tag unclosed");
				StringBuilder middle = new StringBuilder();
				String middlePart = source.substring(listStartTagIndex+1,listEndIndex);
				String collection = extract(source.substring(listStartIndex + LIST_START.length(), listAsIndex));
				String element = extract(source.substring(listAsIndex + LIST_AS.length(), listStartTagIndex)).trim();
				for(Object o : (List)compileEL(collection)){
					//log.debug("Put " + o + " into context as "+element);
					context.put(element, o);
					middle.append(compile(middlePart));
				}
				String frontEnd = source.substring(0, listStartIndex);
				String backEnd = source.substring(listEndIndex + LIST_END.length());
				return compile(frontEnd) + middle.toString() + compile(backEnd);
			case IF_FIRST:
				int ifEndIndex = source.indexOf(IF_END, ifStartIndex);
				int ifStartTagIndex = source.indexOf(TAG_END, ifStartIndex);
				int elseIndex = source.indexOf(IF_ELSE, ifStartIndex);
				if(ifStartTagIndex == -1) throw new TestHarnessException("'if' tag missing '>'");
				if(ifEndIndex == -1) throw new TestHarnessException("'if' tag unclosed");
				String condition = extract(source.substring(ifStartIndex + IF_START.length(), ifStartTagIndex));
				Boolean b = (Boolean)compileEL(condition);
				String ifBlock;
				if(elseIndex != -1){
					String middlePart1 = source.substring(ifStartTagIndex+1, elseIndex);
					String middlePart2 = source.substring(elseIndex + IF_ELSE.length(), ifEndIndex);
					ifBlock = b? middlePart1 : middlePart2;
				}else{
					String middlePart1 = source.substring(ifStartTagIndex+1, ifEndIndex);
					ifBlock = b? middlePart1:"";
				}
				String ifFrontEnd = source.substring(0, ifStartIndex);
				String ifBackEnd = source.substring(ifEndIndex + IF_END.length());
				return compile(ifFrontEnd) + compile(ifBlock) + compile(ifBackEnd);
			case EL_FIRST:
				int elEndIndex = source.indexOf(EL_END, elStartIndex);
				if(elEndIndex == -1) throw new TestHarnessException("'}' expected");
				String elBlock = compileEL(source.substring(elStartIndex + EL_START.length(), elEndIndex)).toString();
				String elFrontEnd = source.substring(0, elStartIndex);
				String elBackEnd = source.substring(elEndIndex+1);
				return compile(elFrontEnd) + elBlock + compile(elBackEnd);
			case TEXT_FIRST:
				return source;
			default:
				throw new TestHarnessException("unexpected error happened!");
			}
		}

		private static int whichFirst(int listStartIndex, int ifStartIndex, int elStartIndex, int textIndex){
			int[] indexes = {listStartIndex, ifStartIndex, elStartIndex, textIndex};
			return whichMinimum(indexes);
		}
		
		
		private static int whichMinimum(int[] nums) {
			if(nums.length == 0){
				return -1;
			}
			int min = nums[0];
			int minIndex = 0; 
			for(int i=1; i<nums.length; i++){
				if(nums[i]<min){
					min = nums[i];
					minIndex = i;
				}
			}
			return minIndex;
		}

		private static int convert(int index, String source){
			return index == -1? source.length() + 1 : index;
		}
		
		private static Object compileEL(String el) {
			//log.debug("EL is " + el);
			String[] references = el.split("\\.");
			if(references.length == 0){
				references = new String[]{el.trim()};
			}
			Object result = context.get(references[0].trim());
			//log.debug("Retrieve "+references[0].trim()+" from context and got "+result);
			for(int i=1; i < references.length; i++){
				result = getFieldOrInvokeMethod(result, references[i].trim());
			}
			return result;
		}
		
		private static String extract(String el) {
			return el.substring(el.indexOf("${")+2,el.indexOf('}')).trim();
		}
		
		private static Object getFieldOrInvokeMethod(Object obj, String reference){
			//log.debug("Reference  is "+reference);
			Object result = null;
			Class clazz = obj.getClass();
			if(clazz.isArray()){
				if(reference.equalsIgnoreCase("length"))
					return Array.getLength(obj);
			}
			try {
				Field field = null;
				try{
					field = clazz.getDeclaredField(reference);
				}catch(NoSuchFieldException e){
					//ignore
					//log.debug("No such field "+e.getMessage());
				}
				if((field != null)&&(Modifier.isPublic(field.getModifiers()))){
					result = field.get(obj);
				}
				if(result == null){
					Method method = null;
					try{
						method = clazz.getMethod(reference);
					}catch(NoSuchMethodException e){
						//log.debug("No such method "+e.getMessage());
						//ignore
					}
					if(method != null){
						result = method.invoke(obj);
					}
					if(result == null){//the last resort
						method = getReadMethod(field, reference, obj.getClass());
						if(method != null){
							result = method.invoke(obj);
						}
					}
				}
			} catch (Exception e) {
				log.debug(e.getMessage(), e);
				//ignore
			} 
			//log.debug("Result is "+result);
			return result;
		}

		private static Method getReadMethod(Field field, String fieldName, Class clazz) throws Exception{
			String convertedName = fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
			Method method = null;
			try{
				method = clazz.getMethod("get"+convertedName);
			}catch(NoSuchMethodException e){
				//ignore
				//log.debug("No such method "+e.getMessage());
			}
			if(method == null){
				try{
					method = clazz.getMethod("is"+convertedName);
				}catch(NoSuchMethodException e){
					//ignore
					//log.debug("No such method " + e.getMessage());
				}
			}
			return method;
		}

	}

}
