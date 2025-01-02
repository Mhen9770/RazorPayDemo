package in.ramit.excepHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandle {

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<Object> getException(Exception ex){
		ex.printStackTrace();
		  Map<String, Object> body = new HashMap<>();
	        body.put("timestamp", LocalDateTime.now());
	        body.put("message", "An unexpected error occurred");
	        body.put("details", ex.getMessage());

	        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
