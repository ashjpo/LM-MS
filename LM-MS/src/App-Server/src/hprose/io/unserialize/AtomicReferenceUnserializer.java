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
 * AtomicReferenceUnserializer.java                       *
 *                                                        *
 * AtomicReference unserializer class for Java.           *
 *                                                        *
 * LastModified: Aug 3, 2016                              *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/

package hprose.io.unserialize;

import static hprose.io.HproseTags.TagNull;
import hprose.util.ClassUtil;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

public final class AtomicReferenceUnserializer implements Unserializer<AtomicReference> {

    public final static AtomicReferenceUnserializer instance = new AtomicReferenceUnserializer();

    @SuppressWarnings({"unchecked"})
    public AtomicReference read(Reader reader, int tag, Type type) throws IOException {
        if (tag == TagNull) return null;
        type = ClassUtil.getComponentType(type);
        return new AtomicReference(reader.unserialize(type));
    }

    public AtomicReference read(Reader reader, Type type) throws IOException {
       return read(reader, reader.stream.read(), type);
    }

    public AtomicReference read(Reader reader) throws IOException {
       return read(reader, AtomicReference.class);
    }
}
