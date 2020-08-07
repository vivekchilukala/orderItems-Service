package com.vivek.orderitems.service;

import com.vivek.orderitems.domain.OrderList;
import com.vivek.orderitems.repository.OrderListRepository;
import com.vivek.orderitems.service.dto.OrderListDTO;
import com.vivek.orderitems.service.mapper.OrderListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link OrderList}.
 */
@Service
@Transactional
public class OrderListService {

    private final Logger log = LoggerFactory.getLogger(OrderListService.class);

    private final OrderListRepository orderListRepository;

    private final OrderListMapper orderListMapper;

    public OrderListService(OrderListRepository orderListRepository, OrderListMapper orderListMapper) {
        this.orderListRepository = orderListRepository;
        this.orderListMapper = orderListMapper;
    }

    /**
     * Save a orderList.
     *
     * @param orderListDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderListDTO save(OrderListDTO orderListDTO) {
        log.debug("Request to save OrderList : {}", orderListDTO);
        OrderList orderList = orderListMapper.toEntity(orderListDTO);
        orderList = orderListRepository.save(orderList);
        return orderListMapper.toDto(orderList);
    }

    /**
     * Get all the orderLists.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderListDTO> findAll() {
        log.debug("Request to get all OrderLists");
        return orderListRepository.findAll().stream()
            .map(orderListMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one orderList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderListDTO> findOne(Long id) {
        log.debug("Request to get OrderList : {}", id);
        return orderListRepository.findById(id)
            .map(orderListMapper::toDto);
    }


    @Transactional(readOnly = true)
    public OrderListDTO findByProductCode(String sku) {
        log.debug("Request to get OrderList : {}", sku);
        return orderListMapper.toDto(orderListRepository.findByProductCode(sku));
    }

    /**
     * Delete the orderList by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderList : {}", id);
        orderListRepository.deleteById(id);
    }
}
