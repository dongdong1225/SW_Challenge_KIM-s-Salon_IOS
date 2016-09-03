package com.app.sample.messenger.model;

import java.io.Serializable;

public class GroupDetails implements Serializable{
	private long id;
	private String date;
	private Group group;
	private String content;
	private boolean fromMe;

	public GroupDetails(long id, String date, Group group, String content, boolean fromMe) {
		this.id = id;
		this.date = date;
		this.group = group;
		this.content = content;
		this.fromMe = fromMe;
	}

	public long getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public Group getGroup() {
		return group;
	}

	public String getContent() {
		return content;
	}

	public boolean isFromMe() {
		return fromMe;
	}
}