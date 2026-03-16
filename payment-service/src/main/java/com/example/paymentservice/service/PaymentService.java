package com.example.paymentservice.service;

import com.example.paymentservice.domain.Money;
import com.example.paymentservice.domain.Payment;
import com.example.paymentservice.domain.PaymentStatus;
import com.example.paymentservice.dto.CreatePaymentRequest;
import com.example.paymentservice.dto.PaymentResponse;
import com.example.paymentservice.dto.UpdatePaymentRequest;
import com.example.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentResponse createPayment(CreatePaymentRequest request) {
        String paymentNumber = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Money amount = new Money(request.getAmount(), request.getCurrency());
        Payment payment = new Payment(
                paymentNumber,
                request.getOrderId(),
                request.getCustomerId(),
                amount,
                request.getPaymentMethod()
        );
        return toResponse(paymentRepository.save(payment));
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id) {
        return toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByOrder(String orderId) {
        return paymentRepository.findByOrderId(orderId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByCustomer(String customerId) {
        return paymentRepository.findByCustomerId(customerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PaymentResponse updatePayment(Long id, UpdatePaymentRequest request) {
        Payment payment = findById(id);
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only PENDING payments can be updated");
        }
        Money amount = new Money(request.getAmount(), request.getCurrency());
        payment.setAmount(amount);
        payment.setPaymentMethod(request.getPaymentMethod());
        return toResponse(paymentRepository.save(payment));
    }

    public PaymentResponse processPayment(Long id) {
        Payment payment = findById(id);
        payment.process();
        return toResponse(paymentRepository.save(payment));
    }

    public PaymentResponse completePayment(Long id) {
        Payment payment = findById(id);
        payment.complete();
        return toResponse(paymentRepository.save(payment));
    }

    public PaymentResponse failPayment(Long id, String reason) {
        Payment payment = findById(id);
        payment.fail(reason);
        return toResponse(paymentRepository.save(payment));
    }

    public PaymentResponse refundPayment(Long id) {
        Payment payment = findById(id);
        payment.refund();
        return toResponse(paymentRepository.save(payment));
    }

    public void deletePayment(Long id) {
        Payment payment = findById(id);
        paymentRepository.delete(payment);
    }

    private Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + id));
    }

    private PaymentResponse toResponse(Payment payment) {
        PaymentResponse resp = new PaymentResponse();
        resp.setId(payment.getId());
        resp.setPaymentNumber(payment.getPaymentNumber());
        resp.setOrderId(payment.getOrderId());
        resp.setCustomerId(payment.getCustomerId());
        resp.setStatus(payment.getStatus());
        resp.setPaymentMethod(payment.getPaymentMethod());
        resp.setFailureReason(payment.getFailureReason());
        resp.setCreatedAt(payment.getCreatedAt());
        resp.setUpdatedAt(payment.getUpdatedAt());
        if (payment.getAmount() != null) {
            resp.setAmount(payment.getAmount().getAmount());
            resp.setCurrency(payment.getAmount().getCurrency());
        }
        return resp;
    }
}
