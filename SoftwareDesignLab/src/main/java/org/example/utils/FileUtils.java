package org.example.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class FileUtils {
    public static final String recent_workspace_path = "run_time_files/recent_workspace.json";
    public static void check_and_create_file_if_not_exist(String file_path){
        File check_file= new File(file_path);
        //检测文件是否存在
        if(!check_file.exists()){
            try {
                if(!check_file.createNewFile()){
                    System.out.println("异常，文件创建失败！");
                    System.exit(-1);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getNameFromPath(String file_path){
        Path path = FileSystems.getDefault().getPath(file_path);
        return removeExtension( path.getFileName().toString());
    }

    public static String removeExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(0, lastDotIndex);
        } else {
            return fileName;
        }
    }
}
