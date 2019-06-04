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
 * LongUnserializer.java                                  *
 *                                                        *
 * long unserializer class for Java.                      *
 *                                                        *
 * LastModified: Aug 3, 2016                              *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/

package hprose.io.unserialize;

import static hprose.io.HproseTags.TagNull;
import java.io.IOException;
import java.lang.reflect.Type;

public final class LongUnserializer extends LongObjectUnserializer {

    public final static LongUnserializer instance = new LongUnserializer();

    @Override
    public Long read(Reader reader, int tag, Type type) throws IOException {
        if (tag == TagNull) return 0l;
        return super.read(reader, tag, type);
    }

    @Override
    public Long read(Reader reader) throws IOException {
        return read(reader, long.class);
    }
}
