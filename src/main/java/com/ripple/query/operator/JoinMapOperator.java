package com.ripple.query.operator;

import com.ripple.database.Attribute;
import com.ripple.database.Condition;
import com.ripple.query.task.MapReduceTask;
import com.ripple.util.Pair;
import org.apache.hadoop.io.Text;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JoinMapOperator {
    public String pathA;
    public String pathB;
    public Integer[] indexsA;
    public Integer[] indexsB;

    @Override
    public String toString() {
        String[] tmp = new String[4];
        tmp[0] = pathA;
        tmp[1] = pathB;
        tmp[2] = String.join(" ",
                Arrays.stream(indexsA).map(i->i.toString()).collect(Collectors.toList()));
        tmp[3] = String.join(" ",
                Arrays.stream(indexsB).map(i->i.toString()).collect(Collectors.toList()));
        return String.join("\t", tmp);
    }

    public void fromString(String config) {
        String[] tmp = config.split("\t");
        pathA = tmp[0];
        pathB = tmp[1];
        indexsA = Arrays.stream(tmp[2].split(" ")).map(Integer::parseInt).toArray(Integer[]::new);
        indexsB = Arrays.stream(tmp[3].split(" ")).map(Integer::parseInt).toArray(Integer[]::new);
    }

    public void setup(MapReduceTask a, MapReduceTask b, List<Condition> conditions) {
        String[] pathsA = a.outputPath.split("/");
        pathA = pathsA[pathsA.length - 1];
        String[] pathsB = b.outputPath.split("/");
        pathB = pathsB[pathsB.length - 1];
        indexsA = new Integer[conditions.size()];
        indexsB = new Integer[conditions.size()];
        for (int i = 0; i < conditions.size(); ++i) {
            Attribute left = conditions.get(i).getLeftAttribute();
            Attribute right = conditions.get(i).getRightAttribute();
            for (int index = 0; index < a.attributes.size(); ++index) {
                Attribute cur = a.attributes.get(index);
                if (cur.equals(left) || cur.equals(right)) {
                    indexsA[i] = index;
                }
            }
            for (int index = 0; index < b.attributes.size(); ++index) {
                Attribute cur = b.attributes.get(index);
                if (cur.equals(left) || cur.equals(right)) {
                    indexsB[i] = index;
                }
            }
        }
    }

    public Pair<Text, Text> map(String flag, String[] input) {
        String[] key = null;
        if (indexsA.length == 0) {
            key = new String[1];
            key[0] = "1";
        } else {
            Integer[] indexs = flag.equals("A") ? indexsA : indexsB;
            key = new String[indexs.length];
            for (int i = 0; i < indexs.length; ++i)
                key[i] = input[indexs[i]];
        }
        String[] value = new String[input.length + 1];
        value[0] = flag;
        for (int i = 0; i < input.length; ++i)
            value[i + 1] = input[i];
        return new Pair<>(new Text(String.join("\t", key)), new Text(String.join("\t", value)));
    }
}
