package com.expand;

import java.util.Date;
import java.util.UUID;

public class Uuid
{
	private static Date date = new Date();
//	private static StringBuilder buf = new StringBuilder();
	private static int seq = 0;
	private static final int ROTATION = 99999;

	public static synchronized long nextLong()
	{
		if (seq > ROTATION)
			seq = 0;
		//buf.delete(0, buf.length());  
		date.setTime(System.currentTimeMillis());
		String str = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%2$05d", date, seq++);
		return Long.parseLong(str);
	}
	public static synchronized String next()
	{
		if (seq > ROTATION)
			seq = 0;
		//buf.delete(0, buf.length());  
		date.setTime(System.currentTimeMillis());
		String str = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%2$05d", date, seq++);
		return str;
	}
	public static synchronized String random()
	{
		String str = UUID.randomUUID().toString().replaceAll("-", "");
		return str;
	}
	public static synchronized String randomUUID()
	{
		String str = UUID.randomUUID().toString();
		return str;
	}
	public static void main(String[] args)
	{
		for (int i = 0; i < 100; i++)
		{
			System.out.println(next());
		}
	}
}