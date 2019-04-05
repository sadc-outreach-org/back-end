package backend.Utility;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
 
  private static final long serialVersionUID = -6786385793860085002L;
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeUtility.simpleDateTimeFormat);
     
  public CustomLocalDateTimeSerializer() {
    this(null);
  }

  public CustomLocalDateTimeSerializer(Class<LocalDateTime> t) {
    super(t);
  }

  @Override
  public void serialize
    (LocalDateTime date, JsonGenerator gen, SerializerProvider arg2)
    throws IOException, JsonProcessingException {
      final String dateString = date.format(this.formatter);
      gen.writeString(dateString);
  }
}