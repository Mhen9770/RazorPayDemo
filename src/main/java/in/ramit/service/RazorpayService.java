package in.ramit.service;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import in.ramit.entity.ProductOrder;
import in.ramit.repo.OrderRepository;

@Service
@RequiredArgsConstructor
public class RazorpayService {
	@Value("${razorpay.key.secret}")
	private String keySecret;
	private final OrderRepository orderRepository;
	private final RazorpayClient razorpayClient;

	public ProductOrder createOrder(ProductOrder productOrder) throws Exception {
		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", productOrder.getAmount() * 100);
		orderRequest.put("currency", "INR");
		Order razorpayOrder = razorpayClient.orders.create(orderRequest);

		productOrder.setRazorpayOrderId(razorpayOrder.get("id"));
		productOrder.setStatus(razorpayOrder.get("status"));

		orderRepository.save(productOrder);
		return productOrder;

	}


	public ProductOrder verifyPaymentAndUpdateOrderStatus(Map<String, String> respPayload) {
		ProductOrder studentOrder = null;
		try {
			String razorpayOrderId = respPayload.get("razorpay_order_id");
			String razorpayPaymentId = respPayload.get("razorpay_payment_id");
			String razorpaySignature = respPayload.get("razorpay_signature");


			// Verify the signature to ensure the payload is genuine
			boolean isValidSignature = verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);

			if (isValidSignature) {
				studentOrder = orderRepository.findByRazorpayOrderId(razorpayOrderId);
				if (studentOrder != null) {
					studentOrder.setStatus("CONFIRMED");
					orderRepository.save(studentOrder);
				}
			} else {
				System.out.println("invalid");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return studentOrder;
	}

	private boolean verifySignature(String orderId, String paymentId, String signature) throws RazorpayException {
		String generatedSignature = HmacSHA256(orderId + "|" + paymentId, keySecret);
		return generatedSignature.equals(signature);
	}

	private String HmacSHA256(String data, String key) throws RazorpayException {
		try {
			javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
			javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key.getBytes(),
					"HmacSHA256");
			mac.init(secretKeySpec);
			byte[] hash = mac.doFinal(data.getBytes());
			return new String(org.apache.commons.codec.binary.Hex.encodeHex(hash));
		} catch (Exception e) {
			throw new RazorpayException("Failed to calculate signature.", e);
		}
	}

}
