/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chanon.dev.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

 
public class ExCounterRate {

    private String date;
    private String round;
 	private String time ;
 	
    private List<ExCounterRateItem> itemList = new ArrayList<ExCounterRateItem>();
	private List<ExRoundItem> roundList = new ArrayList<ExRoundItem>();
	
	
	
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<ExCounterRateItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<ExCounterRateItem> itemList) {
		this.itemList = itemList;
	}
	public List<ExRoundItem> getRoundList() {
		return roundList;
	}
	public void setRoundList(List<ExRoundItem> roundList) {
		this.roundList = roundList;
	}
	
 }
