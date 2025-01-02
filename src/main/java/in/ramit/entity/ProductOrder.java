package in.ramit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class ProductOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long productOrderId;

	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "userId", nullable = false)
	private ProductUser productUser;
	
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "productId", nullable = false)
	private Product product;
	
	private String razorpayOrderId;
	private Double amount;
	private String status;
}
