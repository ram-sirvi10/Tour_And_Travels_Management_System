package com.travelmanagement.util;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.json.JSONObject;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;
import com.razorpay.Utils;

public class PaymentGatewayUtil {

	private static final String RAZORPAY_KEY_ID;
	private static final String RAZORPAY_KEY_SECRET;

	static {
		try {
			Properties props = new Properties();
			try (InputStream is = PaymentGatewayUtil.class.getClassLoader()
					.getResourceAsStream("application.properties")) {
				props.load(is);
			}
			RAZORPAY_KEY_ID = props.getProperty("razorpay.key.id");
			RAZORPAY_KEY_SECRET = props.getProperty("razorpay.key.secret");
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize EmailUtil", e);
		}
	}

	public static Order createOrder(double amount, String currency, String receipt) throws RazorpayException {
		RazorpayClient client = new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET);

		JSONObject orderRequest = new JSONObject();

		orderRequest.put("amount", (int) (amount * 100));
		orderRequest.put("currency", currency);
		orderRequest.put("receipt", receipt);
		orderRequest.put("payment_capture", 1);

		return client.orders.create(orderRequest);
	}

	public static boolean verifyPaymentSignature(Map<String, String> params) throws Exception {
		JSONObject json = new JSONObject();
		json.put("razorpay_order_id", params.get("razorpay_order_id"));
		json.put("razorpay_payment_id", params.get("razorpay_payment_id"));
		json.put("razorpay_signature", params.get("razorpay_signature"));

		return Utils.verifyPaymentSignature(json, RAZORPAY_KEY_SECRET);
	}

	public static Refund refundPayment(String paymentId, double amount) throws RazorpayException {
		RazorpayClient client = new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET);

		JSONObject refundRequest = new JSONObject();
		refundRequest.put("payment_id", paymentId);
		refundRequest.put("amount", (int) (amount * 100));

		return client.payments.refund(refundRequest);
	}
}
