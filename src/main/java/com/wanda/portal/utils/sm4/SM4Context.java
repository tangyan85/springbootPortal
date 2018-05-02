package com.wanda.portal.utils.sm4;

public class SM4Context {
	/**
	 * 加解密模式，SM4.SM4_ENCRYPT和SM4.SM4_DECRYPT
	 */
    public int mode;

    /**
     * 通过SM4方法设置的key值
     */
    public long[] sk;

    /**
     * 是否打开padding
     */
    public boolean isPadding;

    public SM4Context(){
        this.mode = SM4.SM4_ENCRYPT;//
        this.isPadding = true;
        this.sk = new long[32];
    }
}
