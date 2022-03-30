package com.example.clinicio.fragment.patient.upcomingAppointment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicio.R
import com.example.clinicio.data.remote.Status
import com.example.clinicio.data.remote.patient.UpcomingAppointment
import com.example.clinicio.databinding.FragmentUpcomingAppointmentBinding
import com.example.clinicio.utils.LoginHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UpcomingAppointmentFragment : Fragment() {
    private var _binding: FragmentUpcomingAppointmentBinding? = null
    private val binding get() = _binding!!
    lateinit var progressBar: ProgressBar

    val viewModel: UpcomingAppointmentViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    @Inject
    lateinit var loginHelper: LoginHelper

    @Inject
    lateinit var upcomingAppointmentAdapter: UpcomingAppointmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpcomingAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar

        binding.upcomingAppointmentRV.apply {
            adapter = upcomingAppointmentAdapter
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val deleted = upcomingAppointmentAdapter.upcomingAppointmentList[pos]
                upcomingAppointmentAdapter.upcomingAppointmentList.removeAt(pos)
                upcomingAppointmentAdapter.notifyItemRemoved(pos)

                cancelSlot(deleted.slot_id)
                upcomingAppointmentAdapter.notifyDataSetChanged()
                upcomingAppointment()


            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.upcomingAppointmentRV)

        upcomingAppointment()


    }

    fun upcomingAppointment() {
        lifecycleScope.launch {
            viewModel.upcomingAppointmentFlow.collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {

                        if (it.data?.data != null) {
                            progressBar.visibility = View.GONE
                            binding.noDataFoundConstraint.visibility = View.GONE
                            binding.upcomingAppointmentRV.visibility = View.VISIBLE
                            upcomingAppointmentAdapter.upcomingAppointmentList =
                                it.data.data as ArrayList<UpcomingAppointment>

                        }
                    }
                    Status.ERROR -> {
                        val error = it.message.toString()
                        Log.e("Error", error)
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        progressBar.isIndeterminate = true
                    }
                    Status.APIERROR -> {
                        if (it.code == 404) {
                            progressBar.visibility = View.GONE
                            binding.upcomingAppointmentRV.visibility = View.GONE
                            binding.noDataFoundConstraint.visibility = View.VISIBLE
                        } else {
                            showErrorToast(requireContext(), it.data?.error?.message!!)
                        }
                    }
                }
            }
        }
    }

    fun cancelSlot(slotId: Int) {
        lifecycleScope.launch {
            viewModel.cancelAppointment(loginHelper.id, slotId).collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data?.success == true) {
                            Toast.makeText(context,
                                "Appointment cancelled successfully",
                                Toast.LENGTH_SHORT)
                                .show()

                        }
                    }
                    Status.ERROR -> {
                        val error = it.message.toString()
                        Log.e("Error", error)
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        progressBar.isIndeterminate = true
                    }
                    Status.APIERROR -> {
                        // TODO
                    }
                }
            }
        }
    }

    private fun showErrorToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}