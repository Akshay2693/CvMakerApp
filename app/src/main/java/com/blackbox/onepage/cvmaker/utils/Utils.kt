package com.blackbox.onepage.cvmaker.utils

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by umair on 31/05/2017.
 */

internal object Utils {
    val instance = Utils

    val TAG: String = "Utils"
    val PACKAGE = "com.blackbox.onepage.cvmaker"

    fun generateHashKey(context: Context) {
        try {
            val info = context.packageManager.getPackageInfo(PACKAGE, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.i(TAG, Base64.encodeToString(md.digest(), Base64.NO_WRAP));
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d("Name not found", e.message, e)

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

    }
}
