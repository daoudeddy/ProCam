package com.googy.procam.xposedhook

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.res.XModuleResources
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.googy.procam.R
import com.googy.procam.di.Preferences
import com.googy.procam.extension.get
import com.googy.procam.xposedhook.Constants.CAMERA_HOOK_PKG_NAME
import com.googy.procam.xposedhook.Constants.EXPOSURE
import com.googy.procam.xposedhook.Constants.EXPOSURE_TIME_ARRAY
import com.googy.procam.xposedhook.Constants.FOCUS
import com.googy.procam.xposedhook.Constants.GALLERY_HOOK_PKG_NAME
import com.googy.procam.xposedhook.Constants.HOOK_CLASS_NAME_MAKER
import com.googy.procam.xposedhook.Constants.ISO
import com.googy.procam.xposedhook.Constants.MODULE_PATH
import com.googy.procam.xposedhook.Constants.PHOTO_EDITOR_HOOK_PKG_NAME
import com.googy.procam.xposedhook.Constants.SENSOR_SENSITIVITY_ARRAY
import de.robv.android.xposed.*
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.XposedHelpers.findClass
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.util.*
import kotlin.concurrent.fixedRateTimer


class HookBackgroundCallbacks : IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {
    private var layoutRes: Int? = null
    private var arrowUpId: Int? = null
    private var arrowDownId: Int? = null

    var shutterTime = 1000L
    var isProMode = false

    private val sharedPreferences = XSharedPreferences(Preferences.externalFile)

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        MODULE_PATH = startupParam?.modulePath
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam?) {
        Log.e("ProCams", "package name ${resparam?.packageName}")

        if (CAMERA_HOOK_PKG_NAME == resparam?.packageName && sharedPreferences.get("pro_camera", false)) {
            val modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res)

            resparam.res.setReplacement(
                CAMERA_HOOK_PKG_NAME,
                "array",
                "shutter_time_value",
                modRes.fwd(R.array.shutter_time_value)
            )
            resparam.res.setReplacement(
                CAMERA_HOOK_PKG_NAME,
                "array",
                "iso_value",
                modRes.fwd(R.array.iso_value)
            )
            resparam.res.setReplacement(
                CAMERA_HOOK_PKG_NAME,
                "array",
                "shutter_speed_label_step_array",
                modRes.fwd(R.array.shutter_speed_label_step_array)
            )
            resparam.res.setReplacement(
                CAMERA_HOOK_PKG_NAME,
                "array",
                "iso_label_step_array",
                modRes.fwd(R.array.iso_label_step_array)
            )
            resparam.res.setReplacement(
                CAMERA_HOOK_PKG_NAME,
                "array",
                "iso_label_step_array",
                modRes.fwd(R.array.iso_label_step_array)
            )

            arrowUpId = resparam.res.addResource(modRes, R.drawable.ic_close_24dp)
            arrowDownId = resparam.res.addResource(modRes, R.drawable.ic_keyboard_arrow_down_24dp)
            layoutRes = resparam.res.addResource(modRes, R.layout.floating_button)
        }
    }


    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (GALLERY_HOOK_PKG_NAME == lpparam?.packageName) {
            Log.e("ProCams", "gallery")
            findAndHookMethod(
                "com.samsung.android.gallery.app.controller.externals.CreateGifCmd",
                lpparam.classLoader,
                "getMaxCount",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        Log.e("ProCams", "getMaxCount")
                        param?.result = 100
                    }
                })

        } else if (PHOTO_EDITOR_HOOK_PKG_NAME == lpparam?.packageName) {
            Log.e("ProCams", "gallery")
            findAndHookMethod(
                "com.sec.android.mimage.photoretouching.lpe.view.CustomSeekBar",
                lpparam.classLoader,
                "setMax",
                Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        Log.e("ProCams", "setMax")
                        if (param?.args?.get(0) == 14) {
                            param.args[0] = 60
                        }
                    }
                })
            findAndHookMethod(
                "com.sec.android.mimage.photoretouching.agif.MotionPhotoActivity",
                lpparam.classLoader,
                "onCreate",
                Bundle::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        XposedHelpers.setIntField(param?.thisObject, "THUMB_VIEW_TOTAL_NUM", 100)
                        super.beforeHookedMethod(param)
                        val num = XposedHelpers.getIntField(param?.thisObject, "THUMB_VIEW_TOTAL_NUM")
                        Log.e("ProCams", "onCreate $num")
                    }
                }
            )
            findAndHookMethod(
                "com.sec.android.mimage.photoretouching.agif.MotionPhotoActivity",
                lpparam.classLoader,
                "checkImagesCount",
                Int::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        super.afterHookedMethod(param)
                        Log.e("ProCams", "checkImagesCount ${param?.args?.get(0)}")
                    }
                }
            )

            val decodeUtil = findClass(
                "com.sec.android.mimage.photoretouching.agif.util.DecodeUtil",
                lpparam.classLoader
            )

            val method = XposedHelpers.findMethodExact(
                decodeUtil,
                "getBitmapFromUri",
                ContentResolver::class.java,
                Uri::class.java,
                String::class.java
            )
            val method2 = XposedHelpers.findMethodExact(
                decodeUtil,
                "getBitmapFromPath",
                ContentResolver::class.java,
                Uri::class.java,
                String::class.java
            )
            method.setAccessible(true)
            method2.setAccessible(true)

            Log.e("ProCams", "getBitmapFromUri $method")
            Log.e("ProCams", "getBitmapFromPath $method2")


            XposedBridge.hookMethod(
                method,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        Log.e("ProCams", "getBitmapFromUri")
                        val uri = param?.args?.get(1) as Uri
                        val contentResolver = param?.args?.get(0) as ContentResolver
                        Log.e("ProCams", "getBitmapFromUri $uri , $contentResolver")
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                        Log.e("ProCams", "getBitmapFromUri $bitmap")
                        param.result = bitmap
                    }
                }
            )

            XposedBridge.hookMethod(
                method2,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        Log.e("ProCams", "getBitmapFromFile")
                        val path = param?.args?.get(2) as String
                        param.result = BitmapUtil.createBitmap(filePath = path)
                    }
                }
            )

        } else if (CAMERA_HOOK_PKG_NAME == lpparam?.packageName && sharedPreferences.get("pro_camera", false)) {

            findAndHookMethod(
                HOOK_CLASS_NAME_MAKER,
                lpparam.classLoader,
                EXPOSURE,
                Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        Log.e("ProCams", "Exposure value is: ${param?.args?.get(0)}")
                        shutterTime = EXPOSURE_TIME_ARRAY[param?.args?.get(0) as? Int ?: 0]
                        param?.result = shutterTime
                    }
                })

            findAndHookMethod(
                HOOK_CLASS_NAME_MAKER,
                lpparam.classLoader,
                FOCUS,
                Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        Log.e("ProCams", "Focus value: ${param?.args?.get(0)}")
                        param?.result = 1000f / Constants.FOCUS_DISTANCE_ARRAY[param?.args?.get(0) as? Int ?: 1]
                    }
                })

            findAndHookMethod(
                HOOK_CLASS_NAME_MAKER,
                lpparam.classLoader,
                ISO,
                Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        Log.e("ProCams", "ISO value is: ${param?.args?.get(0)}")
                        param?.result = SENSOR_SENSITIVITY_ARRAY[param?.args?.get(0) as? Int ?: 0]
                    }
                })

            val tickSlider = findClass("com.sec.android.app.camera.widget.gl.TickSlider", lpparam.classLoader)

            XposedBridge.hookAllConstructors(tickSlider, object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    if (param?.args?.get(5) ?: 0 == 4 && param?.args?.get(6) ?: 0 == 11) {
                        param?.args?.set(5, 6)
                        param?.args?.set(6, 10)
                    } else if (param?.args?.get(5) ?: 0 == 5 && param?.args?.get(6) ?: 0 == 3) {
                        param?.args?.set(5, 7)
                        param?.args?.set(6, 3)
                    } else if (param?.args?.get(5) ?: 0 == 2 && param?.args?.get(6) ?: 0 == 92) {
                        param?.args?.set(5, 2)
                        param?.args?.set(6, 100)
                    }
                }
            })

            findAndHookMethod(
                "com.sec.android.app.camera.engine.request.TakePictureRequest",
                lpparam.classLoader,
                "getBlockingRequestTimeOut",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        Log.e("ProCams", "Timeout value: 650000")
                        param?.result = 43200000
                    }
                })

            findAndHookMethod(
                "com.sec.android.app.camera.Camera",
                lpparam.classLoader,
                "startInactivityTimer",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        Log.e("ProCams", "startInactivityTimer")
                        param?.result = false
                    }
                })

            findAndHookMethod(
                "com.sec.android.app.camera.Camera",
                lpparam.classLoader,
                "initializeShootingMode",
                Boolean::class.java,
                Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        Log.e("ProCams", "initializeShootingMode ${param?.args?.get(1)}")
                        isProMode = param?.args?.get(1) == 3
                    }
                })


            if (sharedPreferences.get("shutter_scheduler", false)) {
                var activity: Activity? = null
                var button: Button? = null

                val captureMethod = findClass(
                    "com.sec.android.app.camera.interfaces.CameraContext.CaptureMethod",
                    lpparam.classLoader
                )

                findAndHookMethod(
                    "com.sec.android.app.camera.Camera",
                    lpparam.classLoader,
                    "handleShutterKeyReleased",
                    captureMethod,
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam?) {
                            Log.e("ProCams", "onShutterKeyReleased, Pro Mode $isProMode")

                            if (isProMode) {

                                timer.ifNull {
                                    button?.init(layoutRes, arrowUpId, arrowDownId)

                                    val delay = sharedPreferences.get("shutter_scheduler_value", 100).toLong()

                                    val schedule = (shutterTime / 1000000) + delay
                                    timer = fixedRateTimer(
                                        name = "capture",
                                        daemon = false,
                                        initialDelay = schedule,
                                        period = schedule
                                    ) {
                                        val sharedPreferences =
                                            activity?.getSharedPreferences("custom_camera_pref", Context.MODE_PRIVATE)
                                        if (sharedPreferences?.getBoolean("overlay", true) == true) {
                                            XposedHelpers.callMethod(
                                                param?.thisObject,
                                                "handleShutterKeyReleased",
                                                param?.args?.get(0)
                                            )
                                        } else {
                                            timer?.cancel()
                                            timer?.purge()
                                            timer = null
                                        }
                                    }
                                }


                            }
                        }
                    })

                findAndHookMethod(
                    "com.sec.android.app.camera.Camera",
                    lpparam.classLoader,
                    "onPause",
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam?) {
                            Log.e("ProCams", "onPause")
                            super.afterHookedMethod(param)
                            button?.handleOnDestroy()
                        }
                    })

                findAndHookMethod(
                    "com.sec.android.app.camera.Camera",
                    lpparam.classLoader,
                    "onCreate",
                    Bundle::class.java,
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam?) {
                            super.afterHookedMethod(param)
                            activity = param?.thisObject as Activity
                            button = Button(activity!!)
                        }
                    }
                )

                findAndHookMethod(
                    "com.sec.android.app.camera.Camera",
                    lpparam.classLoader,
                    "changeShootingMode",
                    Int::class.java,
                    Boolean::class.java,
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam?) {
                            Log.e("ProCams", "changeShootingMode")
                            isProMode = param?.args?.get(0) == 3
                            button?.handleOnDestroy()
                        }
                    })
            }

        }
    }

    fun Timer?.ifNull(block: () -> Unit) {
        this ?: block()
    }

    companion object {
        var timer: Timer? = null
    }
}