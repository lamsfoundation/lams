package org.lamsfoundation.lams.web.filter;


import javax.servlet.http.*;
import java.io.*;

/** A response wrapper that takes everything the client
 *  would normally output and saves it in one big
 *  character array. 
 *  <P>
 *  Taken from More Servlets and JavaServer Pages
 *  from Prentice Hall and Sun Microsystems Press,
 *  http://www.moreservlets.com/.
 *  &copy; 2002 Marty Hall; may be freely used or adapted.
 */

public class ResponseWrapper extends HttpServletResponseWrapper{

	private CharArrayWriter charWriter;

	  /** Initializes wrapper.
	   *  <P>
	   *  First, this constructor calls the parent
	   *  constructor. That call is crucial so that the response
	   *  is stored and thus setHeader, setStatus, addCookie,
	   *  and so forth work normally.
	   *  <P>
	   *  Second, this constructor creates a CharArrayWriter
	   *  that will be used to accumulate the response.
	   */
	  
	  public ResponseWrapper(HttpServletResponse response) {
	    super(response);
	    charWriter = new CharArrayWriter();
	  }

	  /** When servlets or JSP pages ask for the Writer, 
	   *  don't give them the real one. Instead, give them
	   *  a version that writes into the character array.
	   *  The filter needs to send the contents of the
	   *  array to the client (perhaps after modifying it).
	   */
	  
	  public PrintWriter getWriter() {
	    return(new PrintWriter(charWriter));
	  }

	  /** Get a String representation of the entire buffer.
	   *  <P>
	   *  Be sure <B>not</B> to call this method multiple times
	   *  on the same wrapper. The API for CharArrayWriter
	   *  does not guarantee that it "remembers" the previous
	   *  value, so the call is likely to make a new String
	   *  every time.
	   */
	  
	  public String toString() {
	    return(charWriter.toString());
	  }

	  /** Get the underlying character array. */
	  
	  public char[] toCharArray() {
	    return(charWriter.toCharArray());
	  }
	}