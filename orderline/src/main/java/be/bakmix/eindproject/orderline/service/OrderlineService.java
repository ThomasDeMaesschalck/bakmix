package be.bakmix.eindproject.orderline.service;

import be.bakmix.eindproject.orderline.business.OrderlineEntity;
import be.bakmix.eindproject.orderline.business.repository.OrderlineRepository;
import be.bakmix.eindproject.orderline.service.dto.Orderline;
import be.bakmix.eindproject.orderline.service.mapper.OrderlineMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class OrderlineService {

    private static List<Orderline> orderlines = new ArrayList<>();

    @Autowired
    private OrderlineRepository orderlineRepository;

    @Autowired
    private OrderlineMapper orderlineMapper;

    public List<Orderline> getAll(){
        List<Orderline> orderlines = StreamSupport
                .stream(orderlineRepository.findAll().spliterator(), false)
                .map(e -> orderlineMapper.toDTO(e))
                .collect(Collectors.toList());
        return orderlines;
    }

    public Orderline getById(Long id){
        Optional<OrderlineEntity> orderlineEntityOptional = orderlineRepository.findById(id);
        if(orderlineEntityOptional.isPresent()){
            return orderlineMapper.toDTO(orderlineEntityOptional.get());
        }
        return null;
    }
}
