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
/* $$Id$$ */
package org.lamsfoundation.lams.web.development;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.web.servlet.AbstractStoreWDDXPacketServlet;


/**
 * Deserialise a packet and display the contents. Used to test whether WDDX can process 
 * a packet correctly. 
 * 
 * @author Fiona Malikoff
 * 
 * @web:servlet name="deserialise"
 * @web:servlet-mapping url-pattern="/deserialise"

 */
public class DisplayDeserialisedPacketServlet extends AbstractStoreWDDXPacketServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -478481724893217965L;

	private static Logger log = Logger.getLogger(DisplayDeserialisedPacketServlet.class);

	private static final String MESSAGE_KEY = "deserialise";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException
	{

		PrintWriter writer = null;
		String packet = null;
		try {
			writer = response.getWriter();
	
			packet = getBody(request);
	
			if(containsNulls(packet)){
				outputPage(writer,"<P>WDDXPacket contains null. WDDXPacket was</P><P>"+packet);
			}		

			Hashtable table = (Hashtable)WDDXProcessor.deserialize(packet);
			outputPage(writer,"<P>Deserialised Object <P>"+processMap(table)+"<P>Packet was"+packet+"<P>");
			
		} catch ( Exception e ) {
			log.warn("DisplayDeserialisedPacket: Unable to deserialize packet. ",e);
			outputPage(writer,"<P>Unable to deserialize packet. See log for exception. WDDXPacket was</P><P>"+packet);
		}
	}

	private String processMap(Map map) {
		Set<Map.Entry> entries = map.entrySet();
		String retValue = "<TABLE>";
		for ( Map.Entry entry : entries ) {
			retValue=retValue+"<TR><TD>"+entry.getKey()+"</TD><TD>"+processEntry(entry.getValue())+"</TD></TR>";
		}
		retValue+="</TABLE>";
		return retValue;
	}

	private String processList(List list) {
		String retValue = "<UL>";
		for ( Object entry : list ) {
			retValue=retValue+"<LI>"+processEntry(entry)+"</LI>";
		}
		retValue+="</UL>";
		return retValue;
	}

	private String processEntry(Object value) {
		if (value instanceof Map) {
			return processMap((Map)value);
		} else if ( value instanceof List ) {
			return processList((List)value);
		}else {
			return value.toString();
		}
	}
		
	protected String process(String wddxPacket, HttpServletRequest request) 
		throws Exception
	{
		return null;
	}
	
	private void outputPage(PrintWriter writer, String body) {
		writer.println("<HTML><HEAD>Deserialisation Test<HEAD><BODY>");
		writer.write(body);
		writer.println("</BODY></HTML>");
	}
	
	protected String getMessageKey(String designDetails, HttpServletRequest request) {
		return MESSAGE_KEY;
	}

}