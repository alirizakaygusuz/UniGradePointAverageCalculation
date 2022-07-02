package com.alirizakaygusuz.unigradepointaveragecalculator

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.AnimationUtils
import com.alirizakaygusuz.unigradepointaveragecalculator.databinding.ActivitySplashBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initalizeScreen()

        initalizeAnimation()

        binding.imbuttonSplashButton.setOnClickListener {
            clickImagebuttonSplashButton()
        }

    }


    private fun clickImagebuttonSplashButton() {
        val throwup_cap = AnimationUtils.loadAnimation(this, R.anim.throwup_cap)
        val down_button = AnimationUtils.loadAnimation(this, R.anim.down_button)

        binding.imvGraduationKep.startAnimation(throwup_cap)
        binding.imbuttonSplashButton.startAnimation(down_button)

        object : CountDownTimer(1500, 1500) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }

        }.start()

    }

    private fun initalizeScreen() {
        Glide.with(this).load(R.drawable.background).into(object :
            CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                binding.constraintLayoutSpalsh.background = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }


        })
        Glide.with(this).load(R.drawable.graduation_kep).into(binding.imvGraduationKep)


    }

    private fun initalizeAnimation() {
        val down_cap = AnimationUtils.loadAnimation(this, R.anim.down_cap)
        val up_button = AnimationUtils.loadAnimation(this, R.anim.up_button)

        binding.imvGraduationKep.animation = down_cap
        binding.imbuttonSplashButton.animation = up_button
    }

}