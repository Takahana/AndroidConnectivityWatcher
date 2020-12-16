package tech.takahana.android.connectivitywatcher

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import tech.takahana.android.connectivitywatcher.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMainBinding.bind(view)
        binding.button.setOnClickListener {
            showConnectivity()
        }

        lifecycleScope.launchWhenResumed {
            mainViewModel.connectivity.collectLatest {
                val text = if (!it.isEnabled()) {
                    "No Internet connection"
                } else if (it.isWiFiEnabled()) {
                    "Wifi Internet connection is enabled"
                } else if (it.isCellularEnabled()) {
                    "Cellular Internet connection is enabled"
                } else {
                    "Available"
                }
                Log.v("_TEST_", text)
            }
        }
    }

    private fun showConnectivity() {
        findNavController().navigate(R.id.action_mainFragment_to_connectivityFragment)
    }
}
