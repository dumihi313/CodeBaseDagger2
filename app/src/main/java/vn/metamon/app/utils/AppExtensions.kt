package vn.metamon.app.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.*
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import vn.metamon.app.presentation.NavHost
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

private val DAY_MONTH_YEAR_TIME_UTC = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

fun Context?.dp(size: Float): Int {
    if (this == null) {
        return 0
    }
    return kotlin.math.ceil(size * this.resources.displayMetrics.density).toInt()
}

fun Context?.dpf(size: Float): Float {
    if (this == null) {
        return 0f
    }
    return kotlin.math.ceil(size * this.resources.displayMetrics.density)
}

fun Context?.dp2(size: Float): Int {
    if (this == null) {
        return 0
    }
    return kotlin.math.floor(size * this.resources.displayMetrics.density).toInt()
}

fun Context?.dp2px(size: Float): Float {
    if (this == null) {
        return 0f
    }
    return size * this.resources.displayMetrics.density
}

fun Context?.sp(size: Float): Float {
    if (this == null) {
        return 0f
    }
    return size * this.resources.displayMetrics.scaledDensity
}

fun Context?.px2sp(pixel: Float): Float {
    if (this == null) {
        return 0f
    }
    return pixel / resources.displayMetrics.scaledDensity
}

fun Context.getColorCompat(@ColorRes colorRes: Int): Int =
    ContextCompat.getColor(this, colorRes)

fun Context.getFont(@FontRes fontRes: Int): Typeface? =
    try {
        ResourcesCompat.getFont(this, fontRes)
    } catch (e: Resources.NotFoundException) {
        null
    }

fun Context.getDrawableCompat(@DrawableRes res: Int): Drawable? {
    return ContextCompat.getDrawable(this, res)
}

fun Context.getDrawableUri(@DrawableRes drawableRes: Int): Uri =
    Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(this.resources.getResourcePackageName(drawableRes))
        .appendPath(this.resources.getResourceTypeName(drawableRes))
        .appendPath(this.resources.getResourceEntryName(drawableRes))
        .build()

fun Context.getResourceArray(@ArrayRes arrayRes: Int): List<Int> {
    val ret: MutableList<Int> = ArrayList()

    val typedArray: TypedArray = resources.obtainTypedArray(arrayRes)

    try {
        val length = typedArray.length()
        for (i in 0 until length) {
            val resourceId = typedArray.getResourceId(i, 0)
            ret.add(resourceId)
        }
    } catch (e: Exception) {
    } finally {
        typedArray.recycle()
    }

    return ret
}

fun Activity.openAppSettings(requestCode: Int) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivityForResult(intent, requestCode)
}

fun AppCompatActivity.showDialog(dialog: DialogFragment, tag: String? = null) {
    if (supportFragmentManager.isStateSaved || dialog.isAdded) {
        return
    }

    dialog.show(supportFragmentManager, tag)
}

fun Fragment.openAppSettings(requestCode: Int) {
    context?.let {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", it.packageName, null)
        intent.data = uri
        startActivityForResult(intent, requestCode)
    }
}

fun Fragment.findNavHost(): NavHost? {
    val parent = parentFragment ?: return if (activity is NavHost) {
        activity as NavHost
    } else {
        null
    }

    return if (parent is NavHost) {
        parent
    } else {
        parent.findNavHost()
    }
}

fun Fragment.navigateBack() {
    findNavHost()?.navHostFragmentManager()?.popBackStack()
}

fun Fragment.showDialog(dialog: DialogFragment, tag: String? = null) {
    if (childFragmentManager.isStateSaved || dialog.isAdded) {
        return
    }

    dialog.show(childFragmentManager, tag)
}

fun ViewModel.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend () -> Unit
): Job {
    return viewModelScope.launch(context) {
        block()
    }
}

inline fun <reified T> LiveData<MetaEvent<T>>.observeEvent(
    lifeCycleOwner: LifecycleOwner,
    crossinline onChanged: (observer: T) -> Unit
) {
    this.observe(lifeCycleOwner, EventObserver { onChanged(it) })
}

fun ViewGroup.inflate(@LayoutRes layout: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attachToRoot)
}

fun Int.toExpiredDay(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, this)

    DAY_MONTH_YEAR_TIME_UTC.timeZone = TimeZone.getTimeZone("UTC")
    return DAY_MONTH_YEAR_TIME_UTC.format(calendar.time)
}

fun File?.isMd5Match(md5: String?): Boolean {
    if (this == null) {
        return false
    }

    if (md5.isNullOrEmpty()) {
        return true
    }

    val fileMd5 = FileUtils.getMD5ChecksumOfFile(this.absolutePath).toUpperCase()
    return TextUtils.equals(fileMd5, md5)
}

@ExperimentalCoroutinesApi
fun <T> Flow<T>.flowOnIO(): Flow<T> = this.flowOn(Dispatchers.IO)

fun hasLaunchActivity(pm: PackageManager, packageName: String): Boolean {
    val launchIntent = pm.getLaunchIntentForPackage(packageName)
    return launchIntent?.resolveActivity(pm) != null
}

fun getDeviceMetrics(context: Context): DisplayMetrics {
    val metrics = DisplayMetrics()
    val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    display.getMetrics(metrics)
    return metrics
}

fun Context.getInteger(@IntegerRes id: Int): Int =
    this.resources.getInteger(id)

@ColorInt
fun safeParseColor(colorString: String, @ColorInt fallback: Int = Color.TRANSPARENT): Int {
    return try {
        Color.parseColor(colorString)
    } catch (ignored: Exception) {
        fallback
    }
}