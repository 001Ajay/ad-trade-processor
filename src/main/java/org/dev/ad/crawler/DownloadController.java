package org.dev.ad.crawler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    private DownloadUrlConfigs downloadConfigs;

    @Autowired
    private DownloadService service;

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public ResponseEntity<String> downloadFile() throws IOException {
//        downloadConfigs.getDownloads().forEach(url -> {
//            log.debug("url : "+url);
//            service.download(url, url.substring(url.lastIndexOf('/')+1));
//        });

        String url = downloadConfigs.getDownloads().get(0);
        service.download(url, url.substring(url.lastIndexOf('/') + 1));
        return ResponseEntity.ok("Success : ");
    }

    @RequestMapping(value = "/fileTest", method = RequestMethod.GET)
    public InputStreamResource FileSystemResource(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"test.pdf\"");
        InputStreamResource resource = new InputStreamResource(new FileInputStream("/Users/apple/Downloads/test-file.pdf"));
        return resource;
    }
}
