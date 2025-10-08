package com.travelmanagement.dto.responseDTO;

import java.time.LocalDateTime;

public class PaymentResponseDTO {

	private Integer paymentId;
	private Integer bookingId;
	private LocalDateTime paymentDate;
	private String status;
	private Double amount;
	private String packageName;
	private String razorpayPaymentId;

	public String getRazorpayPaymentId() {
		return razorpayPaymentId;
	}

	public void setRazorpayPaymentId(String razorpayPaymentId) {
		this.razorpayPaymentId = razorpayPaymentId;
	}
	public Integer getPaymentId() {
		return paymentId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime localDateTime) {
		this.paymentDate = localDateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "PaymentResponseDTO [paymentId=" + paymentId + ", bookingId=" + bookingId + ", paymentDate="
				+ paymentDate + ", status=" + status + ", amount=" + amount + ", packageName=" + packageName + "]";
	}

}
