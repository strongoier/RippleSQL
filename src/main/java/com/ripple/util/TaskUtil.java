package com.ripple.util;

import com.ripple.query.selectfilter.SelectFilterTask;
import com.ripple.query.selectfilter.FormatMapOperator;
import com.ripple.query.selectfilter.MapOperator;
import com.ripple.query.selectfilter.ReduceOperator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskUtil {
    public static void runTasks(List<SelectFilterTask> tasks) throws IOException {
        for (SelectFilterTask task : tasks) {
            Class mapperClass = new Mapper<Object, Text, Text, Text>() {
                private List<MapOperator> mapOps;
                private FormatMapOperator formatOp;

                @Override
                protected void setup(Context context) throws IOException, InterruptedException {
                    super.setup(context);
                    mapOps = new ArrayList<>();
                    formatOp = new FormatMapOperator();
                    Configuration conf = context.getConfiguration();
                    String[] mapOpClassNames = conf.get("job.map.operators").split("#");
                    String[] mapOpConfigs = conf.get("job.map.configs").split("#");
                    String formatOpConfig = conf.get("job.map.formatConfig");
                    try {
                        for (int i = 0; i < mapOpClassNames.length; ++i) {
                            MapOperator operator = (MapOperator) Class.forName(mapOpClassNames[i]).newInstance();
                            operator.fromString(mapOpConfigs[i]);
                            mapOps.add(operator);
                        }
                        if (formatOpConfig != null)
                            formatOp.fromString(formatOpConfig);
                    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                        throw new RuntimeException(e.getCause());
                    }
                }

                @Override
                protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
                    List<Pair<Object, Text>> keyValues = new ArrayList<>();
                    keyValues.add(new Pair<>(key, value));
                    for (MapOperator op : mapOps) {
                        keyValues = op.map(keyValues);
                    }
                    List<Pair<Text, Text>> results = formatOp.format(keyValues);
                    for (Pair<Text, Text> result : results) {
                        context.write(result.getKey(), result.getValue());
                    }
                }
            }.getClass();
            Class reducerClass = new Reducer<Text, Text, Text, Text>() {
                private ReduceOperator reduceOp;

                @Override
                protected void setup(Context context) throws IOException, InterruptedException {
                    super.setup(context);
                    Configuration conf = context.getConfiguration();
                    String reduceOpClassName = conf.get("job.reduce.operator");
                    String reduceOpConfig = conf.get("job.reduce.config");
                    try {
                        reduceOp = (ReduceOperator) Class.forName(reduceOpClassName).newInstance();
                        reduceOp.fromString(reduceOpConfig);
                    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                        throw new RuntimeException(e.getCause());
                    }
                }

                @Override
                protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
                    reduceOp.reduce(key, values, context);
                }
            }.getClass();
            Configuration conf = new Configuration();
            conf.set("job.map.operators", String.join("#", task.mapOperators.stream().map(MapOperator::getClass).map(Class::getName).collect(Collectors.toList())));
            conf.set("job.map.configs", String.join("#", task.mapOperators.stream().map(MapOperator::toString).collect(Collectors.toList())));
            if (task.mapFormatConfig != null)
                conf.set("job.map.formatConfig", task.mapFormatConfig.toString());
            conf.set("job.reduce.operator", task.reduceOperator.getClass().getName());
            conf.set("job.reduce.config", task.reduceOperator.toString());
            Job job = Job.getInstance(conf);
            job.setJarByClass(SelectFilterTask.class);
            job.setMapperClass(mapperClass);
            job.setReducerClass(reducerClass);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);
            for (String path : task.inputPaths)
                FileInputFormat.addInputPath(job, new Path(path));
            FileOutputFormat.setOutputPath(job, new Path(task.outputPath));
            try {
                job.waitForCompletion(true);
            } catch (InterruptedException | ClassNotFoundException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }
}
