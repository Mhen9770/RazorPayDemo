package in.ramit.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ramit.entity.ProductOrder;

public interface OrderRepository extends JpaRepository<ProductOrder, Serializable>{

	public ProductOrder findByRazorpayOrderId(String razorpayOrderId);

}
