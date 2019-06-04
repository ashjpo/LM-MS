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
 * NullSerializer.java                                    *
 *                                                        *
 * null serializer class for Java.                        *
 *                                                        *
 * LastModified: Aug 6, 2016                              *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/

package hprose.io.serialize;

import static hprose.io.HproseTags.TagNull;
import java.io.IOException;

public final class NullSerializer implements Serializer {

    public final static NullSerializer instance = new NullSerializer();

    public void write(Writer writer, Object obj) throws IOException {
        writer.stream.write(TagNull);
    }
}
