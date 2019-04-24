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

package org.lamsfoundation.lams.web;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JLabel;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.WebUtil;
import org.scilab.forge.jlatexmath.DefaultTeXFont;
import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import org.scilab.forge.jlatexmath.cyrillic.CyrillicRegistration;
import org.scilab.forge.jlatexmath.greek.GreekRegistration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 * Servlet produces LaTex images based on the provided formulas. It's used by CKEditor's JLaTeXMath plugin.
 * 
 * @author Andrey Balan
 */
public class JlatexmathServlet extends HttpServlet {

    private static final long serialVersionUID = -3544723631369530849L;
    private static Logger log = Logger.getLogger(JlatexmathServlet.class);

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

	TeXFormula formula = null;
	try {
	    formula = new TeXFormula(formulaParam);
	} catch (ParseException e) {
	    // don't throw a full-blown exception whenever an user makes a mistake in the formula
	    if (JlatexmathServlet.log.isTraceEnabled()) {
		JlatexmathServlet.log.trace(e.getMessage());
	    }
	    return;
	}
	TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, fontSize);
	
	//produce SVG image and stream it out to ServletOutputStream 
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);
        SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);

        final boolean FONT_AS_SHAPES = true;
        SVGGraphics2D g2 = new SVGGraphics2D(ctx, FONT_AS_SHAPES);

        DefaultTeXFont.registerAlphabet(new CyrillicRegistration());
        DefaultTeXFont.registerAlphabet(new GreekRegistration());

        g2.setSVGCanvasSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));

        JLabel jl = new JLabel();
        jl.setForeground(new Color(0, 0, 0));
        icon.paintIcon(jl, g2, 0, 0);

        ServletOutputStream out = response.getOutputStream();
        response.setContentType("image/svg+xml");
        Writer writer = new OutputStreamWriter(out, "UTF-8");
        final boolean USE_CSS = true;
        g2.stream(writer, USE_CSS);
        out.flush();
        IOUtils.closeQuietly(out);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request, response);
    }
}
