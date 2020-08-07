package com.vivek.orderitems.web.rest;

import com.vivek.orderitems.OrderItemsApp;
import com.vivek.orderitems.domain.OrderList;
import com.vivek.orderitems.repository.OrderListRepository;
import com.vivek.orderitems.service.OrderListService;
import com.vivek.orderitems.service.dto.OrderListDTO;
import com.vivek.orderitems.service.mapper.OrderListMapper;
import com.vivek.orderitems.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.vivek.orderitems.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link OrderListResource} REST controller.
 */
@SpringBootTest(classes = OrderItemsApp.class)
public class OrderListResourceIT {

    private static final String DEFAULT_PRODUCT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_QUANTITY = 1F;
    private static final Float UPDATED_QUANTITY = 2F;

    @Autowired
    private OrderListRepository orderListRepository;

    @Autowired
    private OrderListMapper orderListMapper;

    @Autowired
    private OrderListService orderListService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restOrderListMockMvc;

    private OrderList orderList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrderListResource orderListResource = new OrderListResource(orderListService);
        this.restOrderListMockMvc = MockMvcBuilders.standaloneSetup(orderListResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderList createEntity(EntityManager em) {
        OrderList orderList = new OrderList()
            .productCode(DEFAULT_PRODUCT_CODE)
            .productName(DEFAULT_PRODUCT_NAME)
            .quantity(DEFAULT_QUANTITY);
        return orderList;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderList createUpdatedEntity(EntityManager em) {
        OrderList orderList = new OrderList()
            .productCode(UPDATED_PRODUCT_CODE)
            .productName(UPDATED_PRODUCT_NAME)
            .quantity(UPDATED_QUANTITY);
        return orderList;
    }

    @BeforeEach
    public void initTest() {
        orderList = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderList() throws Exception {
        int databaseSizeBeforeCreate = orderListRepository.findAll().size();

        // Create the OrderList
        OrderListDTO orderListDTO = orderListMapper.toDto(orderList);
        restOrderListMockMvc.perform(post("/api/order-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderListDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderList in the database
        List<OrderList> orderListList = orderListRepository.findAll();
        assertThat(orderListList).hasSize(databaseSizeBeforeCreate + 1);
        OrderList testOrderList = orderListList.get(orderListList.size() - 1);
        assertThat(testOrderList.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testOrderList.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testOrderList.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createOrderListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderListRepository.findAll().size();

        // Create the OrderList with an existing ID
        orderList.setId(1L);
        OrderListDTO orderListDTO = orderListMapper.toDto(orderList);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderListMockMvc.perform(post("/api/order-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderList in the database
        List<OrderList> orderListList = orderListRepository.findAll();
        assertThat(orderListList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOrderLists() throws Exception {
        // Initialize the database
        orderListRepository.saveAndFlush(orderList);

        // Get all the orderListList
        restOrderListMockMvc.perform(get("/api/order-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderList.getId().intValue())))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())));
    }

    @Test
    @Transactional
    public void getOrderList() throws Exception {
        // Initialize the database
        orderListRepository.saveAndFlush(orderList);

        // Get the orderList
        restOrderListMockMvc.perform(get("/api/order-lists/{id}", orderList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderList.getId().intValue()))
            .andExpect(jsonPath("$.productCode").value(DEFAULT_PRODUCT_CODE))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderList() throws Exception {
        // Get the orderList
        restOrderListMockMvc.perform(get("/api/order-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderList() throws Exception {
        // Initialize the database
        orderListRepository.saveAndFlush(orderList);

        int databaseSizeBeforeUpdate = orderListRepository.findAll().size();

        // Update the orderList
        OrderList updatedOrderList = orderListRepository.findById(orderList.getId()).get();
        // Disconnect from session so that the updates on updatedOrderList are not directly saved in db
        em.detach(updatedOrderList);
        updatedOrderList
            .productCode(UPDATED_PRODUCT_CODE)
            .productName(UPDATED_PRODUCT_NAME)
            .quantity(UPDATED_QUANTITY);
        OrderListDTO orderListDTO = orderListMapper.toDto(updatedOrderList);

        restOrderListMockMvc.perform(put("/api/order-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderListDTO)))
            .andExpect(status().isOk());

        // Validate the OrderList in the database
        List<OrderList> orderListList = orderListRepository.findAll();
        assertThat(orderListList).hasSize(databaseSizeBeforeUpdate);
        OrderList testOrderList = orderListList.get(orderListList.size() - 1);
        assertThat(testOrderList.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testOrderList.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testOrderList.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingOrderList() throws Exception {
        int databaseSizeBeforeUpdate = orderListRepository.findAll().size();

        // Create the OrderList
        OrderListDTO orderListDTO = orderListMapper.toDto(orderList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderListMockMvc.perform(put("/api/order-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderList in the database
        List<OrderList> orderListList = orderListRepository.findAll();
        assertThat(orderListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrderList() throws Exception {
        // Initialize the database
        orderListRepository.saveAndFlush(orderList);

        int databaseSizeBeforeDelete = orderListRepository.findAll().size();

        // Delete the orderList
        restOrderListMockMvc.perform(delete("/api/order-lists/{id}", orderList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderList> orderListList = orderListRepository.findAll();
        assertThat(orderListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
