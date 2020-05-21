package com.william.base_component.rx.scheduler

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * @author William
 * @date 2020/5/19 13:54
 * Class Comment：
 */

class ComputationMainScheduler<T> private constructor() :
    BaseScheduler<T>(Schedulers.computation(), AndroidSchedulers.mainThread())

class SingleMainScheduler<T> private constructor() :
    BaseScheduler<T>(Schedulers.single(), AndroidSchedulers.mainThread())

class TrampolineMainScheduler<T> private constructor() :
    BaseScheduler<T>(Schedulers.trampoline(), AndroidSchedulers.mainThread())

class NewThreadMainScheduler<T> private constructor() :
    BaseScheduler<T>(Schedulers.newThread(), AndroidSchedulers.mainThread())


class IoMainScheduler<T> : BaseScheduler<T>(Schedulers.io(), AndroidSchedulers.mainThread())
