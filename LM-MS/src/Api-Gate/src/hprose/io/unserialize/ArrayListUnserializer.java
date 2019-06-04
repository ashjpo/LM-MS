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
 * ArrayListUnserializer.java                             *
 *                                                        *
 * ArrayList unserializer class for Java.                 *
 *                                                        *
 * LastModified: Aug 3, 2016                              *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/

package hprose.io.unserialize;

import static hprose.io.HproseTags.TagList;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public final class ArrayListUnserializer extends BaseUnserializer<ArrayList> {

    public final static ArrayListUnserializer instance = new ArrayListUnserializer();

    @Override
    public ArrayList unserialize(Reader reader, int tag, Type type) throws IOException {
        if (tag == TagList) return ReferenceReader.readArrayList(reader, type);
        return super.unserialize(reader, tag, type);
    }

    public ArrayList read(Reader reader) throws IOException {
       return read(reader, ArrayList.class);
    }
}
