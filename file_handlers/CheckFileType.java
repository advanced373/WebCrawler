/*
 * CheckFileType
 *
 * Version 1.0
 *
 * All rights reserved.
 */
package file_handlers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Class implemented for checking file type based on magic number
 */
public class CheckFileType {
    /**
     * Predefined magic numbers depending on file type
     */
    public enum FileType {
        DOC_html("3C21444F43545950452068746D6C3E"),
        DOC_HTML("3C21444F43545950452048544D4C3E"),
        HTML("68746D6C3E"),
        PHP("3C3F706870");

        private String value = "";

        private FileType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    /**
     * Method returning file type
     * @param filePath path to file that need to be checked
     * @return
     * @throws IOException
     */
    public static FileType getType(String filePath) throws IOException {
        String fileHead = getFileHeader(filePath);

        if (fileHead != null && fileHead.length() > 0) {
            fileHead = fileHead.toUpperCase();
            FileType[] fileTypes = FileType.values();
            for (FileType type : fileTypes) {
                if (fileHead.startsWith(type.getValue())) {
                    return type;
                }
            }
        }
        return null;
    }

    /**
     * Returns first 28 bytes from the file
     * When testing, I observed that some files come with 0A at the begining
     * of the file so ignored them
     * @param filePath path to file that need to be checked
     * @return
     * @throws IOException
     */
    private static String getFileHeader(String filePath) throws IOException {
        byte[] b = new byte[28];
        InputStream inputStream = null;
        byte[] filteredB;
        int size=0;
        try {
            inputStream = new FileInputStream(filePath);
            inputStream.read(b, 0, 28);
            for(byte byteValue:b){
                if(byteValue == 0x0A)
                {
                    size++;
                }
                else
                    break;
            }
            filteredB = Arrays.copyOfRange(b,size,27);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return bytesToHex(filteredB);
    }

    /**
     * Method that makes conversion from bytes array to hex value
     * @param src byte array that has to be converted
     * @return
     */
    public static String bytesToHex(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
