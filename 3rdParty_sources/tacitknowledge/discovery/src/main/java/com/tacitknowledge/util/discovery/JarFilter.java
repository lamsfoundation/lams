package com.tacitknowledge.util.discovery;

import java.io.File;
import java.io.FileFilter;

/**
 * 
 * 
 * @author  Jun-Dir Liew (jliew@melcoe.mq.edu.au)
 *
 */
class JarFilter implements FileFilter
{
    /**
     * @see java.io.FilenameFilter#accept(File, String)
     */
    public boolean accept(File f)
    {
        if (f.isFile()) {
        	String name = f.getName();
        	if (name.endsWith(".jar")) {
        		return true;
        	}
        }
        return false;
    }
}