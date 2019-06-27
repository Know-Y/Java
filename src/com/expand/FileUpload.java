package com.expand;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.expand.Uuid;

public class FileUpload {
	public static String upload(HttpServletRequest request, FileItem fileItem, String fileName,String path) {
		String url = "";
		try {
			if(fileName == null || "".equals(fileName) || fileName == ""){
				fileName = fileItem.getName();
				
				System.out.println("ԭ�ļ�����" + fileName);// Koala.jpg

				String suffix = fileName.substring(fileName.lastIndexOf('.'));
				System.out.println("��չ����" + suffix);// .jpg
				
				fileName = Uuid.next() + suffix;
				System.out.println("���ļ�����" + fileName);
			}

			// 5. ����FileItem��write()������д���ļ�
			File file = new File(request.getSession().getServletContext().getRealPath("/") + path + fileName);
			//File file = new File(request.getSession().getServletContext().getRealPath("/")  + fileName);
			fileItem.write(file);
			url = file.getAbsolutePath();
			Pattern pattern = Pattern.compile("/upload.*");
			Matcher matcher = pattern.matcher(url);

			if (matcher.find()) {
				url  = matcher.group();
			}
			//url = url.replace("dflc_java","zs"); //�滻url
			System.out.println(url);
			fileItem.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url ;
	}
}
