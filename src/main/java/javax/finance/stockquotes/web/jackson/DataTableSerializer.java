package javax.finance.stockquotes.web.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.render.JsonRenderer;

import java.io.IOException;

public class DataTableSerializer extends StdSerializer<DataTable> {

    public DataTableSerializer() {
        this(null);
    }

    public DataTableSerializer(final Class<DataTable> t) {
        super(t);
    }

    @Override
    public void serialize(final DataTable dataTable, final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeRaw(JsonRenderer.renderDataTable(dataTable, true, false, false).toString());
    }

}
