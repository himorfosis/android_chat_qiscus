package com.siklusdev.qiscuschat.common

import android.R
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


fun openImageChooser(fragment: Fragment, title: String) {
    CropImage.activity()
        .setActivityTitle(title)
        .setAllowFlipping(false)
        .setCropMenuCropButtonTitle("Selesai")
        .setGuidelines(CropImageView.Guidelines.ON)
        .start(fragment.requireContext(), fragment)
}

inline fun handleImageChooserResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?,
    onResult: (Uri?, error: String?) -> Unit
) {
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
        val result = CropImage.getActivityResult(data)
        if (resultCode == Activity.RESULT_OK) {
            onResult(result.uri, null)
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            onResult(null, result.error.toString())
        }
    }
}

suspend fun compressImage(imageFile: File, destWidth: Int = 1080): File = withContext(Dispatchers.IO) {
    val b = BitmapFactory.decodeFile(imageFile.absolutePath)

    val origWidth = b.width
    val origHeight = b.height

    if (origWidth > destWidth) {
        // picture is wider than we want it, we calculate its target height
        val destHeight = origHeight / (origWidth / destWidth.toFloat())
        // we create an scaled bitmap so it reduces the image, not just trim it
        val b2 = Bitmap.createScaledBitmap(b, destWidth, destHeight.toInt(), false)
        val outStream = ByteArrayOutputStream()
        // compress to the format you want, JPEG, PNG...
        // 70 is the 0-100 quality percentage
        b2.compress(Bitmap.CompressFormat.JPEG, 70, outStream)
        // we save the file, at least until we have made use of it
        val fileName = imageFile.absolutePath.replace(
            imageFile.name,
            "${imageFile.nameWithoutExtension}-compressed.${imageFile.extension}"
        )
        val f = File(fileName)
        f.createNewFile()
        //write the bytes in file
        val fo = FileOutputStream(f)
        fo.write(outStream.toByteArray())
        // remember close de FileOutput
        fo.close()

        b.recycle()
        b2.recycle()
        return@withContext f
    }

    b.recycle()
    return@withContext imageFile
}

fun ImageView.imageRotate() {
    val matrix = Matrix()
    this.scaleType = ImageView.ScaleType.MATRIX //required

    matrix.postRotate(R.attr.angle.toFloat(), 0F, 180f)
    this.imageMatrix = matrix

}

fun ImageView.loadPath(
    uri: String?
) {
    val photo: Bitmap = BitmapFactory.decodeFile(uri)
    if (uri != null) this.setImageBitmap(photo)
}