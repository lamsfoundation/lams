/*
 * Copyright (C) 2005 Stefan Strigler <steve@zeank.in-berlin.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.lamsfoundation.lams.tool.chat.JabberHTTPBind;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * this class reflects a session within http binding definition
 * 
 * @author Stefan Strigler <steve@zeank.in-berlin.de>
 */
public class Session {

	/**
	 * Default HTTP Content-Type header.
	 */
	public static final String DEFAULT_CONTENT = "text/xml; charset=utf-8";

	/**
	 * Longest allowable inactivity period (in seconds).
	 */
	public static final int MAX_INACTIVITY = 60;

	/**
	 * Maximum number of simultaneous requests allowed.
	 */
	public static final int MAX_REQUESTS = 2;

	/*
	 * ####### CONSTANTS #######
	 */

	/**
	 * Default value for longest time (in seconds) that the connection manager
	 * is allowed to wait before responding to any request during the session.
	 * This enables the client to prevent its TCP connection from expiring due
	 * to inactivity, as well as to limit the delay before it discovers any
	 * network failure.
	 */
	public static final int MAX_WAIT = 300;

	/**
	 * Shortest allowable polling interval (in seconds).
	 */
	public static final int MIN_POLLING = 2;

	/**
	 * Time to sleep on reading in MSEC.
	 */
	private static final int READ_TIMEOUT = 1;

	protected static final String SESS_START = "starting";
	protected static final String SESS_ACTIVE = "active";
	protected static final String SESS_TERM = "term";
	
	/*
	 * ####### static #######
	 */

	private static Hashtable sessions = new Hashtable();

	private static TransformerFactory tff = TransformerFactory.newInstance();

	private static String createSessionID(int len) {
		String charlist = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";

		Random rand = new Random();

		String str = new String();
		for (int i = 0; i < len; i++)
			str += charlist.charAt(rand.nextInt(charlist.length()));
		return str;
	}

	public static Session getSession(String sid) {
		return (Session) sessions.get(sid);
	}

	public static Enumeration getSessions() {
		return sessions.elements();
	}

	public static void stopSessions() {
		for (Enumeration e = sessions.elements(); e.hasMoreElements();)
			((Session) e.nextElement()).terminate();
	}

	/* ***
	 * END static
	 */
	
	private String authid; // stream id given by remote jabber server
	public boolean authidSent = false;
	private String content = DEFAULT_CONTENT;
	private DocumentBuilder db;
	private int hold = MAX_REQUESTS - 1;
	private String inQueue = "";
	private InputStreamReader isr;
	private String key;
	private long lastActive;
	private long lastPoll = 0;
	private int lastSentRid = 0;
	private OutputStreamWriter osw;
	private TreeMap outQueue;
	private TreeMap responses;
	private String status = SESS_START;
	private String sid;
	public Socket sock;
	private String to;
	private int wait = MAX_WAIT;
	private String xmllang = "en";

	/**
	 * Create a new session and connect to jabber server host denoted by
	 * <code>to</code>.
	 * 
	 * @param to
	 *            hostname of jabber server to connect to.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public Session(String to) throws UnknownHostException, IOException {

		this.to = to;
		this.setLastActive();

		try {
			this.db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (Exception e) {
		}

		// connect to jabber server
		try {
			this.sock = new Socket(to, 5222);

			if (JHBServlet.DEBUG && this.sock.isConnected())
				System.err.println("Succesfully connected to " + to);

			// instantiate <stream>
			this.osw = new OutputStreamWriter(this.sock.getOutputStream(),
					"UTF-8");

			this.osw
					.write("<stream:stream to='"
							+ this.to
							+ "' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'>");
			this.osw.flush();

			// create session id
			while (sessions.get(this.sid = createSessionID(24)) != null)
				;

			if (JHBServlet.DEBUG)
				System.err.println("creating session with id " + this.sid);

			// register session
			sessions.put(this.sid, this);

			// create list of responses
			responses = new TreeMap();

			// create list of nodes to send
			outQueue = new TreeMap();

			this.isr = new InputStreamReader(this.sock.getInputStream(),
					"UTF-8");

			String stream = this.readFromSocket(0);

			Pattern p = Pattern
					.compile(".*\\<stream\\:stream.*id=['\"]([^'\"]+)['\"].*\\>.*");
			Matcher m = p.matcher(stream);
			if (m.matches())
				this.authid = m.group(1);
			else JHBServlet.dbg("failed to get authid",2);
			
			this.setStatus(SESS_ACTIVE);
		} catch (UnknownHostException uhe) {
			throw uhe;
		} catch (IOException ioe) {
			throw ioe;
		}
	}

	/**
	 * Adds new response to list of known responses. Truncates list to allowed
	 * size.
	 * 
	 * @param resp
	 *            the response to add
	 * @return this session object
	 */
	public synchronized Response addResponse(Response r) {
		while (this.responses.size() > 0
				&& this.responses.size() >= Session.MAX_REQUESTS)
			this.responses.remove(this.responses.firstKey());
		return (Response) this.responses.put(new Integer(r.getRID()), r);
	}

	/**
	 * checks InputStream from server for incoming packets blocks until request
	 * timeout or packets available
	 * 
	 * @return nl - NodeList of incoming Nodes
	 */
	public NodeList checkInQ(int rid) {
		NodeList nl = null;
		
		inQueue += this.readFromSocket(rid);

//		((Response) this.responses.get(new Integer(rid))).setStatus(Response.STATUS_LEAVING);
		
		if (this.authid == null) {
			Pattern p = Pattern
					.compile(".*\\<stream\\:stream.*id=['\"]([^'\"]+)['\"].*\\>(.*)");
			Matcher m = p.matcher(inQueue);
			if (m.matches()) {
				this.authid = m.group(1);
				inQueue = m.group(2);
			} else JHBServlet.dbg("failed to get authid",2);
		}

		// try to parse it
		if (!inQueue.equals("")) {
			try {
				/*
				 * wrap inQueue with element so that multiple nodes can be
				 * parsed
				 */
				Document doc = db.parse(new InputSource(new StringReader(
						"<doc xmlns='jabber:client'>" + inQueue + "</doc>")));
				nl = doc.getFirstChild().getChildNodes();
				inQueue = ""; // reset!
			} catch (Exception e3) { /* skip this */
			}
		}
		this.setLastActive();
		return nl;
	}

	/**
	 * Checks whether given request ID is valid within context of this session.
	 * 
	 * @param rid
	 *            Request ID to be checked
	 * @return true if rid is valid
	 */
	public synchronized boolean checkValidRID(int rid) {
		try {
			if (rid <= ((Integer) this.responses.lastKey()).intValue() 
					+ MAX_REQUESTS &&
					rid >= ((Integer) this.responses.firstKey()).intValue())
				return true;
			else {
				JHBServlet.dbg("invalid request id: " + rid
							+ " (last: "
							+ ((Integer) this.responses.lastKey()).intValue()
							+ ")",1);
				return false;
			}
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public String getAuthid() {
		return this.authid;
	}

	public String getContent() {
		return this.content;
	}

	public int getHold() {
		return this.hold;
	}

	/**
	 * @return Returns the key.
	 */
	public synchronized String getKey() {
		return key;
	}

	/**
	 * @return Returns the lastActive.
	 */
	public synchronized long getLastActive() {
		return lastActive;
	}
	
	/**
	 * @return Returns the lastPoll.
	 */
	public synchronized long getLastPoll() {
		return lastPoll;
	}

	/**
	 * lookup response for given request id
	 * 
	 * @param rid
	 *            Request id associated with response
	 * @return the response if found, null otherwise
	 */
	public synchronized Response getResponse(int rid) {
		return (Response) this.responses.get(new Integer(rid));
	}

	public String getSID() {
		return this.sid;
	}

	/*
	 * ######## getters #########
	 */

	public String getTo() {
		return this.to;
	}

	public int getWait() {
		return this.wait;
	}

	public String getXMLLang() {
		return this.xmllang;
	}

	public synchronized int numPendingRequests() {
		int num_pending = 0;
		Iterator it = this.responses.values().iterator();
		while (it.hasNext()) {
			Response r = (Response) it.next();
			if (!r.getStatus().equals(Response.STATUS_DONE))
				num_pending++;
		}
		return num_pending;
	}
	private int lastDoneRID;
	public synchronized int getLastDoneRID() {
		return this.lastDoneRID;
		
//		Iterator it = this.responses.values().iterator();
//		int last_done = 0;
//		while (it.hasNext()) { 
			/* TODO better to traverse in reverted order to be able to stop 
			 * on first found
			 */
//			Response r = (Response) it.next();
//			if (r.getStatus().equals(Response.STATUS_DONE))
//				last_done = r.getRID();
//		}		
//		return last_done;
	}

	/**
	 * reads from socket
	 * 
	 * @return string that was read
	 */
	private String readFromSocket(int rid) {
		String retval = "";
		char buf[] = new char[16];
		int c = 0;

		Response r = this.getResponse(rid);
		
//		synchronized (this.sock) {
			while (!this.sock.isClosed() && !this.isStatus(SESS_TERM)) {
				this.setLastActive();
				try {
					if (this.isr.ready()) {
						while (this.isr.ready()	&& 
								(c = this.isr.read(buf, 0, buf.length)) >= 0) 
							retval += new String(buf, 0, c);
						break; // got sth. to send
					} else {
						if ((this.hold == 0 &&
									System.currentTimeMillis() - this.getLastActive() > 200) || 
								/* makes polling clients feel a little bit more responsive */
								(this.hold > 0 && (
									(r != null && System.currentTimeMillis() - r.getCDate() >= this.getWait()*1000  ) ||
									this.numPendingRequests() > this.getHold() ||
									!retval.equals("")))) {
							JHBServlet.dbg("readFromSocket done for "+rid,3);
							break; // time exeeded
						}

						try {
							Thread.sleep(READ_TIMEOUT); // wait for incoming
							// packets
						} catch (InterruptedException ie) {
							System.err.println(ie.toString());
						}
					}
				} catch (IOException e) {
					System.err.println("Can't read from socket");
					this.terminate();
				}
			}
//		}
		/*
		 * TODO if (this.sock.isClosed()) indicate an error
		 * (remote-connection-failed)
		 */

		return retval;
	}

	/**
	 * sends all nodes in list to remote jabber server make sure that nodes get
	 * sent in requested order
	 * 
	 * @param nl
	 *            list of nodes to send
	 * @return the session itself
	 */
//	public synchronized Session sendNodes(int rid, NodeList aNL) {
	public Session sendNodes(NodeList nl) {
		// init lastSentRid
//		if (lastSentRid == 0)
//			lastSentRid = rid - 1;

		// add to queue
//		outQueue.put(new Integer(rid), aNL);

		// build a string
		String out = "";
		StreamResult strResult = new StreamResult();

//		while (!outQueue.isEmpty()
//				&& ((Integer) outQueue.firstKey()).intValue() == lastSentRid + 1) {
//			lastSentRid = ((Integer) outQueue.firstKey()).intValue();
//			NodeList nl = (NodeList) outQueue.remove(outQueue.firstKey());
//			if (nl == null)
//				continue;
			try {
				Transformer tf = tff.newTransformer();
				tf.setOutputProperty("omit-xml-declaration", "yes");
				// loop list
				for (int i = 0; i < nl.getLength(); i++) {
					strResult.setWriter(new StringWriter());
					tf.transform(new DOMSource(nl.item(i)), strResult);
					String tStr = strResult.getWriter().toString();
					out += tStr;
				}
			} catch (Exception e) {
				System.err.println("XML.toString(Document): " + e);
			}
//		}
		try {
			this.osw.write(out);
			this.osw.flush();
		} catch (IOException ioe) {
			System.err.println(this.sid + " failed to write to stream");
		}

		return this;
	}

	public Session setContent(String content) {
		this.content = content;
		return this;
	}

	/*
	 * ######## setters #########
	 */

	public Session setHold(int hold) {
		if (hold < MAX_REQUESTS && hold >= 0)
			this.hold = hold;
		return this;
	}

	/**
	 * @param key
	 *            The key to set.
	 */
	public synchronized void setKey(String key) {
		this.key = key;
	}

	/**
	 * set lastActive to current timestamp.
	 */
	public synchronized void setLastActive() {
		this.lastActive = System.currentTimeMillis();
	}

	public synchronized void setLastDoneRID(int rid) {
		this.lastDoneRID = rid;
	}
	
	/**
	 * set lastPoll to current timestamp.
	 */
	public synchronized void setLastPoll() {
		this.lastPoll = System.currentTimeMillis();
	}

	public int setWait(int wait) {
		if (wait < 0)
			wait = 0;
		if (wait > MAX_WAIT)
			wait = MAX_WAIT;
		this.wait = wait;
		return wait;
	}

	public Session setXMLLang(String xmllang) {
		this.xmllang = xmllang;
		return this;
	}

	public synchronized void setStatus(String status) {
		this.status = status;
	}
	
	public synchronized boolean isStatus(String status) {
		return (this.status == status);
	}
	
	/**
	 * kill this session
	 *  
	 */
	public void terminate() {
		JHBServlet.dbg("terminating session " + this.getSID(),2);
		this.setStatus(SESS_TERM);
		synchronized (this.sock) {
			if (!this.sock.isClosed()) {
				try {
					this.osw.write("</stream:stream>");
					this.osw.flush();			
					this.sock.close();
				} catch (IOException ie) {	}
			}
			this.sock.notifyAll();
		}
		sessions.remove(this.sid);
	}
}
