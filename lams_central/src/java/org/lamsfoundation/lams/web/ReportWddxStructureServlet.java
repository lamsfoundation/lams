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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.web.servlet.AbstractStoreWDDXPacketServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Write out the contents of WDDX packet as converted to Java objects by deserialisation.
 * The report files are written to the same directory as the crash dumps.
 *
 * @author Fiona Malikoff
 *
 * @web:servlet name="dumpWDDX"
 * @web:servlet-mapping url-pattern="/servlet/dumpWDDX"
 * 
 */
public class ReportWddxStructureServlet extends AbstractStoreWDDXPacketServlet {

    private static final long serialVersionUID = 1993150655543872349L;
    private static Logger log = Logger.getLogger(ReportWddxStructureServlet.class);

    @Override
    protected String process(String wddxPacket, HttpServletRequest request) throws Exception {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = ss != null ? (UserDTO) ss.getAttribute(AttributeNames.USER) : null;
	if (user == null) {
	    String error = "ReportWddxStructureServlet: Attempt to dump file by someone not logged in.";
	    log.error(error);
	    throw new Exception(error);
	}

	String id = "DUMP" + user.getLogin();
	String filename = null;
	try {
	    // attempt to get the special Flash flag which groups packets together - not
	    // all the crash dump data can come in one packet as Flash can't cope with large
	    // packets.
	    Object packet = WDDXProcessor.deserialize(wddxPacket);
	    byte[] dump = convertJavaToDump(packet);
	    filename = FileUtil.createDumpFile(dump, id, "txt");
	} catch (Exception e) {
	    String error = "ReportWddxStructureServlet: Unable to serialise packet.";
	    log.error(error, e);
	    throw e;
	}

	return "<HTML><BODY><P>Dump file is " + filename + "</P></BODY></HTML>";
    }

    private byte[] convertJavaToDump(Object packet) throws IOException {
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	output(packet, out, "");
	return out.toByteArray();
    }

    private void output(Object obj, OutputStream out, String indent) throws IOException {
	if (obj != null) {
	    if (obj instanceof Map) {
		outputMap((Map) obj, out, indent);
	    } else if (obj instanceof Vector) {
		outputVector((Vector) obj, out, indent);
	    } else {
		out.write(obj.toString().getBytes());
	    }
	}
    }

    private void outputMap(Map map, OutputStream out, String indent) throws IOException {
	byte[] indentBytes = indent.getBytes();
	out.write(indentBytes);
	out.write("Map{\n".getBytes());
	String newIndent = indent + "    ";
	byte[] newIndentBytes = newIndent.getBytes();
	Iterator iter = map.entrySet().iterator();
	while (iter.hasNext()) {
	    out.write(newIndentBytes);
	    Map.Entry entry = (Map.Entry) iter.next();
	    String key = (String) entry.getKey();
	    out.write("[".getBytes());
	    out.write(key.getBytes());
	    out.write("=".getBytes());
	    output(entry.getValue(), out, newIndent);
	    out.write("]\n".getBytes());
	}
	out.write(indentBytes);
	out.write("}\n".getBytes());
    }

    private void outputVector(Vector vector, OutputStream out, String indent) throws IOException {
	byte[] indentBytes = indent.getBytes();
	out.write("Vector{\n".getBytes());
	String newIndent = indent + "    ";
	Iterator iter = vector.iterator();
	while (iter.hasNext()) {
	    output(iter.next(), out, newIndent);
	    out.write("\n".getBytes());
	}
	out.write(indentBytes);
	out.write("}".getBytes());
    }

    @Override
    protected String getMessageKey(String packet, HttpServletRequest request) {
	return ("ReportWddxStructureServlet");
    }
}