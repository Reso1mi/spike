package top.imlgw.spike.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Random;

public class MD5Util {
	
	public static String md5(String src) {
		return DigestUtils.md5Hex(src);
	}
	
	private static final String salt = "reso1mi";

	//第一层MD5 输入--> 表单(相对安全)
	public static String inputPassToFormPass(String inputPass) {
		String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
		System.out.println(str);
		return md5(str);
	}

	//第二层MD5 表单--> dbPass
	public static String formPassToDBPass(String formPass, String salt) {
		String str = ""+salt.charAt(1)+salt.charAt(3) + formPass +salt.charAt(4) + salt.charAt(5);
		return md5(str);
	}

    //用户输入（明文）--> dbPass
	public static String inputPassToDbPass(String inputPass, String dbSalt) {
		String formPass = inputPassToFormPass(inputPass);
		String dbPass = formPassToDBPass(formPass,dbSalt);
		return dbPass;
	}

	//生成dbSalt
    public static String randomSalt(int count){
	    //RandomStringUtils.random(6); 乱码
        String chars="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        //线程安全
        StringBuffer sb=new StringBuffer();
        for (int i=0;i<count;i++){
            sb.append(chars.charAt(random.nextInt(62)));
        }
        return  sb.toString();
	}
	
	public static void main(String[] args) {
	    System.out.println(inputPassToFormPass("123456"));
        System.out.println(randomSalt(6));
	}
	
}
