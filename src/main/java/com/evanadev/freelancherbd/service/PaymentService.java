package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.Payment;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getEmployerPayments(User employer) {
        return paymentRepository.findByEmployerId(employer);
    }
}
