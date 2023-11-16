package com.lhduc.restaurantmanagementapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhduc.restaurantmanagementapi.common.enums.PaymentStatus;
import com.lhduc.restaurantmanagementapi.exception.OperationForbiddenException;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.AddMoreItemToBillRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.response.BillDetailDto;
import com.lhduc.restaurantmanagementapi.model.dto.response.BillDto;
import com.lhduc.restaurantmanagementapi.service.BillService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import static com.lhduc.restaurantmanagementapi.common.constant.UriConstant.BILLS_ENDPOINT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BillControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BillService billService;

    private static final BillDto BILL_1 = new BillDto(1, Arrays.asList(
            new BillDetailDto(10, 1000),
            new BillDetailDto(2, 2000)
    ));
    private static final BillDto BILL_2 = new BillDto(2, new ArrayList<>());

    @Test
    void testGetAllBill() throws Exception {
        when(billService.getAllBill(any(), any(), any())).thenReturn(Arrays.asList(BILL_1, BILL_2));
        mockMvc.perform(get(BILLS_ENDPOINT))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllBillWithDescSorting() throws Exception {
        when(billService.getAllBill(any(), any(), any())).thenReturn(Arrays.asList(BILL_2, BILL_1));
        RequestBuilder requestBuilder = get(BILLS_ENDPOINT)
                .param("sort", "-id");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllBillWithWrongSortingField() throws Exception {
        RequestBuilder requestBuilder = get(BILLS_ENDPOINT)
                .param("sort", "ids");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(billService, never()).getAllBill(any(), any(), any());
    }

    @Test
    void testGetById() throws Exception {
        when(billService.getBillById(BILL_1.getId())).thenReturn(BILL_1);
        RequestBuilder requestBuilder = get(BILLS_ENDPOINT + "/{id}", BILL_1.getId());
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testCreateBill() throws Exception {
        BillCreateRequest billCreateRequest = new BillCreateRequest();
        when(billService.createBill(billCreateRequest)).thenReturn(BILL_1);
        RequestBuilder requestBuilder = post(BILLS_ENDPOINT)
                .content(objectMapper.writeValueAsString(billCreateRequest))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void testAddMoreBillItems() throws Exception {
        AddMoreItemToBillRequest addMoreItemToBillRequest = new AddMoreItemToBillRequest();
        RequestBuilder requestBuilder = post(BILLS_ENDPOINT + "/{billId}/items", BILL_1.getId())
                .content(objectMapper.writeValueAsString(addMoreItemToBillRequest))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated());
        verify(billService).addMoreBillItems(BILL_1.getId(), addMoreItemToBillRequest.getItems());
    }

    @Test
    void testUpdateBillStatus() throws Exception {
        BillUpdateRequest billUpdateRequest = new BillUpdateRequest();
        billUpdateRequest.setPaymentStatus(PaymentStatus.PAID);
        RequestBuilder requestBuilder = put(BILLS_ENDPOINT + "/{billId}", BILL_1.getId())
                .content(objectMapper.writeValueAsString(billUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(billService).updateBill(BILL_1.getId(), billUpdateRequest);
    }

    @Test
    void testUpdateBillStatusWithEmptyPayment_throwException() throws Exception {
        BillUpdateRequest billUpdateRequest = new BillUpdateRequest();
        RequestBuilder requestBuilder = put(BILLS_ENDPOINT + "/{billId}", BILL_1.getId())
                .content(objectMapper.writeValueAsString(billUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateBillItems() throws Exception {
        BillUpdateRequest billUpdateRequest = new BillUpdateRequest();
        RequestBuilder requestBuilder = put(BILLS_ENDPOINT + "/{billId}", BILL_1.getId())
                .content(objectMapper.writeValueAsString(billUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteBill() throws Exception {
        RequestBuilder requestBuilder = delete(BILLS_ENDPOINT + "/{billId}", BILL_1.getId());
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(billService).deleteBillById(BILL_1.getId());
    }

    @Test
    void testDeleteBillWithPaidStatus_throwException() throws Exception {
        doThrow(OperationForbiddenException.class).when(billService).deleteBillById(BILL_1.getId());
        RequestBuilder requestBuilder = delete(BILLS_ENDPOINT + "/{billId}", BILL_1.getId());
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteBillItem() throws Exception {
        RequestBuilder requestBuilder = delete(BILLS_ENDPOINT + "/{billId}/items/{itemId}", BILL_1.getId(), 1);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(billService).deleteBillItem(BILL_1.getId(), 1);
    }

    @Test
    void testDeleteBillItemWithPaidStatus_throwException() throws Exception {
        doThrow(OperationForbiddenException.class).when(billService).deleteBillItem(BILL_1.getId(), 1);
        RequestBuilder requestBuilder = delete(BILLS_ENDPOINT + "/{billId}/items/{itemId}", BILL_1.getId(), 1);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}