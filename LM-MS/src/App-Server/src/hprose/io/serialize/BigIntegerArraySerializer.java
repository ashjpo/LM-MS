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
 * BigIntegerArraySerializer.java                         *
 *                                                        *
 * BigInteger array serializer class for Java.            *
 *                                                        *
 * LastModified: Aug 6, 2016                              *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/

package hprose.io.serialize;

import static hprose.io.HproseTags.TagClosebrace;
import static hprose.io.HproseTags.TagList;
import static hprose.io.HproseTags.TagNull;
import static hprose.io.HproseTags.TagOpenbrace;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public final class BigIntegerArraySerializer extends ReferenceSerializer<BigInteger[]> {

    public final static BigIntegerArraySerializer instance = new BigIntegerArraySerializer();

    @Override
    public final void serialize(Writer writer, BigInteger[] array) throws IOException {
        super.serialize(writer, array);
        OutputStream stream = writer.stream;
        stream.write(TagList);
        int length = array.length;
        if (length > 0) {
            ValueWriter.writeInt(stream, length);
        }
        stream.write(TagOpenbrace);
        for (int i = 0; i < length; ++i) {
            BigInteger e = array[i];
            if (e == null) {
                stream.write(TagNull);
            }
            else {
                ValueWriter.write(stream, e);
            }
        }
        stream.write(TagClosebrace);
    }
}
