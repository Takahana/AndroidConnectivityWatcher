package tech.takahana.android.connectivitywatcher

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import tech.takahana.android.connectivitywatcher.databinding.FragmentConnectivityBinding

@AndroidEntryPoint
class ConnectivityFragment : Fragment(R.layout.fragment_connectivity) {

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentConnectivityBinding.bind(view)

        lifecycleScope.launchWhenResumed {
            mainViewModel.connectivity.collectLatest {
                binding.textView.text = if (!it.isEnabled()) {
                    "No Internet connection"
                } else if (it.isWiFiEnabled()) {
                    "Wifi Internet connection is enabled"
                } else if (it.isCellularEnabled()) {
                    "Cellular Internet connection is enabled"
                } else {
                    "Available"
                }
            }
        }
    }

    companion object {
        fun newInstance() = ConnectivityFragment()
    }
}
