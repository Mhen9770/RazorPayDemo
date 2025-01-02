package in.ramit.repo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ramit.entity.ProductOrder;

public interface OrderRepository extends JpaRepository<ProductOrder, Serializable>{

	public ProductOrder findByRazorpayOrderId(String razorpayOrderId);


	List<ProductOrder> findByStatusIn(List<String> created);
}
