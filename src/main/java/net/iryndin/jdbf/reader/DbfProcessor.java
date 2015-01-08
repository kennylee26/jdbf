package net.iryndin.jdbf.reader;

import net.iryndin.jdbf.core.DbfRecord;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kennylee on 8/1/15.
 */
public class DbfProcessor {

    public static <T> List<T> loadDataWithMemo(File dbfFile, File fptFile, DbfRowMapper<T> mapper) throws IOException, ParseException {
        List<T> list = new ArrayList();
        DbfReader reader = new DbfReader(dbfFile, fptFile);

        DbfRecord rec;
        while ((rec = reader.read()) != null) {
            rec.setStringCharset(mapper.getCharset());
            T t = mapper.mapRow(rec);
            if (t != null) {
                list.add(t);
            }
        }
        return list;
    }
}
