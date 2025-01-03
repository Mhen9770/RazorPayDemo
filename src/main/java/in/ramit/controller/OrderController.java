package in.ramit.controller;

import in.ramit.entity.ProductOrder;
import in.ramit.service.RazorpayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class OrderController {

	private final RazorpayService razorpayService;
	public OrderController(RazorpayService razorpayService) {
		this.razorpayService = razorpayService;
	}

	@GetMapping("/")
	public String getIndex() {
		return "index";
	}
	
	@PostMapping("/createOrder")
	@ResponseBody
	public ResponseEntity<ProductOrder> purchaseOrder(@RequestBody ProductOrder productOrder) throws Exception{
			
		ProductOrder createOrder = razorpayService.createOrder(productOrder);	
		return new ResponseEntity<ProductOrder>(createOrder,HttpStatus.OK);			
	}
	
	@PostMapping("/payment-callback")
	public String handlePaymentCallback(@RequestParam Map<String, String> respPayload, Model model) {
		System.out.println(respPayload);
		ProductOrder updatedOrder = razorpayService.verifyPaymentAndUpdateOrderStatus(respPayload);
		model.addAttribute("order", updatedOrder);
		return "response";
	}

	@PostMapping("/razorpayment-callback")
	public String handleRazorPaymentCallback(@RequestParam Map<String, String> respPayload, Model model) {
		System.out.println(respPayload);
		ProductOrder updatedOrder = razorpayService.verifyPaymentAndUpdateOrderStatus(respPayload);
		model.addAttribute("order", updatedOrder);
		return "response";
	}
}
