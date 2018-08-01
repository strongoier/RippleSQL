package com.ripple.util;

import com.ripple.query.operator.FuncOperator;
import com.ripple.query.operator.SelectMapOperator;
import com.ripple.query.operator.SetKeyOperator;
import com.ripple.query.task.GroupByFuncTask;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class GroupByFuncTaskUtil {
    public static void runTask(GroupByFuncTask task) throws IOException {
        Class mapperClass = new Mapper<Object, Text, Text, Text>() {
            private SelectMapOperator selectValueOp;
            private SelectMapOperator selectKeyOp;

            @Override
            protected void setup(Context context) throws IOException, InterruptedException {
                super.setup(context);
                Configuration conf = context.getConfiguration();
                String selectValueOpConfig = conf.get("job.map.selectValue");
                selectValueOp = new SelectMapOperator();
                selectValueOp.fromString(selectValueOpConfig);
                String selectKeyOpConfig = conf.get("job.map.selectKey");
                if (selectKeyOpConfig != null) {
                    selectKeyOp = new SelectMapOperator();
                    selectKeyOp.fromString(selectKeyOpConfig);
                }
            }

            @Override
            protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
                String[] values = value.toString().split("\t");
                if (selectKeyOp == null) {
                    context.write(new Text("1"), new Text(String.join("\t", selectValueOp.map(values))));
                } else {
                    context.write(new Text(String.join("\t", selectKeyOp.map(values))), new Text(String.join("\t", selectValueOp.map(values))));
                }
            }
        }.getClass();
        Class reducerClass = new Reducer<Text, Text, Text, Text>() {
            private FuncOperator funcOp;
            private SetKeyOperator setKeyOp;

            @Override
            protected void setup(Context context) throws IOException, InterruptedException {
                super.setup(context);
                Configuration conf = context.getConfiguration();
                String funcOpConfig = conf.get("job.reduce.func");
                funcOp = new FuncOperator();
                funcOp.fromString(funcOpConfig);
                setKeyOp = new SetKeyOperator();
            }

            @Override
            protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
                Pair<Text, Text> result = setKeyOp.format(funcOp.map(values));
                context.write(result.getKey(), result.getValue());
            }
        }.getClass();
        Configuration conf = new Configuration();
        conf.set("job.map.selectValue", task.selectValueOperator.toString());
        if (task.selectKeyOperator != null) {
            conf.set("job.map.selectKey", task.selectKeyOperator.toString());
        }
        conf.set("job.reduce.func", task.funcOperator.toString());
        Job job = Job.getInstance(conf);
        job.setJarByClass(GroupByFuncTaskUtil.class);
        job.setMapperClass(mapperClass);
        job.setReducerClass(reducerClass);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
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
