package com.shenji.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class TxtToHtml {

	public void readWriteFile(String filePath, int line) {
		String textHtml = "";
		BufferedReader bu = null;
		InputStreamReader in = null;
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				in = new InputStreamReader(new FileInputStream(file), "gbk");
				bu = new BufferedReader(in);
				String lineText = null;
				textHtml = "<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><body>";
				int nowline = 0;
				while ((lineText = bu.readLine()) != null) {
					nowline++;
					lineText = changeToHtml(lineText);
					lineText += "</br>";
					if (nowline == line) {
						lineText = " <font color=\"red\">" + lineText
								+ "</font>";
					}
					textHtml += lineText;
				}
				textHtml += "</html></body>";
			} else {
				System.out.println("文件不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bu.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		File file = new File(filePath.replace("txt", "html"));
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(file));
			System.out.println(textHtml);
			output.write(textHtml);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public String readFile(String filePath, int line) {
		String textHtml = "";
		BufferedReader bu = null;
		InputStreamReader in = null;
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				in = new InputStreamReader(new FileInputStream(file), "gbk");
				bu = new BufferedReader(in);
				String lineText = null;
				textHtml = "<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><body>";
				int nowline = 0;
				while ((lineText = bu.readLine()) != null) {
					nowline++;
					lineText = changeToHtml(lineText);
					lineText += "</br>";
					if (nowline == line) {
						lineText = " <font color=\"red\">" + lineText
								+ "</font>";
					}
					textHtml += lineText;
				}
				textHtml += "</html></body>";
			} else {
				System.out.println("文件不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bu.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return textHtml;
	}

	public String changeToHtml(String text) {
		text = text.replace("&", "&amp;");
		text = text.replace(" ", "&nbsp;");
		text = text.replace("<", "&lt;");
		text = text.replace(">", "&gt;");
		text = text.replace("\"", "&quot;");
		text = text.replace(" ", "&nbsp;&nbsp;&nbsp;&nbsp;");
		text = text.replace("public", "<b>public</b>");
		text = text.replace("class", "<b>class</b>");
		text = text.replace("static", "<b>static</b>");
		text = text.replace("void", "<b>void</b>");
		String t = text.replace("//", "<font color=green>//");
		if (!text.equals(t)) {
			text = t + "</font>";
		}
		return text;
	}

	/**
	 * 获得html文件
	 * 
	 * @param filepath
	 * @param line
	 */
	public static void writeToHtml(String filepath, int line) {
		TxtToHtml c = new TxtToHtml();
		c.readWriteFile(filepath, line);
	}

	/**
	 * 获得html内容
	 * 
	 * @param filepath
	 * @param line
	 * @return
	 */
	public static String getHtml(String filepath, int line) {
		TxtToHtml c = new TxtToHtml();
		System.out.println("文件路径为------>" + filepath);
		return c.readFile(filepath, line);
	}

	public static void main(String[] args) {
		// writeToHtml("D:/685.txt",1);
		System.out.println(getHtml("D:/685.txt", 1));
	}
}
