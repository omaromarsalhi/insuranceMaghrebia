package com.maghrebia.payment.service;

import com.maghrebia.payment.entity.PaymentContract;
import com.maghrebia.payment.entity.PaymentPlan;
import com.maghrebia.payment.entity.enums.PaymentStatus;
import com.maghrebia.payment.exceptions.PaymentAlreadyCompletedException;
import com.maghrebia.payment.repository.PaymentContractRepository;
import com.maghrebia.payment.repository.PaymentPlanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class PaymentPlanService {

    private  final PaymentPlanRepository paymentPlanRepository;

    private  final PaymentContractRepository paymentRepository;

    public PaymentPlan updatePaymentPlan(String id) {

            PaymentPlan paymentPlan = paymentPlanRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("PaymentPlan not found with id: " + id));
            if(paymentPlan.getPaymentStatus()== PaymentStatus.Paid){
                throw new PaymentAlreadyCompletedException("Your payment for this tranche has been  already successfully completed.");
            }

            paymentPlan.setPaymentDate(new Date());
            paymentPlan.setAmountPaid(paymentPlan.getAmountDue());

            if (paymentPlan.getDueDate().before(new Date())) {
                paymentPlan.setPaymentStatus(PaymentStatus.Overdue);
            } else {
                paymentPlan.setPaymentStatus(PaymentStatus.Paid);
            }

            PaymentContract contract = paymentRepository.findById(paymentPlan.getPaymentContractId())
                    .orElseThrow(() -> new RuntimeException("Contract not found"));

            boolean allPlansPaid = paymentPlanRepository.findByPaymentContractId(contract.getContractPaymentId())
                    .stream()
                    .allMatch(p -> p.getPaymentStatus() == PaymentStatus.Paid);

            // Update the contract status if all plans are paid
            if (allPlansPaid) {
                contract.setPaymentStatus("Paid");
            paymentRepository.save(contract);
             }

            return paymentPlanRepository.save(paymentPlan);
        }

    public List<PaymentPlan> getAllPaymentsPlan(String contractId) {

       return paymentPlanRepository.findByPaymentContractId(contractId);
    }
}
