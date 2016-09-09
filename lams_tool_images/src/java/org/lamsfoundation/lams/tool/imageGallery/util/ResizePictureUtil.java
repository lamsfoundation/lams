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

package org.lamsfoundation.lams.tool.imageGallery.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.imageGallery.service.UploadImageGalleryFileException;
import org.lamsfoundation.lams.util.CircularByteBuffer;

/**
 * @author Andrey Balan
 */
public class ResizePictureUtil {

    private static Logger log = Logger.getLogger(ResizePictureUtil.class);

    /**
     * Reads the original image, creates a thumbnail and returns its input stream. largestDimension is the largest
     * dimension of the thumbnail, the other dimension is scaled accordingly. Utilises weighted stepping method to
     * gradually reduce the image size for better results, i.e. larger steps to start with then smaller steps to finish
     * with. Note: always writes a JPEG because GIF is protected or something - so always make your outFilename end in
     * 'jpg' PNG's with transparency are given white backgrounds
     *
     * @param is
     *            original image's input stream
     * @param largestDimension
     *            the largest dimension of the thumbnail, the other dimension is scaled accordingly
     * @return
     * @throws IOException
     * @throws UploadImageGalleryFileException
     */
    public static InputStream resizePicture(BufferedImage inImage, int largestDimension) throws IOException {
	InputStream thumbnailInputStream = null;

	double scale;
	int sizeDifference;
	int originalImageLargestDim;

	// find biggest dimension
	if (inImage.getWidth(null) > inImage.getHeight(null)) {
	    scale = (double) largestDimension / (double) inImage.getWidth(null);
	    sizeDifference = inImage.getWidth(null) - largestDimension;
	    originalImageLargestDim = inImage.getWidth(null);
	} else {
	    scale = (double) largestDimension / (double) inImage.getHeight(null);
	    sizeDifference = inImage.getHeight(null) - largestDimension;
	    originalImageLargestDim = inImage.getHeight(null);
	}
	// create an image buffer to draw to
	BufferedImage outImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB); // arbitrary init so
	// code compiles
	Graphics2D g2d;
	AffineTransform tx;
	if (scale < 1.0d) // only scale if desired size is smaller than original
	{
	    int numSteps = ((sizeDifference / 200) != 0) ? (sizeDifference / 200) : 1;
	    int stepSize = sizeDifference / numSteps;
	    int stepWeight = stepSize / 2;
	    int heavierStepSize = stepSize + stepWeight;
	    int lighterStepSize = stepSize - stepWeight;
	    int currentStepSize, centerStep;
	    double scaledW = inImage.getWidth(null);
	    double scaledH = inImage.getHeight(null);
	    if (numSteps % 2 == 1) {
		centerStep = (int) Math.ceil(numSteps / 2d); // find the center step
	    } else {
		centerStep = -1; // set it to -1 so it's ignored later
	    }
	    Integer intermediateSize = originalImageLargestDim, previousIntermediateSize = originalImageLargestDim;
	    Integer calculatedDim;
	    for (Integer i = 0; i < numSteps; i++) {
		if (i + 1 != centerStep) // if this isn't the center step
		{
		    if (i == numSteps - 1) // if this is the last step
		    {
			// fix the stepsize to account for decimal place errors previously
			currentStepSize = previousIntermediateSize - largestDimension;
		    } else {
			if (numSteps - i > numSteps / 2) {
			    currentStepSize = heavierStepSize;
			} else {
			    currentStepSize = lighterStepSize;
			}
		    }
		} else // center step, use natural step size
		{
		    currentStepSize = stepSize;
		}
		intermediateSize = previousIntermediateSize - currentStepSize;
		scale = (double) intermediateSize / (double) previousIntermediateSize;
		scaledW = (int) scaledW * scale;
		scaledH = (int) scaledH * scale;
		outImage = new BufferedImage((int) scaledW, (int) scaledH, BufferedImage.TYPE_INT_RGB);
		g2d = outImage.createGraphics();
		g2d.setBackground(Color.WHITE);
		g2d.clearRect(0, 0, outImage.getWidth(), outImage.getHeight());
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		tx = new AffineTransform();
		tx.scale(scale, scale);
		g2d.drawImage(inImage, tx, null);
		g2d.dispose();
		inImage = outImage;
		previousIntermediateSize = intermediateSize;
	    }
	} else {
	    // just copy the original
	    outImage = new BufferedImage(inImage.getWidth(null), inImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
	    g2d = outImage.createGraphics();
	    g2d.setBackground(Color.WHITE);
	    g2d.clearRect(0, 0, outImage.getWidth(), outImage.getHeight());
	    tx = new AffineTransform();
	    tx.setToIdentity(); // use identity matrix so image is copied exactly
	    g2d.drawImage(inImage, tx, null);
	    g2d.dispose();
	}

	// buffer all data in a circular buffer of infinite size
	CircularByteBuffer cbb = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
	ImageIO.write(outImage, "JPG", cbb.getOutputStream());
	cbb.getOutputStream().close();

	thumbnailInputStream = cbb.getInputStream();

	return thumbnailInputStream;
    }

}
