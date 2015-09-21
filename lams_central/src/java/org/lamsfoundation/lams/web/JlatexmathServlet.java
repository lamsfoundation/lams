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
package org.lamsfoundation.lams.web;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JLabel;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.util.WebUtil;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

/**
 * @author Andrey Balan
 * 
 * @web:servlet name="jlatexmath"
 * @web:servlet-mapping url-pattern="/servlet/jlatexmath"
 */
public class JlatexmathServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	String formulaParam = request.getParameter("formula");
	if (StringUtils.isBlank(formulaParam)) {
	    return;
	}
	
	Integer fontSize = WebUtil.readIntParam(request, "fontSize", true);
	if (fontSize == null) {
	    fontSize = 20;
	}
	
	TeXFormula formula = new TeXFormula(formulaParam);
	TeXIcon icon = formula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY)
		.setSize(fontSize)
		.setWidth(TeXConstants.UNIT_PIXEL, 256f, TeXConstants.ALIGN_CENTER)
		.setIsMaxWidth(true).setInterLineSpacing(TeXConstants.UNIT_PIXEL, 20f)
		.build();
	
//	TeXFormula fomule = new TeXFormula(formula);
//	TeXIcon ti = fomule.createTeXIcon(TeXConstants.STYLE_DISPLAY, 40);
	BufferedImage b = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
//	Color transparent = new Color(0, true);
//	((Graphics2D)b.getGraphics()).setBackground(transparent);
//	b.getGraphics().clearRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
	
	((Graphics2D)b.getGraphics()).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5F));
	icon.paintIcon(new JLabel(), b.getGraphics(), 0, 0);
	
	response.setContentType("image/png");
	OutputStream out = response.getOutputStream();
	ImageIO.write(b, "png", out);
	out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IOException {
	doGet(request, response);
    }
}
