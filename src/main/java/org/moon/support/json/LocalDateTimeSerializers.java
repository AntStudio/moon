package org.moon.support.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * LocalDateTime序列化
 * @author:Gavin
 * @date 2015/6/18
 */
public class LocalDateTimeSerializers extends StdSerializer<LocalDateTime> implements ContextualSerializer {
    private static final long serialVersionUID = 1L;

    public LocalDateTimeSerializers(Class<LocalDateTime> vc) {
        super(vc);
    }

    public LocalDateTimeSerializers() {
        super((Class)null);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if(Objects.nonNull(property)) {
            JsonFormat.Value format = prov.getAnnotationIntrospector().findFormat((Annotated) property.getMember());
            return new LocalDateTimeSerializer(format);
        }else{
            return new LocalDateTimeSerializer(null);
        }
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        System.out.println("sss");
    }


    public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime>{
        private JsonFormat.Value jsonFormat;

        public LocalDateTimeSerializer(JsonFormat.Value jsonFormat){
            this.jsonFormat = jsonFormat;
        }

        @Override
        public void serialize(LocalDateTime dateTime, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
            if(Objects.nonNull(jsonFormat) && Objects.nonNull(jsonFormat.getPattern())){
                generator.writeString(dateTime.format(DateTimeFormatter.ofPattern(jsonFormat.getPattern())));
            }else{
                generator.writeString(dateTime.toString());
            }

        }
    }
}
