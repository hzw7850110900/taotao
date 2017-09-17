package com.taotao.manage.vo;

import java.io.Serializable;

public class PicUploadResult implements Serializable {

	private int error;//非0表示失败
	private String url;//图片地址
	private String height;//高度
	private String width;//宽度
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	
}
