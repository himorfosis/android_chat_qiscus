package com.siklusdev.qiscuschat.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.google.android.material.snackbar.Snackbar
import com.siklusdev.qiscuschat.R
import kotlin.math.ceil


fun View.snackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun Int.drawable(resources: Resources) : Drawable? {
    return ResourcesCompat.getDrawable(resources, this, null)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.hideKeyboard(view: View) {
    view.hideKeyboard()
}

fun ComponentActivity.bindKeyboardStateEvents() {
    lifecycle.addObserver(ViewGroupHolder(findViewById(Window.ID_ANDROID_CONTENT)))
}

fun ViewGroup.isKeyboardOpen(visibleThreshold: Float = 100f): Boolean {
    val measureRect = Rect()
    getWindowVisibleDisplayFrame(measureRect)
    return rootView.height - measureRect.bottom > ceil((visibleThreshold * Resources.getSystem().displayMetrics.density))
}

val TextView.string: String
    get() = text.toString()

val TextView.int: Int
    get() = text.toString().toIntOrNull() ?: 0

val TextView.float: Float
    get() = text.toString().toFloatOrNull() ?: 0f

val TextView.double: Double
    get() = text.toString().toDoubleOrNull() ?: 0.0

enum class KeyboardState { OPEN, CLOSED }

object KeyboardStateLiveData {
    private val _state = MutableLiveData<KeyboardState>()
    val state: LiveData<KeyboardState> = _state

    fun post(state: KeyboardState) {
        _state.postValue(state)
    }
}

private class ViewGroupHolder(private val root: ViewGroup) : LifecycleEventObserver {
    private val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
        private var previous: Boolean = root.isKeyboardOpen()

        override fun onGlobalLayout() {
            root.isKeyboardOpen().let {
                if (it != previous) {
                    KeyboardStateLiveData.post(if (it) KeyboardState.OPEN else KeyboardState.CLOSED)
                    previous = previous.not()
                }
            }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_PAUSE) {
            unregisterKeyboardListener()
        } else if (event == Lifecycle.Event.ON_RESUME) {
            registerKeyboardListener()
        }
    }

    private fun registerKeyboardListener() {
        root.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    private fun unregisterKeyboardListener() {
        root.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

}

fun ImageView.loadDrawableName(
    icon: String?,
    placeholderResId: Int? = null,
    errorResId: Int? = null,
    requestListener: RequestListener<Drawable>? = null
) {
    val res = context.resources
    val packageName = context.packageName
    val uriIcon = res.getIdentifier("drawable/${icon}", null, packageName)
    this.setImageResource(uriIcon)
}

fun ImageView.loadUrl(
    url: String?,
    placeholderResId: Int? = null,
    errorResId: Int? = null,
    requestListener: RequestListener<Drawable>? = null
) {
    Glide.with(this)
        .load(url)
        .optionalFitCenter()
        .thumbnail(0.1f)
        .error(R.drawable.ic_broken_image)
        .apply {
            placeholderResId?.let { placeholder(it) }
            errorResId?.let { error(it) }
            requestListener?.let { listener(it) }
        }
        .into(this)
}

fun ImageView.loadProfileUrl(
    url: String?,
    placeholderResId: Int? = null,
    errorResId: Int? = null,
    requestListener: RequestListener<Drawable>? = null
) {
    Glide.with(this)
        .load(url)
        .optionalFitCenter()
        .thumbnail(0.1f)
        .error(R.drawable.ic_broken_image)
        .circleCrop()
        .apply {
            placeholderResId?.let { placeholder(it) }
            errorResId?.let { error(it) }
            requestListener?.let { listener(it) }
        }
        .into(this)
}


fun ImageView.loadUrlRounded(
    url: String?,
    placeholderResId: Int? = null,
    errorResId: Int? = null,
    requestListener: RequestListener<Drawable>? = null
) {
    Glide.with(this)
        .load(url)
        .optionalFitCenter()
        .thumbnail(0.1f)
        .error(R.drawable.ic_broken_image)
        .transform(CenterCrop(), RoundedCorners(14))
        .apply {
            placeholderResId?.let { placeholder(it) }
            errorResId?.let { error(it) }
            requestListener?.let { listener(it) }
        }
        .into(this)
}

fun ImageView.loadUrlCircle(
    url: String?,
    placeholderResId: Int? = null,
    errorResId: Int? = null,
    requestListener: RequestListener<Drawable>? = null
) {
    Glide.with(this)
        .load(url)
        .optionalFitCenter()
        .thumbnail(0.1f)
        .error(R.drawable.ic_broken_image)
        .circleCrop()
        .apply {
            placeholderResId?.let { placeholder(it) }
            errorResId?.let { error(it) }
            requestListener?.let { listener(it) }
        }
        .into(this)
}