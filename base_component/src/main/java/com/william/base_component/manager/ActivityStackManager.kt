package com.william.base_component.manager

import android.app.Activity
import java.util.*


/**
 * @author William
 * @date 2020/4/17 11:44
 * Class Comment：Activity Stack Manager
 */
object ActivityStackManager {

    private var mActivityStack: Stack<Activity> = Stack()

    /**
     * push in
     */
    fun addActivity(activity: Activity) {
        mActivityStack.push(activity)
    }

    /**
     * pop out
     */
    private fun removeActivity(activity: Activity) {
        mActivityStack.remove(activity)
    }

    /**
     * get current instance/top instance in the stack
     */
    fun getCurrentActivity(): Activity? {
        if (mActivityStack.size > 0) {
            return mActivityStack[mActivityStack.size - 1]
        }
        return null
    }

    /**
     * check the activity instance is exist
     */
    fun checkActivity(cla: Class<*>): Boolean {
        for (activity in mActivityStack) {
            return activity.javaClass == cla
        }
        return false
    }

    /**
     * end the specified activity class
     */
    fun endActivity(cla: Class<*>) {
        for (activity in mActivityStack) {
            if (activity.javaClass == cla) {
                activity.finish()
            }
        }
    }

    /**
     * end the specified activity instance
     */
    fun endActivity(activity: Activity?) {
        if (activity != null) {
            removeActivity(activity)
            activity.finish()
        }
    }

    /**
     * end specified instances
     */
    fun endActivities(vararg actCls: Class<out Activity>) {
        try {
            val buf: MutableList<Activity> = ArrayList()
            val size = mActivityStack.size
            for (i in size - 1 downTo 0) {
                val activity = mActivityStack[i]
                for (actCl in actCls) {
                    if (activity.javaClass != actCl) {
                        buf.add(activity)
                    }
                }
            }
            for (activity in buf) {
                endActivity(activity)
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * end all activity instance
     */
    fun endAllActivity() {
        var activity: Activity?
        while (mActivityStack.isNotEmpty()) {
            activity = mActivityStack.pop()
            activity?.finish()
        }
    }

    /**
     * end top of the specified instance
     */
    fun endToActivity(actCls: Class<out Activity>, isIncludeSelf: Boolean): Boolean {
        val buf: MutableList<Activity> = ArrayList()
        val size = mActivityStack.size
        var activity: Activity
        for (i in size - 1 downTo 0) {
            activity = mActivityStack[i]
            if (activity.javaClass.isAssignableFrom(actCls)) {
                for (a in buf) {
                    a.finish()
                }
                return true
            } else if (i == size - 1 && isIncludeSelf) {
                buf.add(activity)
            } else if (i != size - 1) {
                buf.add(activity)
            }
        }
        return false
    }

}