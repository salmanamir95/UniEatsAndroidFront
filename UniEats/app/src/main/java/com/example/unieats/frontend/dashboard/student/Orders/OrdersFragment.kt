package com.example.unieats.frontend.dashboard.student.Orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.unieats.R
import com.example.unieats.frontend.dashboard.student.SharedViewModels.SharedStudentViewModel

class OrdersFragment : Fragment() {

    private lateinit var placeOrderViewModel: PlaceOrderViewModel
    private lateinit var sharedStudentViewModel: SharedStudentViewModel

    private lateinit var orderItemsList: ListView
    private lateinit var totalBillTextView: TextView
    private lateinit var paymentMethodGroup: RadioGroup
    private lateinit var placeOrderButton: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_student_order, container, false)

        orderItemsList = binding.findViewById(R.id.orderItemsList)
        totalBillTextView = binding.findViewById(R.id.totalBill)
        paymentMethodGroup = binding.findViewById(R.id.paymentMethodGroup)
        placeOrderButton = binding.findViewById(R.id.placeOrderButton)

        // Setup ViewModel
        placeOrderViewModel = ViewModelProvider(this).get(PlaceOrderViewModel::class.java)

        // Observe the order items and total bill
        placeOrderViewModel.order.observe(viewLifecycleOwner, { order ->
            order?.let {
                val adapter = OrderItemAdapter(requireContext(), it.items)
                orderItemsList.adapter = adapter
            }
        })

        placeOrderViewModel.totalBill.observe(viewLifecycleOwner, { bill ->
            totalBillTextView.text = "Total Bill: $${bill}"
        })

        // Handle payment method selection
        paymentMethodGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.paymentCard -> placeOrderViewModel.setPaymentMethod("Card")
                R.id.paymentCash -> placeOrderViewModel.setPaymentMethod("Cash")
            }
        }

        // Handle place order button click
        placeOrderButton.setOnClickListener {
            placeOrderViewModel.placeOrder()
            Toast.makeText(requireContext(), "Order Placed!", Toast.LENGTH_SHORT).show()
        }

        return binding
    }
}