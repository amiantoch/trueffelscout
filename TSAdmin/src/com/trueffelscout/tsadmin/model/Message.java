package com.trueffelscout.tsadmin.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Message implements Serializable{
	private static final long serialVersionUID = -8038787316211387495L;
	
	protected int id;
	protected String name;
	protected String phone;
	protected String mail;
	protected String message;
	protected String date;
	
	public Message(String name, String phone, String mail, String message, String date){
		this.name = name;
		this.phone = phone;
		this.mail = mail;
		this.message = message;
		this.date = date;
	}
}
