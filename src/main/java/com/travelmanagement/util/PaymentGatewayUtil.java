package com.travelmanagement.util;

import java.util.Map;
import org.json.JSONObject;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;
import com.razorpay.Utils;

public class PaymentGatewayUtil {

    private static final String RAZORPAY_KEY_ID = "rzp_test_RNQiHnsfjn3up2";
    private static final String RAZORPAY_KEY_SECRET = "jfg3IcV54jtIEomhJgct0SZm";

 
    public static Order createOrder(double amount, String currency, String receipt) throws RazorpayException {
        RazorpayClient client = new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int)(amount * 100)); 
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);

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
        refundRequest.put("amount", (int)(amount * 100));

        return client.payments.refund(refundRequest); 
    }
}
