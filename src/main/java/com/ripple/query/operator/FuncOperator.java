package com.ripple.query.operator;

import com.ripple.database.Attribute;
import com.ripple.database.function.Function;
import com.ripple.database.value.Value;
import org.apache.hadoop.io.Text;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FuncOperator {
    private static class FuncInfo {
        Function function;
        Class type;

        public String toString() {
            StringBuilder builder = new StringBuilder()
                    .append(function == null ? "null" : function.getClass().getName()).append(' ')
                    .append(type.getName());
            return builder.toString();
        }

        public void fromString(String config) {
            String[] configs = config.split(" ");
            try {
                type = Class.forName(configs[1]);
                if (configs[0].equals("null")) {
                    function = null;
                } else {
                    Class funcClass = Class.forName(configs[0]);
                    Constructor funcConstructor = funcClass.getDeclaredConstructor();
                    funcConstructor.setAccessible(true);
                    function = (Function) funcConstructor.newInstance();
                }
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e.getClass().getName() + " " + e.getCause() + " " + config);
            }
        }

        public String map(List<String> values) {
            if (function == null) {
                return values.get(0);
            }
            Value[] realValues = new Value[values.size()];
            try {
                Constructor valueConstructor = type.getDeclaredConstructor(String.class);
                for (int i = 0; i < values.size(); ++i) {
                    realValues[i] = (Value) valueConstructor.newInstance(values.get(i));
                }
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e.getClass().getName() + " " + e.getCause());
            }
            return function.map(realValues).toString();
        }
    }

    private List<FuncInfo> funcInfos;

    public String toString() {
        return String.join("\t", funcInfos.stream().map(FuncInfo::toString).collect(Collectors.toList()));
    }

    public void fromString(String configs) {
        funcInfos = new ArrayList<>();
        for (String config : configs.split("\t")) {
            FuncInfo funcInfo = new FuncInfo();
            funcInfo.fromString(config);
            funcInfos.add(funcInfo);
        }
    }

    public void setup(List<Attribute> attributes) {
        funcInfos = new ArrayList<>();
        for (Attribute attribute : attributes) {
            FuncInfo funcInfo = new FuncInfo();
            funcInfo.function = attribute.getFunction();
            funcInfo.type = attribute.getType();
            funcInfos.add(funcInfo);
        }
    }

    public String[] map(Iterable<Text> values) {
        List<List<String>> columns = new ArrayList<>();
        for (int i = 0; i < funcInfos.size(); ++i) {
            columns.add(new ArrayList<>());
        }
        for (Text value : values) {
            String[] tmp = value.toString().split("\t");
            for (int i = 0; i < funcInfos.size(); ++i) {
                columns.get(i).add(tmp[i]);
            }
        }
        String[] result = new String[funcInfos.size()];
        for (int i = 0; i < funcInfos.size(); ++i) {
            result[i] = funcInfos.get(i).map(columns.get(i));
        }
        return result;
    }
}
