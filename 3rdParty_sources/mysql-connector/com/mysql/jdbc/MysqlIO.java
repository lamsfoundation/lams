/*
      Copyright (C) 2002-2007 MySQL AB

      This program is free software; you can redistribute it and/or modify
      it under the terms of version 2 of the GNU General Public License as
      published by the Free Software Foundation.

      There are special exceptions to the terms and conditions of the GPL
      as it is applied to this software. View the full text of the
      exception in file EXCEPTIONS-CONNECTOR-J in the directory of this
      software distribution.

      This program is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU General Public License for more details.

      You should have received a copy of the GNU General Public License
      along with this program; if not, write to the Free Software
      Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA



 */
package com.mysql.jdbc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.zip.Deflater;

import com.mysql.jdbc.profiler.ProfileEventSink;
import com.mysql.jdbc.profiler.ProfilerEvent;
import com.mysql.jdbc.util.ReadAheadInputStream;
import com.mysql.jdbc.util.ResultSetUtil;


/**
 * This class is used by Connection for communicating with the MySQL server.
 *
 * @author Mark Matthews
 *
 *
 * @see java.sql.Connection
 */
class MysqlIO {
    protected static final int NULL_LENGTH = ~0;
    protected static final int COMP_HEADER_LENGTH = 3;
    protected static final int MIN_COMPRESS_LEN = 50;
    protected static final int HEADER_LENGTH = 4;
    protected static final int AUTH_411_OVERHEAD = 33;
    private static int maxBufferSize = 65535;
    private static final int CLIENT_COMPRESS = 32; /* Can use compression
    protcol */
    protected static final int CLIENT_CONNECT_WITH_DB = 8;
    private static final int CLIENT_FOUND_ROWS = 2;
    private static final int CLIENT_LOCAL_FILES = 128; /* Can use LOAD DATA
    LOCAL */

    /* Found instead of
       affected rows */
    private static final int CLIENT_LONG_FLAG = 4; /* Get all column flags */
    private static final int CLIENT_LONG_PASSWORD = 1; /* new more secure
    passwords */
    private static final int CLIENT_PROTOCOL_41 = 512; // for > 4.1.1
    private static final int CLIENT_INTERACTIVE = 1024;
    protected static final int CLIENT_SSL = 2048;
    private static final int CLIENT_TRANSACTIONS = 8192; // Client knows about transactions
    protected static final int CLIENT_RESERVED = 16384; // for 4.1.0 only
    protected static final int CLIENT_SECURE_CONNECTION = 32768;
    private static final int CLIENT_MULTI_QUERIES = 65536; // Enable/disable multiquery support
    private static final int CLIENT_MULTI_RESULTS = 131072; // Enable/disable multi-results
    private static final int SERVER_STATUS_IN_TRANS = 1;
    private static final int SERVER_STATUS_AUTOCOMMIT = 2; // Server in auto_commit mode
    private static final int SERVER_MORE_RESULTS_EXISTS = 8; // Multi query - next query exists
    private static final int SERVER_QUERY_NO_GOOD_INDEX_USED = 16;
    private static final int SERVER_QUERY_NO_INDEX_USED = 32;
	private static final int  SERVER_STATUS_CURSOR_EXISTS = 64;
    private static final String FALSE_SCRAMBLE = "xxxxxxxx"; //$NON-NLS-1$
    protected static final int MAX_QUERY_SIZE_TO_LOG = 1024; // truncate logging of queries at 1K
    protected static final int MAX_QUERY_SIZE_TO_EXPLAIN = 1024 * 1024; // don't explain queries above 1MB
    protected static final int INITIAL_PACKET_SIZE = 1024;
    /**
     * We store the platform 'encoding' here, only used to avoid munging
     * filenames for LOAD DATA LOCAL INFILE...
     */
    private static String jvmPlatformCharset = null;
    
    /**
     * Are we using packed or unpacked binary result set rows?
     */
    private boolean binaryResultsAreUnpacked = true;

    /**
     * We need to have a 'marker' for all-zero datetimes so that ResultSet
     * can decide what to do based on connection setting
     */
    protected final static String ZERO_DATE_VALUE_MARKER = "0000-00-00";
    protected final static String ZERO_DATETIME_VALUE_MARKER = "0000-00-00 00:00:00";

    static {
        OutputStreamWriter outWriter = null;

        //
        // Use the I/O system to get the encoding (if possible), to avoid
        // security restrictions on System.getProperty("file.encoding") in
        // applets (why is that restricted?)
        //
        try {
            outWriter = new OutputStreamWriter(new ByteArrayOutputStream());
            jvmPlatformCharset = outWriter.getEncoding();
        } finally {
            try {
                if (outWriter != null) {
                    outWriter.close();
                }
            } catch (IOException ioEx) {
                // ignore
            }
        }
    }

    /** Max number of bytes to dump when tracing the protocol */
    private final static int MAX_PACKET_DUMP_LENGTH = 1024;
    private boolean packetSequenceReset = false;
    protected int serverCharsetIndex;

    //
    // Use this when reading in rows to avoid thousands of new()
    // calls, because the byte arrays just get copied out of the
    // packet anyway
    //
    private Buffer reusablePacket = null;
    private Buffer sendPacket = null;
    private Buffer sharedSendPacket = null;

    /** Data to the server */
    protected BufferedOutputStream mysqlOutput = null;
    protected com.mysql.jdbc.Connection connection;
    private Deflater deflater = null;
    protected InputStream mysqlInput = null;
    private LinkedList packetDebugRingBuffer = null;
    private RowData streamingData = null;

    /** The connection to the server */
    protected Socket mysqlConnection = null;
    private SocketFactory socketFactory = null;

    //
    // Packet used for 'LOAD DATA LOCAL INFILE'
    //
    // We use a SoftReference, so that we don't penalize intermittent
    // use of this feature
    //
    private SoftReference loadFileBufRef;

    //
    // Used to send large packets to the server versions 4+
    // We use a SoftReference, so that we don't penalize intermittent
    // use of this feature
    //
    private SoftReference splitBufRef;
    protected String host = null;
    protected String seed;
    private String serverVersion = null;
    private String socketFactoryClassName = null;
    private byte[] packetHeaderBuf = new byte[4];
    private boolean colDecimalNeedsBump = false; // do we need to increment the colDecimal flag?
    private boolean hadWarnings = false;
    private boolean has41NewNewProt = false;

    /** Does the server support long column info? */
    private boolean hasLongColumnInfo = false;
    private boolean isInteractiveClient = false;
    private boolean logSlowQueries = false;

    /**
     * Does the character set of this connection match the character set of the
     * platform
     */
    private boolean platformDbCharsetMatches = true; // changed once we've connected.
    private boolean profileSql = false;
    private boolean queryBadIndexUsed = false;
    private boolean queryNoIndexUsed = false;

    /** Should we use 4.1 protocol extensions? */
    private boolean use41Extensions = false;
    private boolean useCompression = false;
    private boolean useNewLargePackets = false;
    private boolean useNewUpdateCounts = false; // should we use the new larger update counts?
    private byte packetSequence = 0;
    private byte readPacketSequence = -1;
    private boolean checkPacketSequence = false;
    byte protocolVersion = 0;
    private int maxAllowedPacket = 1024 * 1024;
    protected int maxThreeBytes = 255 * 255 * 255;
    protected int port = 3306;
    protected int serverCapabilities;
    private int serverMajorVersion = 0;
    private int serverMinorVersion = 0;
    private int serverStatus = 0;
    private int serverSubMinorVersion = 0;
    private int warningCount = 0;
    protected long clientParam = 0;
    protected long lastPacketSentTimeMs = 0;
    private boolean traceProtocol = false;
    private boolean enablePacketDebug = false;
    private Calendar sessionCalendar;
	private boolean useConnectWithDb;
	private boolean needToGrabQueryFromPacket;
	private boolean autoGenerateTestcaseScript;
	private long threadId;
	private boolean useNanosForElapsedTime;
	private long slowQueryThreshold;
	private String queryTimingUnits;
	
    /**
     * Constructor:  Connect to the MySQL server and setup a stream connection.
     *
     * @param host the hostname to connect to
     * @param port the port number that the server is listening on
     * @param props the Properties from DriverManager.getConnection()
     * @param socketFactoryClassName the socket factory to use
     * @param conn the Connection that is creating us
     * @param socketTimeout the timeout to set for the socket (0 means no
     *        timeout)
     *
     * @throws IOException if an IOException occurs during connect.
     * @throws SQLException if a database access error occurs.
     */
    public MysqlIO(String host, int port, Properties props,
			String socketFactoryClassName, com.mysql.jdbc.Connection conn,
			int socketTimeout) throws IOException, SQLException {
		this.connection = conn;

		if (this.connection.getEnablePacketDebug()) {
			this.packetDebugRingBuffer = new LinkedList();
		}

		this.logSlowQueries = this.connection.getLogSlowQueries();

		this.reusablePacket = new Buffer(INITIAL_PACKET_SIZE);
		this.sendPacket = new Buffer(INITIAL_PACKET_SIZE);

		this.port = port;
		this.host = host;

		this.socketFactoryClassName = socketFactoryClassName;
		this.socketFactory = createSocketFactory();

		this.mysqlConnection = this.socketFactory.connect(this.host, this.port,
				props);

		if (socketTimeout != 0) {
			try {
				this.mysqlConnection.setSoTimeout(socketTimeout);
			} catch (Exception ex) {
				/* Ignore if the platform does not support it */
				;
			}
		}

		this.mysqlConnection = this.socketFactory.beforeHandshake();

		if (this.connection.getUseReadAheadInput()) {
			this.mysqlInput = new ReadAheadInputStream(this.mysqlConnection
					.getInputStream(), 16384, this.connection
					.getTraceProtocol(), this.connection.getLog());
		} else if (this.connection.useUnbufferedInput()) {
			this.mysqlInput = this.mysqlConnection.getInputStream();
		} else {
			this.mysqlInput = new BufferedInputStream(this.mysqlConnection
					.getInputStream(), 16384);
		}

		this.mysqlOutput = new BufferedOutputStream(this.mysqlConnection
				.getOutputStream(), 16384);

		this.isInteractiveClient = this.connection.getInteractiveClient();
		this.profileSql = this.connection.getProfileSql();
		this.sessionCalendar = Calendar.getInstance();
		this.autoGenerateTestcaseScript = this.connection
				.getAutoGenerateTestcaseScript();

		this.needToGrabQueryFromPacket = (this.profileSql
				|| this.logSlowQueries || this.autoGenerateTestcaseScript);

		if (this.connection.getUseNanosForElapsedTime()
				&& Util.nanoTimeAvailable()) {
			this.useNanosForElapsedTime = true;

			this.queryTimingUnits = Messages.getString("Nanoseconds");
		} else {
			this.queryTimingUnits = Messages.getString("Milliseconds");
		}

		if (this.connection.getLogSlowQueries()) {
			calculateSlowQueryThreshold();
		}
	}

    /**
	 * Does the server send back extra column info?
	 * 
	 * @return true if so
	 */
    public boolean hasLongColumnInfo() {
        return this.hasLongColumnInfo;
    }

    protected boolean isDataAvailable() throws SQLException {
        try {
            return this.mysqlInput.available() > 0;
        } catch (IOException ioEx) {
            throw new CommunicationsException(this.connection,
                this.lastPacketSentTimeMs, ioEx);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the lastPacketSentTimeMs.
     */
    protected long getLastPacketSentTimeMs() {
        return this.lastPacketSentTimeMs;
    }

    /**
     * Build a result set. Delegates to buildResultSetWithRows() to build a
     * JDBC-version-specific ResultSet, given rows as byte data, and field
     * information.
     *
     * @param callingStatement DOCUMENT ME!
     * @param columnCount the number of columns in the result set
     * @param maxRows the maximum number of rows to read (-1 means all rows)
     * @param resultSetType (TYPE_FORWARD_ONLY, TYPE_SCROLL_????)
     * @param resultSetConcurrency the type of result set (CONCUR_UPDATABLE or
     *        READ_ONLY)
     * @param streamResults should the result set be read all at once, or
     *        streamed?
     * @param catalog the database name in use when the result set was created
     * @param isBinaryEncoded is this result set in native encoding?
     * @param unpackFieldInfo should we read MYSQL_FIELD info (if available)?
     *
     * @return a result set
     *
     * @throws SQLException if a database access error occurs
     */
    protected ResultSet getResultSet(Statement callingStatement,
        long columnCount, int maxRows, int resultSetType,
        int resultSetConcurrency, boolean streamResults, String catalog,
        boolean isBinaryEncoded, boolean unpackFieldInfo, Field[] metadataFromCache)
        throws SQLException {
        Buffer packet; // The packet from the server
        Field[] fields = null;

        // Read in the column information
        
        if (unpackFieldInfo) {
            fields = new Field[(int) columnCount];
            
            for (int i = 0; i < columnCount; i++) {
            	Buffer fieldPacket = null;
            
                fieldPacket = readPacket();
                fields[i] = unpackField(fieldPacket, false);
            }
        } else {
        	for (int i = 0; i < columnCount; i++) {
        		skipPacket();
        	}
        	
        	//this.reusablePacket.clear();
        }

        packet = reuseAndReadPacket(this.reusablePacket);

        readServerStatusForResultSets(packet);
		
		//
		// Handle cursor-based fetch first
		//
		
		if (this.connection.versionMeetsMinimum(5, 0, 2)
				&& this.connection.getUseCursorFetch()
				&& isBinaryEncoded
				&& callingStatement != null
				&& callingStatement.getFetchSize() != 0
				&& callingStatement.getResultSetType() == ResultSet.TYPE_FORWARD_ONLY) {
			ServerPreparedStatement prepStmt = (com.mysql.jdbc.ServerPreparedStatement) callingStatement;
	
			Field[] fieldMetadata = ((com.mysql.jdbc.ResultSetMetaData) prepStmt.getMetaData()).fields;

			boolean usingCursor = true;
			
			//
			// Server versions 5.0.5 or newer will only open
			// a cursor and set this flag if they can, otherwise
			// they punt and go back to mysql_store_results() behavior
			//
			
			if (this.connection.versionMeetsMinimum(5, 0, 5)) {
				usingCursor = (this.serverStatus & 
						SERVER_STATUS_CURSOR_EXISTS) != 0;
			}
			
			if (usingCursor) {
				RowData rows = new CursorRowProvider(
					this,
					prepStmt,
					fields);

				ResultSet rs = buildResultSetWithRows(
					callingStatement,
					catalog,
					fields,
					rows, resultSetType, resultSetConcurrency, isBinaryEncoded);
				
				if (usingCursor) {
		        	rs.setFetchSize(callingStatement.getFetchSize());
		        }
				
				return rs;
			}
		}
		
        RowData rowData = null;
       
        if (!streamResults) {
            rowData = readSingleRowSet(columnCount, maxRows,
                    resultSetConcurrency, isBinaryEncoded, unpackFieldInfo ? fields : metadataFromCache);
        } else {
            rowData = new RowDataDynamic(this, (int) columnCount, unpackFieldInfo ? fields : metadataFromCache,
                    isBinaryEncoded);
            this.streamingData = rowData;
        }

        ResultSet rs = buildResultSetWithRows(callingStatement, catalog, fields,
            rowData, resultSetType, resultSetConcurrency, isBinaryEncoded);
 
        
        
        return rs;
    }

    /**
     * Forcibly closes the underlying socket to MySQL.
     */
    protected final void forceClose() {
        try {
            if (this.mysqlInput != null) {
                this.mysqlInput.close();
            }
        } catch (IOException ioEx) {
            // we can't do anything constructive about this
            // Let the JVM clean it up later
            this.mysqlInput = null;
        }

        try {
            if (this.mysqlOutput != null) {
                this.mysqlOutput.close();
            }
        } catch (IOException ioEx) {
            // we can't do anything constructive about this
            // Let the JVM clean it up later
            this.mysqlOutput = null;
        }

        try {
            if (this.mysqlConnection != null) {
                this.mysqlConnection.close();
            }
        } catch (IOException ioEx) {
            // we can't do anything constructive about this
            // Let the JVM clean it up later
            this.mysqlConnection = null;
        }
    }

    /**
     * Reads and discards a single MySQL packet from the input stream.
     * 
     * @throws SQLException if the network fails while skipping the 
     * packet.
     */
    protected final void skipPacket() throws SQLException {
		try {

			int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf,
					0, 4);

			if (lengthRead < 4) {
				forceClose();
				throw new IOException(Messages.getString("MysqlIO.1")); //$NON-NLS-1$
			}

			int packetLength = (this.packetHeaderBuf[0] & 0xff)
					+ ((this.packetHeaderBuf[1] & 0xff) << 8)
					+ ((this.packetHeaderBuf[2] & 0xff) << 16);

			if (this.traceProtocol) {
				StringBuffer traceMessageBuf = new StringBuffer();

				traceMessageBuf.append(Messages.getString("MysqlIO.2")); //$NON-NLS-1$
				traceMessageBuf.append(packetLength);
				traceMessageBuf.append(Messages.getString("MysqlIO.3")); //$NON-NLS-1$
				traceMessageBuf.append(StringUtils.dumpAsHex(
						this.packetHeaderBuf, 4));

				this.connection.getLog().logTrace(traceMessageBuf.toString());
			}

			byte multiPacketSeq = this.packetHeaderBuf[3];

			if (!this.packetSequenceReset) {
				if (this.enablePacketDebug && this.checkPacketSequence) {
					checkPacketSequencing(multiPacketSeq);
				}
			} else {
				this.packetSequenceReset = false;
			}

			this.readPacketSequence = multiPacketSeq;

			skipFully(this.mysqlInput, packetLength);
		} catch (IOException ioEx) {
			throw new CommunicationsException(this.connection,
					this.lastPacketSentTimeMs, ioEx);
		} catch (OutOfMemoryError oom) {
			try {
				this.connection.realClose(false, false, true, oom);
			} finally {
				throw oom;
			}
		}
	}
    
    /**
	 * Read one packet from the MySQL server
	 * 
	 * @return the packet from the server.
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 * @throws CommunicationsException
	 *             DOCUMENT ME!
	 */
    protected final Buffer readPacket() throws SQLException {
        try {
            
            int lengthRead = readFully(this.mysqlInput,
                    this.packetHeaderBuf, 0, 4);

            if (lengthRead < 4) {
                forceClose();
                throw new IOException(Messages.getString("MysqlIO.1")); //$NON-NLS-1$
            }

            int packetLength = (this.packetHeaderBuf[0] & 0xff) +
                ((this.packetHeaderBuf[1] & 0xff) << 8) +
                ((this.packetHeaderBuf[2] & 0xff) << 16);

            if (this.traceProtocol) {
                StringBuffer traceMessageBuf = new StringBuffer();

                traceMessageBuf.append(Messages.getString("MysqlIO.2")); //$NON-NLS-1$
                traceMessageBuf.append(packetLength);
                traceMessageBuf.append(Messages.getString("MysqlIO.3")); //$NON-NLS-1$
                traceMessageBuf.append(StringUtils.dumpAsHex(
                        this.packetHeaderBuf, 4));

                this.connection.getLog().logTrace(traceMessageBuf.toString());
            }

            byte multiPacketSeq = this.packetHeaderBuf[3];

            if (!this.packetSequenceReset) {
                if (this.enablePacketDebug && this.checkPacketSequence) {
                    checkPacketSequencing(multiPacketSeq);
                }
            } else {
                this.packetSequenceReset = false;
            }

            this.readPacketSequence = multiPacketSeq;

            // Read data
            byte[] buffer = new byte[packetLength + 1];
            int numBytesRead = readFully(this.mysqlInput, buffer, 0,
                    packetLength);

            if (numBytesRead != packetLength) {
                throw new IOException("Short read, expected " +
                    packetLength + " bytes, only read " + numBytesRead);
            }

            buffer[packetLength] = 0;

            Buffer packet = new Buffer(buffer);
            packet.setBufLength(packetLength + 1);

            if (this.traceProtocol) {
                StringBuffer traceMessageBuf = new StringBuffer();

                traceMessageBuf.append(Messages.getString("MysqlIO.4")); //$NON-NLS-1$
                traceMessageBuf.append(getPacketDumpToLog(packet,
                        packetLength));

                this.connection.getLog().logTrace(traceMessageBuf.toString());
            }

            if (this.enablePacketDebug) {
                enqueuePacketForDebugging(false, false, 0,
                    this.packetHeaderBuf, packet);
            }

            return packet;
        } catch (IOException ioEx) {
            throw new CommunicationsException(this.connection,
                this.lastPacketSentTimeMs, ioEx);
        } catch (OutOfMemoryError oom) {
        	try {
    			this.connection.realClose(false, false, true, oom);
    		} finally {
    			throw oom;
    		}
        }
    }

    /**
     * Unpacks the Field information from the given packet. Understands pre 4.1
     * and post 4.1 server version field packet structures.
     *
     * @param packet the packet containing the field information
     * @param extractDefaultValues should default values be extracted?
     *
     * @return the unpacked field
     *
     * @throws SQLException DOCUMENT ME!
     */
    protected final Field unpackField(Buffer packet,
        boolean extractDefaultValues) throws SQLException {
        if (this.use41Extensions) {
            // we only store the position of the string and
            // materialize only if needed...
            if (this.has41NewNewProt) {
                // Not used yet, 5.0?
                int catalogNameStart = packet.getPosition() + 1;
                int catalogNameLength = packet.fastSkipLenString();
                catalogNameStart = adjustStartForFieldLength(catalogNameStart, catalogNameLength);
            }

            int databaseNameStart = packet.getPosition() + 1;
            int databaseNameLength = packet.fastSkipLenString();
            databaseNameStart = adjustStartForFieldLength(databaseNameStart, databaseNameLength);

            int tableNameStart = packet.getPosition() + 1;
            int tableNameLength = packet.fastSkipLenString();
            tableNameStart = adjustStartForFieldLength(tableNameStart, tableNameLength);
            
            // orgTableName is never used so skip
            int originalTableNameStart = packet.getPosition() + 1;
            int originalTableNameLength = packet.fastSkipLenString();
            originalTableNameStart = adjustStartForFieldLength(originalTableNameStart, originalTableNameLength);

            // we only store the position again...
            int nameStart = packet.getPosition() + 1;
            int nameLength = packet.fastSkipLenString();
            
            nameStart = adjustStartForFieldLength(nameStart, nameLength);

            // orgColName is not required so skip...
            int originalColumnNameStart = packet.getPosition() + 1;
            int originalColumnNameLength = packet.fastSkipLenString();
            originalColumnNameStart = adjustStartForFieldLength(originalColumnNameStart, originalColumnNameLength);

            packet.readByte();

            short charSetNumber = (short) packet.readInt();

            long colLength = 0;

            if (this.has41NewNewProt) {
                colLength = packet.readLong();
            } else {
                colLength = packet.readLongInt();
            }

            int colType = packet.readByte() & 0xff;

            short colFlag = 0;

            if (this.hasLongColumnInfo) {
                colFlag = (short) packet.readInt();
            } else {
                colFlag = (short) (packet.readByte() & 0xff);
            }

            int colDecimals = packet.readByte() & 0xff;

            int defaultValueStart = -1;
            int defaultValueLength = -1;

            if (extractDefaultValues) {
                defaultValueStart = packet.getPosition() + 1;
                defaultValueLength = packet.fastSkipLenString();
            }

            Field field = new Field(this.connection, packet.getByteBuffer(),
                    databaseNameStart, databaseNameLength, tableNameStart,
                    tableNameLength, originalTableNameStart,
                    originalTableNameLength, nameStart, nameLength,
                    originalColumnNameStart, originalColumnNameLength,
                    colLength, colType, colFlag, colDecimals,
                    defaultValueStart, defaultValueLength, charSetNumber);

            return field;
        }

        int tableNameStart = packet.getPosition() + 1;
        int tableNameLength = packet.fastSkipLenString();
        tableNameStart = adjustStartForFieldLength(tableNameStart, tableNameLength);
        
        int nameStart = packet.getPosition() + 1;
        int nameLength = packet.fastSkipLenString();
        nameStart = adjustStartForFieldLength(nameStart, nameLength);
        
        int colLength = packet.readnBytes();
        int colType = packet.readnBytes();
        packet.readByte(); // We know it's currently 2

        short colFlag = 0;

        if (this.hasLongColumnInfo) {
            colFlag = (short) (packet.readInt());
        } else {
            colFlag = (short) (packet.readByte() & 0xff);
        }

        int colDecimals = (packet.readByte() & 0xff);

        if (this.colDecimalNeedsBump) {
            colDecimals++;
        }

        Field field = new Field(this.connection, packet.getByteBuffer(),
                nameStart, nameLength, tableNameStart, tableNameLength,
                colLength, colType, colFlag, colDecimals);

        return field;
    }

    private int adjustStartForFieldLength(int nameStart, int nameLength) {
    	if (nameLength < 251) {
    		return nameStart;
    	}
    	
		if (nameLength >= 251 && nameLength < 65536) {
			return nameStart + 2;
		}
		
		if (nameLength >= 65536 && nameLength < 16777216) {
			return nameStart + 3;
		}
		
		return nameStart + 8;
	}

    protected boolean isSetNeededForAutoCommitMode(boolean autoCommitFlag) {
        if (this.use41Extensions && this.connection.getElideSetAutoCommits()) {
            boolean autoCommitModeOnServer = ((this.serverStatus &
                SERVER_STATUS_AUTOCOMMIT) != 0);

            if (!autoCommitFlag && versionMeetsMinimum(5, 0, 0)) {
                // Just to be safe, check if a transaction is in progress on the server....
                // if so, then we must be in autoCommit == false
                // therefore return the opposite of transaction status
                boolean inTransactionOnServer = ((this.serverStatus &
                    SERVER_STATUS_IN_TRANS) != 0);

                return !inTransactionOnServer;
            }

            return autoCommitModeOnServer != autoCommitFlag;
        }

        return true;
    }
    
    protected boolean inTransactionOnServer() {
    	return (this.serverStatus & SERVER_STATUS_IN_TRANS) != 0;
    }

    /**
     * Re-authenticates as the given user and password
     *
     * @param userName DOCUMENT ME!
     * @param password DOCUMENT ME!
     * @param database DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    protected void changeUser(String userName, String password, String database)
        throws SQLException {
        this.packetSequence = -1;

        int passwordLength = 16;
        int userLength = (userName != null) ? userName.length() : 0;
        int databaseLength = (database != null) ? database.length() : 0;
        
        int packLength = ((userLength + passwordLength + databaseLength) * 2) + 7 + HEADER_LENGTH + AUTH_411_OVERHEAD;

        if ((this.serverCapabilities & CLIENT_SECURE_CONNECTION) != 0) {
            Buffer changeUserPacket = new Buffer(packLength + 1);
            changeUserPacket.writeByte((byte) MysqlDefs.COM_CHANGE_USER);

            if (versionMeetsMinimum(4, 1, 1)) {
                secureAuth411(changeUserPacket, packLength, userName, password,
                    database, false);
            } else {
                secureAuth(changeUserPacket, packLength, userName, password,
                    database, false);
            }
        } else {
            // Passwords can be 16 chars long
            Buffer packet = new Buffer(packLength);
            packet.writeByte((byte) MysqlDefs.COM_CHANGE_USER);

            // User/Password data
            packet.writeString(userName);

            if (this.protocolVersion > 9) {
                packet.writeString(Util.newCrypt(password, this.seed));
            } else {
                packet.writeString(Util.oldCrypt(password, this.seed));
            }

			boolean localUseConnectWithDb = this.useConnectWithDb && 
				(database != null && database.length() > 0);
			
            if (localUseConnectWithDb) {
                packet.writeString(database);
            }

            send(packet, packet.getPosition());
            checkErrorPacket();
			
			if (!localUseConnectWithDb) {
				changeDatabaseTo(database);
			}
        }
    }

    /**
     * Checks for errors in the reply packet, and if none, returns the reply
     * packet, ready for reading
     *
     * @return a packet ready for reading.
     *
     * @throws SQLException is the packet is an error packet
     */
    protected Buffer checkErrorPacket() throws SQLException {
        return checkErrorPacket(-1);
    }

    /**
     * Determines if the database charset is the same as the platform charset
     */
    protected void checkForCharsetMismatch() {
        if (this.connection.getUseUnicode() &&
                (this.connection.getEncoding() != null)) {
            String encodingToCheck = jvmPlatformCharset;

            if (encodingToCheck == null) {
                encodingToCheck = System.getProperty("file.encoding"); //$NON-NLS-1$
            }

            if (encodingToCheck == null) {
                this.platformDbCharsetMatches = false;
            } else {
                this.platformDbCharsetMatches = encodingToCheck.equals(this.connection.getEncoding());
            }
        }
    }

    protected void clearInputStream() throws SQLException {
    
        try {
            int len = this.mysqlInput.available();

            while (len > 0) {
                this.mysqlInput.skip(len);
                len = this.mysqlInput.available();
            }
        } catch (IOException ioEx) {
            throw new CommunicationsException(this.connection,
                this.lastPacketSentTimeMs, ioEx);
        }
    }

    protected void resetReadPacketSequence() {
        this.readPacketSequence = 0;
    }

    protected void dumpPacketRingBuffer() throws SQLException {
        if ((this.packetDebugRingBuffer != null) &&
                this.connection.getEnablePacketDebug()) {
            StringBuffer dumpBuffer = new StringBuffer();

            dumpBuffer.append("Last " + this.packetDebugRingBuffer.size() +
                " packets received from server, from oldest->newest:\n");
            dumpBuffer.append("\n");

            for (Iterator ringBufIter = this.packetDebugRingBuffer.iterator();
                    ringBufIter.hasNext();) {
                dumpBuffer.append((StringBuffer) ringBufIter.next());
                dumpBuffer.append("\n");
            }

            this.connection.getLog().logTrace(dumpBuffer.toString());
        }
    }

    /**
     * Runs an 'EXPLAIN' on the given query and dumps the results to  the log
     *
     * @param querySQL DOCUMENT ME!
     * @param truncatedQuery DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    protected void explainSlowQuery(byte[] querySQL, String truncatedQuery)
        throws SQLException {
        if (StringUtils.startsWithIgnoreCaseAndWs(truncatedQuery, "SELECT")) { //$NON-NLS-1$

            PreparedStatement stmt = null;
            java.sql.ResultSet rs = null;

            try {
                stmt = this.connection.clientPrepareStatement("EXPLAIN ?"); //$NON-NLS-1$
                stmt.setBytesNoEscapeNoQuotes(1, querySQL);
                rs = stmt.executeQuery();

                StringBuffer explainResults = new StringBuffer(Messages.getString(
                            "MysqlIO.8") + truncatedQuery //$NON-NLS-1$
                         +Messages.getString("MysqlIO.9")); //$NON-NLS-1$

                ResultSetUtil.appendResultSetSlashGStyle(explainResults, rs);

                this.connection.getLog().logWarn(explainResults.toString());
            } catch (SQLException sqlEx) {
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
            }
        } else {
        }
    }

    static int getMaxBuf() {
        return maxBufferSize;
    }

    /**
     * Get the major version of the MySQL server we are talking to.
     *
     * @return DOCUMENT ME!
     */
    final int getServerMajorVersion() {
        return this.serverMajorVersion;
    }

    /**
     * Get the minor version of the MySQL server we are talking to.
     *
     * @return DOCUMENT ME!
     */
    final int getServerMinorVersion() {
        return this.serverMinorVersion;
    }

    /**
     * Get the sub-minor version of the MySQL server we are talking to.
     *
     * @return DOCUMENT ME!
     */
    final int getServerSubMinorVersion() {
        return this.serverSubMinorVersion;
    }

    /**
     * Get the version string of the server we are talking to
     *
     * @return DOCUMENT ME!
     */
    String getServerVersion() {
        return this.serverVersion;
    }

    /**
     * Initialize communications with the MySQL server. Handles logging on, and
     * handling initial connection errors.
     *
     * @param user DOCUMENT ME!
     * @param password DOCUMENT ME!
     * @param database DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     * @throws CommunicationsException DOCUMENT ME!
     */
    void doHandshake(String user, String password, String database)
        throws SQLException {
        // Read the first packet
        this.checkPacketSequence = false;
        this.readPacketSequence = 0;

        Buffer buf = readPacket();

        // Get the protocol version
        this.protocolVersion = buf.readByte();

        if (this.protocolVersion == -1) {
            try {
                this.mysqlConnection.close();
            } catch (Exception e) {
                ; // ignore
            }

            int errno = 2000;

            errno = buf.readInt();

            String serverErrorMessage = buf.readString();

            StringBuffer errorBuf = new StringBuffer(Messages.getString(
                        "MysqlIO.10")); //$NON-NLS-1$
            errorBuf.append(serverErrorMessage);
            errorBuf.append("\""); //$NON-NLS-1$

            String xOpen = SQLError.mysqlToSqlState(errno,
                    this.connection.getUseSqlStateCodes());

            throw SQLError.createSQLException(SQLError.get(xOpen) + ", " //$NON-NLS-1$
                 +errorBuf.toString(), xOpen, errno);
        }

        this.serverVersion = buf.readString();

        // Parse the server version into major/minor/subminor
        int point = this.serverVersion.indexOf("."); //$NON-NLS-1$

        if (point != -1) {
            try {
                int n = Integer.parseInt(this.serverVersion.substring(0, point));
                this.serverMajorVersion = n;
            } catch (NumberFormatException NFE1) {
                ;
            }

            String remaining = this.serverVersion.substring(point + 1,
                    this.serverVersion.length());
            point = remaining.indexOf("."); //$NON-NLS-1$

            if (point != -1) {
                try {
                    int n = Integer.parseInt(remaining.substring(0, point));
                    this.serverMinorVersion = n;
                } catch (NumberFormatException nfe) {
                    ;
                }

                remaining = remaining.substring(point + 1, remaining.length());

                int pos = 0;

                while (pos < remaining.length()) {
                    if ((remaining.charAt(pos) < '0') ||
                            (remaining.charAt(pos) > '9')) {
                        break;
                    }

                    pos++;
                }

                try {
                    int n = Integer.parseInt(remaining.substring(0, pos));
                    this.serverSubMinorVersion = n;
                } catch (NumberFormatException nfe) {
                    ;
                }
            }
        }

        if (versionMeetsMinimum(4, 0, 8)) {
            this.maxThreeBytes = (256 * 256 * 256) - 1;
            this.useNewLargePackets = true;
        } else {
            this.maxThreeBytes = 255 * 255 * 255;
            this.useNewLargePackets = false;
        }

        this.colDecimalNeedsBump = versionMeetsMinimum(3, 23, 0);
        this.colDecimalNeedsBump = !versionMeetsMinimum(3, 23, 15); // guess? Not noted in changelog
        this.useNewUpdateCounts = versionMeetsMinimum(3, 22, 5);

        threadId = buf.readLong(); 
        this.seed = buf.readString();

        this.serverCapabilities = 0;

        if (buf.getPosition() < buf.getBufLength()) {
            this.serverCapabilities = buf.readInt();
        }

        if (versionMeetsMinimum(4, 1, 1)) {
            int position = buf.getPosition();

            /* New protocol with 16 bytes to describe server characteristics */
            this.serverCharsetIndex = buf.readByte() & 0xff;
            this.serverStatus = buf.readInt();
            buf.setPosition(position + 16);

            String seedPart2 = buf.readString();
            StringBuffer newSeed = new StringBuffer(20);
            newSeed.append(this.seed);
            newSeed.append(seedPart2);
            this.seed = newSeed.toString();
        }

        if (((this.serverCapabilities & CLIENT_COMPRESS) != 0) &&
                this.connection.getUseCompression()) {
            this.clientParam |= CLIENT_COMPRESS;
        }

		this.useConnectWithDb = (database != null) && 
			(database.length() > 0) &&
			!this.connection.getCreateDatabaseIfNotExist();
		
        if (this.useConnectWithDb) {
            this.clientParam |= CLIENT_CONNECT_WITH_DB;
        }

        if (((this.serverCapabilities & CLIENT_SSL) == 0) &&
                this.connection.getUseSSL()) {
            if (this.connection.getRequireSSL()) {
                this.connection.close();
                forceClose();
                throw SQLError.createSQLException(Messages.getString("MysqlIO.15"), //$NON-NLS-1$
                    SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE);
            }

            this.connection.setUseSSL(false);
        }

        if ((this.serverCapabilities & CLIENT_LONG_FLAG) != 0) {
            // We understand other column flags, as well
            this.clientParam |= CLIENT_LONG_FLAG;
            this.hasLongColumnInfo = true;
        }

        // return FOUND rows
        this.clientParam |= CLIENT_FOUND_ROWS;

        if (this.connection.getAllowLoadLocalInfile()) {
            this.clientParam |= CLIENT_LOCAL_FILES;
        }

        if (this.isInteractiveClient) {
            this.clientParam |= CLIENT_INTERACTIVE;
        }

        // Authenticate
        if (this.protocolVersion > 9) {
            this.clientParam |= CLIENT_LONG_PASSWORD; // for long passwords
        } else {
            this.clientParam &= ~CLIENT_LONG_PASSWORD;
        }

        //
        // 4.1 has some differences in the protocol
        //
        if (versionMeetsMinimum(4, 1, 0)) {
            if (versionMeetsMinimum(4, 1, 1)) {
                this.clientParam |= CLIENT_PROTOCOL_41;
                this.has41NewNewProt = true;

                // Need this to get server status values
                this.clientParam |= CLIENT_TRANSACTIONS;

                // We always allow multiple result sets
                this.clientParam |= CLIENT_MULTI_RESULTS;

                // We allow the user to configure whether
                // or not they want to support multiple queries
                // (by default, this is disabled).
                if (this.connection.getAllowMultiQueries()) {
                    this.clientParam |= CLIENT_MULTI_QUERIES;
                }
            } else {
                this.clientParam |= CLIENT_RESERVED;
                this.has41NewNewProt = false;
            }

            this.use41Extensions = true;
        }

        int passwordLength = 16;
        int userLength = (user != null) ? user.length() : 0;
        int databaseLength = (database != null) ? database.length() : 0;
        
        int packLength = ((userLength + passwordLength + databaseLength) * 2) + 7 + HEADER_LENGTH + AUTH_411_OVERHEAD;
        
        Buffer packet = null;

        if (!this.connection.getUseSSL()) {
            if ((this.serverCapabilities & CLIENT_SECURE_CONNECTION) != 0) {
                this.clientParam |= CLIENT_SECURE_CONNECTION;

                if (versionMeetsMinimum(4, 1, 1)) {
                    secureAuth411(null, packLength, user, password, database,
                        true);
                } else {
                    secureAuth(null, packLength, user, password, database, true);
                }
            } else {
                // Passwords can be 16 chars long
                packet = new Buffer(packLength);

                if ((this.clientParam & CLIENT_RESERVED) != 0) {
                    if (versionMeetsMinimum(4, 1, 1)) {
                        packet.writeLong(this.clientParam);
                        packet.writeLong(this.maxThreeBytes);

                        // charset, JDBC will connect as 'latin1',
                        // and use 'SET NAMES' to change to the desired
                        // charset after the connection is established.
                        packet.writeByte((byte) 8);

                        // Set of bytes reserved for future use.
                        packet.writeBytesNoNull(new byte[23]);
                    } else {
                        packet.writeLong(this.clientParam);
                        packet.writeLong(this.maxThreeBytes);
                    }
                } else {
                    packet.writeInt((int) this.clientParam);
                    packet.writeLongInt(this.maxThreeBytes);
                }

                // User/Password data
                packet.writeString(user, "Cp1252", this.connection);

                if (this.protocolVersion > 9) {
                    packet.writeString(Util.newCrypt(password, this.seed), "Cp1252", this.connection);
                } else {
                    packet.writeString(Util.oldCrypt(password, this.seed), "Cp1252", this.connection);
                }

                if (this.useConnectWithDb) {
                    packet.writeString(database, "Cp1252", this.connection);
                }

                send(packet, packet.getPosition());
            }
        } else {
            negotiateSSLConnection(user, password, database, packLength);
        }

        // Check for errors, not for 4.1.1 or newer,
        // as the new auth protocol doesn't work that way
        // (see secureAuth411() for more details...)
        if (!versionMeetsMinimum(4, 1, 1)) {
            checkErrorPacket();
        }

        //
        // Can't enable compression until after handshake
        //
        if (((this.serverCapabilities & CLIENT_COMPRESS) != 0) &&
                this.connection.getUseCompression()) {
            // The following matches with ZLIB's
            // compress()
            this.deflater = new Deflater();
            this.useCompression = true;
            this.mysqlInput = new CompressedInputStream(this.connection,
                    this.mysqlInput);
        }

        if (!this.useConnectWithDb) {
            changeDatabaseTo(database);
        }
    }

	private void changeDatabaseTo(String database) throws SQLException, CommunicationsException {
		if (database == null || database.length() == 0) {
			return;
		}
		
		try {
		    sendCommand(MysqlDefs.INIT_DB, database, null, false, null);
		} catch (Exception ex) {
			if (this.connection.getCreateDatabaseIfNotExist()) {
				sendCommand(MysqlDefs.QUERY, "CREATE DATABASE IF NOT EXISTS " +
					database,
					null, false, null);
				sendCommand(MysqlDefs.INIT_DB, database, null, false, null);
			} else {
				throw new CommunicationsException(this.connection,
						this.lastPacketSentTimeMs, ex);
			}
		}
	}

    /**
    * Retrieve one row from the MySQL server. Note: this method is not
    * thread-safe, but it is only called from methods that are guarded by
    * synchronizing on this object.
    *
    * @param fields DOCUMENT ME!
    * @param columnCount DOCUMENT ME!
    * @param isBinaryEncoded DOCUMENT ME!
    * @param resultSetConcurrency DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
    final Object[] nextRow(Field[] fields, int columnCount,
        boolean isBinaryEncoded, int resultSetConcurrency)
        throws SQLException {
        // Get the next incoming packet, re-using the packet because
        // all the data we need gets copied out of it.
        Buffer rowPacket = checkErrorPacket();

        if (!isBinaryEncoded) {
            //
            // Didn't read an error, so re-position to beginning
            // of packet in order to read result set data
            //
            rowPacket.setPosition(rowPacket.getPosition() - 1);

            if (!rowPacket.isLastDataPacket()) {
                byte[][] rowData = new byte[columnCount][];

                int offset = 0;

                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = rowPacket.readLenByteArray(offset);
                }

                return rowData;
            }

            readServerStatusForResultSets(rowPacket);

            return null;
        }

        // 
        // Handle binary-encoded data for server-side   
        // PreparedStatements...
        //
        if (!rowPacket.isLastDataPacket()) {
            return unpackBinaryResultSetRow(fields, rowPacket,
                resultSetConcurrency);
        }

        rowPacket.setPosition(rowPacket.getPosition() - 1);
        readServerStatusForResultSets(rowPacket);

        return null;
    }

    /**
     * Log-off of the MySQL server and close the socket.
     *
     * @throws SQLException DOCUMENT ME!
     */
    final void quit() throws SQLException {
        Buffer packet = new Buffer(6);
        this.packetSequence = -1;
        packet.writeByte((byte) MysqlDefs.QUIT);
        send(packet, packet.getPosition());
        forceClose();
    }

    /**
     * Returns the packet used for sending data (used by PreparedStatement)
     * Guarded by external synchronization on a mutex.
     *
     * @return A packet to send data with
     */
    Buffer getSharedSendPacket() {
        if (this.sharedSendPacket == null) {
        	this.sharedSendPacket = new Buffer(INITIAL_PACKET_SIZE);
        }

        return this.sharedSendPacket;
    }

    void closeStreamer(RowData streamer) throws SQLException {
        if (this.streamingData == null) {
            throw SQLError.createSQLException(Messages.getString("MysqlIO.17") //$NON-NLS-1$
                 +streamer + Messages.getString("MysqlIO.18")); //$NON-NLS-1$
        }

        if (streamer != this.streamingData) {
            throw SQLError.createSQLException(Messages.getString("MysqlIO.19") //$NON-NLS-1$
                 +streamer + Messages.getString("MysqlIO.20") //$NON-NLS-1$
                 +Messages.getString("MysqlIO.21") //$NON-NLS-1$
                 +Messages.getString("MysqlIO.22")); //$NON-NLS-1$
        }

        this.streamingData = null;
    }

    ResultSet readAllResults(Statement callingStatement, int maxRows,
        int resultSetType, int resultSetConcurrency, boolean streamResults,
        String catalog, Buffer resultPacket, boolean isBinaryEncoded,
        long preSentColumnCount, boolean unpackFieldInfo, Field[] metadataFromCache)
        throws SQLException {
        resultPacket.setPosition(resultPacket.getPosition() - 1);

        ResultSet topLevelResultSet = readResultsForQueryOrUpdate(callingStatement,
                maxRows, resultSetType, resultSetConcurrency, streamResults,
                catalog, resultPacket, isBinaryEncoded, preSentColumnCount,
                unpackFieldInfo, metadataFromCache);

        ResultSet currentResultSet = topLevelResultSet;

        boolean checkForMoreResults = ((this.clientParam &
            CLIENT_MULTI_RESULTS) != 0);

        boolean serverHasMoreResults = (this.serverStatus &
            SERVER_MORE_RESULTS_EXISTS) != 0;

        //
        // TODO: We need to support streaming of multiple result sets
        //
        if (serverHasMoreResults && streamResults) {
            clearInputStream();

            throw SQLError.createSQLException(Messages.getString("MysqlIO.23"), //$NON-NLS-1$
                SQLError.SQL_STATE_DRIVER_NOT_CAPABLE);
        }

        boolean moreRowSetsExist = checkForMoreResults & serverHasMoreResults;

        while (moreRowSetsExist) {
        	Buffer fieldPacket = checkErrorPacket();
            fieldPacket.setPosition(0);
            
            ResultSet newResultSet = readResultsForQueryOrUpdate(callingStatement,
                    maxRows, resultSetType, resultSetConcurrency,
                    streamResults, catalog, fieldPacket, isBinaryEncoded,
                    preSentColumnCount, unpackFieldInfo, metadataFromCache);

            currentResultSet.setNextResultSet(newResultSet);

            currentResultSet = newResultSet;

            moreRowSetsExist = (this.serverStatus & SERVER_MORE_RESULTS_EXISTS) != 0;
        }

        if (!streamResults) {
            clearInputStream();
        }

        reclaimLargeReusablePacket();

        return topLevelResultSet;
    }

    /**
     * Sets the buffer size to max-buf
     */
    void resetMaxBuf() {
        this.maxAllowedPacket = this.connection.getMaxAllowedPacket();
    }

    /**
     * Send a command to the MySQL server If data is to be sent with command,
     * it should be put in extraData.
     *
     * Raw packets can be sent by setting queryPacket to something other
     * than null.
     *
     * @param command the MySQL protocol 'command' from MysqlDefs
     * @param extraData any 'string' data for the command
     * @param queryPacket a packet pre-loaded with data for the protocol (i.e.
     * from a client-side prepared statement).
     * @param skipCheck do not call checkErrorPacket() if true
     * @param extraDataCharEncoding the character encoding of the extraData
     * parameter.
     *
     * @return the response packet from the server
     *
     * @throws SQLException if an I/O error or SQL error occurs
     */
   
    final Buffer sendCommand(int command, String extraData, Buffer queryPacket,
        boolean skipCheck, String extraDataCharEncoding)
        throws SQLException {
        //
        // We cache these locally, per-command, as the checks
        // for them are in very 'hot' sections of the I/O code
        // and we save 10-15% in overall performance by doing this...
        //
        this.enablePacketDebug = this.connection.getEnablePacketDebug();
        this.traceProtocol = this.connection.getTraceProtocol();
        this.readPacketSequence = 0;

        try {
        	
            checkForOutstandingStreamingData();
           
            // Clear serverStatus...this value is guarded by an
            // external mutex, as you can only ever be processing 
            // one command at a time
            this.serverStatus = 0;
            this.hadWarnings = false;
            this.warningCount = 0;

            this.queryNoIndexUsed = false;
            this.queryBadIndexUsed = false;

            //
            // Compressed input stream needs cleared at beginning
            // of each command execution...
            //
            if (this.useCompression) {
                int bytesLeft = this.mysqlInput.available();

                if (bytesLeft > 0) {
                    this.mysqlInput.skip(bytesLeft);
                }
            }

            try {
                clearInputStream();

                //
                // PreparedStatements construct their own packets,
                // for efficiency's sake.
                //
                // If this is a generic query, we need to re-use
                // the sending packet.
                //
                if (queryPacket == null) {
                    int packLength = HEADER_LENGTH + COMP_HEADER_LENGTH + 1 +
                        ((extraData != null) ? extraData.length() : 0) + 2;

                    if (this.sendPacket == null) {
                        this.sendPacket = new Buffer(packLength);
                    }

                    this.packetSequence = -1;
                    this.readPacketSequence = 0;
                    this.checkPacketSequence = true;
                    this.sendPacket.clear();

                    this.sendPacket.writeByte((byte) command);

                    if ((command == MysqlDefs.INIT_DB) ||
                            (command == MysqlDefs.CREATE_DB) ||
                            (command == MysqlDefs.DROP_DB) ||
                            (command == MysqlDefs.QUERY) ||
                            (command == MysqlDefs.COM_PREPARE)) {
                        if (extraDataCharEncoding == null) {
                            this.sendPacket.writeStringNoNull(extraData);
                        } else {
                            this.sendPacket.writeStringNoNull(extraData,
                                extraDataCharEncoding,
                                this.connection.getServerCharacterEncoding(),
                                this.connection.parserKnowsUnicode(), this.connection);
                        }
                    } else if (command == MysqlDefs.PROCESS_KILL) {
                        long id = new Long(extraData).longValue();
                        this.sendPacket.writeLong(id);
                    }

                    send(this.sendPacket, this.sendPacket.getPosition());
                } else {
                    this.packetSequence = -1;
                    send(queryPacket, queryPacket.getPosition()); // packet passed by PreparedStatement
                }
            } catch (SQLException sqlEx) {
                // don't wrap SQLExceptions
                throw sqlEx;
            } catch (Exception ex) {
                throw new CommunicationsException(this.connection,
                    this.lastPacketSentTimeMs, ex);
            }

            Buffer returnPacket = null;

            if (!skipCheck) {
                if ((command == MysqlDefs.COM_EXECUTE) ||
                        (command == MysqlDefs.COM_RESET_STMT)) {
                    this.readPacketSequence = 0;
                    this.packetSequenceReset = true;
                }

                returnPacket = checkErrorPacket(command);
            }

            return returnPacket;
        } catch (IOException ioEx) {
            throw new CommunicationsException(this.connection,
                this.lastPacketSentTimeMs, ioEx);
        }
    }

    /**
     * Send a query stored in a packet directly to the server.
     *
     * @param callingStatement DOCUMENT ME!
     * @param resultSetConcurrency DOCUMENT ME!
     * @param characterEncoding DOCUMENT ME!
     * @param queryPacket DOCUMENT ME!
     * @param maxRows DOCUMENT ME!
     * @param conn DOCUMENT ME!
     * @param resultSetType DOCUMENT ME!
     * @param resultSetConcurrency DOCUMENT ME!
     * @param streamResults DOCUMENT ME!
     * @param catalog DOCUMENT ME!
     * @param unpackFieldInfo should we read MYSQL_FIELD info (if available)?
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    final ResultSet sqlQueryDirect(Statement callingStatement, String query,
        String characterEncoding, Buffer queryPacket, int maxRows,
        Connection conn, int resultSetType, int resultSetConcurrency,
        boolean streamResults, String catalog, boolean unpackFieldInfo)
        throws Exception {
        long queryStartTime = 0;
        long queryEndTime = 0;

        if (query != null) {
        	
        	
            // We don't know exactly how many bytes we're going to get
            // from the query. Since we're dealing with Unicode, the
            // max is 2, so pad it (2 * query) + space for headers
            int packLength = HEADER_LENGTH + 1 + (query.length() * 2) + 2;

            if (this.sendPacket == null) {
            	this.sendPacket = new Buffer(packLength);
            } else {
                this.sendPacket.clear();
            }

            this.sendPacket.writeByte((byte) MysqlDefs.QUERY);

            if (characterEncoding != null) {
                if (this.platformDbCharsetMatches) {
                    this.sendPacket.writeStringNoNull(query, characterEncoding,
                        this.connection.getServerCharacterEncoding(),
                        this.connection.parserKnowsUnicode(),
                        this.connection);
                } else {
                    if (StringUtils.startsWithIgnoreCaseAndWs(query, "LOAD DATA")) { //$NON-NLS-1$
                        this.sendPacket.writeBytesNoNull(query.getBytes());
                    } else {
                        this.sendPacket.writeStringNoNull(query,
                            characterEncoding,
                            this.connection.getServerCharacterEncoding(),
                            this.connection.parserKnowsUnicode(),
                            this.connection);
                    }
                }
            } else {
                this.sendPacket.writeStringNoNull(query);
            }

            queryPacket = this.sendPacket;
        }

        byte[] queryBuf = null;
        int oldPacketPosition = 0;

        
        
        if (needToGrabQueryFromPacket) {
            queryBuf = queryPacket.getByteBuffer();

            // save the packet position
            oldPacketPosition = queryPacket.getPosition();

            queryStartTime = getCurrentTimeNanosOrMillis();
        }
  
        // Send query command and sql query string
        Buffer resultPacket = sendCommand(MysqlDefs.QUERY, null, queryPacket,
                false, null);

        long fetchBeginTime = 0;
        long fetchEndTime = 0;

        String profileQueryToLog = null;

        boolean queryWasSlow = false;

        if (this.profileSql || this.logSlowQueries) {
            queryEndTime = getCurrentTimeNanosOrMillis();

            boolean shouldExtractQuery = false;

            if (this.profileSql) {
                shouldExtractQuery = true;
            } else if (this.logSlowQueries) {

            	if ((queryEndTime - queryStartTime) > this.slowQueryThreshold) {
            		shouldExtractQuery = true;
            		queryWasSlow = true;
            	}
            }

            if (shouldExtractQuery) {
                // Extract the actual query from the network packet 
                boolean truncated = false;

                int extractPosition = oldPacketPosition;

                if (oldPacketPosition > this.connection.getMaxQuerySizeToLog()) {
                    extractPosition = this.connection.getMaxQuerySizeToLog() + 5;
                    truncated = true;
                }

                profileQueryToLog = new String(queryBuf, 5,
                        (extractPosition - 5));

                if (truncated) {
                    profileQueryToLog += Messages.getString("MysqlIO.25"); //$NON-NLS-1$
                }
            }

            fetchBeginTime = queryEndTime;
        }
        
        if (this.autoGenerateTestcaseScript) {
        	String testcaseQuery = null;
        	
        	if (query != null) {
        		testcaseQuery = query;
        	} else {
        		testcaseQuery = new String(queryBuf, 5,
                        (oldPacketPosition - 5));
        	}
        	
    		StringBuffer debugBuf = new StringBuffer(testcaseQuery.length() + 32);
    		this.connection.generateConnectionCommentBlock(debugBuf);
    		debugBuf.append(testcaseQuery);
    		debugBuf.append(';');
    		this.connection.dumpTestcaseQuery(debugBuf.toString());
    	}
        
        ResultSet rs = readAllResults(callingStatement, maxRows, resultSetType,
                resultSetConcurrency, streamResults, catalog, resultPacket,
                false, -1L, unpackFieldInfo, null /* we don't need metadata for cached MD in this case */);

        if (queryWasSlow) {
            StringBuffer mesgBuf = new StringBuffer(48 +
                    profileQueryToLog.length());
            
			mesgBuf.append(Messages.getString("MysqlIO.SlowQuery",
            		new Object[] {new Long(this.slowQueryThreshold),
            						queryTimingUnits,
            						new Long(queryEndTime - queryStartTime)}));
            mesgBuf.append(profileQueryToLog);

            ProfileEventSink eventSink = ProfileEventSink.getInstance(this.connection);

            eventSink.consumeEvent(new ProfilerEvent(ProfilerEvent.TYPE_SLOW_QUERY,
                    "", catalog, this.connection.getId(), //$NON-NLS-1$
                    (callingStatement != null) ? callingStatement.getId() : 999,
                    rs.resultId, System.currentTimeMillis(),
                    (int) (queryEndTime - queryStartTime), queryTimingUnits, null,
                    new Throwable(), mesgBuf.toString()));

            if (this.connection.getExplainSlowQueries()) {
                if (oldPacketPosition < MAX_QUERY_SIZE_TO_EXPLAIN) {
                    explainSlowQuery(queryPacket.getBytes(5,
                            (oldPacketPosition - 5)), profileQueryToLog);
                } else {
                    this.connection.getLog().logWarn(Messages.getString(
                            "MysqlIO.28") //$NON-NLS-1$
                         +MAX_QUERY_SIZE_TO_EXPLAIN +
                        Messages.getString("MysqlIO.29")); //$NON-NLS-1$
                }
            }
        }

        if (this.profileSql) {
            fetchEndTime = getCurrentTimeNanosOrMillis();

            ProfileEventSink eventSink = ProfileEventSink.getInstance(this.connection);

            eventSink.consumeEvent(new ProfilerEvent(ProfilerEvent.TYPE_QUERY,
                    "", catalog, this.connection.getId(), //$NON-NLS-1$
                    (callingStatement != null) ? callingStatement.getId() : 999,
                    rs.resultId, System.currentTimeMillis(),
                    (queryEndTime - queryStartTime), this.queryTimingUnits, 
                    null,
                    new Throwable(), profileQueryToLog));

            eventSink.consumeEvent(new ProfilerEvent(ProfilerEvent.TYPE_FETCH,
                    "", catalog, this.connection.getId(), //$NON-NLS-1$
                    (callingStatement != null) ? callingStatement.getId() : 999,
                    rs.resultId, System.currentTimeMillis(),
                    (fetchEndTime - fetchBeginTime), this.queryTimingUnits,
                    null,
                    new Throwable(), null));

            if (this.queryBadIndexUsed) {
                eventSink.consumeEvent(new ProfilerEvent(
                        ProfilerEvent.TYPE_SLOW_QUERY, "", catalog, //$NON-NLS-1$
                        this.connection.getId(),
                        (callingStatement != null) ? callingStatement.getId()
                                                   : 999, rs.resultId,
                        System.currentTimeMillis(),
                        (queryEndTime - queryStartTime), this.queryTimingUnits,
                        null,
                        new Throwable(),
                        Messages.getString("MysqlIO.33") //$NON-NLS-1$
                         +profileQueryToLog));
            }

            if (this.queryNoIndexUsed) {
                eventSink.consumeEvent(new ProfilerEvent(
                        ProfilerEvent.TYPE_SLOW_QUERY, "", catalog, //$NON-NLS-1$
                        this.connection.getId(),
                        (callingStatement != null) ? callingStatement.getId()
                                                   : 999, rs.resultId,
                        System.currentTimeMillis(),
                        (queryEndTime - queryStartTime), this.queryTimingUnits,
                        null,
                        new Throwable(),
                        Messages.getString("MysqlIO.35") //$NON-NLS-1$
                         +profileQueryToLog));
            }
        }

        if (this.hadWarnings) {
            scanForAndThrowDataTruncation();
        }

        return rs;
    }

	private void calculateSlowQueryThreshold() {
		this.slowQueryThreshold = this.connection.getSlowQueryThresholdMillis();
		
		if (this.connection.getUseNanosForElapsedTime()) {
			long nanosThreshold = this.connection.getSlowQueryThresholdNanos();
			
			if (nanosThreshold != 0) {
				this.slowQueryThreshold = nanosThreshold;
			} else {
				this.slowQueryThreshold *= 1000000; // 1 million millis in a nano
			}
		}
	}

	protected long getCurrentTimeNanosOrMillis() {
		if (this.useNanosForElapsedTime) {
			return Util.getCurrentTimeNanosOrMillis();
		}
		
		return System.currentTimeMillis();
	}

	/**
     * Returns the host this IO is connected to
     *
     * @return DOCUMENT ME!
     */
    String getHost() {
        return this.host;
    }

    /**
     * Is the version of the MySQL server we are connected to the given
     * version?
     *
     * @param major the major version
     * @param minor the minor version
     * @param subminor the subminor version
     *
     * @return true if the version of the MySQL server we are connected  is the
     *         given version
     */
    boolean isVersion(int major, int minor, int subminor) {
        return ((major == getServerMajorVersion()) &&
        (minor == getServerMinorVersion()) &&
        (subminor == getServerSubMinorVersion()));
    }

    /**
     * Does the version of the MySQL server we are connected to meet the given
     * minimums?
     *
     * @param major DOCUMENT ME!
     * @param minor DOCUMENT ME!
     * @param subminor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean versionMeetsMinimum(int major, int minor, int subminor) {
        if (getServerMajorVersion() >= major) {
            if (getServerMajorVersion() == major) {
                if (getServerMinorVersion() >= minor) {
                    if (getServerMinorVersion() == minor) {
                        return (getServerSubMinorVersion() >= subminor);
                    }

                    // newer than major.minor
                    return true;
                }

                // older than major.minor
                return false;
            }

            // newer than major  
            return true;
        }

        return false;
    }

    /**
     * Returns the hex dump of the given packet, truncated to
     * MAX_PACKET_DUMP_LENGTH if packetLength exceeds that value.
     *
     * @param packetToDump the packet to dump in hex
     * @param packetLength the number of bytes to dump
     *
     * @return the hex dump of the given packet
     */
    private final static String getPacketDumpToLog(Buffer packetToDump,
        int packetLength) {
        if (packetLength < MAX_PACKET_DUMP_LENGTH) {
            return packetToDump.dump(packetLength);
        }

        StringBuffer packetDumpBuf = new StringBuffer(MAX_PACKET_DUMP_LENGTH * 4);
        packetDumpBuf.append(packetToDump.dump(MAX_PACKET_DUMP_LENGTH));
        packetDumpBuf.append(Messages.getString("MysqlIO.36")); //$NON-NLS-1$
        packetDumpBuf.append(MAX_PACKET_DUMP_LENGTH);
        packetDumpBuf.append(Messages.getString("MysqlIO.37")); //$NON-NLS-1$

        return packetDumpBuf.toString();
    }

    private final int readFully(InputStream in, byte[] b, int off, int len)
        throws IOException {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }

        int n = 0;

        while (n < len) {
            int count = in.read(b, off + n, len - n);

            if (count < 0) {
            	 throw new EOFException(Messages.getString("MysqlIO.EOF", 
                 		new Object[] {new Integer(len), new Integer(n)}));
            }

            n += count;
        }

        return n;
    }
    
    private final long skipFully(InputStream in, long len) throws IOException {
    	if (len < 0) {
    		throw new IOException("Negative skip length not allowed");
    	}
    	
    	long n = 0;
    	
    	while (n < len) {
    		long count = in.skip(len - n);
    		
    		if (count < 0) {
    			 throw new EOFException(Messages.getString("MysqlIO.EOF", 
                 		new Object[] {new Long(len), new Long(n)}));
    		}
    		
    		n += count;
    	}
    	
    	return n;
    }

    /**
     * Reads one result set off of the wire, if the result is actually an
     * update count, creates an update-count only result set.
     *
     * @param callingStatement DOCUMENT ME!
     * @param maxRows the maximum rows to return in the result set.
     * @param resultSetType scrollability
     * @param resultSetConcurrency updatability
     * @param streamResults should the driver leave the results on the wire,
     *        and read them only when needed?
     * @param catalog the catalog in use
     * @param resultPacket the first packet of information in the result set
     * @param isBinaryEncoded is this result set from a prepared statement?
     * @param preSentColumnCount do we already know the number of columns?
     * @param unpackFieldInfo should we unpack the field information?
     *
     * @return a result set that either represents the rows, or an update count
     *
     * @throws SQLException if an error occurs while reading the rows
     */
    private final ResultSet readResultsForQueryOrUpdate(
        Statement callingStatement, int maxRows, int resultSetType,
        int resultSetConcurrency, boolean streamResults, String catalog,
        Buffer resultPacket, boolean isBinaryEncoded, long preSentColumnCount,
        boolean unpackFieldInfo, Field[] metadataFromCache) throws SQLException {
        long columnCount = resultPacket.readFieldLength();

        if (columnCount == 0) {
            return buildResultSetWithUpdates(callingStatement, resultPacket);
        } else if (columnCount == Buffer.NULL_LENGTH) {
            String charEncoding = null;

            if (this.connection.getUseUnicode()) {
                charEncoding = this.connection.getEncoding();
            }

            String fileName = null;

            if (this.platformDbCharsetMatches) {
                fileName = ((charEncoding != null)
                    ? resultPacket.readString(charEncoding)
                    : resultPacket.readString());
            } else {
                fileName = resultPacket.readString();
            }

            return sendFileToServer(callingStatement, fileName);
        } else {
            com.mysql.jdbc.ResultSet results = getResultSet(callingStatement,
                    columnCount, maxRows, resultSetType, resultSetConcurrency,
                    streamResults, catalog, isBinaryEncoded, unpackFieldInfo, 
                    metadataFromCache);

            return results;
        }
    }

    private int alignPacketSize(int a, int l) {
        return ((((a) + (l)) - 1) & ~((l) - 1));
    }

    private com.mysql.jdbc.ResultSet buildResultSetWithRows(
        Statement callingStatement, String catalog,
        com.mysql.jdbc.Field[] fields, RowData rows, int resultSetType,
        int resultSetConcurrency, boolean isBinaryEncoded)
        throws SQLException {
        ResultSet rs = null;

        switch (resultSetConcurrency) {
        case java.sql.ResultSet.CONCUR_READ_ONLY:
            rs = new com.mysql.jdbc.ResultSet(catalog, fields, rows,
                    this.connection, callingStatement);

            if (isBinaryEncoded) {
                rs.setBinaryEncoded();
            }

            break;

        case java.sql.ResultSet.CONCUR_UPDATABLE:
            rs = new com.mysql.jdbc.UpdatableResultSet(catalog, fields, rows,
                    this.connection, callingStatement);

            break;

        default:
            return new com.mysql.jdbc.ResultSet(catalog, fields, rows,
                this.connection, callingStatement);
        }

        rs.setResultSetType(resultSetType);
        rs.setResultSetConcurrency(resultSetConcurrency);

        return rs;
    }

    private com.mysql.jdbc.ResultSet buildResultSetWithUpdates(
        Statement callingStatement, Buffer resultPacket)
        throws SQLException {
        long updateCount = -1;
        long updateID = -1;
        String info = null;

        try {
            if (this.useNewUpdateCounts) {
                updateCount = resultPacket.newReadLength();
                updateID = resultPacket.newReadLength();
            } else {
                updateCount = resultPacket.readLength();
                updateID = resultPacket.readLength();
            }

            if (this.use41Extensions) {
                this.serverStatus = resultPacket.readInt();

                this.warningCount = resultPacket.readInt();

                if (this.warningCount > 0) {
                    this.hadWarnings = true; // this is a 'latch', it's reset by sendCommand()
                }

                resultPacket.readByte(); // advance pointer

                if (this.profileSql) {
                    this.queryNoIndexUsed = (this.serverStatus &
                        SERVER_QUERY_NO_GOOD_INDEX_USED) != 0;
                    this.queryBadIndexUsed = (this.serverStatus &
                        SERVER_QUERY_NO_INDEX_USED) != 0;
                }
            }

            if (this.connection.isReadInfoMsgEnabled()) {
                info = resultPacket.readString();
            }
        } catch (Exception ex) {
            throw SQLError.createSQLException(SQLError.get(
                    SQLError.SQL_STATE_GENERAL_ERROR) + ": " //$NON-NLS-1$
                 +ex.getClass().getName(), SQLError.SQL_STATE_GENERAL_ERROR, -1);
        }

        ResultSet updateRs = new com.mysql.jdbc.ResultSet(updateCount,
                updateID, this.connection, callingStatement);

        if (info != null) {
            updateRs.setServerInfo(info);
        }

        return updateRs;
    }

    private void checkForOutstandingStreamingData() throws SQLException {
        if (this.streamingData != null) {
            if (!this.connection.getClobberStreamingResults()) {
                throw SQLError.createSQLException(Messages.getString("MysqlIO.39") //$NON-NLS-1$
                     +this.streamingData +
                    Messages.getString("MysqlIO.40") //$NON-NLS-1$
                     +Messages.getString("MysqlIO.41") //$NON-NLS-1$
                     +Messages.getString("MysqlIO.42")); //$NON-NLS-1$
            }

            // Close the result set
            this.streamingData.getOwner().realClose(false);

            // clear any pending data....
            clearInputStream();
        }
    }

    private Buffer compressPacket(Buffer packet, int offset, int packetLen,
        int headerLength) throws SQLException {
        packet.writeLongInt(packetLen - headerLength);
        packet.writeByte((byte) 0); // wrapped packet has 0 packet seq.

        int lengthToWrite = 0;
        int compressedLength = 0;
        byte[] bytesToCompress = packet.getByteBuffer();
        byte[] compressedBytes = null;
        int offsetWrite = 0;

        if (packetLen < MIN_COMPRESS_LEN) {
            lengthToWrite = packetLen;
            compressedBytes = packet.getByteBuffer();
            compressedLength = 0;
            offsetWrite = offset;
        } else {
            compressedBytes = new byte[bytesToCompress.length * 2];

            this.deflater.reset();
            this.deflater.setInput(bytesToCompress, offset, packetLen);
            this.deflater.finish();

            int compLen = this.deflater.deflate(compressedBytes);

            if (compLen > packetLen) {
                lengthToWrite = packetLen;
                compressedBytes = packet.getByteBuffer();
                compressedLength = 0;
                offsetWrite = offset;
            } else {
                lengthToWrite = compLen;
                headerLength += COMP_HEADER_LENGTH;
                compressedLength = packetLen;
            }
        }

        Buffer compressedPacket = new Buffer(packetLen + headerLength);

        compressedPacket.setPosition(0);
        compressedPacket.writeLongInt(lengthToWrite);
        compressedPacket.writeByte(this.packetSequence);
        compressedPacket.writeLongInt(compressedLength);
        compressedPacket.writeBytesNoNull(compressedBytes, offsetWrite,
            lengthToWrite);

        return compressedPacket;
    }

    private final void readServerStatusForResultSets(Buffer rowPacket)
        throws SQLException {
        if (this.use41Extensions) {
            rowPacket.readByte(); // skips the 'last packet' flag

            this.warningCount = rowPacket.readInt();

            if (this.warningCount > 0) {
                this.hadWarnings = true; // this is a 'latch', it's reset by sendCommand()
            }

            this.serverStatus = rowPacket.readInt();

            if (this.profileSql) {
                this.queryNoIndexUsed = (this.serverStatus &
                    SERVER_QUERY_NO_GOOD_INDEX_USED) != 0;
                this.queryBadIndexUsed = (this.serverStatus &
                    SERVER_QUERY_NO_INDEX_USED) != 0;
            }
        }
    }
    
    private SocketFactory createSocketFactory() throws SQLException {
        try {
            if (this.socketFactoryClassName == null) {
                throw SQLError.createSQLException(Messages.getString("MysqlIO.75"), //$NON-NLS-1$
                    SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE);
            }

            return (SocketFactory) (Class.forName(this.socketFactoryClassName)
                                         .newInstance());
        } catch (Exception ex) {
            throw SQLError.createSQLException(Messages.getString("MysqlIO.76") //$NON-NLS-1$
                 +this.socketFactoryClassName +
                Messages.getString("MysqlIO.77") + ex.toString() //$NON-NLS-1$
                 +(this.connection.getParanoid() ? "" //$NON-NLS-1$
                                                 : Util.stackTraceToString(ex)),
                SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE);
        }
    }

    private void enqueuePacketForDebugging(boolean isPacketBeingSent,
        boolean isPacketReused, int sendLength, byte[] header, Buffer packet)
        throws SQLException {
        if ((this.packetDebugRingBuffer.size() + 1) > this.connection.getPacketDebugBufferSize()) {
            this.packetDebugRingBuffer.removeFirst();
        }

        StringBuffer packetDump = null;

        if (!isPacketBeingSent) {
            int bytesToDump = Math.min(MAX_PACKET_DUMP_LENGTH,
                    packet.getBufLength());

            Buffer packetToDump = new Buffer(4 + bytesToDump);

            packetToDump.setPosition(0);
            packetToDump.writeBytesNoNull(header);
            packetToDump.writeBytesNoNull(packet.getBytes(0, bytesToDump));

            String packetPayload = packetToDump.dump(bytesToDump);

            packetDump = new StringBuffer(96 + packetPayload.length());

            packetDump.append("Server ");

            if (isPacketReused) {
                packetDump.append("(re-used)");
            } else {
                packetDump.append("(new)");
            }

            packetDump.append(" ");
            packetDump.append(packet.toSuperString());
            packetDump.append(" --------------------> Client\n");
            packetDump.append("\nPacket payload:\n\n");
            packetDump.append(packetPayload);

            if (bytesToDump == MAX_PACKET_DUMP_LENGTH) {
                packetDump.append("\nNote: Packet of " + packet.getBufLength() +
                    " bytes truncated to " + MAX_PACKET_DUMP_LENGTH +
                    " bytes.\n");
            }
        } else {
            int bytesToDump = Math.min(MAX_PACKET_DUMP_LENGTH, sendLength);

            String packetPayload = packet.dump(bytesToDump);

            packetDump = new StringBuffer(64 + 4 + packetPayload.length());

            packetDump.append("Client ");
            packetDump.append(packet.toSuperString());
            packetDump.append("--------------------> Server\n");
            packetDump.append("\nPacket payload:\n\n");
            packetDump.append(packetPayload);

            if (bytesToDump == MAX_PACKET_DUMP_LENGTH) {
                packetDump.append("\nNote: Packet of " + sendLength +
                    " bytes truncated to " + MAX_PACKET_DUMP_LENGTH +
                    " bytes.\n");
            }
        }

        this.packetDebugRingBuffer.addLast(packetDump);
    }
    
    private RowData readSingleRowSet(long columnCount, int maxRows,
        int resultSetConcurrency, boolean isBinaryEncoded, Field[] fields)
        throws SQLException {
        RowData rowData;
        ArrayList rows = new ArrayList();

        // Now read the data
        Object rowBytes = nextRow(fields, (int) columnCount, isBinaryEncoded,
                resultSetConcurrency);
        
        int rowCount = 0;

        if (rowBytes != null) {
            rows.add(rowBytes);
            rowCount = 1;
        }

        while (rowBytes != null) {
            rowBytes = nextRow(fields, (int) columnCount, isBinaryEncoded,
                    resultSetConcurrency);

            if (rowBytes != null) {
            	if ((maxRows == -1) || (rowCount < maxRows)) {
            		rows.add(rowBytes);
            		rowCount++;
            	}
            }
        }

        rowData = new RowDataStatic(rows);

        return rowData;
    }

    /**
     * Don't hold on to overly-large packets
     */
    private void reclaimLargeReusablePacket() {
        if ((this.reusablePacket != null) &&
                (this.reusablePacket.getCapacity() > 1048576)) {
            this.reusablePacket = new Buffer(INITIAL_PACKET_SIZE);
        }
    }

    /**
     * Re-use a packet to read from the MySQL server
     *
     * @param reuse DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     * @throws SQLException DOCUMENT ME!
     */
    private final Buffer reuseAndReadPacket(Buffer reuse)
        throws SQLException {
        
    	try {
    		reuse.setWasMultiPacket(false);
    		
    		int lengthRead = readFully(this.mysqlInput,
    				this.packetHeaderBuf, 0, 4);
    		
    		if (lengthRead < 4) {
    			forceClose();
    			throw new IOException(Messages.getString("MysqlIO.43")); //$NON-NLS-1$
    		}
    		
    		int packetLength = (this.packetHeaderBuf[0] & 0xff) +
    		((this.packetHeaderBuf[1] & 0xff) << 8) +
    		((this.packetHeaderBuf[2] & 0xff) << 16);
    		
    		if (this.traceProtocol) {
    			StringBuffer traceMessageBuf = new StringBuffer();
    			
    			traceMessageBuf.append(Messages.getString("MysqlIO.44")); //$NON-NLS-1$
    			traceMessageBuf.append(packetLength);
    			traceMessageBuf.append(Messages.getString("MysqlIO.45")); //$NON-NLS-1$
    			traceMessageBuf.append(StringUtils.dumpAsHex(
    					this.packetHeaderBuf, 4));
    			
    			this.connection.getLog().logTrace(traceMessageBuf.toString());
    		}
    		
    		byte multiPacketSeq = this.packetHeaderBuf[3];
    		
    		if (!this.packetSequenceReset) {
    			if (this.enablePacketDebug && this.checkPacketSequence) {
    				checkPacketSequencing(multiPacketSeq);
    			}
    		} else {
    			this.packetSequenceReset = false;
    		}
    		
    		this.readPacketSequence = multiPacketSeq;
    		
    		// Set the Buffer to it's original state
    		reuse.setPosition(0);
    		
    		// Do we need to re-alloc the byte buffer?
    		//
    		// Note: We actually check the length of the buffer,
    		// rather than getBufLength(), because getBufLength() is not
    		// necesarily the actual length of the byte array
    		// used as the buffer
    		if (reuse.getByteBuffer().length <= packetLength) {
    			reuse.setByteBuffer(new byte[packetLength + 1]);
    		}
    		
    		// Set the new length
    		reuse.setBufLength(packetLength);
    		
    		// Read the data from the server
    		int numBytesRead = readFully(this.mysqlInput,
    				reuse.getByteBuffer(), 0, packetLength);
    		
    		if (numBytesRead != packetLength) {
    			throw new IOException("Short read, expected " +
    					packetLength + " bytes, only read " + numBytesRead);
    		}
    		
    		if (this.traceProtocol) {
    			StringBuffer traceMessageBuf = new StringBuffer();
    			
    			traceMessageBuf.append(Messages.getString("MysqlIO.46")); //$NON-NLS-1$
    			traceMessageBuf.append(getPacketDumpToLog(reuse,
    					packetLength));
    			
    			this.connection.getLog().logTrace(traceMessageBuf.toString());
    		}
    		
    		if (this.enablePacketDebug) {
    			enqueuePacketForDebugging(false, true, 0,
    					this.packetHeaderBuf, reuse);
    		}
    		
    		boolean isMultiPacket = false;
    		
    		if (packetLength == this.maxThreeBytes) {
    			reuse.setPosition(this.maxThreeBytes);
    			
    			int packetEndPoint = packetLength;
    			
    			// it's multi-packet
    			isMultiPacket = true;
    			
    			lengthRead = readFully(this.mysqlInput,
    					this.packetHeaderBuf, 0, 4);
    			
    			if (lengthRead < 4) {
    				forceClose();
    				throw new IOException(Messages.getString("MysqlIO.47")); //$NON-NLS-1$
    			}
    			
    			packetLength = (this.packetHeaderBuf[0] & 0xff) +
    				((this.packetHeaderBuf[1] & 0xff) << 8) +
    				((this.packetHeaderBuf[2] & 0xff) << 16);
    			
    			Buffer multiPacket = new Buffer(packetLength);
    			boolean firstMultiPkt = true;
    			
    			while (true) {
    				if (!firstMultiPkt) {
    					lengthRead = readFully(this.mysqlInput,
    							this.packetHeaderBuf, 0, 4);
    					
    					if (lengthRead < 4) {
    						forceClose();
    						throw new IOException(Messages.getString(
    						"MysqlIO.48")); //$NON-NLS-1$
    					}
    					
    					packetLength = (this.packetHeaderBuf[0] & 0xff) +
    					((this.packetHeaderBuf[1] & 0xff) << 8) +
    					((this.packetHeaderBuf[2] & 0xff) << 16);
    				} else {
    					firstMultiPkt = false;
    				}
    				
    				if (!this.useNewLargePackets && (packetLength == 1)) {
    					clearInputStream();
    					
    					break;
    				} else if (packetLength < this.maxThreeBytes) {
    					byte newPacketSeq = this.packetHeaderBuf[3];
    					
    					if (newPacketSeq != (multiPacketSeq + 1)) {
    						throw new IOException(Messages.getString(
    						"MysqlIO.49")); //$NON-NLS-1$
    					}
    					
    					multiPacketSeq = newPacketSeq;
    					
    					// Set the Buffer to it's original state
    					multiPacket.setPosition(0);
    					
    					// Set the new length
    					multiPacket.setBufLength(packetLength);
    					
    					// Read the data from the server
    					byte[] byteBuf = multiPacket.getByteBuffer();
    					int lengthToWrite = packetLength;
    					
    					int bytesRead = readFully(this.mysqlInput, byteBuf,
    							0, packetLength);
    					
    					if (bytesRead != lengthToWrite) {
    						throw new CommunicationsException(this.connection,
    								this.lastPacketSentTimeMs,
    								SQLError.createSQLException(Messages.getString(
    								"MysqlIO.50") //$NON-NLS-1$
    								+lengthToWrite +
    								Messages.getString("MysqlIO.51") +
    								bytesRead //$NON-NLS-1$
    								+".")); //$NON-NLS-1$
    					}
    					
    					reuse.writeBytesNoNull(byteBuf, 0, lengthToWrite);
    					
    					packetEndPoint += lengthToWrite;
    					
    					break; // end of multipacket sequence
    				}
    				
    				byte newPacketSeq = this.packetHeaderBuf[3];
    				
    				if (newPacketSeq != (multiPacketSeq + 1)) {
    					throw new IOException(Messages.getString(
    					"MysqlIO.53")); //$NON-NLS-1$
    				}
    				
    				multiPacketSeq = newPacketSeq;
    				
    				// Set the Buffer to it's original state
    				multiPacket.setPosition(0);
    				
    				// Set the new length
    				multiPacket.setBufLength(packetLength);
    				
    				// Read the data from the server
    				byte[] byteBuf = multiPacket.getByteBuffer();
    				int lengthToWrite = packetLength;
    				
    				int bytesRead = readFully(this.mysqlInput, byteBuf, 0,
    						packetLength);
    				
    				if (bytesRead != lengthToWrite) {
    					throw new CommunicationsException(this.connection,
    							this.lastPacketSentTimeMs,
    							SQLError.createSQLException(Messages.getString(
    							"MysqlIO.54") //$NON-NLS-1$
    							+lengthToWrite +
    							Messages.getString("MysqlIO.55") //$NON-NLS-1$
    							+bytesRead + ".")); //$NON-NLS-1$
    				}
    				
    				reuse.writeBytesNoNull(byteBuf, 0, lengthToWrite);
    				
    				packetEndPoint += lengthToWrite;
    			}
    			
    			reuse.setPosition(0);
    			reuse.setWasMultiPacket(true);
    		}
    		
    		if (!isMultiPacket) {
    			reuse.getByteBuffer()[packetLength] = 0; // Null-termination
    		}
    		
    		return reuse;
    	} catch (IOException ioEx) {
    		throw new CommunicationsException(this.connection,
    				this.lastPacketSentTimeMs, ioEx);
    	} catch (OutOfMemoryError oom) {
    		try {
    			// _Try_ this
    			clearInputStream();
    		} finally {
    			try {
    				this.connection.realClose(false, false, true, oom);
    			} finally {
    				throw oom;
    			}
    		}
    	}
    	
    }

    /**
         * @param multiPacketSeq
         * @throws CommunicationsException
         */
    private void checkPacketSequencing(byte multiPacketSeq)
        throws CommunicationsException {
        if ((multiPacketSeq == -128) && (this.readPacketSequence != 127)) {
            throw new CommunicationsException(this.connection,
                this.lastPacketSentTimeMs,
                new IOException("Packets out of order, expected packet # -128, but received packet # " +
                    multiPacketSeq));
        }

        if ((this.readPacketSequence == -1) && (multiPacketSeq != 0)) {
            throw new CommunicationsException(this.connection,
                this.lastPacketSentTimeMs,
                new IOException("Packets out of order, expected packet # -1, but received packet # " +
                    multiPacketSeq));
        }

        if ((multiPacketSeq != -128) && (this.readPacketSequence != -1) &&
                (multiPacketSeq != (this.readPacketSequence + 1))) {
            throw new CommunicationsException(this.connection,
                this.lastPacketSentTimeMs,
                new IOException("Packets out of order, expected packet # " +
                    (this.readPacketSequence + 1) + ", but received packet # " +
                    multiPacketSeq));
        }
    }

    void enableMultiQueries() throws SQLException {
    	Buffer buf = getSharedSendPacket();
    	
    	buf.clear();
    	buf.writeByte((byte)MysqlDefs.COM_SET_OPTION);
    	buf.writeInt(0);
    	sendCommand(MysqlDefs.COM_SET_OPTION, null, buf, false, null);
    }
    
    void disableMultiQueries() throws SQLException {
    	Buffer buf = getSharedSendPacket();
    	
    	buf.clear();
    	buf.writeByte((byte)MysqlDefs.COM_SET_OPTION);
    	buf.writeInt(1);
    	sendCommand(MysqlDefs.COM_SET_OPTION, null, buf, false, null);
    }
    
    private final void send(Buffer packet, int packetLen)
        throws SQLException {
        try {
            if (packetLen > this.maxAllowedPacket) {
                throw new PacketTooBigException(packetLen, this.maxAllowedPacket);
            }

			if (this.connection.getMaintainTimeStats()) {
				this.lastPacketSentTimeMs = System.currentTimeMillis();
			}

            if ((this.serverMajorVersion >= 4) &&
                    (packetLen >= this.maxThreeBytes)) {
                sendSplitPackets(packet);
            } else {
                this.packetSequence++;

                Buffer packetToSend = packet;

                packetToSend.setPosition(0);

                if (this.useCompression) {
                    int originalPacketLen = packetLen;

                    packetToSend = compressPacket(packet, 0, packetLen,
                            HEADER_LENGTH);
                    packetLen = packetToSend.getPosition();

                    if (this.traceProtocol) {
                        StringBuffer traceMessageBuf = new StringBuffer();

                        traceMessageBuf.append(Messages.getString("MysqlIO.57")); //$NON-NLS-1$
                        traceMessageBuf.append(getPacketDumpToLog(
                                packetToSend, packetLen));
                        traceMessageBuf.append(Messages.getString("MysqlIO.58")); //$NON-NLS-1$
                        traceMessageBuf.append(getPacketDumpToLog(packet,
                                originalPacketLen));

                        this.connection.getLog().logTrace(traceMessageBuf.toString());
                    }
                } else {
                    packetToSend.writeLongInt(packetLen - HEADER_LENGTH);
                    packetToSend.writeByte(this.packetSequence);

                    if (this.traceProtocol) {
                        StringBuffer traceMessageBuf = new StringBuffer();

                        traceMessageBuf.append(Messages.getString("MysqlIO.59")); //$NON-NLS-1$
                        traceMessageBuf.append(packetToSend.dump(packetLen));

                        this.connection.getLog().logTrace(traceMessageBuf.toString());
                    }
                }

                
                this.mysqlOutput.write(packetToSend.getByteBuffer(), 0,
                		packetLen);
                this.mysqlOutput.flush();   
            }

            if (this.enablePacketDebug) {
                enqueuePacketForDebugging(true, false, packetLen + 5,
                    this.packetHeaderBuf, packet);
            }

            // 
            // Don't hold on to large packets
            //
            if (packet == this.sharedSendPacket) {
                reclaimLargeSharedSendPacket();
            }
        } catch (IOException ioEx) {
            throw new CommunicationsException(this.connection,
                this.lastPacketSentTimeMs, ioEx);
        }
    }

    /**
     * Reads and sends a file to the server for LOAD DATA LOCAL INFILE
     *
     * @param callingStatement DOCUMENT ME!
     * @param fileName the file name to send.
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    private final ResultSet sendFileToServer(Statement callingStatement,
        String fileName) throws SQLException {
    	
        Buffer filePacket = (this.loadFileBufRef == null) ? null
                                                          : (Buffer) (this.loadFileBufRef.get());

        int bigPacketLength = Math.min(this.connection.getMaxAllowedPacket() -
                (HEADER_LENGTH * 3),
                alignPacketSize(this.connection.getMaxAllowedPacket() - 16, 4096) -
                (HEADER_LENGTH * 3));
        
        int oneMeg = 1024 * 1024;
        
        int smallerPacketSizeAligned = Math.min(oneMeg - (HEADER_LENGTH * 3), 
        		alignPacketSize(oneMeg - 16, 4096) - (HEADER_LENGTH * 3));
        
        int packetLength = Math.min(smallerPacketSizeAligned, bigPacketLength);

        if (filePacket == null) {
        	try {
        		filePacket = new Buffer((packetLength + HEADER_LENGTH));
        		this.loadFileBufRef = new SoftReference(filePacket);
        	} catch (OutOfMemoryError oom) {
        		throw SQLError.createSQLException("Could not allocate packet of " + packetLength 
        				+ " bytes required for LOAD DATA LOCAL INFILE operation." 
						+ " Try increasing max heap allocation for JVM or decreasing server variable "
						+ "'max_allowed_packet'", SQLError.SQL_STATE_MEMORY_ALLOCATION_FAILURE);
				
        	}
        }

        filePacket.clear();
        send(filePacket, 0);

        byte[] fileBuf = new byte[packetLength];

        BufferedInputStream fileIn = null;

        try {
        	if (!this.connection.getAllowLoadLocalInfile()) {
        		throw SQLError.createSQLException(
        				Messages.getString("MysqlIO.LoadDataLocalNotAllowed"), 
        				SQLError.SQL_STATE_GENERAL_ERROR);
        	}
        	
            if (!this.connection.getAllowUrlInLocalInfile()) {
                fileIn = new BufferedInputStream(new FileInputStream(fileName));
            } else {
                // First look for ':'
                if (fileName.indexOf(":") != -1) {
                    try {
                        URL urlFromFileName = new URL(fileName);
                        fileIn = new BufferedInputStream(urlFromFileName.openStream());
                    } catch (MalformedURLException badUrlEx) {
                        // we fall back to trying this as a file input stream
                        fileIn = new BufferedInputStream(new FileInputStream(
                                    fileName));
                    }
                } else {
                    fileIn = new BufferedInputStream(new FileInputStream(
                                fileName));
                }
            }

            int bytesRead = 0;

            while ((bytesRead = fileIn.read(fileBuf)) != -1) {
                filePacket.clear();
                filePacket.writeBytesNoNull(fileBuf, 0, bytesRead);
                send(filePacket, filePacket.getPosition());
            }
        } catch (IOException ioEx) {
            StringBuffer messageBuf = new StringBuffer(Messages.getString(
                        "MysqlIO.60")); //$NON-NLS-1$

            if (!this.connection.getParanoid()) {
                messageBuf.append("'"); //$NON-NLS-1$

                if (fileName != null) {
                    messageBuf.append(fileName);
                }

                messageBuf.append("'"); //$NON-NLS-1$
            }

            messageBuf.append(Messages.getString("MysqlIO.63")); //$NON-NLS-1$

            if (!this.connection.getParanoid()) {
                messageBuf.append(Messages.getString("MysqlIO.64")); //$NON-NLS-1$
                messageBuf.append(Util.stackTraceToString(ioEx));
            }

            throw SQLError.createSQLException(messageBuf.toString(),
                SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
        } finally {
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (Exception ex) {
                    throw SQLError.createSQLException(Messages.getString("MysqlIO.65"), //$NON-NLS-1$
                        SQLError.SQL_STATE_GENERAL_ERROR);
                }

                fileIn = null;
            } else {
                // file open failed, but server needs one packet
                filePacket.clear();
                send(filePacket, filePacket.getPosition());
                checkErrorPacket(); // to clear response off of queue
            }
        }

        // send empty packet to mark EOF
        filePacket.clear();
        send(filePacket, filePacket.getPosition());

        Buffer resultPacket = checkErrorPacket();

        return buildResultSetWithUpdates(callingStatement, resultPacket);
    }

    /**
     * Checks for errors in the reply packet, and if none, returns the reply
     * packet, ready for reading
     *
     * @param command the command being issued (if used)
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException if an error packet was received
     * @throws CommunicationsException DOCUMENT ME!
     */
    private Buffer checkErrorPacket(int command) throws SQLException {
        int statusCode = 0;
        Buffer resultPacket = null;
        this.serverStatus = 0;

        try {
            // Check return value, if we get a java.io.EOFException,
            // the server has gone away. We'll pass it on up the
            // exception chain and let someone higher up decide
            // what to do (barf, reconnect, etc).
            resultPacket = reuseAndReadPacket(this.reusablePacket);
            statusCode = resultPacket.readByte();
        } catch (SQLException sqlEx) {
            // Don't wrap SQL Exceptions
            throw sqlEx;
        } catch (Exception fallThru) {
            throw new CommunicationsException(this.connection,
                this.lastPacketSentTimeMs, fallThru);
        }

        // Error handling
        if (statusCode == (byte) 0xff) {
            String serverErrorMessage;
            int errno = 2000;

            if (this.protocolVersion > 9) {
                errno = resultPacket.readInt();

                String xOpen = null;

                serverErrorMessage = 
                	resultPacket.readString(this.connection.getErrorMessageEncoding());

                if (serverErrorMessage.startsWith("#")) { //$NON-NLS-1$

                    // we have an SQLState
                    if (serverErrorMessage.length() > 6) {
                        xOpen = serverErrorMessage.substring(1, 6);
                        serverErrorMessage = serverErrorMessage.substring(6);

                        if (xOpen.equals("HY000")) { //$NON-NLS-1$
                            xOpen = SQLError.mysqlToSqlState(errno,
                                    this.connection.getUseSqlStateCodes());
                        }
                    } else {
                        xOpen = SQLError.mysqlToSqlState(errno,
                                this.connection.getUseSqlStateCodes());
                    }
                } else {
                    xOpen = SQLError.mysqlToSqlState(errno,
                            this.connection.getUseSqlStateCodes());
                }

                clearInputStream();

                StringBuffer errorBuf = new StringBuffer();

                String xOpenErrorMessage = SQLError.get(xOpen);

                if (!this.connection.getUseOnlyServerErrorMessages()) {
                    if (xOpenErrorMessage != null) {
                        errorBuf.append(xOpenErrorMessage);
                        errorBuf.append(Messages.getString("MysqlIO.68")); //$NON-NLS-1$
                    }
                }

                errorBuf.append(serverErrorMessage);

                if (!this.connection.getUseOnlyServerErrorMessages()) {
                    if (xOpenErrorMessage != null) {
                        errorBuf.append("\""); //$NON-NLS-1$
                    }
                }
                
                appendInnodbStatusInformation(xOpen, errorBuf);
                
                if (xOpen != null && xOpen.startsWith("22")) {
                	throw new MysqlDataTruncation(errorBuf.toString(), 0, true, false, 0, 0); 
                } else {
                	throw SQLError.createSQLException(errorBuf.toString(), xOpen, errno);
                }
            }

            serverErrorMessage = resultPacket.readString(
            		this.connection.getErrorMessageEncoding());
            clearInputStream();

            if (serverErrorMessage.indexOf(Messages.getString("MysqlIO.70")) != -1) { //$NON-NLS-1$
                throw SQLError.createSQLException(SQLError.get(
                        SQLError.SQL_STATE_COLUMN_NOT_FOUND) +
                    ", " //$NON-NLS-1$
                     +serverErrorMessage, SQLError.SQL_STATE_COLUMN_NOT_FOUND,
                    -1);
            }

            StringBuffer errorBuf = new StringBuffer(Messages.getString(
                        "MysqlIO.72")); //$NON-NLS-1$
            errorBuf.append(serverErrorMessage);
            errorBuf.append("\""); //$NON-NLS-1$
            
            throw SQLError.createSQLException(SQLError.get(
                    SQLError.SQL_STATE_GENERAL_ERROR) + ", " //$NON-NLS-1$
                 +errorBuf.toString(), SQLError.SQL_STATE_GENERAL_ERROR, -1);
        }

        return resultPacket;
    }

	private void appendInnodbStatusInformation(String xOpen,
			StringBuffer errorBuf) throws SQLException {
		if (this.connection.getIncludeInnodbStatusInDeadlockExceptions()
				&& xOpen != null
				&& (xOpen.startsWith("40") || xOpen.startsWith("41"))
				&& this.streamingData == null) {
			ResultSet rs = null;

			try {
				rs = sqlQueryDirect(null, "SHOW ENGINE INNODB STATUS",
						this.connection.getEncoding(), null, -1,
						this.connection, ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY, false, this.connection
								.getCatalog(), true);
				if (rs.next()) {
					errorBuf.append("\n\n");
					errorBuf.append(rs.getString(1));
				} else {
					errorBuf.append(Messages
							.getString("MysqlIO.NoInnoDBStatusFound"));
				}
			} catch (Exception ex) {
				errorBuf.append(Messages
						.getString("MysqlIO.InnoDBStatusFailed"));
				errorBuf.append("\n\n");
				errorBuf.append(Util.stackTraceToString(ex));
			} finally {
				if (rs != null) {
					rs.close();
				}
			}
		}
	}

    /**
	 * Sends a large packet to the server as a series of smaller packets
	 * 
	 * @param packet
	 *            DOCUMENT ME!
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 * @throws CommunicationsException
	 *             DOCUMENT ME!
	 */
    private final void sendSplitPackets(Buffer packet)
        throws SQLException {
        try {
            //
            // Big packets are handled by splitting them in packets of MAX_THREE_BYTES
            // length. The last packet is always a packet that is < MAX_THREE_BYTES.
            // (The last packet may even have a length of 0)
            //
            //
            // NB: Guarded by execSQL. If the driver changes architecture, this
            // will need to be synchronized in some other way
            //
            Buffer headerPacket = (this.splitBufRef == null) ? null
                                                             : (Buffer) (this.splitBufRef.get());

            //
            // Store this packet in a soft reference...It can be re-used if not GC'd (so clients
            // that use it frequently won't have to re-alloc the 16M buffer), but we don't
            // penalize infrequent users of large packets by keeping 16M allocated all of the time
            //
            if (headerPacket == null) {
                headerPacket = new Buffer((this.maxThreeBytes +
                        HEADER_LENGTH));
                this.splitBufRef = new SoftReference(headerPacket);
            }

            int len = packet.getPosition();
            int splitSize = this.maxThreeBytes;
            int originalPacketPos = HEADER_LENGTH;
            byte[] origPacketBytes = packet.getByteBuffer();
            byte[] headerPacketBytes = headerPacket.getByteBuffer();

            while (len >= this.maxThreeBytes) {
                this.packetSequence++;

                headerPacket.setPosition(0);
                headerPacket.writeLongInt(splitSize);

                headerPacket.writeByte(this.packetSequence);
                System.arraycopy(origPacketBytes, originalPacketPos,
                    headerPacketBytes, 4, splitSize);

                int packetLen = splitSize + HEADER_LENGTH;

                //
                // Swap a compressed packet in, if we're using
                // compression...
                //
                if (!this.useCompression) {
                    this.mysqlOutput.write(headerPacketBytes, 0,
                        splitSize + HEADER_LENGTH);
                    this.mysqlOutput.flush();
                } else {
                    Buffer packetToSend;

                    headerPacket.setPosition(0);
                    packetToSend = compressPacket(headerPacket, HEADER_LENGTH,
                            splitSize, HEADER_LENGTH);
                    packetLen = packetToSend.getPosition();

                    this.mysqlOutput.write(packetToSend.getByteBuffer(), 0,
                        packetLen);
                    this.mysqlOutput.flush();
                }

                originalPacketPos += splitSize;
                len -= splitSize;
            }

            //
            // Write last packet
            //
            headerPacket.clear();
            headerPacket.setPosition(0);
            headerPacket.writeLongInt(len - HEADER_LENGTH);
            this.packetSequence++;
            headerPacket.writeByte(this.packetSequence);

            if (len != 0) {
                System.arraycopy(origPacketBytes, originalPacketPos,
                    headerPacketBytes, 4, len - HEADER_LENGTH);
            }

            int packetLen = len - HEADER_LENGTH;

            //
            // Swap a compressed packet in, if we're using
            // compression...
            //
            if (!this.useCompression) {
                this.mysqlOutput.write(headerPacket.getByteBuffer(), 0, len);
                this.mysqlOutput.flush();
            } else {
                Buffer packetToSend;

                headerPacket.setPosition(0);
                packetToSend = compressPacket(headerPacket, HEADER_LENGTH,
                        packetLen, HEADER_LENGTH);
                packetLen = packetToSend.getPosition();

                this.mysqlOutput.write(packetToSend.getByteBuffer(), 0,
                    packetLen);
                this.mysqlOutput.flush();
            }
        } catch (IOException ioEx) {
            throw new CommunicationsException(this.connection,
                this.lastPacketSentTimeMs, ioEx);
        }
    }

    private void reclaimLargeSharedSendPacket() {
        if ((this.sharedSendPacket != null) &&
                (this.sharedSendPacket.getCapacity() > 1048576)) {
            this.sharedSendPacket = new Buffer(INITIAL_PACKET_SIZE);
        }
    }

    boolean hadWarnings() {
    	return this.hadWarnings;
    }
    
    void scanForAndThrowDataTruncation() throws SQLException {
        if ((this.streamingData == null) && versionMeetsMinimum(4, 1, 0) &&
                this.connection.getJdbcCompliantTruncation() && this.warningCount > 0) {
        		SQLError.convertShowWarningsToSQLWarnings(this.connection,
        				this.warningCount, true);
        }
    }

    /**
     * Secure authentication for 4.1 and newer servers.
     *
     * @param packet DOCUMENT ME!
     * @param packLength
     * @param user
     * @param password
     * @param database DOCUMENT ME!
     * @param writeClientParams
     *
     * @throws SQLException
     */
    private void secureAuth(Buffer packet, int packLength, String user,
        String password, String database, boolean writeClientParams)
        throws SQLException {
        // Passwords can be 16 chars long
        if (packet == null) {
            packet = new Buffer(packLength);
        }

        if (writeClientParams) {
            if (this.use41Extensions) {
                if (versionMeetsMinimum(4, 1, 1)) {
                    packet.writeLong(this.clientParam);
                    packet.writeLong(this.maxThreeBytes);

                    // charset, JDBC will connect as 'latin1',
                    // and use 'SET NAMES' to change to the desired
                    // charset after the connection is established.
                    packet.writeByte((byte) 8);

                    // Set of bytes reserved for future use.
                    packet.writeBytesNoNull(new byte[23]);
                } else {
                    packet.writeLong(this.clientParam);
                    packet.writeLong(this.maxThreeBytes);
                }
            } else {
                packet.writeInt((int) this.clientParam);
                packet.writeLongInt(this.maxThreeBytes);
            }
        }

        // User/Password data
        packet.writeString(user, "Cp1252", this.connection);

        if (password.length() != 0) {
            /* Prepare false scramble  */
            packet.writeString(FALSE_SCRAMBLE, "Cp1252", this.connection);
        } else {
            /* For empty password*/
            packet.writeString("", "Cp1252", this.connection); //$NON-NLS-1$
        }

        if (this.useConnectWithDb) {
            packet.writeString(database, "Cp1252", this.connection);
        }

        send(packet, packet.getPosition());

        //
        // Don't continue stages if password is empty
        //
        if (password.length() > 0) {
            Buffer b = readPacket();

            b.setPosition(0);

            byte[] replyAsBytes = b.getByteBuffer();

            if ((replyAsBytes.length == 25) && (replyAsBytes[0] != 0)) {
                // Old passwords will have '*' at the first byte of hash */
                if (replyAsBytes[0] != '*') {
                    try {
                        /* Build full password hash as it is required to decode scramble */
                        byte[] buff = Security.passwordHashStage1(password);

                        /* Store copy as we'll need it later */
                        byte[] passwordHash = new byte[buff.length];
                        System.arraycopy(buff, 0, passwordHash, 0, buff.length);

                        /* Finally hash complete password using hash we got from server */
                        passwordHash = Security.passwordHashStage2(passwordHash,
                                replyAsBytes);

                        byte[] packetDataAfterSalt = new byte[replyAsBytes.length -
                            5];

                        System.arraycopy(replyAsBytes, 4, packetDataAfterSalt,
                            0, replyAsBytes.length - 5);

                        byte[] mysqlScrambleBuff = new byte[20];

                        /* Decypt and store scramble 4 = hash for stage2 */
                        Security.passwordCrypt(packetDataAfterSalt,
                            mysqlScrambleBuff, passwordHash, 20);

                        /* Encode scramble with password. Recycle buffer */
                        Security.passwordCrypt(mysqlScrambleBuff, buff, buff, 20);

                        Buffer packet2 = new Buffer(25);
                        packet2.writeBytesNoNull(buff);

                        this.packetSequence++;

                        send(packet2, 24);
                    } catch (NoSuchAlgorithmException nse) {
                        throw SQLError.createSQLException(Messages.getString("MysqlIO.91") //$NON-NLS-1$
                             +Messages.getString("MysqlIO.92"), //$NON-NLS-1$
                            SQLError.SQL_STATE_GENERAL_ERROR);
                    }
                } else {
                    try {
                        /* Create password to decode scramble */
                        byte[] passwordHash = Security.createKeyFromOldPassword(password);

                        /* Decypt and store scramble 4 = hash for stage2 */
                        byte[] netReadPos4 = new byte[replyAsBytes.length - 5];

                        System.arraycopy(replyAsBytes, 4, netReadPos4, 0,
                            replyAsBytes.length - 5);

                        byte[] mysqlScrambleBuff = new byte[20];

                        /* Decypt and store scramble 4 = hash for stage2 */
                        Security.passwordCrypt(netReadPos4, mysqlScrambleBuff,
                            passwordHash, 20);

                        /* Finally scramble decoded scramble with password */
                        String scrambledPassword = Util.scramble(new String(
                                    mysqlScrambleBuff), password);

                        Buffer packet2 = new Buffer(packLength);
                        packet2.writeString(scrambledPassword, "Cp1252", this.connection);
                        this.packetSequence++;

                        send(packet2, 24);
                    } catch (NoSuchAlgorithmException nse) {
                        throw SQLError.createSQLException(Messages.getString("MysqlIO.93") //$NON-NLS-1$
                             +Messages.getString("MysqlIO.94"), //$NON-NLS-1$
                            SQLError.SQL_STATE_GENERAL_ERROR);
                    }
                }
            }
        }
    }

    /**
     * Secure authentication for 4.1.1 and newer servers.
     *
     * @param packet DOCUMENT ME!
     * @param packLength
     * @param user
     * @param password
     * @param database DOCUMENT ME!
     * @param writeClientParams
     *
     * @throws SQLException
     */
    void secureAuth411(Buffer packet, int packLength, String user,
        String password, String database, boolean writeClientParams)
        throws SQLException {
        //	SERVER:  public_seed=create_random_string()
        //			 send(public_seed)
        //
        //	CLIENT:  recv(public_seed)
        //			 hash_stage1=sha1("password")
        //			 hash_stage2=sha1(hash_stage1)
        //			 reply=xor(hash_stage1, sha1(public_seed,hash_stage2)
        //
        //			 // this three steps are done in scramble()
        //
        //			 send(reply)
        //
        //
        //	SERVER:  recv(reply)
        //			 hash_stage1=xor(reply, sha1(public_seed,hash_stage2))
        //			 candidate_hash2=sha1(hash_stage1)
        //			 check(candidate_hash2==hash_stage2)
        // Passwords can be 16 chars long
        if (packet == null) {
            packet = new Buffer(packLength);
        }

        if (writeClientParams) {
            if (this.use41Extensions) {
                if (versionMeetsMinimum(4, 1, 1)) {
                    packet.writeLong(this.clientParam);
                    packet.writeLong(this.maxThreeBytes);

                    // charset, JDBC will connect as 'latin1',
                    // and use 'SET NAMES' to change to the desired
                    // charset after the connection is established.
                    packet.writeByte((byte) 8);

                    // Set of bytes reserved for future use.
                    packet.writeBytesNoNull(new byte[23]);
                } else {
                    packet.writeLong(this.clientParam);
                    packet.writeLong(this.maxThreeBytes);
                }
            } else {
                packet.writeInt((int) this.clientParam);
                packet.writeLongInt(this.maxThreeBytes);
            }
        }

        // User/Password data
        packet.writeString(user);

        if (password.length() != 0) {
            packet.writeByte((byte) 0x14);

            try {
                packet.writeBytesNoNull(Security.scramble411(password, this.seed));
            } catch (NoSuchAlgorithmException nse) {
                throw SQLError.createSQLException(Messages.getString("MysqlIO.95") //$NON-NLS-1$
                     +Messages.getString("MysqlIO.96"), //$NON-NLS-1$
                    SQLError.SQL_STATE_GENERAL_ERROR);
            }
        } else {
            /* For empty password*/
            packet.writeByte((byte) 0);
        }

        if (this.useConnectWithDb) {
            packet.writeString(database);
        }

        send(packet, packet.getPosition());

        byte savePacketSequence = this.packetSequence++;

        Buffer reply = checkErrorPacket();

        if (reply.isLastDataPacket()) {
            /*
                  By sending this very specific reply server asks us to send scrambled
                  password in old format. The reply contains scramble_323.
            */
            this.packetSequence = ++savePacketSequence;
            packet.clear();

            String seed323 = this.seed.substring(0, 8);
            packet.writeString(Util.newCrypt(password, seed323));
            send(packet, packet.getPosition());

            /* Read what server thinks about out new auth message report */
            checkErrorPacket();
        }
    }

    /**
     * Un-packs binary-encoded result set data for one row
     *
     * @param fields
     * @param binaryData
     * @param resultSetConcurrency DOCUMENT ME!
     *
     * @return byte[][]
     *
     * @throws SQLException DOCUMENT ME!
     */
    private final Object[] unpackBinaryResultSetRow(Field[] fields,
        Buffer binaryData, int resultSetConcurrency) throws SQLException {
        int numFields = fields.length;

        Object[] unpackedRowData = new Object[numFields];

        //
        // Unpack the null bitmask, first
        //

        /* Reserve place for null-marker bytes */
        int nullCount = (numFields + 9) / 8;

        byte[] nullBitMask = new byte[nullCount];

        for (int i = 0; i < nullCount; i++) {
            nullBitMask[i] = binaryData.readByte();
        }

        int nullMaskPos = 0;
        int bit = 4; // first two bits are reserved for future use
       
        //
        // TODO: Benchmark if moving check for updatable result
        //       sets out of loop is worthwhile?
        //
        
        for (int i = 0; i < numFields; i++) {
            if ((nullBitMask[nullMaskPos] & bit) != 0) {
                unpackedRowData[i] = null;
            } else {
            	if (resultSetConcurrency != ResultSet.CONCUR_UPDATABLE) {
            		extractNativeEncodedColumn(binaryData, fields, i, 
            				unpackedRowData);
            	} else {
            		unpackNativeEncodedColumn(binaryData, fields, i, 
            				unpackedRowData);
            	}   
            }
            
        	if (((bit <<= 1) & 255) == 0) {
        		bit = 1; /* To next byte */

        		nullMaskPos++;
        	}
        }

        return unpackedRowData;
    }

        
    private final void extractNativeEncodedColumn(Buffer binaryData, 
    		Field[] fields, int columnIndex, Object[] unpackedRowData) throws SQLException {
    	Field curField = fields[columnIndex];
    	
    	switch (curField.getMysqlType()) {
    	case MysqlDefs.FIELD_TYPE_NULL:
    		break; // for dummy binds
    	
    	case MysqlDefs.FIELD_TYPE_TINY:

    		unpackedRowData[columnIndex] = new byte[] {binaryData.readByte()};
    		break;
    	
    	case MysqlDefs.FIELD_TYPE_SHORT:
    	case MysqlDefs.FIELD_TYPE_YEAR:
    		
    		unpackedRowData[columnIndex] = binaryData.getBytes(2);
    		break;
    	case MysqlDefs.FIELD_TYPE_LONG:
    	case MysqlDefs.FIELD_TYPE_INT24:
    		
    		unpackedRowData[columnIndex] = binaryData.getBytes(4);
    		break;
    	case MysqlDefs.FIELD_TYPE_LONGLONG:

    		unpackedRowData[columnIndex] = binaryData.getBytes(8);
    		break;
    	case MysqlDefs.FIELD_TYPE_FLOAT:
    		
    		unpackedRowData[columnIndex] = binaryData.getBytes(4);
    		break;   	
    	case MysqlDefs.FIELD_TYPE_DOUBLE:
    		
    		unpackedRowData[columnIndex] = binaryData.getBytes(8);
    		break;
    	case MysqlDefs.FIELD_TYPE_TIME:
    		
    		int length = (int) binaryData.readFieldLength();
    	
    		unpackedRowData[columnIndex] = binaryData.getBytes(length);

    		break;
    	case MysqlDefs.FIELD_TYPE_DATE:
    		
    		length = (int) binaryData.readFieldLength();
    	
    		unpackedRowData[columnIndex] = binaryData.getBytes(length);

    		break;
    	case MysqlDefs.FIELD_TYPE_DATETIME:
    	case MysqlDefs.FIELD_TYPE_TIMESTAMP:
    		length = (int) binaryData.readFieldLength();
    	
    		unpackedRowData[columnIndex] = binaryData.getBytes(length);
    		break;
    	case MysqlDefs.FIELD_TYPE_TINY_BLOB:
    	case MysqlDefs.FIELD_TYPE_MEDIUM_BLOB:
    	case MysqlDefs.FIELD_TYPE_LONG_BLOB:
    	case MysqlDefs.FIELD_TYPE_BLOB:
    	case MysqlDefs.FIELD_TYPE_VAR_STRING:
    	case MysqlDefs.FIELD_TYPE_VARCHAR:
    	case MysqlDefs.FIELD_TYPE_STRING:
    	case MysqlDefs.FIELD_TYPE_DECIMAL:
    	case MysqlDefs.FIELD_TYPE_NEW_DECIMAL:
    	case MysqlDefs.FIELD_TYPE_GEOMETRY:
    		unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
    	
    		break;
    	case MysqlDefs.FIELD_TYPE_BIT:
    		unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
    		
    		break;
    	default:
    		throw SQLError.createSQLException(Messages.getString("MysqlIO.97") //$NON-NLS-1$
    				+curField.getMysqlType() +
					Messages.getString("MysqlIO.98") + columnIndex +
					Messages.getString("MysqlIO.99") //$NON-NLS-1$ //$NON-NLS-2$
					+ fields.length + Messages.getString("MysqlIO.100"), //$NON-NLS-1$
					SQLError.SQL_STATE_GENERAL_ERROR);
    	}
    }

    private final void unpackNativeEncodedColumn(Buffer binaryData, 
    		Field[] fields, int columnIndex, Object[] unpackedRowData) 
    throws SQLException {
    	Field curField = fields[columnIndex];
    	
    	switch (curField.getMysqlType()) {
    	case MysqlDefs.FIELD_TYPE_NULL:
    		break; // for dummy binds
    		
    	case MysqlDefs.FIELD_TYPE_TINY:
    		
    		byte tinyVal = binaryData.readByte();
    		
    		if (!curField.isUnsigned()) {			
    			unpackedRowData[columnIndex] = String.valueOf(tinyVal)
    			.getBytes();  			
    		} else {
    			short unsignedTinyVal = (short) (tinyVal & 0xff);
 
    			unpackedRowData[columnIndex] = String.valueOf(unsignedTinyVal)
    			.getBytes();   			
    		}
    		
    		break;
    		
    	case MysqlDefs.FIELD_TYPE_SHORT:
    	case MysqlDefs.FIELD_TYPE_YEAR:
    		
    		short shortVal = (short) binaryData.readInt();
    		
    		if (!curField.isUnsigned()) {
    			unpackedRowData[columnIndex] = String.valueOf(shortVal)
    			.getBytes();	
    		} else {
    			int unsignedShortVal = shortVal & 0xffff;

    			unpackedRowData[columnIndex] = String.valueOf(unsignedShortVal)
    			.getBytes();	
    		}
    		
    		break;
    		
    	case MysqlDefs.FIELD_TYPE_LONG:
    	case MysqlDefs.FIELD_TYPE_INT24:
    		
    		int intVal = (int) binaryData.readLong();
    		
    		if (!curField.isUnsigned()) {
    			unpackedRowData[columnIndex] = String.valueOf(intVal)
    			.getBytes();
    		} else {
    			long longVal = intVal & 0xffffffffL;

    			unpackedRowData[columnIndex] = String.valueOf(longVal)
    			.getBytes();	
    		}
    		
    		break;
    		
    	case MysqlDefs.FIELD_TYPE_LONGLONG:
    		
    		long longVal = binaryData.readLongLong();
    		
    		if (!curField.isUnsigned()) {
    			unpackedRowData[columnIndex] = String.valueOf(longVal)
    			.getBytes();
    		} else {
    			BigInteger asBigInteger = ResultSet.convertLongToUlong(longVal);

    			unpackedRowData[columnIndex] = asBigInteger.toString()
    			.getBytes();	
    		}
    		
    		break;
    		
    	case MysqlDefs.FIELD_TYPE_FLOAT:
    		
    		float floatVal = Float.intBitsToFloat(binaryData.readIntAsLong());
    		
    		unpackedRowData[columnIndex] = String.valueOf(floatVal).getBytes();

    		break;
    		
    	case MysqlDefs.FIELD_TYPE_DOUBLE:
    		
    		double doubleVal = Double.longBitsToDouble(binaryData.readLongLong());

    		unpackedRowData[columnIndex] = String.valueOf(doubleVal).getBytes();

    		break;
    		
    	case MysqlDefs.FIELD_TYPE_TIME:
    		
    		int length = (int) binaryData.readFieldLength();
    		
    		int hour = 0;
    		int minute = 0;
    		int seconds = 0;
    		
    		if (length != 0) {
    			binaryData.readByte(); // skip tm->neg
    			binaryData.readLong(); // skip daysPart
    			hour = binaryData.readByte();
    			minute = binaryData.readByte();
    			seconds = binaryData.readByte();
    			
    			if (length > 8) {
    				binaryData.readLong(); // ignore 'secondsPart'
    			}
    		}
    		
    		
    		byte[] timeAsBytes = new byte[8];
    		
    		timeAsBytes[0] = (byte) Character.forDigit(hour / 10, 10);
    		timeAsBytes[1] = (byte) Character.forDigit(hour % 10, 10);
    		
    		timeAsBytes[2] = (byte) ':';
    		
    		timeAsBytes[3] = (byte) Character.forDigit(minute / 10,
    				10);
    		timeAsBytes[4] = (byte) Character.forDigit(minute % 10,
    				10);
    		
    		timeAsBytes[5] = (byte) ':';
    		
    		timeAsBytes[6] = (byte) Character.forDigit(seconds / 10,
    				10);
    		timeAsBytes[7] = (byte) Character.forDigit(seconds % 10,
    				10);
    		
    		unpackedRowData[columnIndex] = timeAsBytes;
    		
    		
    		break;
    		
    	case MysqlDefs.FIELD_TYPE_DATE:
    		length = (int) binaryData.readFieldLength();
    		
    		int year = 0;
    		int month = 0;
    		int day = 0;
    		
    		hour = 0;
    		minute = 0;
    		seconds = 0;
    		
    		if (length != 0) {
    			year = binaryData.readInt();
    			month = binaryData.readByte();
    			day = binaryData.readByte();
    		}
    		
    		if ((year == 0) && (month == 0) && (day == 0)) {
    			if (ConnectionProperties.ZERO_DATETIME_BEHAVIOR_CONVERT_TO_NULL.equals(
    					this.connection.getZeroDateTimeBehavior())) {
    				unpackedRowData[columnIndex] = null;
    				
    				break;
    			} else if (ConnectionProperties.ZERO_DATETIME_BEHAVIOR_EXCEPTION.equals(
    					this.connection.getZeroDateTimeBehavior())) {
    				throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Date",
    						SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
    			}
    			
    			year = 1;
    			month = 1;
    			day = 1;
    		}
    		
    		
    		byte[] dateAsBytes = new byte[10];
    		
    		dateAsBytes[0] = (byte) Character.forDigit(year / 1000,
    				10);
    		
    		int after1000 = year % 1000;
    		
    		dateAsBytes[1] = (byte) Character.forDigit(after1000 / 100,
    				10);
    		
    		int after100 = after1000 % 100;
    		
    		dateAsBytes[2] = (byte) Character.forDigit(after100 / 10,
    				10);
    		dateAsBytes[3] = (byte) Character.forDigit(after100 % 10,
    				10);
    		
    		dateAsBytes[4] = (byte) '-';
    		
    		dateAsBytes[5] = (byte) Character.forDigit(month / 10,
    				10);
    		dateAsBytes[6] = (byte) Character.forDigit(month % 10,
    				10);
    		
    		dateAsBytes[7] = (byte) '-';
    		
    		dateAsBytes[8] = (byte) Character.forDigit(day / 10, 10);
    		dateAsBytes[9] = (byte) Character.forDigit(day % 10, 10);
    		
    		unpackedRowData[columnIndex] = dateAsBytes;
    		
    		
    		break;
    		
    	case MysqlDefs.FIELD_TYPE_DATETIME:
    	case MysqlDefs.FIELD_TYPE_TIMESTAMP:
    		length = (int) binaryData.readFieldLength();
    		
    		year = 0;
    		month = 0;
    		day = 0;
    		
    		hour = 0;
    		minute = 0;
    		seconds = 0;
    		
    		int nanos = 0;
    		
    		if (length != 0) {
    			year = binaryData.readInt();
    			month = binaryData.readByte();
    			day = binaryData.readByte();
    			
    			if (length > 4) {
    				hour = binaryData.readByte();
    				minute = binaryData.readByte();
    				seconds = binaryData.readByte();
    			}
    			
    			//if (length > 7) {
    			//    nanos = (int)binaryData.readLong();
    			//}
    		}
    		
    		if ((year == 0) && (month == 0) && (day == 0)) {
    			if (ConnectionProperties.ZERO_DATETIME_BEHAVIOR_CONVERT_TO_NULL.equals(
    					this.connection.getZeroDateTimeBehavior())) {
    				unpackedRowData[columnIndex] = null;
    				
    				break;
    			} else if (ConnectionProperties.ZERO_DATETIME_BEHAVIOR_EXCEPTION.equals(
    					this.connection.getZeroDateTimeBehavior())) {
    				throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp",
    						SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
    			}
    			
    			year = 1;
    			month = 1;
    			day = 1;
    		}
    		
    		
    		int stringLength = 19;
    		
    		byte[] nanosAsBytes = Integer.toString(nanos).getBytes();
    		
    		stringLength += (1 + nanosAsBytes.length); // '.' + # of digits
    		
    		byte[] datetimeAsBytes = new byte[stringLength];
    		
    		datetimeAsBytes[0] = (byte) Character.forDigit(year / 1000,
    				10);
    		
    		after1000 = year % 1000;
    		
    		datetimeAsBytes[1] = (byte) Character.forDigit(after1000 / 100,
    				10);
    		
    		after100 = after1000 % 100;
    		
    		datetimeAsBytes[2] = (byte) Character.forDigit(after100 / 10,
    				10);
    		datetimeAsBytes[3] = (byte) Character.forDigit(after100 % 10,
    				10);
    		
    		datetimeAsBytes[4] = (byte) '-';
    		
    		datetimeAsBytes[5] = (byte) Character.forDigit(month / 10,
    				10);
    		datetimeAsBytes[6] = (byte) Character.forDigit(month % 10,
    				10);
    		
    		datetimeAsBytes[7] = (byte) '-';
    		
    		datetimeAsBytes[8] = (byte) Character.forDigit(day / 10,
    				10);
    		datetimeAsBytes[9] = (byte) Character.forDigit(day % 10,
    				10);
    		
    		datetimeAsBytes[10] = (byte) ' ';
    		
    		datetimeAsBytes[11] = (byte) Character.forDigit(hour / 10,
    				10);
    		datetimeAsBytes[12] = (byte) Character.forDigit(hour % 10,
    				10);
    		
    		datetimeAsBytes[13] = (byte) ':';
    		
    		datetimeAsBytes[14] = (byte) Character.forDigit(minute / 10,
    				10);
    		datetimeAsBytes[15] = (byte) Character.forDigit(minute % 10,
    				10);
    		
    		datetimeAsBytes[16] = (byte) ':';
    		
    		datetimeAsBytes[17] = (byte) Character.forDigit(seconds / 10,
    				10);
    		datetimeAsBytes[18] = (byte) Character.forDigit(seconds % 10,
    				10);
    		
    		datetimeAsBytes[19] = (byte) '.';
    		
    		int nanosOffset = 20;
    		
    		for (int j = 0; j < nanosAsBytes.length; j++) {
    			datetimeAsBytes[nanosOffset + j] = nanosAsBytes[j];
    		}
    		
    		unpackedRowData[columnIndex] = datetimeAsBytes;
    		
    		
    		break;
    		
    	case MysqlDefs.FIELD_TYPE_TINY_BLOB:
    	case MysqlDefs.FIELD_TYPE_MEDIUM_BLOB:
    	case MysqlDefs.FIELD_TYPE_LONG_BLOB:
    	case MysqlDefs.FIELD_TYPE_BLOB:
    	case MysqlDefs.FIELD_TYPE_VAR_STRING:
    	case MysqlDefs.FIELD_TYPE_STRING:
    	case MysqlDefs.FIELD_TYPE_VARCHAR:
    	case MysqlDefs.FIELD_TYPE_DECIMAL:
    	case MysqlDefs.FIELD_TYPE_NEW_DECIMAL:
    	case MysqlDefs.FIELD_TYPE_BIT:
    		unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
    		
    		break;
    		
    	default:
    		throw SQLError.createSQLException(Messages.getString("MysqlIO.97") //$NON-NLS-1$
    				+curField.getMysqlType() +
    				Messages.getString("MysqlIO.98") + columnIndex +
    				Messages.getString("MysqlIO.99") //$NON-NLS-1$ //$NON-NLS-2$
    				+ fields.length + Messages.getString("MysqlIO.100"), //$NON-NLS-1$
    				SQLError.SQL_STATE_GENERAL_ERROR);
    	}
    }
    
    /**
     * Optimization to only use one calendar per-session, or calculate it
     * for each call, depending on user configuration
     */
    private Calendar getCalendarInstanceForSessionOrNew() {
    	if (this.connection.getDynamicCalendars()) {
    		return Calendar.getInstance();
    	} else {
    		return this.sessionCalendar;
    	}
    }
    
    /**
     * Negotiates the SSL communications channel used when connecting
     * to a MySQL server that understands SSL.
     *
     * @param user
     * @param password
     * @param database
     * @param packLength
     * @throws SQLException
     * @throws CommunicationsException
     */
    private void negotiateSSLConnection(String user, String password,
        String database, int packLength)
        throws SQLException, CommunicationsException {
        if (!ExportControlled.enabled()) {
            throw new ConnectionFeatureNotAvailableException(this.connection,
                this.lastPacketSentTimeMs, null);
        }

        boolean doSecureAuth = false;

        if ((this.serverCapabilities & CLIENT_SECURE_CONNECTION) != 0) {
            this.clientParam |= CLIENT_SECURE_CONNECTION;
            doSecureAuth = true;
        }

        this.clientParam |= CLIENT_SSL;

        Buffer packet = new Buffer(packLength);

        if (this.use41Extensions) {
            packet.writeLong(this.clientParam);
        } else {
            packet.writeInt((int) this.clientParam);
        }

        send(packet, packet.getPosition());

        ExportControlled.transformSocketToSSLSocket(this);

        packet.clear();

        if (doSecureAuth) {
            if (versionMeetsMinimum(4, 1, 1)) {
                secureAuth411(null, packLength, user, password, database, true);
            } else {
                secureAuth411(null, packLength, user, password, database, true);
            }
        } else {
            if (this.use41Extensions) {
                packet.writeLong(this.clientParam);
                packet.writeLong(this.maxThreeBytes);
            } else {
                packet.writeInt((int) this.clientParam);
                packet.writeLongInt(this.maxThreeBytes);
            }

            // User/Password data
            packet.writeString(user);

            if (this.protocolVersion > 9) {
                packet.writeString(Util.newCrypt(password, this.seed));
            } else {
                packet.writeString(Util.oldCrypt(password, this.seed));
            }

            if (((this.serverCapabilities & CLIENT_CONNECT_WITH_DB) != 0) &&
                    (database != null) && (database.length() > 0)) {
                packet.writeString(database);
            }

            send(packet, packet.getPosition());
        }
    }

	protected int getServerStatus() {
		return this.serverStatus;
	}

	protected List fetchRowsViaCursor(List fetchedRows, long statementId,
			Field[] columnTypes, int fetchSize) throws SQLException {
		
		if (fetchedRows == null) {
			fetchedRows = new ArrayList(fetchSize);
		} else {
			fetchedRows.clear();
		}
	
		this.sharedSendPacket.clear();
	
		this.sharedSendPacket.writeByte((byte) MysqlDefs.COM_FETCH);
		this.sharedSendPacket.writeLong(statementId);
		this.sharedSendPacket.writeLong(fetchSize);
	
		sendCommand(MysqlDefs.COM_FETCH, null, this.sharedSendPacket, true,
				null);
	
		Object[] row = null;
	
		while ((row = nextRow(columnTypes, columnTypes.length, true,
				ResultSet.CONCUR_READ_ONLY)) != null) {
			fetchedRows.add(row);
		}
	
		return fetchedRows;
	}

	protected long getThreadId() {
		return this.threadId;
	}

	protected boolean useNanosForElapsedTime() {
		return this.useNanosForElapsedTime;
	}

	protected long getSlowQueryThreshold() {
		return this.slowQueryThreshold;
	}

	protected String getQueryTimingUnits() {
		return this.queryTimingUnits;
	}
}
