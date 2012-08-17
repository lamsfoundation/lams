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
package servletunit.struts;

import java.io.*;
import javax.servlet.ServletException;
/**
 * <p>Title: ExceptionDuringTestError</p>
 * <p>Description: An error indicating an uncaught exception
 * occurred during testing</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Sean Pritchard
 * @version 1.0
 */
public class ExceptionDuringTestError extends Error {

    Throwable rootCause;

    public ExceptionDuringTestError(String message, Throwable rootCause) {
        super(message);
        this.rootCause = rootCause;
    }

    public void printStackTrace(){
        super.printStackTrace();
        System.out.println("------------");
        System.out.println("Root Cause:");
        System.out.println("------------");
        rootCause.printStackTrace();
        if (rootCause instanceof ServletException){
            Throwable root2 = ((ServletException)rootCause).getRootCause();
            if(root2!=null){
                System.out.println("------------");
                System.out.println("Root Cause:");
                System.out.println("------------");
                root2.printStackTrace();
            }
        }
    }

    public void printStackTrace(PrintStream stream){
        super.printStackTrace(stream);
        stream.println("------------");
        stream.println("Root Cause:");
        stream.println("------------");
        rootCause.printStackTrace(stream);
        if (rootCause instanceof ServletException){
            Throwable root2 = ((ServletException)rootCause).getRootCause();
            if(root2!=null){
                stream.println("------------");
                stream.println("Root Cause:");
                stream.println("------------");
                root2.printStackTrace(stream);
            }
        }
    }

    public void printStackTrace(PrintWriter stream){
        super.printStackTrace(stream);
        stream.println("------------");
        stream.println("Root Cause:");
        stream.println("------------");
        rootCause.printStackTrace(stream);
        if (rootCause instanceof ServletException){
            Throwable root2 = ((ServletException)rootCause).getRootCause();
            if(root2!=null){
                stream.println("------------");
                stream.println("Root Cause:");
                stream.println("------------");
                root2.printStackTrace(stream);
            }
        }
    }
}