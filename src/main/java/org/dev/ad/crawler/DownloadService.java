package org.dev.ad.crawler;

import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@Service
public class DownloadService {

    public void download(String FILE_URL, String FILE_NAME) throws IOException {
        URL url = new URL(FILE_URL);
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(getFileName(FILE_NAME));
        //FileChannel fileChannel = fileOutputStream.getChannel();
        fileOutputStream.getChannel()
                .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
    }

    public void download1(String FILE_URL, String FILE_NAME) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(getFileName(FILE_NAME))) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private String getFileName(String fileName) {
        return "/Users/apple/Downloads/" + fileName + ".mp4";
    }

}
