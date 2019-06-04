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
 * DateUnserializer.java                                  *
 *                                                        *
 * Date unserializer class for Java.                      *
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
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;

public final class DateUnserializer extends BaseUnserializer<Date> {

    public final static DateUnserializer instance = new DateUnserializer();

    @Override
    public Date unserialize(Reader reader, int tag, Type type) throws IOException {
        switch (tag) {
            case TagDate: return ReferenceReader.readDateTime(reader).toDate();
            case TagTime:  return ReferenceReader.readTime(reader).toDate();
            case TagEmpty: return null;
            case TagString: return Date.valueOf(ReferenceReader.readString(reader));
            case TagInteger:
            case TagLong: return new Date(ValueReader.readLong(reader));
            case TagDouble: return new Date((long)ValueReader.readDouble(reader));
        }
        if (tag >= '0' && tag <= '9') return new Date(tag - '0');
        return super.unserialize(reader, tag, type);
    }

    public Date read(Reader reader) throws IOException {
        return read(reader, Date.class);
    }
}
