/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.webservice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.flagstone.transform.FSBounds;
import com.flagstone.transform.FSColor;
import com.flagstone.transform.FSDoAction;
import com.flagstone.transform.FSImport;
import com.flagstone.transform.FSMovie;
import com.flagstone.transform.FSSetBackgroundColor;
import com.flagstone.transform.FSShowFrame;
import com.flagstone.translate.ASParser;

/**
 * @author pgeorges
 *
 * @web:servlet name="WhiteboardToSwfServlet"
 * @web:servlet-mapping url-pattern="/WhiteboardToSwf"
 */
public class WhiteboardToSwfServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(WhiteboardToSwfServlet.class);
    private int swfVersion = 6;
    private ASParser parser = new ASParser();
    private String FILEPATH = "c:\\test\\";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {

	PrintWriter out = null;
	String dir = System.getProperty("java.io.tmpdir");

	try {
	    out = response.getWriter();

	    String trace = request.getParameter("trace");
	    String courseId = request.getParameter("courseId");
	    String nTotal = request.getParameter("nTotal");
	    String nPages = request.getParameter("nPages");
	    String author = request.getParameter("author");
	    String bgName = request.getParameter("bgName");
	    String bgColor = request.getParameter("bgColor");
	    String bgClip = request.getParameter("bgClip");
	    String nFigures = request.getParameter("nFigures");
	    String nShapes = request.getParameter("nShapes");
	    String nTexts = request.getParameter("nTexts");
	    String nLines = request.getParameter("nLines");
	    String vFigure = request.getParameter("vFigure");
	    String vShape = request.getParameter("vShape");
	    String vText = request.getParameter("vText");
	    String vLine = request.getParameter("vLine");
	    String xMaxInTwips = request.getParameter("xMaxInTwips");
	    String yMaxInTwips = request.getParameter("yMaxInTwips");
	    String bgColorR = request.getParameter("bgColorR");
	    String bgColorB = request.getParameter("bgColorB");
	    String bgColorG = request.getParameter("bgColorG");

	    Random r = new Random();

	    String filename = Long.toString(Math.abs(r.nextLong()), 50);
	    String completeFilename = FILEPATH + filename + ".as";

	    try {
		BufferedWriter outFile = new BufferedWriter(new FileWriter(completeFilename));
		String fromFlashToFile = trace + author + bgName + bgColor + nPages + nTexts + nLines + nShapes
			+ nFigures + vText + vLine + vShape + vFigure;
		String correctedFlashToFile = fromFlashToFile.replace("//,", "");
		String correctedFlashToFileSecond = correctedFlashToFile.replace("//", "");
		outFile.write(correctedFlashToFileSecond);
		outFile.close();

		log.debug(correctedFlashToFileSecond);
		out.write(correctedFlashToFileSecond);
	    } catch (IOException e) {
	    }

	    FSMovie movie = new FSMovie();
	    movie.setFrameSize(new FSBounds(0, 0, Integer.valueOf(xMaxInTwips), Integer.valueOf(yMaxInTwips)));
	    movie.add(new FSSetBackgroundColor(
		    new FSColor(Integer.valueOf(bgColorR), Integer.valueOf(bgColorG), Integer.valueOf(bgColorB))));
	    movie.setFrameRate(12);
	    movie.setVersion(swfVersion);

	    importSwfFiles(movie, bgClip, Integer.parseInt(nTotal));

	    String readFromFlash = readFile(completeFilename);
	    byte[] actionsFromFlash = parser.parse(readFromFlash).encode(swfVersion);
	    FSDoAction frameFromFlash = new FSDoAction(actionsFromFlash);
	    movie.add(frameFromFlash);
	    movie.add(new FSShowFrame());

	    String readFromDrawAs = readFile(FILEPATH + "draw.as");
	    byte[] actionsFromDrawAs = parser.parse(readFromDrawAs).encode(swfVersion);
	    FSDoAction frameFromDrawAs = new FSDoAction(actionsFromDrawAs);
	    movie.add(frameFromDrawAs);
	    movie.add(new FSShowFrame());

	    writeFile(movie, FILEPATH, "test.swf");

	} catch (IOException e) {
	    log.error("print writer threw exception, " + e);
	} catch (Exception e) {
	    //log.error(e);
	    //out.write("\n"+e);
	} finally {
	    if (out != null) {
		out.close();
	    }
	}

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {

	doPost(request, response);
    }

    private void importSwfFiles(FSMovie movie, String bgClip, int nTotal) {
	movie.add(new FSImport(FILEPATH + "logo.swf", movie.newIdentifier(), "logo"));
	movie.add(new FSImport(FILEPATH + bgClip + ".swf", movie.newIdentifier(), "background"));
	movie.add(new FSImport(FILEPATH + "right.swf", movie.newIdentifier(), "right"));
	movie.add(new FSImport(FILEPATH + "left.swf", movie.newIdentifier(), "left"));

	for (int i = 1; i <= nTotal; i++) {
	    movie.add(new FSImport(FILEPATH + "shape" + i + ".swf", movie.newIdentifier(), "shape" + i));
	}
    }

    private String readFile(String filename) {
	String readText = new String();
	try {
	    BufferedReader in = new BufferedReader(new FileReader(filename));
	    String line = in.readLine();
	    while (line != null) {
		readText += line;
		line = in.readLine();
	    }
	    in.close();
	} catch (IOException e) {
	}

	return readText;
    }

    private void writeFile(FSMovie movie, String path, String fileName) {
	try {
	    movie.encodeToFile(path + fileName);
	} catch (FileNotFoundException e) {
	    System.err.println("Cannot open file: " + e.getMessage());
	} catch (IOException e) {
	    System.err.println("Cannot write to file: " + e.getMessage());
	}
    }

}
