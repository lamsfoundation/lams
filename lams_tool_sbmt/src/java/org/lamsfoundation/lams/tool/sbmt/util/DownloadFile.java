package org.lamsfoundation.lams.tool.sbmt.util;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
/**
 * Utility class for downloading a file from within a servlet. 
 * Will either download as an attachement (should trigger a 
 * save pop up dialogue) or output the stream and leave the 
 * browser to decide whether to display or save file.
 * 
 * @author Manpreet Minhas
 */
public class DownloadFile
{

	private static Logger log = Logger.getLogger(DownloadFile.class);

	private final static String UTF_8_ENCODING = "UTF-8";

	/** Send the file as a stream and attempts to have the browser save the file if preferDownload is -true- 
	 * or display in the window if preferDownload is -false-. mimeType is optional - if null then calls getMimeType() to
	 * work it out. Assumes that the calling function has already determined that it is okay 
	 * to download the file. The servlet context is needed to determine the mime type.
	 * @param filename - just the name.ext filename
	 * @param fullFilename - fill path and filename,
	 * */
	public static void download(
		HttpServletResponse response,
		ServletContext context,
		String filename,
		String fullFilename,
		String inputMimeType,
		boolean preferDownload)
		throws IOException
	{
		process(response, context, filename, fullFilename, inputMimeType, preferDownload);
	}

	/** Send the file as a stream and attempts to have the browser save the file rather
	 * than displaying in the window. mimeType is optional - if null then calls getMimeType() to
	 * work it out. Assumes that the calling function has already determined that it is okay 
	 * to download the file. The servlet context is needed to determine the mime type.
	 * @param filename - just the name.ext filename
	 * @param fullFilename - fill path and filename,
	 * */
	public static void download(
		HttpServletResponse response,
		ServletContext context,
		String filename,
		String fullFilename,
		String inputMimeType)
		throws IOException
	{
		process(response, context, filename, fullFilename, inputMimeType, true);
	}

	/** Send the file as a stream and leaves it up to the browser whether to display in a 
	 * browser window or save the file. mimeType is optional - if null then calls getMimeType() to
	 * work it out. Assumes that the calling function has already determined that it is okay 
	 * to download the file. The servlet context is needed to determine the mime type.
	 * @param filename - just the name.ext filename
	 * @param fullFilename - fill path and filename,
	 * */
	public static void send(
		HttpServletResponse response,
		ServletContext context,
		String filename,
		String fullFilename,
		String inputMimeType)
		throws IOException
	{
		process(
			response,
			context,
			filename,
			fullFilename,
			inputMimeType,
			false);
	}

	private static void process(
		HttpServletResponse response,
		ServletContext context,
		String filename,
		String fullFilename,
		String inputMimeType,
		boolean saveFile)
		throws IOException
	{
	
		String mimeType = null;

		if (inputMimeType != null)
		{
			mimeType = inputMimeType;
		}
		else if (context != null)
		{
			mimeType = context.getMimeType(filename);
		}

		if (mimeType == null)
		{
			mimeType = "application/octet-stream";
		}

		log.debug("Downloading file " + filename + " mime type " + mimeType);

		response.setContentType(mimeType);
		
		InputStream in = null;
		ServletOutputStream out = null;
		
		try
		{
			
					
			File fileob = new File(fullFilename);
			//	Check if the requested file exists				
			if(fileob.exists())
			{				
				if (saveFile)
					{
							log.debug("Sending as attachment");
							response.setHeader("Content-Disposition","attachment;filename=" + filename);
					}
					else
					{
						log.debug("Sending as inline");
						response.setHeader("Content-Disposition","inline;filename=" + filename);
					}
					response.setHeader("Cache-control","must-revalidate");
					response.addHeader("Content-Description", filename);
			}
			else
			{	
				throw new IOException("Requested File is missing : " + fullFilename);
			}
			out = response.getOutputStream();
			log.debug("Writing out file ");
			int count = 0;
				
			FileInputStream fileToRead = new FileInputStream(fileob);
			in = new BufferedInputStream(fileToRead);			
			int ch;
			while ((ch = in.read()) != -1)
			{
				out.write((char) ch);
				count++;
			}
			log.debug("Wrote out " + count + " bytes");
			response.setContentLength(count);
			out.flush();
	 }
	catch (IOException e)
	{
		log.error( "Exception in process():" + e.getMessage());		
		throw new IOException(e.getClass().getName());
	}
	finally
	{
			try
					{
						log.debug("Closing file");
						if (in != null)
							in.close(); // very important
						if (out != null)
							out.close();
					}
					catch (IOException e)
					{
						log.error("Error Closing file" + e.getMessage());
						throw new IOException(e.getClass().getName());
					}
	}
}


	/**
	 * Method export. Downloading the data not from a file but
	 * from a byte[]
	 * @param res
	 * @param context
	 * @param designName
	 * @param exportType
	 * @param encryptedContent
	 */
	public static void export(
		HttpServletResponse res,
		ServletContext context,
		String designName,
		String exportType,
		String exportString)
		throws IOException, ServletException
	{
		ServletOutputStream out = null;
		ByteArrayInputStream bai = null;
		BufferedInputStream bis = null;
		try
		{
			String filename = designName + "." + exportType;
			byte[] exportBytes = exportString.getBytes( UTF_8_ENCODING );
			int size = exportBytes.length;
			res.setContentType("application/x-lams;name=" + filename);
			res.setHeader(
				"Content-Disposition",
				"attachment; filename=" + filename + ";");
			res.setHeader("Cache-Control", "no-cache");
			res.setHeader("Content-Transfer-Encoding", "binary");
			res.setContentLength(size);

			out = res.getOutputStream();
			bai = new ByteArrayInputStream( exportBytes );
			bis = new BufferedInputStream(bai);
			int data;
			while ((data = bis.read()) != -1)
			{
				out.write(data);
			}
		}
		catch (IOException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
		finally
		{
			if (bis != null)
				bis.close();
			if (out != null)
			{
				out.flush();
				out.close();
			}
		}
	}

}

