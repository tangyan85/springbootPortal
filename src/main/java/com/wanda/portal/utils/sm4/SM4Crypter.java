package com.wanda.portal.utils.sm4;
import com.wanda.portal.utils.Base64;



public class SM4Crypter{
	public static String encrypt(String msg,String key,String cbc_iv){
		try {
			SM4 sm4 = new SM4();
			SM4Context mSM4Context=new SM4Context();
			mSM4Context.isPadding=true;
			mSM4Context.mode=SM4.SM4_ENCRYPT;
			//设置密钥
			sm4.sm4_setkey_enc(mSM4Context, key.getBytes());
			byte[] result = sm4.sm4_crypt_cbc(mSM4Context, cbc_iv.getBytes(), msg.getBytes("UTF-8"));
			if(result==null) {
				throw new Exception("SM4加密失败！");
			}
			return Base64.encodeToString(result, Base64.NO_WRAP);
		} catch ( Exception e) {
			e.printStackTrace();
			return "ERROR";
		}
	}
	public static String decrypt(String msg,String key,String cbc_iv){
		try {
			byte[] enc=Base64.decode(msg.getBytes(), Base64.NO_WRAP);
			SM4 sm4 = new SM4();
			SM4Context mSM4Context=new SM4Context();
			mSM4Context.isPadding=true;
			mSM4Context.mode=SM4.SM4_DECRYPT;
			//设置密钥
			sm4.sm4_setkey_dec(mSM4Context, key.getBytes());
			byte[] dec= sm4.sm4_crypt_cbc(mSM4Context, cbc_iv.getBytes(), enc);
			return new String(dec, "UTF-8");
		} catch ( Exception e) {
			e.printStackTrace();
			return "ERROR";
		}
	}


}
