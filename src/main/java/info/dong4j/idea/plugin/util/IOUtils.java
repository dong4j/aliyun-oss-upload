/*
 * MIT License
 *
 * Copyright (c) 2021 dong4j <dong4j@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package info.dong4j.idea.plugin.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.14 22:52
 * @since 1.1.0
 */
public class IOUtils {

    /**
     * To string
     *
     * @param input input
     * @return the string
     * @throws IOException io exception
     * @since 1.1.0
     */
    @Deprecated
    public static String toString(byte[] input) throws IOException {
        return new String(input, Charset.defaultCharset());
    }

    /**
     * To string
     *
     * @param input    input
     * @param encoding encoding
     * @return the string
     * @throws IOException io exception
     * @since 1.1.0
     */
    public static String toString(byte[] input, String encoding) throws IOException {
        return new String(input, Charsets.toCharset(encoding));
    }

    /**
     * Copy
     *
     * @param input  input
     * @param output output
     * @return the int
     * @throws IOException io exception
     * @since 1.1.0
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        return count > 2147483647L ? -1 : (int) count;
    }

    /**
     * Copy
     *
     * @param input      input
     * @param output     output
     * @param bufferSize buffer size
     * @return the long
     * @throws IOException io exception
     * @since 1.1.0
     */
    public static long copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        return copyLarge(input, output, new byte[bufferSize]);
    }

    /**
     * Copy large
     *
     * @param input  input
     * @param output output
     * @return the long
     * @throws IOException io exception
     * @since 1.1.0
     */
    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, 4096);
    }

    /**
     * Copy large
     *
     * @param input  input
     * @param output output
     * @param buffer buffer
     * @return the long
     * @throws IOException io exception
     * @since 1.1.0
     */
    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count;
        int n;
        for (count = 0L; -1 != (n = input.read(buffer)); count += n) {
            output.write(buffer, 0, n);
        }

        return count;
    }
}
