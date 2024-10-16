package com.yandex.navigationdemo.ui.routevariants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yandex.mapkit.geometry.Point
import com.yandex.navigationdemo.ui.R
import com.yandex.navigationdemo.ui.common.BaseMapFragment
import com.yandex.navigationdemo.ui.databinding.FragmentRouteVariantsBinding
import com.yandex.navigationdemo.ui.utils.subscribe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RouteVariantsFragment : BaseMapFragment(R.layout.fragment_route_variants) {

    private lateinit var binding: FragmentRouteVariantsBinding
    private val viewModel: RouteVariantsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRouteVariantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) { closeRouteVariants() }

        binding.apply {
            viewMapControls.setFindMeButtonClickCallback { cameraManager.moveCameraToUserLocation() }
            buttonCancel.setOnClickListener { closeRouteVariants() }
            buttonGo.setOnClickListener { openGuidance() }
        }

        viewModel.hasRequestPoints.subscribe(viewLifecycleOwner) {
            binding.buttonGo.isVisible = !it
        }

        mapTapManager.longTapActions.subscribe(viewLifecycleOwner) {
            showRequestPointDialog(point = it)
        }
    }

    override fun calculateFocusBounds(): FocusBounds {
        return super.calculateFocusBounds().run {
            copy(
                bottom = bottom - binding.layoutCardContent.height,
                right = right - binding.viewMapControls.width,
            )
        }
    }

    private fun showRequestPointDialog(point: Point) {
        alertDialogFactory.requestPointDialog({
            viewModel.setToPoint(point)
        }, {
            viewModel.addViaPoint(point)
        }, {
            viewModel.setFromPoint(point)
        }).show()
    }

    private fun openGuidance() {
        val action = RouteVariantsFragmentDirections.actionRouteVariantsFragmentToGuidanceFragment()
        findNavController().navigate(action)
    }

    private fun closeRouteVariants() {
        viewModel.resetRouteVariants()
        findNavController().popBackStack()
    }
}
