# AndroidConnectivityWatcher
Monitor Internet status

```
val watcher = ConnectivityWatcher(this)  
lifecycleScope.launch {  
    watcher.statusFlow.collect { status ->  
        if (!status.isConnected()) {  
            // Disconnected action  
            Log.v("CONN", "disconnected")  
        } else {  
            // switch wifi or cellular  
            if (status.isCellular()) {  
                Log.v("CONN", "cellular is available")  
            }  
            if (status.isWiFi()) {  
                Log.v("CONN", "wifi is available")  
            }  
        }  
    }  
}  
```
