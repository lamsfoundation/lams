/* 
**	Scans the local computer ports
**	
**	Coded by C.K Nimmagadda
**	Copyright 2005 (c) LAMS International
**	
*/

import java.net.*;
import java.io.*;
 
public class LocalPortScanner {
 
  public static void main(String[] args) {

	int current = 0;
      for (int i = 0; i < args.length; i++) {
      try {
        ServerSocket server = new ServerSocket(Integer.parseInt(args[i]));
      }
      catch (IOException e) {
        if (current < 2)
	{
		System.out.print("Warning: There is a server already on port " + args[i] + " ");
	}
	else
	{
		System.out.print(args[i] + " ");
	}
        current = current+2;
      }
      }
	System.out.println(".");
	System.exit(current);

  }

}
