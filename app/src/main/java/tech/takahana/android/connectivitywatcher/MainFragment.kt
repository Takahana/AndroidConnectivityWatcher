package tech.takahana.android.connectivitywatcher

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tech.takahana.android.connectivitywatcher.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by activityViewModels()

    private var scopeCancelledWhenPaused = CoroutineScope(Dispatchers.IO)
    private var connectivityJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMainBinding.bind(view)
        binding.button.setOnClickListener {
            showConnectivity()
        }

        connectivityJob = scopeCancelledWhenPaused.launch {
            viewModel.connectivity
                .collectLatest {
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

    override fun onPause() {
        super.onPause()
        connectivityJob?.cancel()
    }

    private fun showConnectivity() {
        findNavController().navigate(R.id.action_mainFragment_to_connectivityFragment)
    }
}
