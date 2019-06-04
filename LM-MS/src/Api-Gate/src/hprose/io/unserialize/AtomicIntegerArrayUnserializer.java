/**********************************************************\
|                                                          |
|                          hprose                          |
|                                                          |
| Official WebSite: http://www.hprose.com/                 |
|                   http://www.hprose.org/                 |
|                                                          |
\**********************************************************/
/**********************************************************\
 *                                                        *
 * AtomicIntegerArrayUnserializer.java                    *
 *                                                        *
 * AtomicIntegerArray unserializer class for Java.        *
 *                                                        *
 * LastModified: Aug 3, 2016                              *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/

package hprose.io.unserialize;

import static hprose.io.HproseTags.TagNull;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicIntegerArray;

public final class AtomicIntegerArrayUnserializer implements Unserializer<AtomicIntegerArray> {

    public final static AtomicIntegerArrayUnserializer instance = new AtomicIntegerArrayUnserializer();

    public AtomicIntegerArray read(Reader reader, int tag, Type type) throws IOException {
        if (tag == TagNull) return null;
        return new AtomicIntegerArray(IntArrayUnserializer.instance.read(reader, tag, int[].class));
    }

    public AtomicIntegerArray read(Reader reader, Type type) throws IOException {
       return read(reader, reader.stream.read(), type);
    }

    public AtomicIntegerArray read(Reader reader) throws IOException {
       return read(reader, AtomicIntegerArray.class);
    }
}
