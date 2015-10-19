package org.xiangbalao;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ResourceBundle;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;

public class DESUtils {
	private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish

	   //keybyte为加密密钥，长度为24字节
    //src为被加密的数据缓冲区（源）
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
       try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

            //加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
    /**
     * 加密
     * @param szSrc
     * @return
     */
    public static String encryptMode(String szSrc,Context context)throws Exception{
    	 byte[] _key = readCrt(context);
         byte[] keyBytes = new byte[24];
         System.arraycopy(_key, 32, keyBytes, 0,24);
         byte[] encoded = encryptMode(keyBytes, szSrc.getBytes()); 
         return byte2hex(encoded);
    }
    /**
     * 解密
     * @param szSrc
     * @return
     */
    public static String decryptMode(String szSrc,Context context)throws Exception{
    	 byte[] _key = readCrt(context);
         byte[] keyBytes = new byte[24];
         System.arraycopy(_key, 32, keyBytes, 0,24);
         byte[] encoded=hex2byte(szSrc);
         byte[] srcBytes = decryptMode(keyBytes, encoded);
         return new String(srcBytes);
    }

    //keybyte为加密密钥，长度为24字节
    //src为加密后的缓冲区
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {      
    try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

   /**
    * byte转为十六进制字符串
    * @param b
    * @return
    */
    public static String byte2hex(byte[] b) {
        String hs="";
        String stmp="";

        for (int n=0;n<b.length;n++) {
            stmp=(java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length()==1) hs=hs+"0"+stmp;
            else hs=hs+stmp;
            //if (n<b.length-1)  hs=hs+":";
        }
        return hs.toUpperCase();
    }
    /**
     * 十六进制字符串转byte数组
     * @param hex
     * @return
     */
    public static byte[] hex2byte(String hex){
    	int length=hex.length()/2;
    	char[] hexChars=hex.toCharArray();
    	byte[] d=new byte[length];
    	for (int i=0;i<length;i++){
    		int pos=i*2;
    		d[i]=(byte)(char2byte(hexChars[pos])<<4|char2byte(hexChars[pos+1]));
    	}
    	return d;
    }

    public static byte char2byte(char c){
    	return (byte)"0123456789ABCDEF".indexOf(c);
    }
    
    public static byte[] readCrt(Context context) throws Exception{
//    	ResourceBundle res = ResourceBundle.getBundle("perbank");
//    	String path = res.getString("secret");
    	String path = "file:///android_asset/logicm.crt";
//    	String path=DESUtils.class.getResource("/").getPath()+"crt/logicm.crt";
    	
    	byte[] Field = null;
    	InputStream file_inputstream=null;
    	try{
	    	CertificateFactory certificate_factory=CertificateFactory.getInstance("X.509");
//	    	File crtf = new File(path); 
	    	file_inputstream=context.getAssets().open("logicm.crt");
	    	X509Certificate  x509certificate=(X509Certificate)certificate_factory.generateCertificate(file_inputstream);
	    	//byte[] Field = x509certificate.getEncoded();
	    	Field = x509certificate.getPublicKey().getEncoded();
//	    	System.err.println(Field.length);
//	    	for(int n=0; n<Field.length; n++){
//	    	   System.err.println(Field[n]);
//	    	}
    	}finally{
    		if(file_inputstream!=null){
    			file_inputstream.close();
    		}
    	}
    	return Field;
    }
    
    public static byte[] readCrt(String path) throws Exception{
    	byte[] Field = null;
    	FileInputStream file_inputstream=null;
    	try{
	    	CertificateFactory certificate_factory=CertificateFactory.getInstance("X.509");
	    	File crtf = new File(path); 
	    	file_inputstream=new FileInputStream(crtf);
	    	X509Certificate  x509certificate=(X509Certificate)certificate_factory.generateCertificate(file_inputstream);
	    	Field = x509certificate.getPublicKey().getEncoded();
    	}finally{
    		if(null!=file_inputstream){
    			file_inputstream.close();
    		}
    	}
    	return Field;
    }
    
  
    
}
