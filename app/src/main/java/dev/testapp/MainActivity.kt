package dev.testapp

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import devx.insta.popsave.PopSave
import devx.insta.popsave.PopSaveAnimUtils
import devx.insta.popsave.PopSaveDrawee


class MainActivity : AppCompatActivity() {
    var isDebug = true
    var counter = 0
    lateinit var popSaveDrawee: PopSaveDrawee
    lateinit var popSave: PopSave
    lateinit var buttonImagevw: Button
    lateinit var buttonDrawee: Button
    lateinit var button2: Button
    lateinit var wishlistHome1: ImageView
    lateinit var wishList_badge1: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        popSaveDrawee=findViewById(R.id.popSaveDrawee)
        popSave=findViewById(R.id.popSave)
        buttonImagevw=findViewById(R.id.buttonImagevw)
        buttonDrawee=findViewById(R.id.buttonDrawee)
        button2=findViewById(R.id.button2)
        wishlistHome1=findViewById(R.id.wishlistHome1)
        wishList_badge1=findViewById(R.id.wishList_badge1)


        popSaveDrawee
//            .setPopperImage(R.drawable.bags)//For local image.  For Remote image Use: "popSave?.imageView" and handle I/O
//            .setPopperAreaSize(0, 500)
//            .setPopperSize(130, 130)
            .loadImage("https://i.picsum.photos/id/41/200/300.jpg?hmac=oimmvNf5GBZCx44LN9J4KGnDqUvSWmmUwPcLUaUMxF0")
            .setOnFinishListener(object : PopSaveDrawee.OnFinish {
                override fun onFinish() {
                    onAnimFinishUI()
                 }
            })

        popSave
            .setPopperImage(R.drawable.bags)//For local image.  For Remote image Use: "popSave?.imageView" and handle I/O
//            .setPopperAreaSize(0, 500)
//            .setPopperSize(130, 130)
            //.loadImage not available Use: popSave.imageView object with Glide/Picasso etc
            .setOnFinishListener(object : PopSave.OnFinish {
                override fun onFinish() {
                    onAnimFinishUI()
                }
            })


        buttonImagevw.setOnClickListener {
            popSave.visibility = View.VISIBLE
            popSaveDrawee.visibility = View.GONE
            popSave.popNow()
        }

        buttonDrawee.setOnClickListener {
            popSave.visibility = View.GONE
            popSaveDrawee.visibility = View.VISIBLE
            popSaveDrawee.popNow()
        }

        button2.setOnClickListener {
            popSaveDrawee.highlightAreaToDebug(isDebug)
            popSave.highlightAreaToDebug(isDebug)
            if (isDebug == false) {
                button2.setText("Release")
                isDebug = true
            } else {
                button2.setText("Debug")
                isDebug = false
            }
        }

    }

    private fun onAnimFinishUI() {
        PopSaveAnimUtils.scaleUp(wishlistHome1)
        Handler().postDelayed({
            counter++
            wishList_badge1.visibility = View.VISIBLE
            wishList_badge1.text = "" + counter
            PopSaveAnimUtils.scaleUp(wishList_badge1)
        }, 800)
//        Snackbar.make(this, "Saved to Wishlist", Snackbar.LENGTH_LONG).show()
    }


}
