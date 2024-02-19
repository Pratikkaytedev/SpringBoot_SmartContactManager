package com.smart.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.smart.entity.MyOrder;
import com.smart.entity.User;
import com.smart.repository.MyOrderRepository;
import com.smart.repository.UserRepository;
import com.smart.service.UserService;

import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/payment")
public class PaymentController {
	
	@Autowired
	private MyOrderRepository myOrderRepository;
	
	@Autowired
	private UserService userService;

    @RequestMapping("/create_order")
    @ResponseBody
    public String createOrder(@RequestBody Map<String, Object> data , HttpSession session) {
        int amt = Integer.parseInt(data.get("amount").toString());

        try {
            RazorpayClient client = new RazorpayClient("rzp_test_D92cs1pGm8i0YF", "EULi5KQaqHk8wHx2q97MJbXm");

            Random random = new Random();
            
            int txnNo = random.nextInt(999999);
            
            JSONObject ob = new JSONObject();
            ob.put("amount", amt * 100);
            ob.put("currency", "INR");
            ob.put("receipt", "txn_"+txnNo);

            // Creating a new order
            Order order = client.Orders.create(ob);

            System.out.println("Order is " + order);

            // If you want, you can save this to your database
            
            
            MyOrder myOrder = new MyOrder();
            
            int amount = order.get("amount");
            
             amount = amount/100;
            
            myOrder.setAmount(amount+"");
            myOrder.setOrder_Id(order.get("id"));
            myOrder.setPaymentId(null);
            myOrder.setStatus("created");
            
            String name = (String) session.getAttribute("userName");
    		User user = userService.getUserByUserName(name);
            myOrder.setUser(user);
            myOrder.setReceipt(order.get("receipt"));
            
            MyOrder save = myOrderRepository.save(myOrder);
            
            return order.toString();

        } catch (RazorpayException e) {
            e.printStackTrace();
            return "Error creating order";
        }
    }
    
    @PostMapping("/update_order")
    public ResponseEntity<?> update_order(@RequestBody Map<String,Object> data) {
    	
    	String orderId = data.get("order_id").toString();
    	
    	MyOrder myOrder = myOrderRepository.findByOrderId(orderId);
    	
    	myOrder.setPaymentId(data.get("payment_id").toString());
    	myOrder.setStatus(data.get("status").toString());
    	
    	MyOrder save = myOrderRepository.save(myOrder);
    	
    	if(save!=null) {
    		return ResponseEntity.ok(Map.of("msg","updated"));
    	}
    	
    	System.out.println(data);
    	return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
    }
    
    
}
