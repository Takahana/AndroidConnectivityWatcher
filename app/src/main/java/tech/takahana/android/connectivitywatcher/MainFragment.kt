package tech.takahana.android.connectivitywatcher

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import tech.takahana.android.connectivitywatcher.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMainBinding.bind(view)
        binding.button.setOnClickListener {
            showConnectivity()
        }

        job = lifecycleScope.launchWhenResumed {
            mainViewModel.connectivity.collect { /* do nothing */ }
        }
    }

    override fun onPause() {
        super.onPause()
        job?.cancel()
    }

    private fun showConnectivity() {
        findNavController().navigate(R.id.action_mainFragment_to_connectivityFragment)
    }
}
