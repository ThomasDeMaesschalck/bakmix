package be.bakmix.eindproject.order.service;

import be.bakmix.eindproject.order.business.repository.OrderRepository;
import be.bakmix.eindproject.order.service.dto.Order;
import be.bakmix.eindproject.order.service.dto.Orderline;
import be.bakmix.eindproject.order.service.dto.Product;
import be.bakmix.eindproject.order.service.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderServiceTest {

    private Order testOrder;
    private Order testOrder2;
    private Orderline testOrderline1;
    private Orderline testOrderline2;
    private Product product1;
    private Product product2;

    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp(){

        testOrder = new Order();
        testOrder.setId(Long.parseLong("1"));
        testOrder.setDiscount(BigDecimal.valueOf(0));
        testOrder.setShippingCost(BigDecimal.valueOf(0));

        testOrder2 = new Order();
        testOrder2.setId(Long.parseLong("2"));
        testOrder2.setDiscount(BigDecimal.valueOf(10));
        testOrder2.setShippingCost(BigDecimal.valueOf(5.50));

        orderService = new OrderService(orderRepository, orderMapper );

        testOrderline1 = new Orderline();
        testOrderline1.setId(Long.parseLong("1"));
        testOrderline1.setQty(Long.parseLong("2"));
        testOrderline1.setPurchasePrice(BigDecimal.valueOf(5.50));

        testOrderline2 = new Orderline();
        testOrderline2.setId(Long.parseLong("2"));
        testOrderline2.setQty(Long.parseLong("5"));
        testOrderline2.setPurchasePrice(BigDecimal.valueOf(10.00));

        product1 = new Product();
        product1.setId(Long.parseLong("1"));
        product1.setName("Koekjesdeeg");
        product1.setPrice(BigDecimal.valueOf(5.50));
        product1.setVat(10);

        product2 = new Product();
        product2.setId(Long.parseLong("2"));
        product2.setName("Brownies");
        product2.setPrice(BigDecimal.valueOf(10.00));
        product2.setVat(10);

        testOrderline1.setProduct(product1);
        testOrderline2.setProduct(product2);

        List<Orderline> listOfOrderlines = new ArrayList<>();
        listOfOrderlines.add(testOrderline1);
        listOfOrderlines.add(testOrderline2);

        testOrder.setOrderlines(listOfOrderlines);

        testOrder2.setOrderlines(listOfOrderlines);

    }

    @Test
    void subTotalTest(){
        BigDecimal subTotal = BigDecimal.valueOf(6100, 2);
        assertEquals(subTotal, orderService.subTotal(testOrder), "Test 01 failed - subTotal not correct");

    }

    @Test
    void vatTotal(){
        BigDecimal vatTotal = BigDecimal.valueOf(610, 2);
        testOrder.setSubTotal(orderService.subTotal(testOrder));
        assertEquals(vatTotal, orderService.vatTotal(testOrder), "Test 02 failed - vatTotal not correct");
    }

    @Test
    void total(){
        BigDecimal total = BigDecimal.valueOf(6710, 2);
        testOrder.setSubTotal(orderService.subTotal(testOrder));
        testOrder.setVatTotal(orderService.vatTotal(testOrder));
        assertEquals(total, orderService.total(testOrder), "Test 03 failed - total not correct");

        BigDecimal total2 = BigDecimal.valueOf(6215, 2);
        testOrder2.setSubTotal(orderService.subTotal(testOrder2));
        testOrder2.setVatTotal(orderService.vatTotal(testOrder2));
        assertEquals(total2, orderService.total(testOrder2), "Test 04 failed - total not correct");
    }
}
