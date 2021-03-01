package com.naown.utils;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 获得随机盐
 * @USER: chenjian
 * @DATE: 2021/2/19 23:54 周五
 **/
public class SaltUtils implements ByteSource, Serializable {

    /**
     * 返回随机盐
     * @param n 返回几位的随机盐 如果为空则返回8位随机盐
     * @return
     */
    public static String getSalt(Integer n){
        if (n==null || n==1){
            return new SecureRandomNumberGenerator().nextBytes(4).toHex();
        }
        Double floor = Math.floor(n / 2);
        return new SecureRandomNumberGenerator().nextBytes(floor.intValue()).toHex();
    }

    public static String getSalt(){
        return getSalt(null);
    }

    /**
     * 实现盐的序列号
     * 直接拷贝的SimpleByteSource
     * @param string
     */
    private byte[] bytes;
    private String cachedHex;
    private String cachedBase64;

    public SaltUtils(){
        super();
    }
    public SaltUtils(byte[] bytes) {
        this.bytes = bytes;
    }

    public SaltUtils(char[] chars) {
        this.bytes = CodecSupport.toBytes(chars);
    }

    public SaltUtils(String string) {
        this.bytes = CodecSupport.toBytes(string);
    }

    public SaltUtils(ByteSource source) {
        this.bytes = source.getBytes();
    }

    public SaltUtils(File file) {
        this.bytes = (new SaltUtils.BytesHelper()).getBytes(file);
    }

    public SaltUtils(InputStream stream) {
        this.bytes = (new SaltUtils.BytesHelper()).getBytes(stream);
    }

    public static boolean isCompatible(Object o) {
        return o instanceof byte[] || o instanceof char[] || o instanceof String || o instanceof ByteSource || o instanceof File || o instanceof InputStream;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public boolean isEmpty() {
        return this.bytes == null || this.bytes.length == 0;
    }

    public String toHex() {
        if (this.cachedHex == null) {
            this.cachedHex = Hex.encodeToString(this.getBytes());
        }

        return this.cachedHex;
    }

    public String toBase64() {
        if (this.cachedBase64 == null) {
            this.cachedBase64 = Base64.encodeToString(this.getBytes());
        }

        return this.cachedBase64;
    }

    public String toString() {
        return this.toBase64();
    }

    public int hashCode() {
        return this.bytes != null && this.bytes.length != 0 ? Arrays.hashCode(this.bytes) : 0;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof ByteSource) {
            ByteSource bs = (ByteSource)o;
            return Arrays.equals(this.getBytes(), bs.getBytes());
        } else {
            return false;
        }
    }

    private static final class BytesHelper extends CodecSupport {
        private BytesHelper() {
        }

        public byte[] getBytes(File file) {
            return this.toBytes(file);
        }

        public byte[] getBytes(InputStream stream) {
            return this.toBytes(stream);
        }
    }
}
