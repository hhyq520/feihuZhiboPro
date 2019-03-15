package cn.feihutv.zhibofeihu.data.network.socket.xsocket.utils;

import java.io.UnsupportedEncodingException;

public class CharsetUtil {
    public static final String UTF_8 = "UTF-8";
    public static final String GBK = "GBK";


    public static byte[] stringToData(String string, String charsetName) {
        if (string != null) {
            try {
                return string.getBytes(charsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String dataToString(byte[] data, String charsetName) {
        if (data != null) {
            try {
                return new String(data, charsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] int2byteArray(int i){
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }

    public static byte[] appendByteArray(byte[] target,byte[] addValue){
        byte[] ret = new byte[target.length+addValue.length];
        System.arraycopy(target,0,ret,0,target.length);
        System.arraycopy(addValue,0,ret,target.length,addValue.length);
        return ret;
    }
}