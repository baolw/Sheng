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
 *��װһЩ��̬���������������
 *
 *��������������
 *get /post
 */
public class HttpUtils {
	
	/**
	 * ��������ת�����ַ���
	 * 
	 */
	public static String getStr(InputStream is){
		StringBuffer buffer=new StringBuffer();
		//���İ�װ
		//ת����
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
	 * get ��ʽ��ȡ������
	 * 
	 */
	public static InputStream  getIs(String path){
		
		InputStream is=null;
		try {
			//url
			URL url=new URL(path);
			//conn
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			//���󷽷�
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
	 * ͨ��post�����ȡ������
	 */
	public static InputStream  postIs(String path,String param){
		InputStream is=null;
		try {
			//url
			URL url=new URL(path);
			//conn
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			//���󷽷�
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
