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
 * HproseDateTimeUnserializer.java                        *
 *                                                        *
 * Hprose DateTime unserializer class for Java.           *
 *                                                        *
 * LastModified: Aug 3, 2016                              *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/

package hprose.io.unserialize;

import static hprose.io.HproseTags.TagDate;
import static hprose.io.HproseTags.TagDouble;
import static hprose.io.HproseTags.TagEmpty;
import static hprose.io.HproseTags.TagInteger;
import static hprose.io.HproseTags.TagLong;
import static hprose.io.HproseTags.TagString;
import static hprose.io.HproseTags.TagTime;
import hprose.util.DateTime;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

public final class HproseDateTimeUnserializer extends BaseUnserializer<DateTime> {

    public final static HproseDateTimeUnserializer instance = new HproseDateTimeUnserializer();

    @Override
    @SuppressWarnings({"deprecation"})
    public DateTime unserialize(Reader reader, int tag, Type type) throws IOException {
        switch (tag) {
            case TagDate: return ReferenceReader.readDateTime(reader);
            case TagTime:  return ReferenceReader.readTime(reader);
            case TagEmpty: return null;
            case TagString: return new DateTime(new Date(ReferenceReader.readString(reader)));
            case TagInteger:
            case TagLong: return new DateTime(new Date(ValueReader.readLong(reader)));
            case TagDouble: return new DateTime(new Date((long)ValueReader.readDouble(reader)));
        }
        if (tag >= '0' && tag <= '9') return new DateTime(new Date(tag - '0'));
        return super.unserialize(reader, tag, type);
    }

    public DateTime read(Reader reader) throws IOException {
        return read(reader, DateTime.class);
    }
}
