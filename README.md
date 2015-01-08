jdbf
====

Java utility to read/write DBF files

### Version 2.1.0

Fix issue [#5](https://github.com/iryndin/jdbf/issues/5) - don't load DBF and MEMO files into memory when reading it (thanks to Eugene Michuk for noticing this!)

Fix issue [#9](https://github.com/iryndin/jdbf/issues/9) - don't define some DBF file types correctly (thanks to [l1feh4ck3r](https://github.com/l1feh4ck3r)!!)

### Version 2.0.2

Fix issue [#7](https://github.com/iryndin/jdbf/issues/7) - add DbfRecord.isDeleted() method that checks if record is deleted.

### Version 2.0.1 

Fix issue [#3](https://github.com/iryndin/jdbf/issues/3) - read the last record two times for "FoxBASE+/Dbase III plus" files

Fix issue [#4](https://github.com/iryndin/jdbf/issues/4) - incorrect parsing of update date in DBF header for "FoxBASE+/Dbase III plus" files

### Version 2.0 

Add ability to read MEMO files (tested with Visual FoxPro DBFs)

[![Dependency Status](https://www.versioneye.com/user/projects/53c55ce7c4a986cbb3000002/badge.svg?style=flat)](https://www.versioneye.com/user/projects/53c55ce7c4a986cbb3000002)

## User Guide

### Read DBF file with MEMO fields to Mapper

把每行DbfRecord转成Bean例子，并且支持中文字段。

See [TestCpiReader.java](src/test/java/net/iryndin/jdbf/TestCpiReader.java)

```java
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
```

### Read DBF file 

Piece of code that reads file from classpath. Single DBF record is represented here as a Map.

See [TestDbfReader.java](src/test/java/net/iryndin/jdbf/TestDbfReader.java)

```java
    public void readDBF() throws IOException, ParseException {
        Charset stringCharset = Charset.forName("Cp866");

        InputStream dbf = getClass().getClassLoader().getResourceAsStream("data1/gds_im.dbf");

        DbfRecord rec;
        try (DbfReader reader = new DbfReader(dbf)) {
            DbfMetadata meta = reader.getMetadata();

            System.out.println("Read DBF Metadata: " + meta);
            while ((rec = reader.read()) != null) {
                rec.setStringCharset(stringCharset);
                System.out.println("Record #" + rec.getRecordNumber() + ": " + rec.toMap());
            }
        }
    }
```

### Read DBF file with MEMO fields

Piece of code that reads DBF and MEMO fields. 

See [TestMemo.java](src/test/java/net/iryndin/jdbf/TestMemo.java)

```java
    public void test1() {
        Charset stringCharset = Charset.forName("cp1252");

        InputStream dbf = getClass().getClassLoader().getResourceAsStream("memo1/texto.dbf");
        InputStream memo = getClass().getClassLoader().getResourceAsStream("memo1/texto.fpt");

        try (DbfReader reader = new DbfReader(dbf, memo)) {
            DbfMetadata meta = reader.getMetadata();
            System.out.println("Read DBF Metadata: " + meta);

            DbfRecord rec;
            while ((rec = reader.read()) != null) {
                rec.setStringCharset(stringCharset);

                System.out.println("TEXVER: " + rec.getString("TEXVER"));
                // this reads MEMO field
                System.out.println("TEXTEX: " + rec.getMemoAsString("TEXTEX"));
                System.out.println("TEXDAT: " + rec.getDate("TEXDAT"));
                System.out.println("TEXSTA: " + rec.getString("TEXSTA"));
                System.out.println("TEXCAM: " + rec.getString("TEXCAM"));
                System.out.println("++++++++++++++++++++++++++++++++++");
            }

        } catch (IOException e) {
            //e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
```
