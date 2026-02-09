package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.model.JobTraffic;
import com.evanadev.freelancherbd.model.Payment;
import com.evanadev.freelancherbd.model.TrafficType;
import com.evanadev.freelancherbd.repository.JobRepository;
import com.evanadev.freelancherbd.repository.PaymentRepository;
import com.evanadev.freelancherbd.service.JobService;
import com.evanadev.freelancherbd.service.PaymentService;
import com.evanadev.freelancherbd.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final JobService jobService;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    @Autowired
    private AESUtil aesUtil;

    @Autowired
    public PaymentController(JobService jobService, PaymentService paymentService, PaymentRepository paymentRepository) {
        this.jobService = jobService;
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    /*
     * @author: evana
     * @Desc: Payment History of a employer
     * @Date: 14-02-26
     * */

    @GetMapping("/employer/payments/payment_history")
    public String pendingPaymentJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());

        List<Payment> paymentList =
                paymentService.getEmployerPayments(loggedUser.getUser());

        BigDecimal totalCompletedAmount =
                paymentRepository.getTotalPaidAmount(loggedUser.getUser());

        BigDecimal totalTransactionAmount =
                paymentRepository.getTotalTransactionAmount(loggedUser.getUser());

        BigDecimal totalPendingAmount =
                paymentRepository.getTotalPendingAmount(loggedUser.getUser());

        BigDecimal totalProcessAmount =
                paymentRepository.getTotalProcessAmount(loggedUser.getUser());

        BigDecimal totalFailedAmount =
                paymentRepository.getTotalFailedAmount(loggedUser.getUser());

        BigDecimal totalRefundAmount =
                paymentRepository.getTotalRefundAmount(loggedUser.getUser());

        model.addAttribute("paymentHistory", paymentList);
        model.addAttribute("totalPayments", paymentList.size());
        model.addAttribute("totalCompletedAmount", totalCompletedAmount);
        model.addAttribute("totalProcessAmount", totalProcessAmount);
        model.addAttribute("totalPendingAmount", totalPendingAmount);
        model.addAttribute("totalRefundAmount", totalRefundAmount);
        model.addAttribute("totalFailedAmount", totalFailedAmount);

        model.addAttribute("currentPath", "/employer/payments/payments");

        return "payment-history";
    }

    @GetMapping("/employer/payments/invoice")
    public String viewInvoice(@RequestParam("encId") String encId, Model model) {

        Long paymentId = aesUtil.decryptId(encId);

        Payment payment = paymentService.getPaymentForInvoice(paymentId);

        model.addAttribute("payment", payment);
        model.addAttribute("invoiceNo", "INV-" + payment.getId());
        model.addAttribute("invoiceDate", payment.getCreatedAt());

        return "invoice";
    }

}
