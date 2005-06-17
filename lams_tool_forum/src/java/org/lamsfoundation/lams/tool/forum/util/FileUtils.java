package org.lamsfoundation.lams.tool.forum.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 8/06/2005
 * Time: 15:42:19
 * To change this template use File | Settings | File Templates.
 */
public class FileUtils {
    private static FileUtils util;

    private FileUtils() {

    }

    public static FileUtils getInstance() {
        if (util == null) {
           util = new FileUtils();
        }
        return util;
    }

    public static byte[] getBytes(File file) throws FileNotFoundException, Exception {
        byte[] byteArray = new byte[(int) file.length()];
        FileInputStream stream = new FileInputStream(file);
        stream.read(byteArray);
        stream.close();
        return byteArray;
    }

}
