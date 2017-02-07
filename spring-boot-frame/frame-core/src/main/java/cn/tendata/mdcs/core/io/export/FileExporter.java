package cn.tendata.mdcs.core.io.export;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import cn.tendata.mdcs.core.io.FieldDescriptor;

public interface FileExporter {
    
    void export(OutputStream stream, Collection<?> dataCol, List<FieldDescriptor> fieldDescriptors) throws IOException;
    void export(File file, Collection<?> dataCol, List<FieldDescriptor> fieldDescriptors) throws IOException;
}
