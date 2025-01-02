package in.ramit.schedular;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import in.ramit.entity.ProductOrder;
import in.ramit.repo.OrderRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class FetchOrderStatus {

    private final RazorpayClient razorpayClient;
    private final OrderRepository orderRepository;


    @Scheduled(fixedDelay = 100, timeUnit = TimeUnit.SECONDS)
    public void schedule(){
        List<ProductOrder> paymentOrders = orderRepository.findByStatusIn(List.of("created", "pending")); // todo add more status to check
        paymentOrders.forEach(order-> {
            log.info("Checking Status for Order Id {}", order.getRazorpayOrderId());
            try {
                Order razorpayOrder = razorpayClient.orders.fetch(order.getRazorpayOrderId());
                if(!order.getStatus().equalsIgnoreCase(razorpayOrder.get("status"))){
                    String oldStatus = order.getStatus();
                    order.setStatus(razorpayOrder.get("status"));
                    orderRepository.save(order);
                    log.info("Order Status Update to : {}, from : {} for Id {}",razorpayOrder.get("status"), oldStatus, order.getRazorpayOrderId());
                }else{
                    log.info("No Change in Status for Order Id {}", order.getRazorpayOrderId());
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }






}
