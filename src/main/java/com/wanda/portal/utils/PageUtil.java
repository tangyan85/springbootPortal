package com.wanda.portal.utils;

public class PageUtil {
	public static int calStartIndex(int pageNo, int pageSize) {
		int start = (pageNo - 1) * pageSize;
		return ((start >= 0) ? start : 0);
	}

	public static int calEndIndex(int pageNo, int pageSize, int totalCount) {
		int end = calStartIndex(pageNo, pageSize) + pageSize - 1;
		return ((end >= totalCount) ? totalCount - 1 : end);
	}

	public static int calEndIndexBystart(int start, int pageSize, int totalCount) {
		int end = start + pageSize - 1;
		return ((end >= totalCount) ? totalCount - 1 : end);
	}

	public static int parsePageNo(String pageNo) {
		try {
			return Integer.parseInt(pageNo);
		} catch (Exception e) {
		}
		return 1;
	}

	public static int parsePageSize(String pageSize) {
		try {
			return Integer.parseInt(pageSize);
		} catch (Exception e) {
		}
		return 20;
	}
//
//	public static void main(String[] args) {
//		System.out.println(calEndIndex(7, 5, 31));
//	}
}
