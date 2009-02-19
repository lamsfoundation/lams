package org.lamsfoundation.lams.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

public class ExternalServerUtil {
	
	private static Logger logger = Logger.getLogger(ExternalServerUtil.class);
	
    public static InputStream getResponseInputStreamFromExternalServer(String urlStr, HashMap<String, String> params)
    throws IOException {
		if (!urlStr.endsWith("?"))
		    urlStr += "?";
		
		for (Entry<String, String> entry : params.entrySet()) {
		    urlStr += "&" + entry.getKey() + "=" + entry.getValue();
		}
		
		logger.debug("Making request to external servlet: " + urlStr);
		
		URL url = new URL(urlStr);
		URLConnection conn = url.openConnection();
		if (!(conn instanceof HttpURLConnection)) {
		    logger.error("Fail to connect to external server though url:  " + urlStr);
		    throw new ToolException("Fail to connect to external server though url:  " + urlStr);
		}
		
		HttpURLConnection httpConn = (HttpURLConnection) conn;
		if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		    logger.error("Fail to fetch data from external server, response code:  " + httpConn.getResponseCode()
			    + " Url: " + urlStr);
		    throw new ToolException("Fail to fetch data from external server, response code:  "
			    + httpConn.getResponseCode() + " Url: " + urlStr);
		}
		
		InputStream is = url.openConnection().getInputStream();
		if (is == null) {
		    logger.error("Fail to fetch data from external server, return InputStream null:  " + urlStr);
		    throw new ToolException("Fail to fetch data from external server, return inputStream null:  " + urlStr);
		}
		
		return is;
	}
    
    public static File writeFileAndDir(InputStream is, String filename, String dir){
    	File file = null;
    	file = new File(dir);
    	file.mkdirs();
    	return writeFile(is, dir + filename);
    }
    
    public static File writeFile(InputStream is, String filename){
    	File file = null;
    	
    	try{
    		file = new File(filename);
    		
            FileOutputStream out = new FileOutputStream(file);
      
            //int maxMem = Integer.getInteger(Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_MEMORY_SIZE))
            
            byte[] buf = new byte[2 * 1024];
            int len;
            while ((len = is.read(buf)) > 0) {
            	out.write(buf, 0, len);
            }    
    	}catch(FileNotFoundException e){
    		logger.error("File not found:  " + e.getMessage());
    	}catch(Exception e){
    		logger.error("Fail to create file:  " + e.getMessage());
    	}
    	
        return file;
    }
}
