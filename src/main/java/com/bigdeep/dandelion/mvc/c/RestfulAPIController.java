package com.bigdeep.dandelion.mvc.c;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by weiliyang on 23/05/2017.
 */

@RestController
@RequestMapping("/api/")
public class RestfulAPIController {
    @Value("${dandelion.fileupload.dir}")
    private String fileUploadDir;

    @GetMapping(value = "/datasource")
    public Object getDataSource(@RequestParam String name) throws IOException {
        File dataSourceFile = new File(fileUploadDir + File.separator + name);
        List data = new ArrayList();
        if (dataSourceFile.exists()) {
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(new FileReader(dataSourceFile));
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
}
