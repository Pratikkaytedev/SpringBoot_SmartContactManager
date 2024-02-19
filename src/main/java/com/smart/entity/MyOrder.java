package com.smart.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class MyOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long myOrder_Id;
	private String order_Id;
	private String amount;
	private String receipt;
	private String status;

	@ManyToOne
	private User user;

	private String paymentId;

	public long getMyOrder_Id() {
		return myOrder_Id;
	}

	public void setMyOrder_Id(long myOrder_Id) {
		this.myOrder_Id = myOrder_Id;
	}

	public String getOrder_Id() {
		return order_Id;
	}

	public void setOrder_Id(String order_Id) {
		this.order_Id = order_Id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	@Override
	public String toString() {
		return "MyOrder [myOrder_Id=" + myOrder_Id + ", order_Id=" + order_Id + ", amount=" + amount + ", receipt="
				+ receipt + ", status=" + status + ", user=" + user + ", paymentId=" + paymentId + "]";
	}

}
