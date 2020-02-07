package hr.ferit.matijasokol.colorrecognizer.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.MotionEvent
import android.view.View
import hr.ferit.matijasokol.colorrecognizer.R
import hr.ferit.matijasokol.colorrecognizer.extensions.displayToast
import hr.ferit.matijasokol.colorrecognizer.extensions.getBitmap
import hr.ferit.matijasokol.colorrecognizer.extensions.isNoColor
import hr.ferit.matijasokol.colorrecognizer.extensions.toHexString
import hr.ferit.matijasokol.colorrecognizer.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int =
        R.layout.activity_main

    override fun setUpUi() {
        setListeners()
    }

    private fun setListeners() {
        imageViewColorPallet.setOnTouchListener { view, motionEvent -> handleRecognizeColor(view!!, motionEvent!!) }
        textViewColorHex.setOnLongClickListener { handleCopyToClipboard() }
    }

    private fun handleCopyToClipboard(): Boolean {
        val text = textViewColorHex.text.toString()

        if (text.isNotEmpty()) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text label", text.subSequence(5, text.length))
            clipboard.setPrimaryClip(clipData)
            displayToast("Color copied to clipboard")
            return true
        }

        return false
    }

    private fun handleRecognizeColor(view: View, motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == MotionEvent.ACTION_DOWN || motionEvent.action == MotionEvent.ACTION_MOVE) {

            val bitmap = view.getBitmap()

            if (motionEvent.x.toInt() < 0 || motionEvent.x.toInt() >= bitmap.width || motionEvent.y.toInt() < 0 || motionEvent.y.toInt() >= bitmap.height) {
                return false
            }

            val pixel = bitmap.getPixel(motionEvent.x.toInt(), motionEvent.y.toInt())

            if (pixel.isNoColor()) {
                return false
            }

            setColor(pixel)
        }

        return true
    }

    private fun setColor(pixel: Int) {
        val hex = pixel.toHexString()

        val r = Color.red(pixel)
        val g = Color.green(pixel)
        val b = Color.blue(pixel)

        val rgbText = "RGB: $r $g $b"
        val spannableRgbText = SpannableString(rgbText)

        val fcsRed = ForegroundColorSpan(Color.RED)
        val fcsGreen = ForegroundColorSpan(Color.GREEN)
        val fcsBlue = ForegroundColorSpan(Color.BLUE)

        val readStart = 5
        val readEnd = readStart + r.toString().length
        spannableRgbText.setSpan(fcsRed, readStart, readEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableRgbText.setSpan(StyleSpan(Typeface.BOLD), readStart, readEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val greenStart = readEnd + 1
        val greenEnd = greenStart + g.toString().length
        spannableRgbText.setSpan(fcsGreen, greenStart, greenEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableRgbText.setSpan(StyleSpan(Typeface.BOLD), greenStart, greenEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val blueStart = greenEnd + 1
        val blueEnd = blueStart + b.toString().length
        spannableRgbText.setSpan(fcsBlue, blueStart, blueEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableRgbText.setSpan(StyleSpan(Typeface.BOLD), blueStart, blueEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        viewColor.setBackgroundColor(Color.rgb(r, g, b))
        textViewColorRGB.text = spannableRgbText
        textViewColorHex.text = "HEX: #${hex.subSequence(2, hex.length)}"
    }
}
