package com.ripple.query.selectfilter;

import com.ripple.database.Attribute;
import com.ripple.database.binop.BinOp;
import com.ripple.database.Condition;
import com.ripple.query.MapOperator;
import com.ripple.util.Pair;
import com.ripple.database.value.Value;
import org.apache.hadoop.io.Text;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterMapOperator {
    private static class Filter {
        Pair<Integer, Class> leftAttribute;
        BinOp binOp;
        boolean rightIsAttribute;
        Pair<Integer, Class> rightAttribute;
        Value rightValue;

        public String toString() {
            StringBuilder builder = new StringBuilder()
                    .append(leftAttribute.getKey())
                    .append(' ').append(leftAttribute.getValue().getName())
                    .append(' ').append(binOp.getClass().getName())
                    .append(' ').append(rightIsAttribute);
            if (rightIsAttribute)
                builder.append(' ').append(rightAttribute.getKey())
                .append(' ').append(rightAttribute.getValue().getName());
            else
                builder.append(' ').append(rightValue.toString())
                .append(' ').append(rightValue.getClass().getName());
            return builder.toString();
        }

        public void fromString(String config) {
            String[] configs = config.split(" ");
            try {
                leftAttribute = new Pair<>(new Integer(configs[0]), Class.forName(configs[1]));
                Class opClass = Class.forName(configs[2]);
                Constructor opConstructor = opClass.getDeclaredConstructor();
                opConstructor.setAccessible(true);
                binOp = (BinOp) opConstructor.newInstance();
                // todo binOp = BinOp.gtOp;
                rightIsAttribute = Boolean.parseBoolean(configs[3]);
                if (rightIsAttribute) {
                    rightAttribute = new Pair<>(new Integer(configs[4]), Class.forName(configs[5]));
                } else {
                    Class valueClass = Class.forName(configs[5]);
                    Constructor valueConstructor = valueClass.getDeclaredConstructor(String.class);
                    rightValue = (Value) valueConstructor.newInstance(configs[4]);
                }
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e.getClass().getName() + " " + e.getCause() + " " + config);
            }
        }

        public boolean filter(String[] values) {
            try {
                Constructor leftConstructor = leftAttribute.getValue().getDeclaredConstructor(String.class);
                Value leftValue = (Value) leftConstructor.newInstance(values[leftAttribute.getKey()]);
                Value value = rightValue;
                if (rightIsAttribute) {
                    Constructor rightConstructor = rightAttribute.getValue().getDeclaredConstructor(String.class);
                    value = (Value) rightConstructor.newInstance(values[rightAttribute.getKey()]);
                }
                return binOp.apply(leftValue, value);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getCause());
            }
        }
    }

    private List<Filter> filters;

    public String toString() {
        return String.join("\t", filters.stream().map(Filter::toString).collect(Collectors.toList()));
    }

    public void fromString(String configs) {
        filters = new ArrayList<>();
        for (String config : configs.split("\t")) {
            Filter filter = new Filter();
            filter.fromString(config);
            filters.add(filter);
        }
    }

    public void setup(List<Condition> conditions, List<Attribute> attrs) {
        filters = new ArrayList<>();
        for (Condition condition : conditions) {
            Filter filter = new Filter();
            Attribute leftAttribute = condition.getLeftAttribute();
            int leftIndex = -1;
            for (int i = 0; i < attrs.size(); ++i) {
                if (leftAttribute.equals(attrs.get(i))) {
                    leftIndex = i;
                }
            }
            filter.leftAttribute = new Pair<>(leftIndex, leftAttribute.getType());
            filter.binOp = condition.getBinOp();
            filter.rightIsAttribute = condition.isRightIsAttribute();
            if (filter.rightIsAttribute) {
                Attribute rightAttribute = condition.getRightAttribute();
                int rightIndex = -1;
                for (int i = 0; i < attrs.size(); ++i) {
                    if (rightAttribute.equals(attrs.get(i))) {
                        rightIndex = i;
                    }
                }
                filter.rightAttribute = new Pair<>(rightIndex, rightAttribute.getType());
            } else {
                filter.rightValue = condition.getRightValue();
            }
            filters.add(filter);
        }
    }

    public String[] map(String[] input) {
        for (Filter filter : filters)
            if (!filter.filter(input))
                return null;
        return input;
    }
}
