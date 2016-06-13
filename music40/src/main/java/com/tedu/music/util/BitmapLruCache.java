package com.tedu.music.util;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageCache { 
	    // LruCache ԭ��Cache����һ��ǿ��������������������ÿ��Item�����ʵ�ʱ�򣬴�Item�ͻ��ƶ������е�ͷ���� ��cache������ʱ������µ�itemʱ���ڶ���β����item�ᱻ���ա�  
	    // ���ͣ�������ָ���ڴ�ֵ���Ƴ���������õ�ͼƬ�ڴ�  
	    public static int getDefaultLruCacheSize() {  
	        // �õ�����ڴ�  
	        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);  
	        // �õ��ڴ�İ˷�֮һ����ͼƬ�ڴ滺��  
	        final int cacheSize = maxMemory / 8;  
	  
	        return cacheSize;  
	    }  
	  
	    public BitmapLruCache() {  
	        this(getDefaultLruCacheSize());  
	    }  
	  
	    public BitmapLruCache(int sizeInKiloBytes) {  
	        super(sizeInKiloBytes);  
	    }  
	  
	    @Override  
	    protected int sizeOf(String key, Bitmap value) {  
	        return value.getRowBytes() * value.getHeight() / 1024;  
	    }  
	  
	    @Override  
	    public Bitmap getBitmap(String url) {  
	        return get(url);  
	    }  
	  
	    @Override  
	    public void putBitmap(String url, Bitmap bitmap) {  
	        put(url, bitmap);  
	    }  
	 
}
