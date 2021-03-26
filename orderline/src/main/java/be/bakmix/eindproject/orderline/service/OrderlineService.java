package be.bakmix.eindproject.orderline.service;

import be.bakmix.eindproject.orderline.business.OrderlineEntity;
import be.bakmix.eindproject.orderline.business.repository.OrderlineRepository;
import be.bakmix.eindproject.orderline.service.dto.Orderline;
import be.bakmix.eindproject.orderline.service.dto.Product;
import be.bakmix.eindproject.orderline.service.mapper.OrderlineMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
 public class OrderlineService {

    private static List<Orderline> orderlines = new ArrayList<>();

    @Autowired
    private OrderlineRepository orderlineRepository;

    @Autowired
    private OrderlineMapper orderlineMapper;

    @Value("http://localhost:7778/api/products/")
    private String urlProducts;

    public OrderlineService(OrderlineRepository orderlineRepository, OrderlineMapper orderlineMapper)
    {
        this.orderlineRepository = orderlineRepository;
        this.orderlineMapper = orderlineMapper;
    }

    public List<Orderline> getAll(){
        List<Orderline> orderlines = StreamSupport
                .stream(orderlineRepository.findAll().spliterator(), false)
                .map(e -> orderlineMapper.toDTO(e))
                .collect(Collectors.toList());

        for(Orderline orderline : orderlines){

            RestTemplate rtProduct = new RestTemplate();
            Product product = rtProduct.getForObject(urlProducts+orderline.getProductId(), Product.class);
            orderline.setProductId(product.getId());
            orderline.setProduct(product);
        }
        return orderlines;
    }

    public List<Orderline> getByOrderId(Long id){
        List<Orderline> orderlines = StreamSupport
                .stream(orderlineRepository.findAll().spliterator(), false)
                .filter(o -> o.getOrderId() == id)
                .map(e -> orderlineMapper.toDTO(e))
                .collect(Collectors.toList());

        for(Orderline orderline : orderlines){

            RestTemplate rtProduct = new RestTemplate();
            Product product = rtProduct.getForObject(urlProducts+orderline.getProductId(), Product.class);
            orderline.setProductId(product.getId());
            orderline.setProduct(product);
        }
        return orderlines;
    }

    public Orderline getById(Long id){
        Optional<OrderlineEntity> orderlineEntityOptional = orderlineRepository.findById(id);
        if(orderlineEntityOptional.isPresent()){
            Orderline orderline = orderlineMapper.toDTO(orderlineEntityOptional.get());
            RestTemplate rtProduct = new RestTemplate();
            Product product = rtProduct.getForObject(urlProducts+orderline.getProductId(), Product.class);
            orderline.setProductId(product.getId());
            orderline.setProduct(product);
            return orderline;
        }
        return null;
    }
}
