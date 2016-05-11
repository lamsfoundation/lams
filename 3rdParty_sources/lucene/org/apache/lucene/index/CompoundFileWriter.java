package org.apache.lucene.index;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.IndexInput;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Iterator;
import java.io.IOException;


/**
 * Combines multiple files into a single compound file.
 * The file format:<br>
 * <ul>
 *     <li>VInt fileCount</li>
 *     <li>{Directory}
 *         fileCount entries with the following structure:</li>
 *         <ul>
 *             <li>long dataOffset</li>
 *             <li>String fileName</li>
 *         </ul>
 *     <li>{File Data}
 *         fileCount entries with the raw data of the corresponding file</li>
 * </ul>
 *
 * The fileCount integer indicates how many files are contained in this compound
 * file. The {directory} that follows has that many entries. Each directory entry
 * contains a long pointer to the start of this file's data section, and a String
 * with that file's name.
 *
 *
 *
 */
final class CompoundFileWriter {

    private static final class FileEntry {
        /** source file */
        String file;

        /** temporary holder for the start of directory entry for this file */
        long directoryOffset;

        /** temporary holder for the start of this file's data section */
        long dataOffset;
    }


    private Directory directory;
    private String fileName;
    private HashSet ids;
    private LinkedList entries;
    private boolean merged = false;
    private SegmentMerger.CheckAbort checkAbort;

    /** Create the compound stream in the specified file. The file name is the
     *  entire name (no extensions are added).
     *  @throws NullPointerException if <code>dir</code> or <code>name</code> is null
     */
    public CompoundFileWriter(Directory dir, String name) {
      this(dir, name, null);
    }

    CompoundFileWriter(Directory dir, String name, SegmentMerger.CheckAbort checkAbort) {
        if (dir == null)
            throw new NullPointerException("directory cannot be null");
        if (name == null)
            throw new NullPointerException("name cannot be null");
        this.checkAbort = checkAbort;
        directory = dir;
        fileName = name;
        ids = new HashSet();
        entries = new LinkedList();
    }

    /** Returns the directory of the compound file. */
    public Directory getDirectory() {
        return directory;
    }

    /** Returns the name of the compound file. */
    public String getName() {
        return fileName;
    }

    /** Add a source stream. <code>file</code> is the string by which the 
     *  sub-stream will be known in the compound stream.
     * 
     *  @throws IllegalStateException if this writer is closed
     *  @throws NullPointerException if <code>file</code> is null
     *  @throws IllegalArgumentException if a file with the same name
     *   has been added already
     */
    public void addFile(String file) {
        if (merged)
            throw new IllegalStateException(
                "Can't add extensions after merge has been called");

        if (file == null)
            throw new NullPointerException(
                "file cannot be null");

        if (! ids.add(file))
            throw new IllegalArgumentException(
                "File " + file + " already added");

        FileEntry entry = new FileEntry();
        entry.file = file;
        entries.add(entry);
    }

    /** Merge files with the extensions added up to now.
     *  All files with these extensions are combined sequentially into the
     *  compound stream. After successful merge, the source files
     *  are deleted.
     *  @throws IllegalStateException if close() had been called before or
     *   if no file has been added to this object
     */
    public void close() throws IOException {
        if (merged)
            throw new IllegalStateException(
                "Merge already performed");

        if (entries.isEmpty())
            throw new IllegalStateException(
                "No entries to merge have been defined");

        merged = true;

        // open the compound stream
        IndexOutput os = null;
        try {
            os = directory.createOutput(fileName);

            // Write the number of entries
            os.writeVInt(entries.size());

            // Write the directory with all offsets at 0.
            // Remember the positions of directory entries so that we can
            // adjust the offsets later
            Iterator it = entries.iterator();
            long totalSize = 0;
            while(it.hasNext()) {
                FileEntry fe = (FileEntry) it.next();
                fe.directoryOffset = os.getFilePointer();
                os.writeLong(0);    // for now
                os.writeString(fe.file);
                totalSize += directory.fileLength(fe.file);
            }

            // Pre-allocate size of file as optimization --
            // this can potentially help IO performance as
            // we write the file and also later during
            // searching.  It also uncovers a disk-full
            // situation earlier and hopefully without
            // actually filling disk to 100%:
            final long finalLength = totalSize+os.getFilePointer();
            os.setLength(finalLength);

            // Open the files and copy their data into the stream.
            // Remember the locations of each file's data section.
            byte buffer[] = new byte[16384];
            it = entries.iterator();
            while(it.hasNext()) {
                FileEntry fe = (FileEntry) it.next();
                fe.dataOffset = os.getFilePointer();
                copyFile(fe, os, buffer);
            }

            // Write the data offsets into the directory of the compound stream
            it = entries.iterator();
            while(it.hasNext()) {
                FileEntry fe = (FileEntry) it.next();
                os.seek(fe.directoryOffset);
                os.writeLong(fe.dataOffset);
            }

            assert finalLength == os.length();

            // Close the output stream. Set the os to null before trying to
            // close so that if an exception occurs during the close, the
            // finally clause below will not attempt to close the stream
            // the second time.
            IndexOutput tmp = os;
            os = null;
            tmp.close();

        } finally {
            if (os != null) try { os.close(); } catch (IOException e) { }
        }
    }

    /** Copy the contents of the file with specified extension into the
     *  provided output stream. Use the provided buffer for moving data
     *  to reduce memory allocation.
     */
    private void copyFile(FileEntry source, IndexOutput os, byte buffer[])
    throws IOException
    {
        IndexInput is = null;
        try {
            long startPtr = os.getFilePointer();

            is = directory.openInput(source.file);
            long length = is.length();
            long remainder = length;
            int chunk = buffer.length;

            while(remainder > 0) {
                int len = (int) Math.min(chunk, remainder);
                is.readBytes(buffer, 0, len, false);
                os.writeBytes(buffer, len);
                remainder -= len;
                if (checkAbort != null)
                  // Roughly every 2 MB we will check if
                  // it's time to abort
                  checkAbort.work(80);
            }

            // Verify that remainder is 0
            if (remainder != 0)
                throw new IOException(
                    "Non-zero remainder length after copying: " + remainder
                    + " (id: " + source.file + ", length: " + length
                    + ", buffer size: " + chunk + ")");

            // Verify that the output length diff is equal to original file
            long endPtr = os.getFilePointer();
            long diff = endPtr - startPtr;
            if (diff != length)
                throw new IOException(
                    "Difference in the output file offsets " + diff
                    + " does not match the original file length " + length);

        } finally {
            if (is != null) is.close();
        }
    }
}
