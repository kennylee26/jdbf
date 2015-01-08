package net.iryndin.jdbf.reader;

import net.iryndin.jdbf.core.DbfRecord;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by kennylee on 8/1/15.
 */
public abstract class DbfRowMapper<T> {

    public final static Charset DEFAULT_CHARET = Charset.forName("gbk");
    private Charset charset = null;

    public DbfRowMapper() {
        super();
    }

    public DbfRowMapper(Charset charset) {
        super();
        this.charset = charset;
    }

    //public List<T>

    public abstract T mapRow(DbfRecord rec) throws IOException;

    public Charset getCharset() {
        return charset == null ? DEFAULT_CHARET : charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
