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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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

    public static void main(String[] args) throws JDOMException, IOException, ParseException, TranscoderException {
	
	Options options = new Options();

	options.addOption("file", true, "* Absolute path to file");
	options.addOption("width", true, "SVG width");
	options.addOption("output", true, "Directory to output resulted files");

	BasicParser parser = new BasicParser();
	CommandLine cl = parser.parse(options, args);
	
	if (! cl.hasOption("file")) {
	    HelpFormatter f = new HelpFormatter();
	    f.printHelp("java -jar lams-svggenerator.jar", options);
	    System.exit(1);
	}

	String fullFilePath = cl.getOptionValue("file");
	String outputOption = cl.getOptionValue("output");
	String widthStr = cl.getOptionValue("width");
	Integer width = null;
	if (widthStr != null) {
	    try {
		width = Integer.valueOf(widthStr);
	    } catch (NumberFormatException e) {
		HelpFormatter f = new HelpFormatter();
		f.printHelp("java -jar lams-svggenerator.jar", options);
		System.exit(1);
	    }
	}
	
	LearningDesignDTO learningDesign = (LearningDesignDTO) FileUtil.getObjectFromXML(null, fullFilePath);

	SVGGenerator svgGenerator = new SVGGenerator();
	svgGenerator.adjustDocumentWidth(width);
	svgGenerator.generateSvgDom(learningDesign);

	// Create output file
	String fileName = FileUtil.getFileName(fullFilePath);
	String fileExtension = FileUtil.getFileExtension(fileName);
	String svgFileName = fileName.replaceFirst(fileExtension + "$", "svg");	
	String svgOutputPath = (outputOption == null) 
		? FileUtil.getFullPath(FileUtil.getFileDirectory(fullFilePath), svgFileName) 
		: FileUtil.getFullPath(outputOption, svgFileName);
	OutputStream svgOutputStream = new FileOutputStream(svgOutputPath);
	svgGenerator.streamOutDocument(svgOutputStream, SVGGenerator.OUTPUT_FORMAT_SVG);
	System.out.println("Creating a file " + svgOutputPath);
	
	String pngFileName = fileName.replaceFirst(fileExtension + "$", "png");	
	String pngOutputPath = (outputOption == null) 
		? FileUtil.getFullPath(FileUtil.getFileDirectory(fullFilePath), pngFileName) 
		: FileUtil.getFullPath(outputOption, pngFileName);
	OutputStream pngOutputStream = new FileOutputStream(pngOutputPath);
	svgGenerator.streamOutDocument(pngOutputStream, SVGGenerator.OUTPUT_FORMAT_PNG);
	System.out.println("Creating a file " + pngOutputPath);
	
    }

}
