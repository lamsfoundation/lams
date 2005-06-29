package org.lamsfoundation.lams.tool.forum.util;

import java.io.*;

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

    public static File getFile(String fileName, InputStream is) throws FileNotFoundException, Exception {
        InputStream in = new BufferedInputStream(is, 500);
        File file = new File(fileName);
        OutputStream out = new BufferedOutputStream(new FileOutputStream(file), 500);
        int bytes;
        while ((bytes = in.available()) >  0) {
            byte[] byteArray = new byte[bytes];
            in.read(byteArray);
            out.write(byteArray);
        }
        in.close();
        out.close();
        out.flush();
        return file;
    }

    public static byte[] getBytes(File file) throws FileNotFoundException, Exception {
        byte[] byteArray = new byte[(int) file.length()];
        FileInputStream stream = new FileInputStream(file);
        stream.read(byteArray);
        stream.close();
        return byteArray;
    }

}
