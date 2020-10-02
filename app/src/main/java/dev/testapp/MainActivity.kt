package dev.testapp

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
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
    var imageUrl =
        "https://coutloot-cdn.gumlet.com/listingImages/image_8ytm02si7hk_1601652244747.jpeg"
    var isLoadFromDrawable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFromButton.setOnClickListener {
            if (isLoadFromDrawable) {
                isLoadFromDrawable = false
                loadFromButton.text = "Load From Drawable"
            } else {
                isLoadFromDrawable = true
                loadFromButton.text = "Load From Url"
            }
            reload()
        }

        button.setOnClickListener {
            popSave.popNow()
        }

        button2.setOnClickListener {
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

    private fun reload() {
        if (isLoadFromDrawable) {
            mainImage.setImageResource(R.drawable.bags)
            setUpPopSave(R.drawable.bags, "")
        } else {
            Glide.with(this)
                .load(imageUrl)
                .into(mainImage)
            setUpPopSave(null, imageUrl)
        }
    }

    private fun setUpPopSave(imageResource: Int?, imageUrl: String?) {
        popSave.setPopperImage(imageResource ?: imageUrl!!)
            .setPopperSize(130, 130)
            .setOnFinishListener(object : PopSaveDrawee.OnFinish {
                override fun onFinish() {
                    PopSaveAnimUtils.scaleUp(wishlistHome1)
                    Handler().postDelayed({
                        counter++
                        wishList_badge1.visibility = View.VISIBLE
                        wishList_badge1.setText("" + counter)
                        PopSaveAnimUtils.scaleUp(wishList_badge1)
                    }, 800)
                    Snackbar.make(mainScreen, "Saved to Wishlist", Snackbar.LENGTH_LONG).show()
                }
            })
    }
}
