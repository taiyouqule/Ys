package com.zlj.db;

import java.sql.Timestamp;


public class Paper {
	private int paperid;
	private String title;
	private String writer;
	private Timestamp write_time;
	private String where_from;
	public int getPaperid() {
		return paperid;
	}
	public String getWhere_from() {
		return where_from;
	}
	public void setWhere_from(String where_from) {
		this.where_from = where_from;
	}
	public void setPaperid(int paperid) {
		this.paperid = paperid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public Timestamp getWrite_time() {
		return write_time;
	}
	public void setWrite_time(Timestamp write_time) {
		this.write_time = write_time;
	}
	
}
