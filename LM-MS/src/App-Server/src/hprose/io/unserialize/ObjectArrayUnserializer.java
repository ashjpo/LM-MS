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
 * ObjectArrayUnserializer.java                           *
 *                                                        *
 * Object array unserializer class for Java.              *
 *                                                        *
 * LastModified: Aug 3, 2016                              *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/

package hprose.io.unserialize;

import static hprose.io.HproseTags.TagEmpty;
import static hprose.io.HproseTags.TagList;
import java.io.IOException;
import java.lang.reflect.Type;

public final class ObjectArrayUnserializer extends BaseUnserializer<Object[]> {

    public final static ObjectArrayUnserializer instance = new ObjectArrayUnserializer();

    @Override
    public Object[] unserialize(Reader reader, int tag, Type type) throws IOException {
        if (tag == TagList) return ReferenceReader.readArray(reader);
        if (tag == TagEmpty) return new Object[0];
        return super.unserialize(reader, tag, type);
    }

    public Object[] read(Reader reader) throws IOException {
        return read(reader, Object[].class);
    }
}
