package com.vivek.orderitems.web.rest;

import com.vivek.orderitems.service.OrderListService;
import com.vivek.orderitems.web.rest.errors.BadRequestAlertException;
import com.vivek.orderitems.service.dto.OrderListDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.vivek.orderitems.domain.OrderList}.
 */
@RestController
@RequestMapping("/api")
public class OrderListResource {

    private final Logger log = LoggerFactory.getLogger(OrderListResource.class);

    private static final String ENTITY_NAME = "orderitemsOrderList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderListService orderListService;

    public OrderListResource(OrderListService orderListService) {
        this.orderListService = orderListService;
    }

    /**
     * {@code POST  /order-lists} : Create a new orderList.
     *
     * @param orderListDTO the orderListDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderListDTO, or with status {@code 400 (Bad Request)} if the orderList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-items")
    public ResponseEntity<OrderListDTO> createOrderList(@RequestBody OrderListDTO orderListDTO) throws URISyntaxException {
        log.debug("REST request to save OrderList : {}", orderListDTO);
        if (orderListDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderListDTO result = orderListService.save(orderListDTO);
        return ResponseEntity.created(new URI("/api/order-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-lists} : Updates an existing orderList.
     *
     * @param orderListDTO the orderListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderListDTO,
     * or with status {@code 400 (Bad Request)} if the orderListDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-items")
    public ResponseEntity<OrderListDTO> updateOrderList(@RequestBody OrderListDTO orderListDTO) throws URISyntaxException {
        log.debug("REST request to update OrderList : {}", orderListDTO);
        if (orderListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrderListDTO result = orderListService.save(orderListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orderListDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /order-lists} : get all the orderLists.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderLists in body.
     */
    @GetMapping("/order-items")
    public List<OrderListDTO> getAllOrderLists() {
        log.debug("REST request to get all OrderLists");
        return orderListService.findAll();
    }

    /**
     * {@code GET  /order-lists/:id} : get the "id" orderList.
     *
     * @param id the id of the orderListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-items/{id}")
    public ResponseEntity<OrderListDTO> getOrderList(@PathVariable Long id) {
        log.debug("REST request to get OrderList : {}", id);
        Optional<OrderListDTO> orderListDTO = orderListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderListDTO);
    }

    /**
     * {@code GET  /order-lists/:sku} : get the "id" orderList.
     *
     * @param sku the sku of the orderListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-items/product/{sku}")
    public OrderListDTO getOrderListBySKU(@PathVariable String sku) {
        log.debug("REST request to get OrderList : {}", sku);
        OrderListDTO orderListDTO = orderListService.findByProductCode(sku);
        return orderListDTO;
    }

    /**
     * {@code DELETE  /order-lists/:id} : delete the "id" orderList.
     *
     * @param id the id of the orderListDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-items/{id}")
    public ResponseEntity<Void> deleteOrderList(@PathVariable Long id) {
        log.debug("REST request to delete OrderList : {}", id);
        orderListService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
