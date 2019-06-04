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
 * CaseInsensitiveMapUnserializer.java                    *
 *                                                        *
 * CaseInsensitiveMap unserializer class for Java.        *
 *                                                        *
 * LastModified: Aug 4, 2016                              *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/

package hprose.io.unserialize;

import static hprose.io.HproseTags.TagList;
import static hprose.io.HproseTags.TagMap;
import static hprose.io.HproseTags.TagObject;
import hprose.util.CaseInsensitiveMap;
import java.io.IOException;
import java.lang.reflect.Type;

public final class CaseInsensitiveMapUnserializer extends BaseUnserializer<CaseInsensitiveMap> {

    public final static CaseInsensitiveMapUnserializer instance = new CaseInsensitiveMapUnserializer();

    @Override
    public CaseInsensitiveMap unserialize(Reader reader, int tag, Type type) throws IOException {
        switch (tag) {
            case TagList: return ReferenceReader.readListAsCaseInsensitiveMap(reader, type);
            case TagMap: return ReferenceReader.readCaseInsensitiveMap(reader, type);
            case TagObject:  return ReferenceReader.readObjectAsCaseInsensitiveMap(reader, type);
        }

        return super.unserialize(reader, tag, type);
    }

    public CaseInsensitiveMap read(Reader reader) throws IOException {
        return read(reader, CaseInsensitiveMap.class);
    }
}
