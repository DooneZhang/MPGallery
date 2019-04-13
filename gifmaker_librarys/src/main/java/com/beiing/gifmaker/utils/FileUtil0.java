package com.beiing.gifmaker.utils;

import java.io.File;

public class FileUtil0 {public static String[] getImageNames(String folderPath) {
    File file01 = new File(folderPath);
    String[] files01 = file01.list();

    int imageFileNums = 0;
    for (int i = 0; i < files01.length; i++) {
        File file02 = new File(folderPath + "/" + files01[i]);

        if (!file02.isDirectory()) {

            if (isImageFile(file02.getName())) {

                imageFileNums++;
            }
        }
    }

    String[] files02 = new String[imageFileNums];

    int j = 0;
    for (int i = 0; i < files01.length; i++) {
        File file02 = new File(folderPath + "/" + files01[i]);

        if (!file02.isDirectory()) {

            if (isImageFile(file02.getName())) {
                files02[j] = file02.getName();
                j++;
            }
        }
    }
    return files02;
}

    private static boolean isImageFile(String fileName) {
        String fileEnd = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length());
        if (fileEnd.equalsIgnoreCase("jpg")) {
            return false;
        } else if (fileEnd.equalsIgnoreCase("png")) {
            return false;
        } else if (fileEnd.equalsIgnoreCase("gif")) {
            return true;
        }else if (fileEnd.equalsIgnoreCase("bmp")) {
            return false;
        } else {
            return false;
        }
    }

}
