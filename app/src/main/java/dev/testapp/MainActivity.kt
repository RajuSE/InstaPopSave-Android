package dev.testapp

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import dev.android.R
import devx.insta.popsave.PopSave
import devx.insta.popsave.PopSaveAnimUtils
import devx.insta.popsave.PopSaveDrawee
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.top_bar_home.*

class MainActivity : AppCompatActivity() {
    var isDebug = true
    var counter = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val imageUri1 = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/master/docs/static/logo.png")
//        mainImage1?.setImageURI(imageUri1)

        popSaveDrawee
            .setPopperImage(R.drawable.bags)//For local image.  For Remote image Use: "popSave?.imageView" and handle I/O
            .setPopperAreaSize(0, 500)
            .setPopperSize(130, 130)
            .shouldMoveUp()
//            .loadImage("https://raw.githubusercontent.com/facebook/fresco/master/docs/static/logo.png")
            .setOnFinishListener(object : PopSaveDrawee.OnFinish {
                override fun onFinish() {

                    PopSaveAnimUtils.scaleUp(wishlistHome1)
                    Handler().postDelayed({
                        counter++
                        wishList_badge1.visibility = View.VISIBLE
                        wishList_badge1.text = "" + counter
                        PopSaveAnimUtils.scaleUp(wishList_badge1)
                    }, 800)
                    Snackbar.make(mainScreen, "Saved to Wishlist", Snackbar.LENGTH_LONG).show()
                }
            })

//        val imageUri = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/master/docs/static/logo.png")
//        popSave.frescoImageView?.setImageURI(imageUri)

        button.setOnClickListener {
            popSaveDrawee.popNow()
        }

        button2.setOnClickListener {
            popSaveDrawee.highlightAreaToDebug(isDebug)
            if (isDebug == false) {
                button2.setText("Release")
                isDebug = true
            } else {
                button2.setText("Debug")
                isDebug = false
            }
        }

    }


}
