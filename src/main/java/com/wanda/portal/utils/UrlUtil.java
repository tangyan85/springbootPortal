package com.wanda.portal.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;

public class UrlUtil {
	
	public static String getHost(String urlstr){
		URL url=null;
		try {
			url = new URL(urlstr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url.getHost();
	}
	
	public static String getProtocol(String urlstr){
		URL url=null;
		try {
			url = new URL(urlstr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url.getProtocol();
	}
	
	public static String getPath(String urlstr){
		URL url=null;
		try {
			url = new URL(urlstr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url.getPath();
	}
	
	public static String getContext(String urlstr){
		URL url=null;
		try {
			url = new URL(urlstr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String path=url.getPath();
		if(StringUtils.isNotBlank(path)){
			if(path.startsWith("/")){
				path=path.substring(1);
			}
			return path.split("/")[0];
		}
		return path;
	}
	
	public static int getPort(String urlstr){
		URL url=null;
		try {
			url = new URL(urlstr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url.getPort();
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		URL url = new URL("//www.w3cschool.cn/html/html-tutorial.html");
		System.out.println("URL 是 " + url.toString());
		System.out.println("协议是 " + url.getProtocol());
		System.out.println("文件名是 " + url.getFile());
		System.out.println("主机是 " + url.getHost());
		System.out.println("路径是 " + url.getPath());
		System.out.println("端口号是 " + url.getPort());
		System.out.println("默认端口号是 " + url.getDefaultPort());
	}

}
