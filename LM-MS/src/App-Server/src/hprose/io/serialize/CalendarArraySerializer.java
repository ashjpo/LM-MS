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
 * CalendarArraySerializer.java                           *
 *                                                        *
 * Calendar array serializer class for Java.              *
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
import java.util.Calendar;

public final class CalendarArraySerializer extends ReferenceSerializer<Calendar[]> {

    public final static CalendarArraySerializer instance = new CalendarArraySerializer();

    @Override
    public final void serialize(Writer writer, Calendar[] array) throws IOException {
        super.serialize(writer, array);
        OutputStream stream = writer.stream;
        stream.write(TagList);
        int length = array.length;
        if (length > 0) {
            ValueWriter.writeInt(stream, length);
        }
        stream.write(TagOpenbrace);
        for (int i = 0; i < length; ++i) {
            Calendar e = array[i];
            if (e == null) {
                stream.write(TagNull);
            }
            else {
                CalendarSerializer.instance.write(writer, e);
            }
        }
        stream.write(TagClosebrace);
    }
}
