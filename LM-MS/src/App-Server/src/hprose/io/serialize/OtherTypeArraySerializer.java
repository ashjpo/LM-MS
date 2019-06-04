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
 * OtherTypeArraySerializer.java                          *
 *                                                        *
 * other type array serializer class for Java.            *
 *                                                        *
 * LastModified: Aug 6, 2016                              *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/

package hprose.io.serialize;

import static hprose.io.HproseTags.TagClosebrace;
import static hprose.io.HproseTags.TagList;
import static hprose.io.HproseTags.TagOpenbrace;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;

public final class OtherTypeArraySerializer extends ReferenceSerializer {

    public final static OtherTypeArraySerializer instance = new OtherTypeArraySerializer();

    @Override
    @SuppressWarnings({"unchecked"})
    public final void serialize(Writer writer, Object array) throws IOException {
        super.serialize(writer, array);
        OutputStream stream = writer.stream;
        int length = Array.getLength(array);
        stream.write(TagList);
        if (length > 0) {
            ValueWriter.writeInt(stream, length);
        }
        stream.write(TagOpenbrace);
        for (int i = 0; i < length; ++i) {
            writer.serialize(Array.get(array, i));
        }
        stream.write(TagClosebrace);
    }
}
