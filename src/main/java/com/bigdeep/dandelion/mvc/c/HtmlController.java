package com.bigdeep.dandelion.mvc.c;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by weiliyang on 23/05/2017.
 */
@Controller
public class HtmlController {

    @Value("${dandelion.fileupload.dir}")
    private String fileUploadDir;

    @RequestMapping(value = {"/index.html", "/", "/index"}, method = RequestMethod.GET)
    public String index(HttpServletRequest request) {
        List<String> fileNames = ((Collection<File>)FileUtils.listFiles(new File(fileUploadDir), null, false)).stream()
                .filter(f -> f.isFile())
                .map(f -> f.getName())
                .collect(Collectors.toList());

        request.setAttribute("FileNames", fileNames);
        return "index";
    }

    @RequestMapping(value = "example.html", method = RequestMethod.GET)
    public String example(HttpServletRequest request) {
        return "example";
    }

    @PostMapping("/upload.html")
    public String fileUpload(@RequestParam("file") MultipartFile[] files, HttpServletResponse response) throws IOException {
        File dir = new File(fileUploadDir);
        if (!dir.exists()) {
            FileUtils.forceMkdir(dir);
        }

        for (MultipartFile file : files) {
            file.transferTo(new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename()));
        }

        return null;
    }
}
