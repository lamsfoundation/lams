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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.util;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

/**
 * @author jliew
 *
 */
public class PortraitUtils {

	private static Logger log = Logger.getLogger(PortraitUtils.class);
	
	/* 
	 * Resize picture to specified width and height in pixels.  Maintains aspect ratio.
	 * Output image in the format specified by formatName.
	 */
	public static ByteArrayInputStream resizePicture(InputStream is, int width, int height, String formatName) {
		
		ByteArrayOutputStream os = null;
		try {
			// create buffer for source image
			BufferedImage bsrc = ImageIO.read(is);
			log.debug("old width: "+bsrc.getWidth()+", old height: "+bsrc.getHeight());
			
			// maintain aspect ratio
			double widthScale = (double)width/bsrc.getWidth();
			double heightScale = (double)height/bsrc.getHeight();
			double minScale = Math.min(widthScale,heightScale);
			double newWidth = minScale*(double)bsrc.getWidth();
			double newHeight = minScale*(double)bsrc.getHeight();
			log.debug("scaling picture by "+minScale+"... new width: "+newWidth+", new height: "+newHeight);
			
			// create buffer for resized image
			BufferedImage bdest = new BufferedImage((int)newWidth, (int)newHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bdest.createGraphics();
			AffineTransform at = AffineTransform.getScaleInstance(minScale, minScale);
			g.drawRenderedImage(bsrc,at);
		
			// write new picture into a buffer usable by content repository
			os = new ByteArrayOutputStream();
			ImageIO.write(bdest,formatName,os);
			// alternative may be to use a File on disk as the buffer
		} catch (IOException e) {
			log.error(e.getStackTrace());
			return null;
		}
		return new ByteArrayInputStream(os.toByteArray());
	}
	
}
