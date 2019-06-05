package com.example.cr2

import androidx.lifecycle.*
import kotlinx.coroutines.*

class MainViewModel: ViewModel() {

    private val _whenCurrentList = MutableLiveData<MutableList<String>>().apply { postValue(ArrayList()) }

    val whenCurrentLog = MediatorLiveData<String>().apply {
        addSource(_whenCurrentList) {
            val newList = _whenCurrentList.value

            this.value = newList?.joinToString("\n")
        }
    }

    fun processAction(action: String) {
        _whenCurrentList.value = ArrayList()

        when (action) {
            "C1" -> c1()
            "C2" -> c2()
            "C3" -> c3()
            "C4" -> c4()
            "C5" -> c5()
            "C6" -> c6()
            "C7" -> c7()
            "C8" -> c8()
            "C9" -> c9()
            "C10" -> c10()
        }
    }

    ///-----------------------------------------------------------------------------------------------------------------
    /// C1
    ///-----------------------------------------------------------------------------------------------------------------
    private fun c1() {
        viewModelScope.launch {
            val a1 = async {
                logInfo("Start a1")
                delay(500)
                logInfo("End a1")
            }

            val a2 = async {
                logInfo("Start a2")
                delay(1000)
                logInfo("End a2")
            }

            launch {
                logInfo("Start l1")
            }
        }
    }

    ///-----------------------------------------------------------------------------------------------------------------
    /// C2
    ///-----------------------------------------------------------------------------------------------------------------
    private fun c2() {
        viewModelScope.launch {
            val a1 = async {
                logInfo("Start a1")
                delay(1500)
                logInfo("End a1")
                "example a1 result TADAA!"
            }

            val a2 = async {
                logInfo("Start a2")
                delay(1000)
                logInfo("End a2")
            }

            val a1result = a1.await()
            logInfo("Read result of a1")
            a2.await()
            logInfo("Read result of a2")

            launch {
                logInfo("Result of a1 : $a1result")

                launch {
                    logInfo("Start of nested launch ll1")
                }

                logInfo("Start l1")
            }
        }
    }

    ///-----------------------------------------------------------------------------------------------------------------
    /// C3
    ///-----------------------------------------------------------------------------------------------------------------
    private fun c3() {
        viewModelScope.launch {
            launch {
                logInfo("Start l1")

                launch {
                    delay(500)
                    logInfo("Start of nested 1 launch ll1")
                }

                launch {
                    delay(200)
                    logInfo("Start of nested 2 launch ll2")
                }

                launch {
                    logInfo("Start of nested3 launch (loong operation)")
                    delay(5000)
                    logInfo("Nested 3 finished")
                }

                logInfo("I will not be displayed at the end")
            }
        }
    }

    ///-----------------------------------------------------------------------------------------------------------------
    /// C4
    ///-----------------------------------------------------------------------------------------------------------------
    private fun c4() {
        logInfo("Here I am outside of coroutines")

        viewModelScope.launch {
            logInfo("I will call a suspend Dispatchers.IO function now")
            c4HelperIO()
            logInfo("I will call a suspend Dispatchers.Default function now")
            c4HelperDefault()
            logInfo("I will call a suspend Dispatchers.Unconfined function now")
            c4HelperUnconfined()
            logInfo("I will call a suspend Dispatchers.Main function now")
            c4HelperMain()
        }
    }

    private suspend fun c4HelperIO() = withContext(Dispatchers.IO) {
        logInfo("c4HelperIO called")
    }

    private suspend fun c4HelperDefault() = withContext(Dispatchers.Default) {
        logInfo("c4HelperDefault called")
    }

    private suspend fun c4HelperUnconfined() = withContext(Dispatchers.Unconfined) {
        logInfo("c4HelperUnconfined called")
    }

    private suspend fun c4HelperMain() = withContext(Dispatchers.Main) {
        logInfo("c4HelperMain called")
    }

    ///-----------------------------------------------------------------------------------------------------------------
    /// C5
    ///-----------------------------------------------------------------------------------------------------------------
    private fun c5() {
        viewModelScope.launch {
            val a1 = async {
                logInfo("Start a1")
                delay(500)
                logInfo("End a1")
            }

            withContext(Dispatchers.IO){
                val a2 = async {
                    logInfo("Start a2")
                    delay(1000)
                    logInfo("End a2")
                }
            }

            launch {
                logInfo("Start l1")
            }
        }
    }

    ///-----------------------------------------------------------------------------------------------------------------
    /// C6
    ///-----------------------------------------------------------------------------------------------------------------
    private fun c6() {
        viewModelScope.launch {
            logInfo("I will call a suspend function without defined context")
            c6Helper()
        }
    }

    private suspend fun c6Helper() {
        delay(1000)
        logInfo("c6Helper called")
    }

    ///-----------------------------------------------------------------------------------------------------------------
    /// C7
    ///-----------------------------------------------------------------------------------------------------------------
    private fun c7() {
        viewModelScope.launch {
            logInfo("I will call a suspend function without defined context that calls a function with context")
            c7Helper()
        }
    }

    private suspend fun c7Helper() {
        delay(1000)
        logInfo("c7Helper called")
        c7Helper2()
    }

    private suspend fun c7Helper2() = withContext(Dispatchers.IO) {
        delay(1000)
        logInfo("c7Helper2 called")
    }

    ///-----------------------------------------------------------------------------------------------------------------
    /// C8
    ///-----------------------------------------------------------------------------------------------------------------
    private fun c8() {
        viewModelScope.launch {
            logInfo("I will call a suspend function with defined context here that calls another function without context")
            withContext(Dispatchers.IO) {
                c8Helper()
                logInfo("Call to c8Helper2 changes thread (sometimes)")
            }
        }
    }

    private suspend fun c8Helper() {
        delay(1000)
        logInfo("c8Helper called")
        c8Helper2()
    }

    private suspend fun c8Helper2() = withContext(Dispatchers.IO) {
        delay(1000)
        logInfo("c8Helper2 called")
    }

    ///-----------------------------------------------------------------------------------------------------------------
    /// C9
    ///-----------------------------------------------------------------------------------------------------------------
    private fun c9() {
        viewModelScope.launch {
            logInfo("I will call a suspend function with defined context that calls another function with same context")
            c9Helper()
            logInfo("Changes threads (sometimes)")
        }
    }

    private suspend fun c9Helper() = withContext(Dispatchers.IO) {
        delay(1000)
        logInfo("c9Helper called")
        c9Helper2()
    }

    private suspend fun c9Helper2() = withContext(Dispatchers.IO) {
        delay(1000)
        logInfo("c8Helper2 called")
    }

    ///-----------------------------------------------------------------------------------------------------------------
    /// C10
    ///-----------------------------------------------------------------------------------------------------------------
    private fun c10() {
        viewModelScope.launch {
            logInfo("Example of calling a function with direct async as body")
            val result = c10HelperAsync().await()
            logInfo("funcrion c10HelperAsync() returned: $result")
        }
    }

    private suspend fun c10HelperAsync() = viewModelScope.async {
        delay(1000)
        logInfo("c10Helper called")
        "MY RESULT!!!"
    }

    private fun logInfo(info: String) {
        val currentList = _whenCurrentList.value
        currentList?.add(info + " (Thread: ${Thread.currentThread().name})")
        currentList?.add("----------------------------------------")
        _whenCurrentList.postValue(currentList)
    }
}