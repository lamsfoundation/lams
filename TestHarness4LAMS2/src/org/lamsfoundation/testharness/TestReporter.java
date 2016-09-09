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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.lamsfoundation.testharness.Call.CallRecord;
import org.lamsfoundation.testharness.learner.LearnerTest;

import edu.stanford.ejalbert.BrowserLauncher;

/**
 * @author Fei Yang, Marcin Cieslak
 */
public class TestReporter {
    private static class TemplateCompiler {

	private static Map<String, Object> context;
	private static final int LIST_FIRST = 0;
	private static final int IF_FIRST = 1;
	private static final int EL_FIRST = 2;
	private static final int TEXT_FIRST = 3;
	private static final String EL_START = "${";
	private static final char EL_END = '}';
	private static final String LIST_START = "<#list";
	private static final String LIST_AS = "as";
	private static final String LIST_END = "</#list>";
	private static final String IF_START = "<#if";
	private static final String IF_ELSE = "<#else>";
	private static final String IF_END = "</#if>";
	private static final char TAG_END = '>';

	@SuppressWarnings("unchecked")
	private static String compile(String source) {
	    int listStartIndex = source.indexOf(TemplateCompiler.LIST_START);
	    int ifStartIndex = source.indexOf(TemplateCompiler.IF_START);
	    int elStartIndex = source.indexOf(TemplateCompiler.EL_START);
	    switch (TemplateCompiler.whichFirst(TemplateCompiler.convert(listStartIndex, source),
		    TemplateCompiler.convert(ifStartIndex, source), TemplateCompiler.convert(elStartIndex, source),
		    source.length())) {
		case LIST_FIRST:
		    int listEndIndex = source.indexOf(TemplateCompiler.LIST_END, listStartIndex);
		    int listStartTagIndex = source.indexOf(TemplateCompiler.TAG_END, listStartIndex);
		    int listAsIndex = source.indexOf(TemplateCompiler.LIST_AS, listStartIndex);
		    if (listStartTagIndex == -1) {
			throw new TestHarnessException("'list' tag missing '>'");
		    }
		    if (listAsIndex == -1) {
			throw new TestHarnessException("'as' is required for 'list'");
		    }
		    if (listEndIndex == -1) {
			throw new TestHarnessException("'list' tag unclosed");
		    }
		    StringBuilder middle = new StringBuilder();
		    String middlePart = source.substring(listStartTagIndex + 1, listEndIndex);
		    String collection = TemplateCompiler.extract(
			    source.substring(listStartIndex + TemplateCompiler.LIST_START.length(), listAsIndex));
		    String element = TemplateCompiler.extract(
			    source.substring(listAsIndex + TemplateCompiler.LIST_AS.length(), listStartTagIndex))
			    .trim();
		    for (Object o : (List<Object>) TemplateCompiler.compileEL(collection)) {
			// log.debug("Put " + o + " into context as "+element);
			TemplateCompiler.context.put(element, o);
			middle.append(TemplateCompiler.compile(middlePart));
		    }
		    String frontEnd = source.substring(0, listStartIndex);
		    String backEnd = source.substring(listEndIndex + TemplateCompiler.LIST_END.length());
		    return TemplateCompiler.compile(frontEnd) + middle.toString() + TemplateCompiler.compile(backEnd);
		case IF_FIRST:
		    int ifEndIndex = source.indexOf(TemplateCompiler.IF_END, ifStartIndex);
		    int ifStartTagIndex = source.indexOf(TemplateCompiler.TAG_END, ifStartIndex);
		    int elseIndex = source.indexOf(TemplateCompiler.IF_ELSE, ifStartIndex);
		    if (ifStartTagIndex == -1) {
			throw new TestHarnessException("'if' tag missing '>'");
		    }
		    if (ifEndIndex == -1) {
			throw new TestHarnessException("'if' tag unclosed");
		    }
		    String condition = TemplateCompiler.extract(
			    source.substring(ifStartIndex + TemplateCompiler.IF_START.length(), ifStartTagIndex));
		    Boolean b = (Boolean) TemplateCompiler.compileEL(condition);
		    String ifBlock;
		    if (elseIndex != -1) {
			String middlePart1 = source.substring(ifStartTagIndex + 1, elseIndex);
			String middlePart2 = source.substring(elseIndex + TemplateCompiler.IF_ELSE.length(),
				ifEndIndex);
			ifBlock = b ? middlePart1 : middlePart2;
		    } else {
			String middlePart1 = source.substring(ifStartTagIndex + 1, ifEndIndex);
			ifBlock = b ? middlePart1 : "";
		    }
		    String ifFrontEnd = source.substring(0, ifStartIndex);
		    String ifBackEnd = source.substring(ifEndIndex + TemplateCompiler.IF_END.length());
		    return TemplateCompiler.compile(ifFrontEnd) + TemplateCompiler.compile(ifBlock)
			    + TemplateCompiler.compile(ifBackEnd);
		case EL_FIRST:
		    int elEndIndex = source.indexOf(TemplateCompiler.EL_END, elStartIndex);
		    if (elEndIndex == -1) {
			throw new TestHarnessException("'}' expected");
		    }
		    String elBlock = TemplateCompiler
			    .compileEL(source.substring(elStartIndex + TemplateCompiler.EL_START.length(), elEndIndex))
			    .toString();
		    String elFrontEnd = source.substring(0, elStartIndex);
		    String elBackEnd = source.substring(elEndIndex + 1);
		    return TemplateCompiler.compile(elFrontEnd) + elBlock + TemplateCompiler.compile(elBackEnd);
		case TEXT_FIRST:
		    return source;
		default:
		    throw new TestHarnessException("Unknown error");
	    }
	}

	private static Object compileEL(String el) {
	    String[] references = el.split("\\.");
	    if (references.length == 0) {
		references = new String[] { el.trim() };
	    }
	    Object result = TemplateCompiler.context.get(references[0].trim());
	    for (int i = 1; i < references.length; i++) {
		result = TemplateCompiler.getFieldOrInvokeMethod(result, references[i].trim());
	    }
	    return result;
	}

	private static int convert(int index, String source) {
	    return index == -1 ? source.length() + 1 : index;
	}

	private static String extract(String el) {
	    return el.substring(el.indexOf("${") + 2, el.indexOf('}')).trim();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Object getFieldOrInvokeMethod(Object obj, String reference) {
	    // log.debug("Reference is "+reference);
	    Object result = null;
	    Class clazz = obj.getClass();
	    if (clazz.isArray()) {
		if (reference.equalsIgnoreCase("length")) {
		    return Array.getLength(obj);
		}
	    }
	    try {
		Field field = null;
		try {
		    field = clazz.getDeclaredField(reference);
		} catch (NoSuchFieldException e) {
		    // ignore
		    // log.debug("No such field "+e.getMessage());
		}
		if ((field != null) && (Modifier.isPublic(field.getModifiers()))) {
		    result = field.get(obj);
		}
		if (result == null) {
		    Method method = null;
		    try {
			method = clazz.getMethod(reference);
		    } catch (NoSuchMethodException e) {
			// log.debug("No such method "+e.getMessage());
			// ignore
		    }
		    if (method != null) {
			result = method.invoke(obj);
		    }
		    if (result == null) {// the last resort
			method = TemplateCompiler.getReadMethod(field, reference, obj.getClass());
			if (method != null) {
			    result = method.invoke(obj);
			}
		    }
		}
	    } catch (Exception e) {
		TestReporter.log.debug(e.getMessage(), e);
		// ignore
	    }
	    // log.debug("Result is "+result);
	    return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Method getReadMethod(Field field, String fieldName, Class clazz) throws Exception {
	    String convertedName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	    Method method = null;
	    try {
		method = clazz.getMethod("get" + convertedName);
	    } catch (NoSuchMethodException e) {
		// ignore
		// log.debug("No such method "+e.getMessage());
	    }
	    if (method == null) {
		try {
		    method = clazz.getMethod("is" + convertedName);
		} catch (NoSuchMethodException e) {
		    // ignore
		    // log.debug("No such method " + e.getMessage());
		}
	    }
	    return method;
	}

	private static void init(List<TestSuite> testSuites, List<CallRecord> callRecords) {
	    TemplateCompiler.context = new HashMap<String, Object>();
	    TemplateCompiler.context.put("callRecords", callRecords);
	    TemplateCompiler.context.put("testSuites", testSuites);
	    TemplateCompiler.context.put("time", new SimpleDateFormat("HH:mm:ss dd MMM yyyy").format(new Date()));
	    long total = 0;
	    long count = 0;
	    long joinLessonTotal = 0;
	    long joinLessonCount = 0;
	    for (CallRecord r : callRecords) {
		if ((r.getHttpStatusCode() != null) && r.getHttpStatusCode().equals(200)) {
		    total += r.getTimeInMillis();
		    count++;
		    if (r.getCallee().indexOf("learner.do?method=joinLesson") > 0) {
			joinLessonTotal += r.getTimeInMillis();
			joinLessonCount++;
		    }
		}
	    }
	    TestReporter.log.info("Total response time: " + (total / 1000.0) + " seconds");
	    TestReporter.log.info("Average response time: " + (total / 1000.0 / count) + " seconds");
	    TestReporter.log.info("Average response time of joinLesson calls: "
		    + (joinLessonTotal / 1000.0 / joinLessonCount) + " seconds");
	    TemplateCompiler.context.put("totalResponseTime", total / 1000.0);
	    TemplateCompiler.context.put("averageResponseTime", total / 1000.0 / count);
	    TemplateCompiler.context.put("joinLessonAverageResponseTime", joinLessonTotal / 1000.0 / joinLessonCount);
	}

	private static String load() throws IOException {
	    BufferedReader bReader = null;
	    try {
		StringBuilder source = new StringBuilder();
		bReader = new BufferedReader(new FileReader(TestReporter.fileTemplate));
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

	private static int whichFirst(int listStartIndex, int ifStartIndex, int elStartIndex, int textIndex) {
	    int[] indexes = { listStartIndex, ifStartIndex, elStartIndex, textIndex };
	    return TemplateCompiler.whichMinimum(indexes);
	}

	private static int whichMinimum(int[] nums) {
	    if (nums.length == 0) {
		return -1;
	    }
	    int min = nums[0];
	    int minIndex = 0;
	    for (int i = 1; i < nums.length; i++) {
		if (nums[i] < min) {
		    min = nums[i];
		    minIndex = i;
		}
	    }
	    return minIndex;
	}

    }

    private static final Logger log = Logger.getLogger(TestReporter.class);
    private static final String NEW_LINE = "\n* ";
    private static final String NEW_LINE_INDENT = "\n*   ";
    private static final String REPORT_HEADER = new StringBuilder()
	    .append("See below\n\n**********************************Brief Report*******************************************")
	    .append(TestReporter.NEW_LINE).append(TestReporter.NEW_LINE).append("Disclaimer:")
	    .append(TestReporter.NEW_LINE_INDENT)
	    .append("This program is created in the hope that it will help estimate how many concurrent")
	    .append(TestReporter.NEW_LINE_INDENT)
	    .append("users a LAMS 2.x server can handle, but WITHOUT ANY GARANTEE  the server can support")
	    .append(TestReporter.NEW_LINE_INDENT).append("that number of users in service use.")
	    .append(TestReporter.NEW_LINE).append(TestReporter.NEW_LINE_INDENT)
	    .append("This program is more a load test tool than a functional test tool, ")
	    .append(TestReporter.NEW_LINE_INDENT)
	    .append("so it does NOT GARANTEE there is no functional bug in the target server.")
	    .append(TestReporter.NEW_LINE).append(TestReporter.NEW_LINE).append("Test Result Summary:").toString();

    private static final String REPORT_FOOTER = new StringBuilder().append(TestReporter.NEW_LINE)
	    .append("Refer to the formal test report document for the details.").append(TestReporter.NEW_LINE)
	    .append("\n*****************************************************************************************\n")
	    .toString();
    private static List<CallRecord> callRecords = Collections.synchronizedList(new LinkedList<CallRecord>());
    private static String fileName;

    private static String fileTemplate;

    public static void generateReportFile(TestManager manager) {
	TestReporter.log.info("Generating the formal test report document");
	TemplateCompiler.init(manager.testSuites, TestReporter.callRecords);
	String filename = TestReporter.fileName + "_" + new SimpleDateFormat("MMM-d-HH'h-'mm'm'").format(new Date())
		+ ".html";
	BufferedWriter out = null;
	try {
	    String report = TemplateCompiler.compile(TemplateCompiler.load());
	    out = new BufferedWriter(new FileWriter(filename));
	    out.write(report);
	    BrowserLauncher launcher = new BrowserLauncher(null);
	    launcher.openURLinBrowser(new File(filename).toURI().toURL().toString());
	} catch (Exception e) {
	    TestReporter.log.error("Error when generating report file", e);
	} finally {
	    if (out != null) {
		try {
		    out.close();
		} catch (IOException e) {
		    TestReporter.log.warn("Error when generating report file", e);
		}
	    }
	}
    }

    public static void generateReportLog(TestManager manager) {
	StringBuilder report = new StringBuilder(TestReporter.REPORT_HEADER);
	report.append(TestReporter.NEW_LINE_INDENT).append(manager.testSuites.size()).append(" test suites launched. ");
	report.append(manager.testSuites.size() - manager.countAborted()).append(" test suites finished and ")
		.append(manager.countAborted()).append(" test suites aborted.");

	for (TestSuite testSuite : manager.testSuites) {
	    report.append(TestReporter.NEW_LINE_INDENT).append("Test Suite ").append(testSuite.getSuiteIndex())
		    .append(testSuite.isFinished() ? " finished" : " aborted").append(", in which");
	    report.append(TestReporter.NEW_LINE_INDENT);
	    AbstractTest[] tests = new AbstractTest[] { testSuite.getAdminTest(), testSuite.getAuthorTest(),
		    testSuite.getMonitorTest(), testSuite.getLearnerTest() };
	    boolean first = true;
	    for (AbstractTest test : tests) {
		if (test != null) {
		    if (!first) {
			report.append(", ");
		    }
		    report.append(test.getTestName()).append(test.isFinished() ? " finished" : " aborted");
		    first = false;
		}
	    }
	    report.append(TestReporter.NEW_LINE_INDENT);
	    LearnerTest learnerTest = testSuite.getLearnerTest();
	    report.append("In ").append(learnerTest.getTestName()).append(", ");
	    report.append(learnerTest.countLearners()).append(" learners attended, ");
	    report.append(learnerTest.countLearners() - learnerTest.countAborted()).append(" finished and ");
	    report.append(learnerTest.countAborted()).append(" aborted.");
	    report.append(TestReporter.NEW_LINE);
	}

	report.append(TestReporter.REPORT_FOOTER);
	TestReporter.log.info(report.toString());
    }

    public static List<CallRecord> getCallRecords() {
	return TestReporter.callRecords;
    }

    public static String getFileName() {
	return TestReporter.fileName;
    }

    public static String getFileTemplate() {
	return TestReporter.fileTemplate;
    }

    public static boolean initialized() {
	return ((TestReporter.fileName != null) && (TestReporter.fileTemplate != null));
    }

    public static void setFileName(String fileName) {
	TestReporter.fileName = fileName;
    }

    public static void setFileTemplate(String fileTemplate) {
	TestReporter.fileTemplate = fileTemplate;
    }
}