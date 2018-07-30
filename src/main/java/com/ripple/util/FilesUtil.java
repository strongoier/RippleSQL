package com.ripple.util;

import com.ripple.query.task.MapReduceTask;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class FilesUtil {
    public static String readAll(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    public static void getOutputFileInfo(MapReduceTask task, Configuration conf) throws IOException {
        final FileSystem fileSystem = FileSystem.get(conf);
        org.apache.hadoop.fs.Path outputPath = new org.apache.hadoop.fs.Path(task.outputPath);
        task.length = fileSystem.getContentSummary(outputPath).getLength();
        task.lines = Arrays.stream(fileSystem.listStatus(outputPath))
                .filter(status->!status.isDir())
                .map(FileStatus::getPath)
                .mapToLong(path -> {
                    long line = 0L;
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileSystem.open(path)));
                        while (reader.readLine() != null)
                            ++line;
                        return line;
                    } catch (IOException e) {
                        throw new RuntimeException(e.getClass().getName() + " " + e.getCause());
                    }
                }).reduce(0L, (len1, len2)->len1 + len2);
    }
}
