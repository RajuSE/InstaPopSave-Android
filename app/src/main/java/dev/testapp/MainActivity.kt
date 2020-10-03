package dev.testapp

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.google.android.material.snackbar.Snackbar
import dev.android.R
import devx.insta.popsave.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.top_bar_home.*

class MainActivity : AppCompatActivity() {
    var isDebug = true
    var counter = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        popSaveDrawee
//            .setPopperImage(R.drawable.bags)//For local image.  For Remote image Use: "popSave?.imageView" and handle I/O
//            .setPopperAreaSize(0, 500)
//            .setPopperSize(130, 130)
            .shouldMoveUp()
//            .loadImage("http://raw.githubusercontent.com/RajuSE/InstaPopSave-Android/master/app/src/main/res/drawable/bags.jpg")
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

        var imageUrl="http://www.winterquilts.com/wp-content/uploads/2016/11/packing.png"

        button.setOnClickListener {
//            popSaveDrawee.popNow()
            popSaveDrawee?.frescoImageView.loadImage(imageUrl)//"http://raw.githubusercontent.com/RajuSE/InstaPopSave-Android/master/app/src/main/res/drawable/bags.jpg")

            val imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUrl))
//                .setResizeOptions(ResizeOptions(100, 100))
                .build()

//            popSaveDrawee?.frescoImageView?.setImageRequest(imageRequest)

            /*val controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(popSaveDrawee?.frescoImageView?.controller)
                .setTapToRetryEnabled(true)
                .build()*/

//            popSaveDrawee?.frescoImageView?.hierarchy?.setProgressBarImage(CircleProgressBarDrawable(this, 45 , false ))

//            if (true) {
//                popSaveDrawee?.frescoImageView?.hierarchy?.setPlaceholderImage(devx.insta.popsave.R.color.violet)
//                popSaveDrawee?.frescoImageView?.hierarchy?.setFailureImage(devx.insta.popsave.R.color.blue)
//            }
//            popSaveDrawee?.frescoImageView?.controller = controller

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
