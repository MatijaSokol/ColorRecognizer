package hr.ferit.matijasokol.colorrecognizer.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.widget.Toast

fun View.getBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    this.draw(canvas)
    return bitmap
}

fun Int.toHexString(): String {
    return "%X".format(this)
}

fun Int.isNoColor(): Boolean {
    val r = Color.red(this)
    val g = Color.green(this)
    val b = Color.blue(this)

    if (r == 0 && g == 0 && b == 0) {
        return true
    }

    return false
}

fun Context.displayToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}