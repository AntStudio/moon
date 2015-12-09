package org.moon.support.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import net.sf.cglib.core.Local;
import org.springframework.util.Assert;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author:Gavin
 * @date 2015/5/6 0006
 */
public class LocalDateTimeDeserializers extends StdDeserializer<LocalDateTime> implements ContextualDeserializer {
    private static final long serialVersionUID = 1L;

    public LocalDateTimeDeserializers(Class<?> vc) {
        super(vc);
    }

    public LocalDateTimeDeserializers() {
        super((Class)null);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return null;
    }

    public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime>{
        private JsonFormat.Value jsonFormat;

        public LocalDateTimeDeserializer(JsonFormat.Value jsonFormat){
            this.jsonFormat = jsonFormat;
        }

        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
            switch(parser.getCurrentToken())
            {
                case START_ARRAY:
                    if(parser.nextToken() == JsonToken.END_ARRAY)
                        return null;
                    int year = parser.getIntValue();

                    parser.nextToken();
                    int month = parser.getIntValue();

                    parser.nextToken();
                    int day = parser.getIntValue();

                    parser.nextToken();
                    int hour = parser.getIntValue();

                    parser.nextToken();
                    int minute = parser.getIntValue();

                    if(parser.nextToken() != JsonToken.END_ARRAY)
                    {
                        int second = parser.getIntValue();

                        if(parser.nextToken() != JsonToken.END_ARRAY)
                        {
                            int partialSecond = parser.getIntValue();
                            if(partialSecond < 1_000 &&
                                    !context.isEnabled(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS))
                                partialSecond *= 1_000_000; // value is milliseconds, convert it to nanoseconds

                            if(parser.nextToken() != JsonToken.END_ARRAY)
                                throw context.wrongTokenException(parser, JsonToken.END_ARRAY, "Expected array to end.");

                            return LocalDateTime.of(year, month, day, hour, minute, second, partialSecond);
                        }

                        return LocalDateTime.of(year, month, day, hour, minute, second);
                    }

                    return LocalDateTime.of(year, month, day, hour, minute);

                case VALUE_STRING:
                    String string = parser.getText().trim();
                    if(string.length() == 0)
                        return null;
                    if(Objects.nonNull(jsonFormat) && Objects.nonNull(jsonFormat.getPattern())){
                        return LocalDateTime.parse(string,DateTimeFormatter.ofPattern(jsonFormat.getPattern()));
                    }else{
                        return LocalDateTime.parse(string);
                    }
            }

            throw context.wrongTokenException(parser, JsonToken.START_ARRAY, "Expected array or string.");
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        if(Objects.nonNull(property)) {
            JsonFormat.Value format = ctxt.getAnnotationIntrospector().findFormat((Annotated) property.getMember());
            return new LocalDateTimeDeserializer(format);
        }else{
            return new LocalDateTimeDeserializer(null);
        }
    }
}
