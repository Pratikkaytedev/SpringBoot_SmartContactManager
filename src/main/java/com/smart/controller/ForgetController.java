package com.smart.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.entity.User;
import com.smart.service.EmailService;
import com.smart.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgetController {

	@Autowired
	EmailService emailSerevice;

	@Autowired
	UserService userService;

	@GetMapping("/forget")
	public String openEmailForm() {
		return "forget_email_form";
	}

	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam String email, HttpSession session) {

		System.out.println("email is " + email);

		Random random = new Random();

		int otp = random.nextInt(9999);

		System.out.println(otp);

		String subject = "OTP From SCM";
		String message = "<div style='border: 2px solid #3498db; border-radius: 10px; padding: 20px; font-family: Arial, sans-serif; color: #333;'>"
				+ "    <h2 style='color: #3498db;'>OTP Notification</h2>" + "    <p>" + "        Dear User,<br>"
				+ "        Your One-Time Password (OTP) is:" + "    </p>"
				+ "    <h1 style='color: #e74c3c; font-size: 36px; margin-bottom: 10px;'>" + "        <b>" + otp
				+ "</b>" + "    </h1>" + "    <p>"
				+ "        This OTP is valid for a short period. Do not share it with anyone for security reasons."
				+ "    </p>" + "    <p style='font-size: 12px; color: #555;'>"
				+ "        If you did not request this OTP, please ignore this message." + "    </p>" + "</div>";

		String to = email;

		User user = userService.getUserByUserName(email);

		if (user == null) {
			session.setAttribute("messaage", "Email Id is not exist");
			return "forget_email_form";
		}

		boolean flag = EmailService.sendEmail(subject, message, to);

		if (flag) {
			session.setAttribute("sendOtp", otp);
			session.setAttribute("email", email);
			return "verify_otp";
		} else {
			session.setAttribute("messaage", "check Your OTP Id!!");
			return "forget_email_form";
		}

	}

	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam int otp, HttpSession session) {

		int sendOtp = (Integer) session.getAttribute("sendOtp");

		if (sendOtp == otp) {

			String email = (String) session.getAttribute("email");

			User user = userService.getUserByUserName(email);

			session.setAttribute("user", user);

			return "password_cange_form";
		} else {
			session.setAttribute("messaage", "You have entered wrong OTP");
			return "verify_otp";
		}

	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam String newPassword, HttpSession session) {

		String email = (String) session.getAttribute("email");

		User user = userService.getUserByUserName(email);

		int i = userService.updatePassword(newPassword, user.getuId());
		
		if(i>0) {
			session.setAttribute("messaage", "Password Updated Successfully");
			return "login";
		}else {
			session.setAttribute("messaage", "Password Updated failed");
			return "password_cange_form";
		}

		

	}

}
