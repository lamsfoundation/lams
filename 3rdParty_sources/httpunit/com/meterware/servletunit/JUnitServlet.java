package com.meterware.servletunit;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2001-2003, Russell Gold
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 *******************************************************************************************************************/
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import junit.runner.BaseTestRunner;
import junit.runner.TestSuiteLoader;
import junit.runner.StandardTestSuiteLoader;
import junit.framework.Test;
import junit.framework.AssertionFailedError;
import junit.framework.TestResult;
import junit.framework.TestFailure;


/**
 * A servlet which can run unit tests inside a servlet context.  It may be extended to provide InvocationContext-access
 * to such tests if a container-specific implementation of InvocationContextFactory is provided.
 * Combined with ServletTestCase, this would permit
 * in-container tests of servlets in a fashion similar to that supported by ServletUnit.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class JUnitServlet extends HttpServlet {

    public JUnitServlet() {
    }


    protected JUnitServlet( InvocationContextFactory factory ) {
        _factory = factory;
    }


    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        ResultsFormatter formatter = getResultsFormatter( request.getParameter( "format" ) );
        response.setContentType( formatter.getContentType() );
        final String testName = request.getParameter( "test" );
        if (testName == null || testName.length() == 0) {
            reportCannotRunTest( response.getWriter(), "No test class specified" );
        } else {
            ServletTestRunner runner = new ServletTestRunner( response.getWriter(), formatter );
            runner.runTestSuite( testName );
        }
        response.getWriter().close();
    }


    private ResultsFormatter getResultsFormatter( String formatterName ) {
        if ("text".equalsIgnoreCase( formatterName )) {
            return new TextResultsFormatter();
        } else if ("xml".equalsIgnoreCase( formatterName )) {
            return new XMLResultsFormatter();
        } else {
            return new HTMLResultsFormatter();
        }
    }


    private InvocationContextFactory _factory;


    private void reportCannotRunTest( PrintWriter writer, final String errorMessage ) {
        writer.print( "<html><head><title>Cannot run test</title></head><body>" + errorMessage + "</body></html>" );
    }


    class ServletTestRunner extends BaseTestRunner {
        private PrintWriter _writer;
        private ResultsFormatter _formatter;


        public ServletTestRunner( PrintWriter writer, ResultsFormatter formatter ) {
            ServletTestCase.setInvocationContextFactory( _factory );
            _writer = writer;
            _formatter = formatter;
        }


        void runTestSuite( String testClassName ) {
            Test suite = getTest( testClassName );

            if (suite != null) {
                TestResult testResult = new TestResult();
                testResult.addListener( this );
                long startTime= System.currentTimeMillis();
                suite.run( testResult );
                long endTime= System.currentTimeMillis();
                _formatter.displayResults( _writer, testClassName, elapsedTimeAsString( endTime-startTime ), testResult );
            }
        }


        public void addError( Test test, Throwable throwable ) {
        }


        public void addFailure( Test test, AssertionFailedError error ) {
        }


        public void endTest( Test test ) {
        }


        protected void runFailed( String s ) {
            reportCannotRunTest( _writer, s );
        }


        public void startTest( Test test ) {
        }


        public void testStarted( String s ) {
        }


        public void testEnded( String s ) {
        }


        public void testFailed( int i, Test test, Throwable throwable ) {
        }


        /**
         * Always use the StandardTestSuiteLoader. Overridden from
         * BaseTestRunner.
         */
        public TestSuiteLoader getLoader() {
            return new StandardTestSuiteLoader();
        }

    }


    static abstract class ResultsFormatter {

        private static final char LF = 10;
        private static final char CR = 13;


        abstract String getContentType();


        void displayResults( PrintWriter writer, String testClassName, String elapsedTimeString, TestResult testResult ) {
            displayHeader( writer, testClassName, testResult, elapsedTimeString );
            displayResults( writer, testResult );
            displayFooter( writer );
        }


        protected abstract void displayHeader( PrintWriter writer, String testClassName, TestResult testResult, String elapsedTimeString );


        protected abstract void displayResults( PrintWriter writer, TestResult testResult );


        protected abstract void displayFooter( PrintWriter writer );


        protected String sgmlEscape( String s ) {
            if (s == null) return "NULL";
            StringBuffer result = new StringBuffer( s.length() );
            char[] chars = s.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                switch( chars[i] ) {
                case '&':
                    result.append( "&amp;" );
                    break;
                case '<':
                    result.append( "&lt;" );
                    break;
                case '>':
                    result.append( "&gt;" );
                    break;
                case LF:
                    if (i > 0 && chars[i-1] == CR) {
                        result.append( chars[i] );
                        break;
                    }
                case CR:
                    result.append( getLineBreak() );
                default:
                    result.append( chars[i] );
                }
            }
            return result.toString();
        }


        protected String getLineBreak() {
            return "<br>";
        }
    }


    abstract static class DisplayedResultsFormatter extends ResultsFormatter {

        protected void displayHeader( PrintWriter writer, String testClassName, TestResult testResult, String elapsedTimeString ) {
            displayHeader( writer, testClassName, getFormatted( testResult.runCount(), "test" ),
                                   elapsedTimeString, testResult.wasSuccessful() ? "OK" : "Problems Occurred" );
        }

        protected void displayResults( PrintWriter writer, TestResult testResult ) {
            if (!testResult.wasSuccessful()) {
                displayProblems( writer, "failure", testResult.failureCount(), testResult.failures() );
                displayProblems( writer, "error", testResult.errorCount(), testResult.errors() );
            }
        }


        protected abstract void displayHeader( PrintWriter writer, String testClassName, String testCountText, String elapsedTimeString, String resultString );


        protected abstract void displayProblemTitle( PrintWriter writer, String title );


        protected abstract void displayProblemDetailHeader( PrintWriter writer, int i, String testName );


        protected abstract void displayProblemDetailFooter( PrintWriter writer );


        protected abstract void displayProblemDetail( PrintWriter writer, String message );


        private void displayProblems( PrintWriter writer, String kind, int count, Enumeration enumeration ) {
            if (count != 0) {
                displayProblemTitle( writer, getFormatted( count, kind ) );
                Enumeration e = enumeration;
                for (int i = 1; e.hasMoreElements(); i++) {
                    TestFailure failure = (TestFailure) e.nextElement();
                    displayProblemDetailHeader( writer, i, failure.failedTest().toString() );
                    if (failure.thrownException() instanceof AssertionFailedError) {
                        displayProblemDetail( writer, failure.thrownException().getMessage() );
                    } else {
                        displayProblemDetail( writer, BaseTestRunner.getFilteredTrace( failure.thrownException() ) );
                    }
                    displayProblemDetailFooter( writer );
                }
            }
        }


        private String getFormatted( int count, String name ) {
            return count + " " + name + (count == 1 ? "" : "s");
        }


    }


    static class TextResultsFormatter extends DisplayedResultsFormatter {

        String getContentType() {
            return "text/plain";
        }


        protected void displayHeader( PrintWriter writer, String testClassName, String testCountText,
                                      String elapsedTimeString, String resultString ) {
            writer.println( testClassName + " (" + testCountText + "): " + resultString );
        }


        protected void displayFooter( PrintWriter writer ) {
        }


        protected void displayProblemTitle( PrintWriter writer, String title ) {
            writer.println();
            writer.println( title + ':' );
        }


        protected void displayProblemDetailHeader( PrintWriter writer, int i, String testName ) {
            writer.println( i + ". " + testName + ":" );
        }


        protected void displayProblemDetailFooter( PrintWriter writer ) {
            writer.println();
        }


        protected void displayProblemDetail( PrintWriter writer, String message ) {
            writer.println( message );
        }
    }


    static class HTMLResultsFormatter extends DisplayedResultsFormatter {

        String getContentType() {
            return "text/html";
        }


        protected void displayHeader( PrintWriter writer, String testClassName, String testCountText,
                                      String elapsedTimeString, String resultString ) {
            writer.println( "<html><head><title>Test Suite: " + testClassName + "</title>" );
            writer.println( "<style type='text/css'>" );
            writer.println( "<!--" );
            writer.println( "  td.detail { font-size:smaller; vertical-align: top }" );
            writer.println( "  -->" );
            writer.println( "</style></head><body>" );
            writer.println( "<table id='results' border='1'><tr>" );
            writer.println( "<td>" + testCountText + "</td>" );
            writer.println( "<td>Time: " + elapsedTimeString + "</td>" );
            writer.println( "<td>" + resultString + "</td></tr>" );
        }


        protected void displayFooter( PrintWriter writer ) {
            writer.println( "</table>" );
            writer.println( "</body></html>" );
        }


        protected void displayProblemTitle( PrintWriter writer, String title ) {
            writer.println( "<tr><td colspan=3>" + title + "</td></tr>" );
        }


        protected void displayProblemDetailHeader( PrintWriter writer, int i, String testName ) {
            writer.println( "<tr><td class='detail' align='right'>" + i + "</td>" );
            writer.println( "<td class='detail'>" + testName + "</td><td class='detail'>" );
        }


        protected void displayProblemDetailFooter( PrintWriter writer ) {
            writer.println( "</td></tr>" );
        }


        protected void displayProblemDetail( PrintWriter writer, String message ) {
            writer.println( sgmlEscape( message ) );
        }

    }



    static class XMLResultsFormatter extends ResultsFormatter {

        String getContentType() {
            return "text/xml;charset=UTF-8";
        }


        protected void displayHeader( PrintWriter writer, String testClassName, TestResult testResult, String elapsedTimeString ) {
            writer.println( "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                            "<testsuite name=" + asAttribute( testClassName ) +
                                      " tests=" + asAttribute( testResult.runCount() ) +
                                      " failures=" + asAttribute( testResult.failureCount() ) +
                                      " errors=" + asAttribute( testResult.errorCount() ) +
                                      " time=" + asAttribute( elapsedTimeString ) + ">" );
        }


        private String asAttribute( int value ) {
            return '"' + Integer.toString( value ) + '"';
        }


        private String asAttribute( String value ) {
            return '"' + sgmlEscape( value ) + '"';
        }


        protected void displayFooter( PrintWriter writer ) {
            writer.println( "</testsuite>" );
        }


        protected void displayResults( PrintWriter writer, TestResult testResult ) {
            displayResults( writer, "failure", testResult.failures() );
            displayResults( writer, "error", testResult.errors() );
        }


        private void displayResults( PrintWriter writer, String failureNodeName, Enumeration resultsEnumeration ) {
            for (Enumeration e = resultsEnumeration; e.hasMoreElements();) {
                TestFailure failure = (TestFailure) e.nextElement();
                writer.println( "  <testcase name=" + asAttribute( failure.failedTest().toString() ) + ">" );
                writer.print( "    <" + failureNodeName + " type=" + asAttribute( failure.thrownException().getClass().getName() ) +
                                                      " message=" + asAttribute( failure.exceptionMessage() ) );
                if (!displayException( failure )) {
                    writer.println( "/>" );
                } else {
                    writer.println( ">" );
                    writer.print( sgmlEscape( BaseTestRunner.getFilteredTrace( failure.thrownException() ) ) );
                    writer.println( "    </" + failureNodeName + ">" );
                }
                writer.println( "  </testcase>" );
            }
        }


        private boolean displayException( TestFailure failure ) {
            return true;
        }


        protected String getLineBreak() {
            return "";
        }
    }


}
