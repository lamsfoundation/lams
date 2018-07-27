package org.lamsfoundation.lams.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class Base64StringToImageUtil {
    private static Logger log = Logger.getLogger(Base64StringToImageUtil.class);

    public static boolean create(String dir, String filename, String ext, String data) {
	try {
	    File fileDir = new File(dir);
	    fileDir.mkdirs();

	    byte[] byteArray = Base64.decodeBase64(data.getBytes());

	    InputStream in = new ByteArrayInputStream(byteArray);
	    BufferedImage image = javax.imageio.ImageIO.read(in);

	    ByteArrayOutputStream os = new ByteArrayOutputStream();

	    try {
		// put buffer data into byte output stream
		ImageIO.write(image, ext, os);
		byte[] imagebytes = os.toByteArray();

		// write bytes into file
		FileOutputStream fileos = new FileOutputStream(new File(dir + filename + "." + ext));
		fileos.write(imagebytes);
		fileos.close();

		return true;
	    } catch (Exception e) {
		log.error(e);
	    }
	} catch (Exception e) {
	    log.error(e);
	}

	return false;
    }
}
