package com.ripple.util;

import com.ripple.mapreduce.Task;
import com.ripple.mapreduce.TaskInfo;
import com.ripple.sqloperator.FormatOperator;
import com.ripple.sqloperator.MapOperator;
import com.ripple.sqloperator.ReduceOperator;
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
    public static List<Job> createJob(List<TaskInfo> infos) throws IOException {
        List<Job> jobs = new ArrayList<>();
        for (TaskInfo info : infos) {
            Task task = new Task() {
                @Override
                public void setup() {
                    mapClass = new Mapper<Object, Text, Text, Text>() {
                        private List<MapOperator> operators;
                        private FormatOperator formatOperator;

                        @Override
                        protected void setup(Context context) throws IOException, InterruptedException {
                            super.setup(context);
                            operators = new ArrayList<>();
                            Configuration configuration = context.getConfiguration();
                            String[] classNames = configuration.get("job.map.operators").split("#");
                            String[] configs = configuration.get("job.map.configs").split("#");
                            String formatConfig = configuration.get("job.map.formatConfig");
                            try {
                                for (int i = 0; i < classNames.length; ++i) {
                                    MapOperator operator = (MapOperator) Class.forName(classNames[i]).newInstance();
                                    operator.fromString(configs[i]);
                                    operators.add(operator);
                                }
                                if (formatConfig != null)
                                    formatOperator.fromString(formatConfig);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException("ClassNotFoundException");
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("IllegalAccessException");
                            } catch (InstantiationException e) {
                                throw new RuntimeException("InstantiationException");
                            }
                        }

                        @Override
                        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
                            List<Pair<Object, Text>> pairs = new ArrayList<>();
                            pairs.add(new Pair<>(key, value));
                            for (MapOperator op : operators)
                                pairs = op.map(pairs);
                            List<Pair<Text, Text>> results = formatOperator.format(pairs);
                            for (Pair<Text, Text> result : results)
                                context.write(result.getKey(), result.getValue());
                        }
                    }.getClass();
                    reduceClass = new Reducer<Text, Text, Text, Text>() {
                        private ReduceOperator operator;

                        @Override
                        protected void setup(Context context) throws IOException, InterruptedException {
                            super.setup(context);
                            Configuration configuration = context.getConfiguration();
                            String className = configuration.get("job.reduce.operator");
                            String config = configuration.get("job.reduce.config");
                            try {
                                operator = (ReduceOperator) Class.forName(className).newInstance();
                                operator.fromString(config);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException("ClassNotFoundException");
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("IllegalAccessException");
                            } catch (InstantiationException e) {
                                throw new RuntimeException("InstantiationException");
                            }
                        }

                        @Override
                        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
                            operator.reduce(key, values, context);
                        }
                    }.getClass();
                }
            };
            task.setup();
            Configuration configuration = new Configuration();
            configuration.set("job.map.operators", String.join("#", info.mapOperators.stream().map(MapOperator::getClass).map(Class::getName).collect(Collectors.toList())));
            configuration.set("job.map.configs", String.join("#", info.mapOperators.stream().map(MapOperator::toString).collect(Collectors.toList())));
            if (info.mapFormatConfig != null)
                configuration.set("job.map.formatConfig", info.mapFormatConfig.toString());
            configuration.set("job.reduce.operator", info.reduceOperator.getClass().getName());
            configuration.set("job.reduce.config", info.reduceOperator.toString());
            Job job = Job.getInstance(configuration);
            job.setJarByClass(task.getClass());
            job.setMapperClass(task.getMapClass());
            job.setReducerClass(task.getReduceClass());
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);
            for (String path : info.inputPaths)
                FileInputFormat.addInputPath(job, new Path(path));
            FileOutputFormat.setOutputPath(job, new Path(info.outputPath));
            jobs.add(job);
        }
        return jobs;
    }
}
