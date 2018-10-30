package com.chanon.dev.beans;

public class ReqCurrentRate extends RequestHeader {
	
	private String date;    // �ѹ���
	private String round;   // �ͺ���
	
	private String currencytype;  // �ѵ���š����¹
	private String monthhistory;  // ��͹��͹��ѧ
	private String dayhistory;  // �ѹ��͹��ѧ
	private String extype;  // extype
	
	
	public String getDayhistory() {
		return dayhistory;
	}
	public void setDayhistory(String dayhistory) {
		this.dayhistory = dayhistory;
	}
	public String getExtype() {
		return extype;
	}
	public void setExtype(String extype) {
		this.extype = extype;
	}
	
	public String getCurrencytype() {
		return currencytype;
	}
	public void setCurrencytype(String currencytype) {
		this.currencytype = currencytype;
	}
	public String getMonthhistory() {
		return monthhistory;
	}
	public void setMonthhistory(String monthhistory) {
		this.monthhistory = monthhistory;
	}
 	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRound() {
		return round;
	}
	public void setRound(String round) {
		this.round = round;
	}
 
}
