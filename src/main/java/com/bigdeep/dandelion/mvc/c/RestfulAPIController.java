package com.bigdeep.dandelion.mvc.c;

import com.bigdeep.dandelion.transformers.Transformer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.archetype.common.util.FileCharsetDetector;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by weiliyang on 23/05/2017.
 */

@RestController
@RequestMapping("/api/")
public class RestfulAPIController {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${dandelion.fileupload.dir}")
    private String fileUploadDir;

    @GetMapping(value = "/datasource")
    public Object getDataSource(@RequestParam String name) throws IOException {
        File dataSourceFile = new File(fileUploadDir + File.separator + name);
        List data = new ArrayList();
        if (dataSourceFile.exists()) {
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(new InputStreamReader(new FileInputStream(dataSourceFile), "UTF-8"));
            final Set<String> headers = parser.getHeaderMap().keySet();
            data = parser.getRecords().stream().map(record -> {
                Hashtable hash = new Hashtable();
                for (String header : headers) {
                    hash.put(header, record.get(header));
                }
                return hash;
            }).collect(Collectors.toList());
        }
        return data;
    }

    @GetMapping(value = "/action/reverse-string")
    public Object reverseString(@RequestParam String name, @RequestParam String column) throws IOException {
        File dataSourceFile = new File(fileUploadDir + File.separator + name);
        List reversedColumns = new ArrayList();
        if (dataSourceFile.exists()) {
            CSVFormat csvFormat = CSVFormat.RFC4180.withFirstRecordAsHeader();
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(new FileReader(dataSourceFile));

            Map<Integer, String> indexToColumns = parser.getHeaderMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
            ArrayList<Integer> indexes = new ArrayList<>(indexToColumns.keySet());
            Collections.sort(indexes);

            List<String> rows = new ArrayList();
            reversedColumns = parser.getRecords().stream().map(record -> {
                String columnValue = record.get(column);
                String reversedColumnValue = StringUtils.reverse(columnValue);


                List<String> columns = new ArrayList();
                for (Integer index : indexes) {
                    String columnName = indexToColumns.get(index);
                    columns.add(columnName.equals(column) ? reversedColumnValue : record.get(columnName));
                }
                rows.add(StringUtils.join(columns, csvFormat.getDelimiter()));

                return reversedColumnValue;
            }).collect(Collectors.toList());

            rows.add(0, StringUtils.join(indexes.stream().map(i -> indexToColumns.get(i)).collect(Collectors.toList()), csvFormat.getDelimiter()));

            FileWriter writer = new FileWriter(dataSourceFile);
            writer.write(StringUtils.join(rows, csvFormat.getRecordSeparator()));
            writer.flush();
            writer.close();
        }
        return reversedColumns;
    }

    @GetMapping(value = "/transform/{transformer}/")
    public Object transform(@PathVariable String transformer, @RequestParam String name, @RequestParam String column) throws IOException {
        Transformer t = applicationContext.getBean(transformer, Transformer.class);

        File dataSourceFile = new File(fileUploadDir + File.separator + name);
        List reversedColumns = new ArrayList();
        if (dataSourceFile.exists()) {
            CSVFormat csvFormat = CSVFormat.RFC4180.withFirstRecordAsHeader();
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(new FileReader(dataSourceFile));

            Map<Integer, String> indexToColumns = parser.getHeaderMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
            ArrayList<Integer> indexes = new ArrayList<>(indexToColumns.keySet());
            Collections.sort(indexes);

            List<String> rows = new ArrayList();
            reversedColumns = parser.getRecords().stream().map(record -> {
                String columnValue = record.get(column);
                String transformedValue = t.perform(columnValue);

                List<String> columns = new ArrayList();
                for (Integer index : indexes) {
                    String columnName = indexToColumns.get(index);
                    columns.add(columnName.equals(column) ? (transformedValue == null ? record.get(columnName) : transformedValue) : record.get(columnName));
                }
                rows.add(StringUtils.join(columns, csvFormat.getDelimiter()));

                return transformedValue;
            }).collect(Collectors.toList());

            rows.add(0, StringUtils.join(indexes.stream().map(i -> indexToColumns.get(i)).collect(Collectors.toList()), csvFormat.getDelimiter()));

            FileWriter writer = new FileWriter(dataSourceFile);
            writer.write(StringUtils.join(rows, csvFormat.getRecordSeparator()));
            writer.flush();
            writer.close();
        }

        return reversedColumns;
    }

    public static String getEncoding(final String filename) throws IOException {
        // System.out.println("getEncoding: " + filename);
        final nsDetector det = new nsDetector();

        final BufferedInputStream imp = new BufferedInputStream(new FileInputStream(filename));

        final byte[] buf = new byte[1024];
        int len;
        boolean done = false;
        boolean isAscii = true;

        while ((len = imp.read(buf, 0, buf.length)) != -1) {
            // Check if the stream is only ascii.
            if (isAscii) {
                isAscii = det.isAscii(buf, len);
            }

            // DoIt if non-ascii and not done yet.
            if (!isAscii && !done) {
                done = det.DoIt(buf, len, false);
            }
        }
        det.DataEnd();

        if (isAscii) {
            return "ASCII";
        } else {
            final String prob[] = det.getProbableCharsets();
            if (prob.length == 0) {
                throw new IllegalStateException("cannot determine file encoding for : " + filename);
            }

            for (int i = 0; i < prob.length; i++) {
                // System.out.println("Probable Charset = " + prob[i]);
                if (prob[i].equals("windows-1252") || prob[i].equals("UTF-8")) {
                    return prob[i];
                }
            }
            return prob[0];
        }
    }
}
