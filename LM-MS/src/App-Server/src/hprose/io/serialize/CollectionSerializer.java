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
 * CollectionSerializer.java                              *
 *                                                        *
 * Collection serializer class for Java.                  *
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
import java.util.Collection;
import java.util.Iterator;

public final class CollectionSerializer<T> extends ReferenceSerializer<Collection<T>> {

    public final static CollectionSerializer instance = new CollectionSerializer();

    @Override
    public final void serialize(Writer writer, Collection<T> collection) throws IOException {
        super.serialize(writer, collection);
        OutputStream stream = writer.stream;
        stream.write(TagList);
        int count = collection.size();
        if (count > 0) {
            ValueWriter.writeInt(stream, count);
        }
        stream.write(TagOpenbrace);
        for (Iterator<T> i = collection.iterator(); i.hasNext();) {
            writer.serialize(i.next());
        }
        stream.write(TagClosebrace);
    }
}
