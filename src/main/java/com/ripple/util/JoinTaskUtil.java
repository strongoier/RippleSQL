package com.ripple.util;

import com.ripple.query.operator.JoinMapOperator;
import com.ripple.query.task.JoinTask;
import com.ripple.query.operator.FilterOperator;
import com.ripple.query.operator.SetKeyOperator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JoinTaskUtil {
    public static void runTask(JoinTask task) throws IOException {
        Class mapperClass = new Mapper<Object, Text, Text, Text>() {
            private JoinMapOperator joinMapOp;
            private String flag;

            @Override
            protected void setup(Context context) throws IOException, InterruptedException {
                super.setup(context);
                Configuration conf = context.getConfiguration();
                String joinMapOpConfig = conf.get("job.map.join");
                joinMapOp = new JoinMapOperator();
                joinMapOp.fromString(joinMapOpConfig);
                String[] paths = ((FileSplit) context.getInputSplit()).getPath().toString().split("/");
                if (paths[paths.length - 2].equals(joinMapOp.pathA))
                    flag = "A";
                else if (paths[paths.length - 2].equals(joinMapOp.pathB))
                    flag = "B";
            }

            @Override
            protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
                String[] values = value.toString().split("\t");
                Pair<Text, Text> result = joinMapOp.map(flag, values);
                context.write(result.getKey(), result.getValue());
            }
        }.getClass();
        Class reducerClass = new Reducer<Text, Text, Text, Text>() {
            private FilterOperator filterOp;
            private SetKeyOperator setKeyOp;

            @Override
            protected void setup(Context context) throws IOException, InterruptedException {
                super.setup(context);
                Configuration conf = context.getConfiguration();
                String filterOpConfig = conf.get("job.reduce.filter");
                if (filterOpConfig != null) {
                    filterOp = new FilterOperator();
                    filterOp.fromString(filterOpConfig);
                }
                setKeyOp = new SetKeyOperator();
            }

            @Override
            protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
                List<String[]> rowFromA = new ArrayList<>();
                List<String[]> rowFromB = new ArrayList<>();
                for (Text value : values) {
                    String[] tmp = value.toString().split("\t");
                    String[] row = new String[tmp.length - 1];
                    for (int i = 0; i < row.length; ++i)
                        row[i] = tmp[i + 1];
                    if (tmp[0].equals("A"))
                        rowFromA.add(row);
                    else
                        rowFromB.add(row);
                }
                if (rowFromA.size() == 0 || rowFromB.size() == 0)
                    return;
                int lenA = rowFromA.get(0).length;
                int lenB = rowFromB.get(0).length;
                for (String[] a : rowFromA) {
                    for (String[] b : rowFromB) {
                        String[] r = new String[lenA + lenB];
                        for (int i = 0; i < lenA; ++i)
                            r[i] = a[i];
                        for (int i = 0; i < lenB; ++i)
                            r[i + lenA] = b[i];
                        if (filterOp == null || filterOp.map(r)) {
                            Pair<Text, Text> result = setKeyOp.format(r);
                            context.write(result.getKey(), result.getValue());
                        }
                    }
                }
            }
        }.getClass();
        Configuration conf = new Configuration();
        conf.set("job.map.join", task.joinMapOperator.toString());
        if (task.filterOperator != null)
            conf.set("job.reduce.filter", task.filterOperator.toString());
        Job job = Job.getInstance(conf);
        job.setJarByClass(JoinTaskUtil.class);
        job.setMapperClass(mapperClass);
        job.setReducerClass(reducerClass);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        for (String inputPath : task.inputPaths)
            FileInputFormat.addInputPath(job, new Path(inputPath));
        Path outputPath = new Path(task.outputPath);
        FileOutputFormat.setOutputPath(job, outputPath);
        try {
            job.waitForCompletion(true);
        } catch (InterruptedException | ClassNotFoundException e) {
            throw new RuntimeException(e.getCause());
        }
        FilesUtil.getOutputFileInfo(task, conf);
    }
}
