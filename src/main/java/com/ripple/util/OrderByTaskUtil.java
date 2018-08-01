package com.ripple.util;

import com.ripple.database.value.FloatValue;
import com.ripple.database.value.IntValue;
import com.ripple.database.value.StringValue;
import com.ripple.query.operator.SetKeyOperator;
import com.ripple.query.task.OrderByTask;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class OrderByTaskUtil {

    public static class IntValueComparator extends WritableComparator {
        public IntValueComparator() {
            super(Text.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            return new IntValue(a.toString()).compareTo(new IntValue(b.toString()));
        }
    }

    public static class FloatValueComparator extends WritableComparator {
        public FloatValueComparator() {
            super(Text.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            return new FloatValue(a.toString()).compareTo(new FloatValue(b.toString()));
        }
    }

    public static class StringValueComparator extends WritableComparator {
        public StringValueComparator() {
            super(Text.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            return new StringValue(a.toString()).compareTo(new StringValue(b.toString()));
        }
    }

    public static void runTask(OrderByTask task) throws IOException {
        Class mapperClass = new Mapper<Object, Text, Text, Text>() {
            private SetKeyOperator setKeyOp;

            @Override
            protected void setup(Context context) throws IOException, InterruptedException {
                super.setup(context);
                Configuration conf = context.getConfiguration();
                String setKeyOpConfig = conf.get("job.map.setKey");
                setKeyOp = new SetKeyOperator();
                setKeyOp.fromString(setKeyOpConfig);
            }

            @Override
            protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
                Pair<Text, Text> result = setKeyOp.format(value.toString().split("\t"));
                context.write(result.getKey(), result.getValue());
            }
        }.getClass();
        Class reducerClass = new Reducer<Text, Text, Text, Text>() {
            private SetKeyOperator setKeyOp;

            @Override
            protected void setup(Context context) throws IOException, InterruptedException {
                super.setup(context);
                setKeyOp = new SetKeyOperator();
            }

            @Override
            protected void reduce(Text key, Iterable<Text> values, Context context)
                    throws IOException, InterruptedException {
                for (Text value : values) {
                    Pair<Text, Text> result = setKeyOp.format(value.toString().split("\t"));
                    context.write(result.getKey(), result.getValue());
                }
            }
        }.getClass();

        Configuration conf = new Configuration();
        conf.set("job.map.setKey", task.setKeyOperator.toString());
        Job job = Job.getInstance(conf);
        job.setJarByClass(GroupByFuncTaskUtil.class);
        job.setMapperClass(mapperClass);
        job.setReducerClass(reducerClass);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        if (task.orderByType == IntValue.class) {
            job.setSortComparatorClass(IntValueComparator.class);
        } else if (task.orderByType == FloatValue.class) {
            job.setSortComparatorClass(FloatValueComparator.class);
        } else if (task.orderByType == StringValue.class) {
            job.setSortComparatorClass(StringValueComparator.class);
        }
        for (String inputPath : task.inputPaths) {
            FileInputFormat.addInputPath(job, new Path(inputPath));
        }
        Path outputPath = new Path(task.outputPath);
        FileOutputFormat.setOutputPath(job, outputPath);
        try {
            job.waitForCompletion(true);
        } catch (InterruptedException | ClassNotFoundException e) {
            throw new RuntimeException(e.getCause());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        FilesUtil.getOutputFileInfo(task, conf);
    }
}
