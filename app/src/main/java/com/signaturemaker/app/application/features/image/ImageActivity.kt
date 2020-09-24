package com.signaturemaker.app.application.features.image

import android.R.id
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.navArgs
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.application.core.extensions.loadFromUrl
import com.signaturemaker.app.application.core.extensions.loadFromUrlWhitoutPlaceHolder
import com.signaturemaker.app.application.core.platform.BaseActivity
import com.signaturemaker.app.databinding.ImageActivityBinding

/**
 * Created by Raúl Rodríguez Concepción on 06/05/2020.

 * raulrcs@gmail.com
 */

class ImageActivity : BaseActivity() {

    companion object {

        const val TAG = "itemFile"
        const val PHOTO = "PHOTO"
    }

    private lateinit var binding: ImageActivityBinding

    private val args: ImageActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ImageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivPhoto.transitionName = args.itemFile?.name + PHOTO
        //imageActivityBinding.tvTittle.transitionName = medicine.uuid + TITLE

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tvTittle.text = args.itemFile?.name

        args.itemFile?.uri?.let {uri->
            binding.ivPhoto.loadFromUrl(uri)
        }

        //binding.ivPhoto.loadFromUrlWhitoutPlaceHolder("file:///" + Utils.path + "/" + args.itemFile?.name)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            id.home -> {
                onBackPressed()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
