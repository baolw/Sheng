package com.tedu.music.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


import com.tedu.music.util.*;

/**
 * @author pjy
 *封装一些静态方法供其他类调用
 *
 *处理网络访问相关
 *get /post
 */
public class HttpUtils {
	
	/**
	 * 将输入流转换成字符串
	 * 
	 */
	public static String getStr(InputStream is){
		StringBuffer buffer=new StringBuffer();
		//流的包装
		//转换流
		InputStreamReader  isr=new InputStreamReader(is);
		
		BufferedReader br=new BufferedReader(isr);
		try {
			String line=br.readLine();
			while (line!=null) {
				buffer.append(line);
				line=br.readLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();

	}
	/**
	 * @return
	 * get 方式获取输入流
	 * 
	 */
	public static InputStream  getIs(String path){
		
		InputStream is=null;
		try {
			//url
			URL url=new URL(path);
			//conn
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			//请求方法
			conn.setRequestMethod("GET");
			is=conn.getInputStream();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return is;
		
	}
	/**
	 * @param path
	 * @param param
	 * @return
	 * 通过post请求获取输入流
	 */
	public static InputStream  postIs(String path,String param){
		InputStream is=null;
		try {
			//url
			URL url=new URL(path);
			//conn
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			//请求方法
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			OutputStream os=conn.getOutputStream();
			os.write(param.getBytes("utf-8"));
			os.flush();
			is=conn.getInputStream();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return is;
	}
	
	

}
