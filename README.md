# AndroidConnectivityWatcher
Monitor Internet Connectivity

```
  val watcher = ConnectivityWatcher(this)
  //..
  
  
  lifecycleScope.launchWhenResumed {
        watcher.status.collect { status ->
            if (!status.isEnabled()) {
                showToast("No Internet connection")
            }
        }
    }
```
