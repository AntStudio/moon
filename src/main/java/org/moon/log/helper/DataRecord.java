package org.moon.log.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.moon.utils.Maps;
import org.moon.utils.Objects;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Supplier;

/**
 * 数据记录，主要用于日志处理数据变化的记录
 * @author:Gavin
 * @date 2015/6/9 0009
 */
public class DataRecord {

    private List<String> properties = new ArrayList<>();

    private boolean isArray = false;//记录内容为数组

    private boolean onlyContent = false;//是否仅仅显示内容

    private transient ObjectMapper objectMapper;

    public DataRecord property(String name, Object oldValue , Object newValue){
        properties.add(name+" : "+wrapValue(oldValue)+" --> "+wrapValue(newValue));
        return this;
    }

    public DataRecord property(String name, Object value){
        properties.add(name+" : "+wrapValue(value));
        return this;
    }

    public DataRecord property(Supplier<String> supplier){
        properties.add(supplier.get());
        return this;
    }

    /**
     * 设置数据记录内容，当重复调取该方法时，使用最后一次的内容
     * @param content
     * @return
     */
    public DataRecord content(Object content){
        try {
            if(objectMapper == null) {
                objectMapper = new ObjectMapper();
            }
            properties.add(0,objectMapper.writeValueAsString(content));
            onlyContent = true;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public DataRecord array(){
        isArray = true;
        return this;
    }

    @Override
    public String toString(){
        if(onlyContent){
            return properties.get(0);
        }
        StringJoiner sj = new StringJoiner(",\n  ", isArray?"[\n  ":"{\n  ", isArray?"\n]":"\n}");
        properties.stream().forEach(p->sj.add(p));
        return sj.toString();
    }

    public static DataRecord make(){
        return new DataRecord();
    }

    public static DataRecord make(Object content){
        return DataRecord.make().content(content);
    }

    /**
     * 包装参数值，如果为数组处理为[xx,xxx]
     * @param value
     * @return
     */
    private String wrapValue(Object value){
        if(Objects.isNull(value)){
            return "";
        }
        if(value instanceof Object[]){
            return Arrays.toString((Object[])value);
        }else{
            return value.toString();
        }
    }
}
