package net.iryndin.jdbf;

import net.iryndin.jdbf.core.DbfRecord;
import net.iryndin.jdbf.reader.DbfProcessor;
import net.iryndin.jdbf.reader.DbfRowMapper;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.List;

/**
 * Created by kennylee on 8/1/15.
 */
public class TestCpiReader {

    public class CpiGgp {
        private Integer year;
        private String zbdm;
        private String zbmc;
        private Integer cjq;
        private String memo;

        public CpiGgp(DbfRecord rec) throws IOException {
            this.year = rec.getInteger(yearFieldName);
            this.zbdm = rec.getString(zbdmFieldName);
            this.zbmc = rec.getString(zbmcFieldName);
            this.cjq = rec.getInteger(cjqFieldName);
            this.memo = rec.getMemoAsString(memoFieldName);
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public String getZbdm() {
            return zbdm;
        }

        public void setZbdm(String zbdm) {
            this.zbdm = zbdm;
        }

        public String getZbmc() {
            return zbmc;
        }

        public void setZbmc(String zbmc) {
            this.zbmc = zbmc;
        }

        public Integer getCjq() {
            return cjq;
        }

        public void setCjq(Integer cjq) {
            this.cjq = cjq;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        @Override
        public String toString() {
            StringBuilder sbd = new StringBuilder();
            String lieSeparator = System.getProperty("line.separator");
            sbd.append(yearFieldName).append("：").append(year).append(lieSeparator);
            sbd.append(zbdmFieldName).append("：").append(zbdm).append(lieSeparator);
            sbd.append(zbmcFieldName).append("：").append(zbmc).append(lieSeparator);
            sbd.append(cjqFieldName).append("：").append(cjq).append(lieSeparator);
            sbd.append(memoFieldName).append("：").append(memo).append(lieSeparator);
            return sbd.toString();
        }
    }

    private final static String memoFieldName = "CJDCODE";
    private final static String yearFieldName = "NYEAR";
    private final static String zbdmFieldName = "ZBDM";
    private final static String cjqFieldName = "CJQ";
    private final static String zbmcFieldName = "ZBMC";

    @Test
    public void testCpiWithMemo() throws IOException, ParseException {
        Charset stringCharset = Charset.forName("gbk");

        File dbfFile = new File(getClass().getClassLoader().getResource("memo1/cpiggp_440683_small.dbf").getFile());
        File memoFile = new File(getClass().getClassLoader().getResource("memo1/CpiGgp_440683_small.fpt").getFile());

        List<CpiGgp> sheets = DbfProcessor.loadDataWithMemo(dbfFile, memoFile, new DbfRowMapper<CpiGgp>(stringCharset) {

            @Override
            public CpiGgp mapRow(DbfRecord rec) throws IOException {
                return new CpiGgp(rec);
            }
        });
        for (CpiGgp sheet : sheets) {
            System.out.println(sheet);
        }
    }
}
