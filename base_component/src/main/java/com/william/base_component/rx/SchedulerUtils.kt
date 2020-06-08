package com.william.base_component.rx

import com.william.base_component.rx.scheduler.*

fun <T> ioToMain(): IoMainScheduler<T> {
    return IoMainScheduler()
}

fun <T> ioToIo(): IoIoScheduler<T> {
    return IoIoScheduler()
}

fun <T> computationToMain(): ComputationMainScheduler<T> {
    return ComputationMainScheduler()
}

fun <T> singleToMain(): SingleMainScheduler<T> {
    return SingleMainScheduler()
}

fun <T> trampolineToMain(): TrampolineMainScheduler<T> {
    return TrampolineMainScheduler()
}

fun <T> newThreadToMain(): NewThreadMainScheduler<T> {
    return NewThreadMainScheduler()
}