package com.example.util

import android.content.Context
import android.media.ToneGenerator
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object AudioVibrateUtil {
    private var toneGen: ToneGenerator? = null

    init {
        try {
            toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 60)
        } catch (e: Exception) {
            // ignore
        }
    }

    fun playClick(context: Context, soundEnabled: Boolean, vibrationEnabled: Boolean) {
        if (vibrationEnabled) {
            try {
                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                    vibratorManager?.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                }
                vibrator?.let { v ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        @Suppress("DEPRECATION")
                        v.vibrate(20)
                    }
                }
            } catch (e: Exception) {
                // ignore
            }
        }

        if (soundEnabled) {
            try {
                toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 30)
            } catch (e: Exception) {
                // ignore
            }
        }
    }

    fun playGoalComplete(context: Context, soundEnabled: Boolean, vibrationEnabled: Boolean) {
        if (vibrationEnabled) {
            try {
                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                    vibratorManager?.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                }
                vibrator?.let { v ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val timings = longArrayOf(0, 100, 50, 100)
                        val amplitudes = intArrayOf(0, 255, 0, 255)
                        v.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
                    } else {
                        @Suppress("DEPRECATION")
                        v.vibrate(longArrayOf(0, 100, 50, 100), -1)
                    }
                }
            } catch (e: Exception) {
                // ignore
            }
        }

        if (soundEnabled) {
            try {
                toneGen?.startTone(ToneGenerator.TONE_PROP_ACK, 150)
            } catch (e: Exception) {
                // ignore
            }
        }
    }
}
