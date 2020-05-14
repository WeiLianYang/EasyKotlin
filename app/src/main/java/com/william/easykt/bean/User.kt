package com.william.easykt.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * @author William
 * @date 2020/4/16 10:09
 * Class Comment：user value bean
 */
@Parcelize
data class User constructor(var name: String, var age: Int) : Parcelable
//class User constructor( name: String,  age: Int)