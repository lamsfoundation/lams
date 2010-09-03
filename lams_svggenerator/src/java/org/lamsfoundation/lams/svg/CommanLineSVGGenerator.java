package org.lamsfoundation.lams.svg;

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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.jdom.JDOMException;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.svg.SVGGenerator;

/**
 * The command line svggenerator;
 * 
 * @author Andrey Balan
 */
public class CommanLineSVGGenerator {

    public static void main(String[] args) throws JDOMException, IOException, ParseException {
	
	Options options = new Options();

	options.addOption("file", true, "* Absolute path to file");
	options.addOption("width", true, "SVG width");
	options.addOption("height", true, "SVG height");
	options.addOption("scale", true, "scale");

	BasicParser parser = new BasicParser();
	CommandLine cl = parser.parse(options, args);
	
	if (! cl.hasOption("file")) {
	    HelpFormatter f = new HelpFormatter();
	    f.printHelp("OptionsTip", options);
	    System.exit(1);
	}

	String fullFilePath = cl.getOptionValue("file");
	String width = cl.getOptionValue("width");
	String height = cl.getOptionValue("height");
	Double scale = null;
	if (cl.hasOption("scale")) {
	    String scaleStr = cl.getOptionValue("scale");
	    try {
		scale = Double.valueOf(scaleStr);
	    } catch (NumberFormatException e) {
		HelpFormatter f = new HelpFormatter();
		f.printHelp("OptionsTip", options);
		System.exit(1);
	    }
	}
	
	LearningDesignDTO learningDesign = (LearningDesignDTO) FileUtil.getObjectFromXML(null, fullFilePath);

	 SVGGenerator svgGenerator = new SVGGenerator();
	 svgGenerator.setSVGDocumentParameters(width, height, scale);
	 svgGenerator.generateSvg(learningDesign);

	// // Stream out svg document to display
	// OutputFormat format = new OutputFormat(svgGenerator.getSVGDocument());
	// format.setLineWidth(65);
	// format.setIndenting(true);
	// format.setIndent(2);
	// Writer out = new StringWriter();
	// XMLSerializer serializer = new XMLSerializer(out, format);
	// serializer.serialize(svgGenerator.getSVGDocument());
	// System.out.println(out.toString());

	 OutputFormat format = new OutputFormat(svgGenerator.getSVGDocument());
	 format.setLineWidth(65);
	 format.setIndenting(true);
	 format.setIndent(2);
	 // Create file
	 String svgFileName = FileUtil.getFileName(fullFilePath);
	 String fileExtension = FileUtil.getFileExtension(svgFileName);
	 svgFileName = svgFileName.replaceFirst(fileExtension + "$", "svg");
	 String svgFileFullPath = FileUtil.getFullPath(FileUtil.getFileDirectory(fullFilePath), svgFileName);
	 FileWriter fstream = new FileWriter(svgFileFullPath);
	 BufferedWriter out = new BufferedWriter(fstream);
	 XMLSerializer serializer = new XMLSerializer(out, format);
	 serializer.serialize(svgGenerator.getSVGDocument());
	 System.out.println("Creating a file " + svgFileFullPath );
	 // Close the output stream
	 out.close();
    }

}
