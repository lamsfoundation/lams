/**
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 */

package org.lamsfoundation.lams.util;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * CSVUtil Provides "Comma Seperated Value" writing and parsing.
 * The two methods write() and parse() will perform writing to and parse from
 * the CSV format
 *
 * @author <a href="mailto:anthony.xiao@lamsinternational.com">Anthony Xiao</a>
 */
public class CSVUtil {

    private static final char QUOTE = '"';
    private static final char COMMA = ',';

    /* precompile the patterns to speed up the search */

    // should we put quotes around a value?
    //private static final Pattern CONTAINS_NEWLINE = Pattern.compile(".*(\\n|\\r)+.*");

    // should we put quotes around a value?
    private static final Pattern CONTAINS_COMMA = Pattern.compile(".*(,)+.*");

    // should we escape the quotes?
    private static final Pattern CONTAINS_QUOTE = Pattern.compile("\"");

    // how should we wrap qoutes around comma or newline?
    //private static final String WRAP_QOUTE = "\"$0\"";

    // how should we escape the value if it has qoutes?
    private static final String ESCAPE_QUOTE = "\"\"";

    // has this value been wrapped with quotes
    private static final Pattern WRAPPED_QUOTE = Pattern.compile("^\"(.*((,|\\n|\\r)+).*)\"$");

    // has this value been escaped by ESCAPE_QUOTE
    private static final Pattern ESCAPED_QUOTE = Pattern.compile("\"\"");

    // how should we unescape the the ESCAPED_QUOTE?
    private static final String UNWRAP_QOUTE = "$1";

    // how should we unescape the the ESCAPED_COMMA?
    private static final String UNESCAPE_QUOTE = "\"";

    /*
     * NOTE: why are we using \\n|\\r in CONTAINS_NEWLINE and WRAPPED_QUOTE?
     * javadoc says "." represents "Any character (may or may not match line terminators)"
     * and we want to make sure terminiators such as newline (\n) gets matched as well
     * if we dont match it then ,\n, will get written as ","\n","
     */

    /**
     * Writes a array of String into CSV format
     * 
     * @param vals
     *            - The array of string to be written into CSV format
     * @return
     */
    public static String write(String[] vals) {
	String str = "";
	int lastIndex = vals.length - 1;
	for (int i = 0; i < vals.length; i++) {
	    // str += vals[i].replaceAll("\"", "\"\"").replaceAll(".*,.*", "\"$0\"") + 
	    // (i==lastIndex?"":","); //same as below but used compiled patterns

	    //check for quotes then escape it 
	    String tmp = CONTAINS_QUOTE.matcher(vals[i]).replaceAll(ESCAPE_QUOTE);
	    //System.out.println("trace: " + vals[i] + ", " + tmp);

	    //check for comma then escape it
	    /**
	     * NOTE: the replaceAll method will not replace accross multiple line
	     * hence \n,\n will be replaced as \n","\n but "\n,\n" is expected
	     * therefore we put quotes around tmp without using replace
	     */
	    // String wrapped = CONTAINS_COMMA.matcher(tmp).replaceAll("\"$0\"");
	    // String wrapped = CONTAINS_COMMA.matcher(tmp).replaceAll('"' + tmp + '"');
	    String wrapped = CONTAINS_COMMA.matcher(tmp).find() ? '"' + tmp + '"' : tmp;

	    //check if tmp has been wrapped because of commas found,
	    //if not wrapped then look look for newline and then wrap it
	    if (wrapped.equals(tmp)) {
		wrapped = CONTAINS_COMMA.matcher(tmp).find() ? '"' + tmp + '"' : tmp;
	    }

	    str += wrapped + (i == lastIndex ? "" : ","); //dont append comma to the last value
	}
	return str;
    }

    /**
     * Parse the CSV formatted string and return each value seperatly stored in an array
     *
     * The parse() method is design to parse 1 record only, each value in the record
     * can contain , newline(\n) or carriage-return(\r).
     * but parse() is not design to handle newline or carriage-return outside the quoted
     * values. newline or carriage-return outside the quoted values signals a new record.
     *
     * If someone decide parse() need to handle multiple rows of record then we can extend
     * the functionaliy of this method.
     *
     * @param str
     * @return
     */
    public static String[] parse(String str) throws ParseException {
	List<String> res = new LinkedList<String>();
	int startIndex = 0;
	boolean openQuote = false;

	str += ","; //end the last value with comma, so last value can be found correctly

	for (int i = 0; i < str.length(); i++) {
	    char ch = str.charAt(i);

	    //match pairs of quote
	    if (ch == QUOTE) {
		openQuote = !openQuote;
	    }

	    //if comma is detected and is not inside quotes, then we have found a value
	    else if ((ch == COMMA) && !openQuote) {
		String val = str.substring(startIndex, i);
		//System.out.println("trace: found - " + val);

		// check for escaped quote then unescape it
		String tmp = ESCAPED_QUOTE.matcher(val).replaceAll(UNESCAPE_QUOTE);

		// check for wrapped quotes then unwrap it
		res.add(WRAPPED_QUOTE.matcher(tmp).replaceAll(UNWRAP_QOUTE));
		//System.out.println("trace: " + val + " - " + tmp);

		//res.add(val.replaceAll("^\"((.|\\p{Space})+)\"$", "$1").replaceAll("\"\"", "\""));

		startIndex = i + 1;
	    }
	}
	if (openQuote) {
	    throw new ParseException("Fail to find matching \" while parsing [" + str + "] ", startIndex);
	}

	String[] resStr = new String[res.size()];
	res.toArray(resStr);

	return resStr;
    }
}
