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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.unieats.R
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel
import com.example.unieats.frontend.dashboard.student.SharedViewModels.SelectedMenuItemsSharedtoCart
import com.example.unieats.frontend.dashboard.student.SharedViewModels.SharedStudentViewModel

class OrdersFragment : Fragment() {

    private val placeOrderViewModel: PlaceOrderViewModel by viewModels()
    private val sharedStudentViewModel: SharedStudentViewModel by activityViewModels()
    private val selectedItemsViewModel: SelectedMenuItemsSharedtoCart by activityViewModels()

    private lateinit var orderItemsList: ListView
    private lateinit var totalBillTextView: TextView
    private lateinit var paymentMethodGroup: RadioGroup
    private lateinit var placeOrderButton: View

    private var itemsInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_student_order, container, false)

        orderItemsList = rootView.findViewById(R.id.orderItemsList)
        totalBillTextView = rootView.findViewById(R.id.totalBill)
        paymentMethodGroup = rootView.findViewById(R.id.paymentMethodGroup)
        placeOrderButton = rootView.findViewById(R.id.placeOrderButton)

        // Observe order items
        placeOrderViewModel.order.observe(viewLifecycleOwner) { order ->
            orderItemsList.adapter = OrderItemAdapter(requireContext(), order.items)
        }

        placeOrderViewModel.totalBill.observe(viewLifecycleOwner) { bill ->
            totalBillTextView.text = "Total Bill: $${"%.2f".format(bill)}"
        }

        paymentMethodGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.paymentCard -> placeOrderViewModel.setPaymentMethod("Card")
                R.id.paymentCash -> placeOrderViewModel.setPaymentMethod("Cash")
            }
        }

        placeOrderButton.setOnClickListener {
            placeOrderViewModel.placeOrder()
            Toast.makeText(requireContext(), "Order Placed!", Toast.LENGTH_SHORT).show()

            // Optional: Clear cart here
            selectedItemsViewModel.clear()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedItemsViewModel.studentSelection.observe(viewLifecycleOwner) { selectedItems ->
            if (!itemsInitialized) {
                val orderItems = selectedItems.map {
//                    MenuItemModel.toOrderItem(it) // you must implement this
                }
//                placeOrderViewModel.setOrderItems(orderItems)
                itemsInitialized = true
            }
        }
    }
}
