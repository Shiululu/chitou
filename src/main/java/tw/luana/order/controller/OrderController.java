package tw.luana.order.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import tw.luana.attraction.model.AttractionService_Luana;
import tw.luana.cart.model.Cart;
import tw.luana.cart.model.CartService;
import tw.luana.order.model.OrderService;

@Controller
public class OrderController {

	@Autowired
	private AttractionService_Luana attractionService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private HttpSession session;
	
	String path_Luana_Atttraction = "luana/attraction/";
	String path_Luana_Cart = "luana/cart/";
	String path_Luana_Order = "luana/order/";
	
	
	//查看個人所有訂單
	@RequestMapping(path = "order", method = RequestMethod.GET)
	public String showOrders(//@RequestParam("memberid")int memberid,
			Model m) {

		int memberid = 1;
		m.addAttribute("orders",orderService.showOrderLists(memberid));

		return path_Luana_Order + "Luana_order";
		
	}
	//查看個人訂單細項
	@RequestMapping(path = "orderDetail", method = RequestMethod.POST)
	public String OrderDetail(@RequestParam("orderid") String orderid,Model m1,Model m2) {
		
		m1.addAttribute("orderList", orderService.showSingleOrderList(orderid));
		m2.addAttribute("aOrderDetail",orderService.showAttractionOrders(orderid));
		
		return path_Luana_Order + "Luana_order3";
		
	}

	//更改景點訂單狀態
	@RequestMapping(path = "orderStatus", method = RequestMethod.POST)
	public String updateStatusByOrderId(@RequestParam("orderStatus") int orderStatus,
			@RequestParam("orderid") String orderid, Model m) {


//		orderService.updateAttractionOrderStatus(orderStatus, orderid);
		m.addAttribute("orders",orderService.showAttractionOrders(orderid));

		 return path_Luana_Order + "Luana_order";
	}
	
}
