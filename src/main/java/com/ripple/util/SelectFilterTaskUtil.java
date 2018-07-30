package com.ripple.util;

import com.ripple.query.task.SelectFilterTask;
import com.ripple.query.operator.FilterOperator;
import com.ripple.query.operator.SelectMapOperator;
import com.ripple.query.operator.SetKeyOperator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.List;

public class SelectFilterTaskUtil {
    public interface SFMapper {
        void map(Object key, Text value, Mapper<Object, Text, IntWritable, Text>.Context context)
                throws IOException, InterruptedException;
    }

    public static void runTask(SelectFilterTask task) throws IOException {
        Class mapperClass = new Mapper<Object, Text, IntWritable, Text>() {
            private final IntWritable ONE = new IntWritable(1);
            private SelectMapOperator selectOp;
            private FilterOperator filterOp;
            private SFMapper mapper;

            @Override
            protected void setup(Context context) throws IOException, InterruptedException {
                super.setup(context);
                Configuration conf = context.getConfiguration();
                String selectOpConfig = conf.get("job.map.select");
                if (selectOpConfig != null) {
                    selectOp = new SelectMapOperator();
                    selectOp.fromString(selectOpConfig);
                }
                String filterOpConfig = conf.get("job.map.filter");
                if (filterOpConfig != null) {
                    filterOp = new FilterOperator();
                    filterOp.fromString(filterOpConfig);
                }
                if (selectOp == null && filterOp == null)
                    mapper = (key, value, context1) -> {
                        context1.write(ONE, value);
                    };
                else if (selectOp != null && filterOp == null)
                    mapper = (key, value, context1) -> {
                        String[] values = value.toString().split("\t");
                        values = selectOp.map(values);
                        context1.write(ONE, new Text(String.join("\t", values)));
                    };
                else if (selectOp == null && filterOp != null)
                    mapper = (key, value, context1) -> {
                        String[] values = value.toString().split("\t");
                        if (filterOp.map(values))
                            context1.write(ONE, value);
                    };
                else
                    mapper = (key, value, context1) -> {
                        String[] values = value.toString().split("\t");
                        values = selectOp.map(values);
                        if (filterOp.map(values))
                            context1.write(ONE, new Text(String.join("\t", values)));
                    };
            }

            @Override
            protected void map(Object key, Text value, Context context)
                    throws IOException, InterruptedException {
                mapper.map(key, value, context);
            }
        }.getClass();
        Class reducerClass = new Reducer<IntWritable, Text, Text, Text>() {
            private SetKeyOperator setKeyOp;

            @Override
            protected void setup(Context context) throws IOException, InterruptedException {
                super.setup(context);
                setKeyOp = new SetKeyOperator();
            }

            @Override
            protected void reduce(IntWritable key, Iterable<Text> values, Context context)
                    throws IOException, InterruptedException {
                for (Text value : values) {
                    Pair<Text, Text> result = setKeyOp.format(value.toString().split("\t"));
                    context.write(result.getKey(), result.getValue());
                }
            }
        }.getClass();
        Configuration conf = new Configuration();
        if (task.selectOperator != null)
            conf.set("job.map.select", task.selectOperator.toString());
        if (task.filterOperator != null)
            conf.set("job.map.filter", task.filterOperator.toString());
        Job job = Job.getInstance(conf);
        job.setJarByClass(SelectFilterTask.class);
        job.setMapperClass(mapperClass);
        job.setReducerClass(reducerClass);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Text.class);
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

    public static void runTasks(List<SelectFilterTask> tasks) throws IOException {
        for (SelectFilterTask task : tasks)
            runTask(task);
    }
}
