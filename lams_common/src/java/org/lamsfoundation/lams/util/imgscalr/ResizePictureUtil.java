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

package org.lamsfoundation.lams.util.imgscalr;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.CircularByteBuffer;
import org.lamsfoundation.lams.util.imgscalr.Scalr.Method;

/**
 * @author Andrey Balan
 */
public class ResizePictureUtil {

    private static Logger log = Logger.getLogger(ResizePictureUtil.class);

    /**
     * Reads the original image, creates a resized copy of it and returns its input stream. largestDimension is the
     * largest
     * dimension of the resized image, the other dimension is scaled accordingly.
     *
     * @param is
     *            original image's input stream
     * @param largestDimension
     *            the largest dimension of the resized image, the other dimension is scaled accordingly
     * @return
     * @throws IOException
     * @throws UploadImageGalleryFileException
     */
    public static InputStream resize(InputStream is, int largestDimension) throws IOException {
	try {
	    // load image
	    BufferedImage image = ImageIO.read(is); 
	    return ResizePictureUtil.resize(image, largestDimension);

	} catch (IOException e) {
	    log.error(e.getStackTrace());
	    return null;
	}
    }

    /**
     * Reads the original image, creates a resized copy of it and returns its input stream. largestDimension is the
     * largest dimension of the resized image, the other dimension is scaled accordingly.
     *
     * @param image
     *            original image
     * @param largestDimension
     *            the largest dimension of the resized image, the other dimension is scaled accordingly
     * @return
     * @throws IOException
     * @throws UploadImageGalleryFileException
     */
    public static InputStream resize(BufferedImage image, int largestDimension) throws IOException {
	try {
	    //resize to 150 pixels max
	    BufferedImage outImage = Scalr.resize(image, Method.QUALITY, largestDimension);
	    
	    //set jpeg compression quality explicitly
	    ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpeg").next();
	    ImageWriteParam jpgWriteParam = imageWriter.getDefaultWriteParam();
	    jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	    jpgWriteParam.setCompressionQuality(.95f);

	    // buffer all data in a circular buffer of infinite sizes
	    CircularByteBuffer outputBuffer = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
	    ImageOutputStream ios = ImageIO.createImageOutputStream(outputBuffer.getOutputStream());
	    imageWriter.setOutput(ios);
	    IIOImage outputImage = new IIOImage(outImage, null, null);
	    imageWriter.write(null, outputImage, jpgWriteParam);
	    imageWriter.dispose();
	    outputBuffer.getOutputStream().close();

	    return outputBuffer.getInputStream();

	} catch (IOException e) {
	    log.error(e.getStackTrace());
	    return null;
	}
    }

}
