/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jasper.runtime;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * An efficient character buffer class. 
 * This class is not thread-safe, so its clients will have to take care of any necessary thread issues.
 * @author Brian Remmington
 *
 */
public class CharBuffer {
    private static final int DEFAULT_INITIAL_CAPACITY = 512;

    /**
     * List of all fully populated character arrays.
     */
    private LinkedList bufList;
    private int listSize = 0;

    /**
     * The character array that is currently being filled
     */
    private char[] currentBuf;

    /**
     * The index of the next character to write in 'currentBuf'.
     */
    private int index;

    /**
     * The minimum number of characters by which the buffer should grow when 
     * 'currentBuf' becomes full.
     */
    private int minimumGrowth;


    /**
     * Create a new character buffer, specifying its initial capacity.
     * If this buffer ever has to grow then it will grow by at least the same as its initial 
     * capacity again
     * @param initialCapacity
     */
    public CharBuffer(int initialCapacity) {
        this(initialCapacity, 0);
    }

    /**
     * Create a new character buffer, specifying its initial capacity and minimum growth.
     * @param initialCapacity The number of characters initially provisioned for. 
     * @param minimumGrowth The smallest number of characters by which the buffer will grow.
     * If zero is specified then the value of the initial capacity will be used.
     */
    public CharBuffer(int initialCapacity, int minimumGrowth) {
        if (initialCapacity == 0) {
            initialCapacity = DEFAULT_INITIAL_CAPACITY;
        }
        if (minimumGrowth == 0) {
            minimumGrowth = initialCapacity;
        }
        this.bufList = new LinkedList();
        this.currentBuf = new char[initialCapacity];
        this.index = 0;
        this.minimumGrowth = minimumGrowth;
        this.listSize = 0;
    }

    /**
     * Add the supplied character to this buffer, growing the buffer if necessary.
     * @param character
     */
    public void buffer(char character) {
        if (this.getCapacity() == 0) {
            this.grow();
        }
        currentBuf[index++] = character;
    }

    /**
     * Add a substring of the supplied string to this buffer, growing the buffer if necessary.
     * @param text The original String that is to be added to the buffer.
     * @param start The index of the starting character to add to the buffer (zero-based).
     * @param length The number of characters to add to the buffer.
     * @throws IllegalArgumentException If text is null, start points beyond end of text, or start + length
     * goes beyond end of text.
     */
    public void buffer(String text, int start, int length) throws IllegalArgumentException {
        //This method looks very similar to the overloaded version of it, but I can't see a good way of
        //refactoring without introducing at least one extra char[] allocation and arraycopy per write.
        //Ideas welcome.
        if (length <= 0) {
            return;
        }
        if (text == null) {
            throw new IllegalArgumentException("text: may not be null.");
        }
        if (start > text.length()) {
            throw new IllegalArgumentException("start: points beyond end of text.");
        }
        if ((start + length) > text.length()) {
            throw new IllegalArgumentException("length: specifies length in excess of text length.");
        }

        //If length of string to add is greater than will fit in current char array then add what will fit
        //and then bolt the rest on.
        int charsToCopy = 0;
        while (length != 0) {
            charsToCopy = this.getCapacity();
            if (charsToCopy > length) {
                charsToCopy = length;
            }

            if (charsToCopy > 0) {
                //Add as many characters as will fit in currentBuf, updating the indexes as necessary.
                text.getChars(start, start + charsToCopy, currentBuf, index);
                start += charsToCopy;
                length -= charsToCopy;
                this.index += charsToCopy;
            }

            //If there are still some characters to write then grow the buffer by enough to take the 
            //remainder and loop.
            if (length != 0) {
                this.grow(length);
            }
        }
    }

    /**
     * Add a section of the supplied character array to this buffer, growing the buffer if necessary.
     * @param characters The character array that is to be added to the buffer.
     * @param start The index of the starting character to add to the buffer (zero-based).
     * @param length The number of characters to add to the buffer.
     * @throws IllegalArgumentException If array is null, start points beyond end of array, or start + length
     * goes beyond end of array.
     */
    public void buffer(char[] characters, int start, int length) throws IllegalArgumentException {
        //This method looks very similar to the overloaded version of it, but I can't see a good way of
        //refactoring without introducing at least one extra char[] allocation and arraycopy per write.
        //Ideas welcome.
        if (length <= 0) {
            return;
        }
        if (characters == null) {
            throw new IllegalArgumentException("characters: may not be null.");
        }
        if (start > characters.length) {
            throw new IllegalArgumentException("start: points beyond end of array.");
        }
        if ((start + length) > characters.length) {
            throw new IllegalArgumentException("length: specifies length in excess of array length.");
        }

        //If length of string to add is greater than will fit in current char array then add what will fit
        //and then bolt the rest on.
        int charsToCopy = 0;
        while (length != 0) {
            charsToCopy = this.getCapacity();
            if (charsToCopy > length) {
                charsToCopy = length;
            }

            if (charsToCopy > 0) {
                //Add as many characters as will fit in currentBuf, updating the indexes as necessary.
                System.arraycopy(characters, start, currentBuf, index, charsToCopy);
                start += charsToCopy;
                length -= charsToCopy;
                this.index += charsToCopy;
            }

            //If there are still some characters to write then grow the buffer by enough to take the 
            //remainder and loop.
            if (length != 0) {
                this.grow(length);
            }
        }
    }

    /**
     * Render this buffer as a character array.
     * @return
     */
    public char[] toArray() {
        char[] result = new char[size()];
        int offset = 0;
        for (Iterator iter = this.bufList.iterator(); iter.hasNext();) {
            char[] curBuf = (char[]) iter.next();
            System.arraycopy(curBuf, 0, result, offset, curBuf.length);
            offset += curBuf.length;
        }
        System.arraycopy(this.currentBuf, 0, result, offset, index);
        return result;
    }

    /**
     * Render this buffer as a String.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer(size());
        for (Iterator iter = this.bufList.iterator(); iter.hasNext();) {
            char[] curBuf = (char[]) iter.next();
            sb.append(curBuf);
        }
        sb.append(currentBuf, 0, index);
        return sb.toString();
    }

    /**
     * Find the current capacity of this buffer.
     * @return The capacity of this buffer. This is the number of characters that can be 
     * added to this buffer without it having to grow.
     */
    public int getCapacity() {
        return this.currentBuf.length - this.index;
    }

    /**
     * Obtain the current size of this buffer.
     * @return The number of characters that are currently in this buffer. Equates to the size of 
     * array that would be returned by {@link #toArray()} or the length of String returned by {@link #toString()}.
     */
    public int size() {
        return (this.listSize + index);
    }

    /**
     * Clear this buffer.
     * Upon return, a call to {@link #size()} will return zero. 
     *
     */
    public void clear() {
        this.bufList.clear();
        this.index = 0;
        this.listSize = 0;
    }

    /**
     * Write the content of this buffer out to the supplied Writer object.
     * This will not flush the writer before returning.
     * @param writer The writer in to which the buffer should be written.
     * @throws IOException If any error occurs while writing the buffer into the stream.
     * @throws IllegalArgumentException If 'writer' is null. 
     */
    public void writeOut(Writer writer) throws IOException, IllegalArgumentException {
        if (writer == null) {
            throw new IllegalArgumentException("writer: may not be null.");
        }
        for (Iterator iter = this.bufList.iterator(); iter.hasNext();) {
            char[] curBuf = (char[]) iter.next();
            writer.write(curBuf);
        }
        writer.write(currentBuf, 0, index);
    }

    private void grow() {
        this.grow(this.minimumGrowth);
    }

    private void grow(int requiredChars) {
        if (requiredChars < this.minimumGrowth) {
            requiredChars = this.minimumGrowth;
        }
        this.bufList.add(currentBuf);
        this.listSize += currentBuf.length;
        currentBuf = new char[requiredChars];
        index = 0;
    }
}
