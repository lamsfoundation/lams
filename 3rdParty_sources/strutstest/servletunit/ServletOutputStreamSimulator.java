package servletunit;

//  StrutsTestCase - a JUnit extension for testing Struts actions
//  within the context of the ActionServlet.
//  Copyright (C) 2002 Deryl Seale
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the Apache Software License as
//  published by the Apache Software Foundation; either version 1.1
//  of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  Apache Software Foundation Licens for more details.
//
//  You may view the full text here: http://www.apache.org/LICENSE.txt

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ServletOutputStreamSimulator extends ServletOutputStream
{
    private OutputStream outStream;
    
    /**
     * Default constructor that sends all output to <code>System.out</code>.
     */
    public ServletOutputStreamSimulator()
    {
        this.outStream = System.out;
    }
    
    /**
     * Constructor that sends all output to given OutputStream.
     * @param out OutputStream to which all output will be sent.
     */
    public ServletOutputStreamSimulator( OutputStream out )
    {
        this.outStream = out;
    }
    
    public void write( int b )
    {
        try
	    {
		outStream.write( b );
	    }
        catch( IOException io )
	    {
		System.err.println("IOException: " + io.getMessage());
		io.printStackTrace();
	    }
    }
}
