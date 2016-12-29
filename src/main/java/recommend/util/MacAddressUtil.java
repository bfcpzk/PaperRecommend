package recommend.util;
import java.math.BigInteger;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.Enumeration;
/**
 * Created by zhaokangpan on 2016/12/28.
 */
public class MacAddressUtil {

    public String getMacAddress(){
        try {
            Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
            StringBuilder builder = new StringBuilder();
            while (el.hasMoreElements()) {
                byte[] mac = el.nextElement().getHardwareAddress();
                if (mac == null){
                    continue;
                }
                if(builder.length() > 0){
                    builder.append(",");
                }
                for (byte b : mac) {

                    //convert to hex string.
                    String hex = Integer.toHexString(0xff & b).toUpperCase();
                    if(hex.length() == 1){
                        hex  = "0" + hex;
                    }
                    builder.append(hex);
                    builder.append("-");
                }
                builder.deleteCharAt(builder.length() - 1);
            }

            if(builder.length() == 0){
                System.out.println("Sorry, can't find your MAC Address.");
                return "";
            }else{
                System.out.println("Your MAC Address is " + builder.toString());
                return builder.toString();
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
        return "";
    }

    public String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        MacAddressUtil mau = new MacAddressUtil();
        String mac = mau.getMacAddress();
        System.out.println(mac);
        System.out.println(mau.getMD5(mac));
    }
}
