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
 * DateSerializer.java                                    *
 *                                                        *
 * Date serializer class for Java.                        *
 *                                                        *
 * LastModified: Aug 6, 2016                              *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/

package hprose.io.serialize;

import static hprose.io.HproseTags.TagSemicolon;
import hprose.util.DateTime;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.util.Calendar;

public final class DateSerializer extends ReferenceSerializer<Date> {

    public final static DateSerializer instance = new DateSerializer();

    @Override
    public final void serialize(Writer writer, Date date) throws IOException {
        super.serialize(writer, date);
        OutputStream stream = writer.stream;
        Calendar calendar = DateTime.toCalendar(date);
        ValueWriter.writeDateOfCalendar(stream, calendar);
        stream.write(TagSemicolon);
    }
}
