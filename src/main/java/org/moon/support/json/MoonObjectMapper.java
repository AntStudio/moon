package org.moon.support.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import java.time.LocalDateTime;

/**
 * @author:Gavin
 * @date 2015/5/6 0006
 */
public class MoonObjectMapper extends ObjectMapper {

    public MoonObjectMapper(){
        JSR310Module module = new JSR310Module();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializers());
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializers());
        registerModule(module);
    }

}
