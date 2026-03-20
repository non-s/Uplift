package com.motivacional.frases.utils

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.LoadAdError

class AdMobHelper(private val context: Context) {
    
    private var interstitialAd: InterstitialAd? = null
    private var adCounter = 0
    
    companion object {
        // IDs de teste do AdMob - SUBSTITUIR pelos IDs reais em produção
        const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
        const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        private const val AD_FREQUENCY = 5 // Mostrar anúncio a cada 5 ações
    }
    
    fun initialize() {
        MobileAds.initialize(context) {}
    }
    
    fun loadBannerAd(adView: AdView) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
    
    fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        
        InterstitialAd.load(
            context,
            INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
                
                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }
    
    fun showInterstitialAdIfReady(activity: Activity, onAdClosed: () -> Unit = {}) {
        adCounter++
        
        if (adCounter >= AD_FREQUENCY) {
            adCounter = 0
            
            interstitialAd?.let { ad ->
                ad.show(activity)
                ad.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        interstitialAd = null
                        loadInterstitialAd() // Pré-carregar próximo anúncio
                        onAdClosed()
                    }
                }
            } ?: run {
                loadInterstitialAd()
                onAdClosed()
            }
        } else {
            onAdClosed()
        }
    }
}
