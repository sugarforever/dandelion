package com.bigdeep.dandelion.mvc.c;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
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
}
