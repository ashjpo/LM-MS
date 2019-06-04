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
 * FloatObjectUnserializer.java                           *
 *                                                        *
 * Float unserializer class for Java.                     *
 *                                                        *
 * LastModified: Aug 3, 2016                              *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/

package hprose.io.unserialize;

import static hprose.io.HproseTags.TagDouble;
import static hprose.io.HproseTags.TagEmpty;
import static hprose.io.HproseTags.TagFalse;
import static hprose.io.HproseTags.TagInfinity;
import static hprose.io.HproseTags.TagInteger;
import static hprose.io.HproseTags.TagLong;
import static hprose.io.HproseTags.TagNaN;
import static hprose.io.HproseTags.TagSemicolon;
import static hprose.io.HproseTags.TagString;
import static hprose.io.HproseTags.TagTrue;
import static hprose.io.HproseTags.TagUTF8Char;
import java.io.IOException;
import java.lang.reflect.Type;

public class FloatObjectUnserializer extends BaseUnserializer<Float> {

    public final static FloatObjectUnserializer instance = new FloatObjectUnserializer();

    @Override
    public Float unserialize(Reader reader, int tag, Type type) throws IOException {
        if (tag == TagDouble) return ValueReader.readFloat(reader);
        if (tag >= '0' && tag <= '9') return (float)(tag - '0');
        if (tag == TagInteger) return (float)ValueReader.readInt(reader, TagSemicolon);
        switch (tag) {
            case TagLong: return ValueReader.readLongAsFloat(reader);
            case TagEmpty: return 0.0f;
            case TagTrue: return 1.0f;
            case TagFalse: return 0.0f;
            case TagNaN: return Float.NaN;
            case TagInfinity: return ValueReader.readFloatInfinity(reader);
            case TagUTF8Char: return ValueReader.parseFloat(ValueReader.readUTF8Char(reader));
            case TagString: return ValueReader.parseFloat(ReferenceReader.readString(reader));
        }
        return super.unserialize(reader, tag, type);
    }

    public Float read(Reader reader) throws IOException {
        return read(reader, Float.class);
    }
}
