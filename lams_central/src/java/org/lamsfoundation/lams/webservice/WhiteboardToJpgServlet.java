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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @author jliew
 *
 * @web:servlet name="BogoPogoServlet"
 * @web:servlet-mapping url-pattern="/BogoPogo"
 */
public class WhiteboardToJpgServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(WhiteboardToJpgServlet.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {

	PrintWriter out = null;
	String ext = "jpg";
	String dir = System.getProperty("java.io.tmpdir");

	try {
	    out = response.getWriter();

	    String d = request.getParameter("data");
	    String w = request.getParameter("width");
	    String h = request.getParameter("height");

	    log.debug("got parameters data=" + d + ", width=" + w + ", height=" + h);
	    out.write("got parameters data=" + d + ", width=" + w + ", height=" + h);

	    String[] darray = d.split(",");
	    int[] pixels = new int[darray.length];
	    for (int i = 0; i < darray.length; i++) {
		pixels[i] = new Integer(darray[i]).intValue();
	    }
	    int width = new Integer(w).intValue();
	    int height = new Integer(h).intValue();

	    BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    buffer.setRGB(0, 0, width, height, pixels, 0, width);

	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    try {
		// put buffer data into byte output stream
		ImageIO.write(buffer, ext, os);
		byte[] imagebytes = os.toByteArray();

		// write bytes into file
		FileOutputStream fileos = new FileOutputStream(new File(dir + "/new." + ext));
		fileos.write(imagebytes);
		fileos.close();
	    } catch (IOException e) {
		log.error("file output stream threw exception, " + e);
	    } catch (Exception e) {
		log.error(e);
		out.write("\n" + e);
	    }
	} catch (IOException e) {
	    log.error("print writer threw exception, " + e);
	} catch (Exception e) {
	    log.error(e);
	    out.write("\n" + e);
	} finally {
	    if (out != null) {
		out.close();
	    }
	}

    }

}
