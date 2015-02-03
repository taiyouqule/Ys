package com.shenji.web.bean;

/**
 * 对应最终显示的一条记录
 * 
 * @author Administrator
 * 
 */
public class ItemBean {
	private float score;
	private double similarity;
	private String title;
	private String content;
	private String view;
	private String download;
	private String tag_title;
	private String tag_writer;
	private String tag_write_time;
	private String tag_from;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getTag_title() {
		return tag_title;
	}

	public void setTag_title(String tag_title) {
		this.tag_title = tag_title;
	}

	public String getTag_writer() {
		return tag_writer;
	}

	public void setTag_writer(String tag_writer) {
		this.tag_writer = tag_writer;
	}

	public String getTag_write_time() {
		return tag_write_time;
	}

	public void setTag_write_time(String tag_write_time) {
		this.tag_write_time = tag_write_time;
	}

	public String getTag_from() {
		return tag_from;
	}

	public void setTag_from(String tag_from) {
		this.tag_from = tag_from;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

}
