package org.example.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.receiver.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkSpace {
    public Document document=new Document();
    public String workspace_name=null;
}
