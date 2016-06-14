package tedu.sheng.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;





public class HttpUtils {
	

	public static String getStr(InputStream is){

		StringBuffer buffer=new StringBuffer();
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String line=null;
		try {
			while((line=br.readLine())!=null){
                buffer.append(line);
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public static InputStream  getIs(String path){
		
		InputStream is=null;
		try {
			URL url=new URL(path);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			is=conn.getInputStream();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return is;
		
	}

	public static InputStream  postIs(String path,String param){
		InputStream is=null;
		try {
			URL url=new URL(path);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			OutputStream os=conn.getOutputStream();
			os.write(param.getBytes("utf-8"));
			os.flush();
			is=conn.getInputStream();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return is;
	}
	
	

}
