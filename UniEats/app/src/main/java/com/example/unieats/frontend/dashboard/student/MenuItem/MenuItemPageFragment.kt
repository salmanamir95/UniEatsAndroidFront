package com.example.unieats.frontend.dashboard.student.MenuItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unieats.frontend.dashboard.student.SharedViewModels.MenuItemSharedViewModel
import com.example.unieats.frontend.dashboard.student.SharedViewModels.SharedStudentViewModel
import com.example.unieats.R


class MenuItemPageFragment : Fragment() {

    private val menuItemShared: MenuItemSharedViewModel by viewModels({ requireParentFragment() })
    private val menuItemRecvd: MenuItemRecieveViewModel by viewModels()
    private val currentStudentShared: SharedStudentViewModel by viewModels({ requireParentFragment() })
    private val currentStudentRecvd: StudentRecieved by viewModels()
    private val reviewViewModel: ReviewViewModel by viewModels()

    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var ratingBar: RatingBar
    private lateinit var commentEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_menu_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind views
        reviewRecyclerView = view.findViewById(R.id.rvReviews)
        ratingBar = view.findViewById(R.id.ratingBar)
        commentEditText = view.findViewById(R.id.etComment)
        submitButton = view.findViewById(R.id.btnSubmitReview)

        reviewAdapter = ReviewAdapter(emptyList())
        reviewRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        reviewRecyclerView.adapter = reviewAdapter

        menuItemShared.menuItem.observe(viewLifecycleOwner) { item ->
            menuItemRecvd.setMenuItem(item)
            reviewViewModel.loadReviews(item.id) // Load reviews
        }

        menuItemRecvd.recvd.observe(viewLifecycleOwner) { item ->
            // Populate UI with item info (TODO if required)
        }

        currentStudentShared.user.observe(viewLifecycleOwner) { currStud ->
            currentStudentRecvd.setStudent(currStud)
        }

        currentStudentRecvd.recvd.observe(viewLifecycleOwner) { student ->
            submitButton.setOnClickListener {
                val rating = ratingBar.rating
                val comment = commentEditText.text.toString().trim()

                val item = menuItemRecvd.recvd.value
                if (item != null && comment.isNotEmpty()) {
                    reviewViewModel.submitReview(
                        menuItemId = item.id.toString(),
                        userId = student.id,
                        userName = student.name,
                        rating = rating,
                        comment = comment
                    ) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Review submitted", Toast.LENGTH_SHORT).show()
                            commentEditText.text.clear()
                            ratingBar.rating = 0f
                            reviewViewModel.loadReviews(item.id.toString())
                        } else {
                            Toast.makeText(requireContext(), "Submission failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Please enter a comment", Toast.LENGTH_SHORT).show()
                }
            }
        }

        reviewViewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            reviewAdapter.updateReviews(reviews)
        }
    }
}
