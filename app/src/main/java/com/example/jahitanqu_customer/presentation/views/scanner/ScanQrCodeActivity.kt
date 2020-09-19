package com.example.jahitanqu_customer.presentation.views.scanner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jahitanqu_customer.common.utils.Constant
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

/**
 * Created by Maulana Ibrahim on 19/September/2020
 * Email maulibrahim19@gmail.com
 */
class ScanQrCodeActivity: AppCompatActivity(),ZXingScannerView.ResultHandler {

    lateinit var scannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)
    }

    override fun handleResult(p0: Result?) {
        val intent = Intent()
        intent.putExtra(Constant.KEY_ID_TRANSACTION, p0?.text)
        setResult(Constant.QRCODE_REQUEST_CODE, intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }
}