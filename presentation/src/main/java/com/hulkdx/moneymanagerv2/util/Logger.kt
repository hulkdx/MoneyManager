package com.hulkdx.moneymanagerv2.util

import android.util.Log

import java.io.PrintWriter
import java.io.StringWriter

class Logger(tag: String) {

    private val mTag: String = tag
    private var mDisabled = false

    private fun log(msg: String,
                    throwable: Throwable?,
                    logLevel: Int,
                    vararg args: Any) {
        var formattedMsg = msg

        if (mDisabled) {
            return
        }

        if (args.isNotEmpty()) {
            formattedMsg = String.format(msg, *args)
        }

        var throwableText: String? = null
        if (throwable != null) {
            throwableText = getStackTraceString(throwable)
        }

        //
        // Logging to Logcat:
        //
        val logText = if (throwableText != null) formattedMsg + "\n" + throwableText else msg

        @Suppress("ConstantConditionIf")
        when (logLevel) {
            TAG_ERROR -> if (sDebugLevel >= TAG_ERROR) {
                Log.e(mTag, logText)
            }
            TAG_WARN -> if (sDebugLevel >= TAG_WARN) {
                Log.w(mTag, logText)
            }
            TAG_INFO -> if (sDebugLevel >= TAG_INFO) {
                Log.i(mTag, logText)
            }
            TAG_DEBUG -> if (sDebugLevel >= TAG_DEBUG) {
                Log.d(mTag, logText)
            }
            TAG_VERBOSE -> if (sDebugLevel >= TAG_VERBOSE) {
                Log.v(mTag, logText)
            }
        }

        //
        // TODO: Logging to File?
        //

        //
        // TODO: Crashlytics log:
        //
    }

    fun verbose(msg: String, vararg args: Any) {
        log(msg, null, TAG_VERBOSE, *args)
    }

    fun debug(msg: String, vararg args: Any) {
        log(msg, null, TAG_DEBUG, *args)
    }

    fun info(msg: String, vararg args: Any) {
        log(msg, null, TAG_INFO, *args)
    }

    fun warn(msg: String, t: Throwable?, vararg args: Any) {
        log(msg, t, TAG_WARN, *args)
    }

    fun warn(msg: String) {
        warn(msg, null)
    }

    fun error(msg: String, t: Throwable?, vararg args: Any) {
        log(msg, t, TAG_ERROR, *args)
    }

    fun error(msg: String) {
        error(msg, null)
    }

    fun error(t: Throwable) {
        error("", t)
    }

    private fun getStackTraceString(t: Throwable): String {
        // Don't replace this with Log.getStackTraceString() - it hides
        // UnknownHostException, which is not what we want.
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        t.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }

    companion object {

        private const val STACK_CALL_LENGTH = 3

        private const val TAG_VERBOSE = 32
        private const val TAG_DEBUG = 16
        private const val TAG_INFO = 8
        private const val TAG_WARN = 4
        private const val TAG_ERROR = 2

        private const val sDebugLevel = TAG_VERBOSE

        private val staticLogger: Logger
            get() {
                val tag = getTag()
                return Logger(tag)
            }

        private fun getTag(): String {
            val stackTrace = Throwable().stackTrace
            if (stackTrace.size < STACK_CALL_LENGTH) {
                return "Unknown TAG"
            }
            val stackTraceElement = stackTrace[STACK_CALL_LENGTH]
            val className = stackTraceElement.className
            return className.substring(className.lastIndexOf('.') + 1)
        }

        fun v(msg: String, vararg args: Any) {
            staticLogger.verbose(msg, false, args)
        }

        fun e(msg: String) {
            staticLogger.error(msg)
        }

        fun e(t: Throwable) {
            staticLogger.error(t)
        }

        fun e(msg: String, t: Throwable) {
            staticLogger.error(msg, t)
        }

        fun e(msg: String, vararg args: Any) {
            staticLogger.error(msg, null, *args)
        }

        fun i(msg: String) {
            staticLogger.info(msg)
        }

        fun i(msg: String, vararg args: Any) {
            staticLogger.info(msg, false, args)
        }

        fun d(msg: String) {
            staticLogger.debug(msg)
        }

        fun d(msg: String, vararg args: Any) {
            staticLogger.debug(msg, *args)
        }

        fun w(msg: String, vararg args: Any) {
            staticLogger.warn(msg, null, *args)
        }
    }

}
