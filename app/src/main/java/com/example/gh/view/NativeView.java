package com.example.gh.view;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.drawerlayout.widget.DrawerLayout;

import com.anythink.core.api.ATAdConst;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.ATNetworkConfirmInfo;
import com.anythink.core.api.AdError;
import com.anythink.nativead.api.ATNative;
import com.anythink.nativead.api.ATNativeAdView;
import com.anythink.nativead.api.ATNativeDislikeListener;
import com.anythink.nativead.api.ATNativeEventExListener;
import com.anythink.nativead.api.ATNativeNetworkListener;
import com.anythink.nativead.api.NativeAd;
import com.anythink.network.gdt.GDTDownloadFirmInfo;
import com.example.gh.R;
import com.example.gh.util.ResouresUtils;

import java.util.HashMap;
import java.util.Map;

//public class NativeView {
//    private static final String TAG = "NativeView";
//    ATNative atNatives[] = new ATNative[1];
//    ;
//    ATNativeAdView anyThinkNativeAdView;
//    NativeAd mNativeAd;
//    ImageView mCloseView;
//    private Context mContext;
//    private FrameLayout mView;
//    private int mAdViewWidth = -1;
//    private int mAdViewHeight = -1;
//
//    private int mCurrentSelectIndex = 0;
//
//
//    public NativeView(Context context, FrameLayout layout) {
//        mContext = context;
//        mView = layout;
//        initCloseView();
//        initNatives();
//    }
//
//    public int getAdViewWidth() {
//        return mAdViewWidth;
//    }
//
//    public void setAdViewWidth(int adViewWidth) {
//        this.mAdViewWidth = adViewWidth;
//    }
//
//    public int getAdViewHeight() {
//        return mAdViewHeight;
//    }
//
//    public void setAdViewHeight(int adViewHeight) {
//        this.mAdViewHeight = adViewHeight;
//    }
//
//    private void initCloseView() {
//        if (mCloseView == null) {
//            mCloseView = new ImageView(mContext);
//            mCloseView.setImageResource(R.mipmap.ad_close);
//
//            int padding = ResouresUtils.dip2px(mContext, 5);
//            mCloseView.setPadding(padding, padding, padding, padding);
//
//            int size = ResouresUtils.dip2px(mContext, 30);
//            int margin = ResouresUtils.dip2px(mContext, 2);
//
//            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size);
//            layoutParams.topMargin = margin;
//            layoutParams.rightMargin = margin;
//            layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
//
//            mCloseView.setLayoutParams(layoutParams);
//        }
//    }
//
//
//    private void initNatives() {
//        initCloseView();
//
//        int padding = ResouresUtils.dip2px(mContext, 16);
//        final int containerHeight = ResouresUtils.dip2px(mContext, 340);
//        final int adViewWidth = mContext.getResources().getDisplayMetrics().widthPixels - 2 * padding;
//        final int adViewHeight = containerHeight- 2 * padding;
//
//        final NativeDemoRender anyThinkRender = new NativeDemoRender(mContext);
//        anyThinkRender.setCloseView(mCloseView);
//
//        atNatives[0] = new ATNative(mContext, "b6262159ae151e", new ATNativeNetworkListener() {
//            @Override
//            public void onNativeAdLoaded() {
//                Log.i(TAG, "onNativeAdLoaded");
//                NativeAd nativeAd = atNatives[mCurrentSelectIndex].getNativeAd();
//                if (nativeAd != null) {
//
//                    if (anyThinkNativeAdView != null) {
//                        anyThinkNativeAdView.removeAllViews();
//
//                        if (anyThinkNativeAdView.getParent() == null) {
//                            mView.addView(anyThinkNativeAdView, new FrameLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, containerHeight));
//                        }
//                    }
//
//                    if (mNativeAd != null) {
//                        mNativeAd.destory();
//                    }
//                    mNativeAd = nativeAd;
//
//                    mNativeAd.setNativeEventListener(new ATNativeEventExListener() {
//                        @Override
//                        public void onDeeplinkCallback(ATNativeAdView view, ATAdInfo adInfo, boolean isSuccess) {
//                            Log.i(TAG, "onDeeplinkCallback:" + adInfo.toString() + "--status:" + isSuccess);
//                        }
//
//                        @Override
//                        public void onAdImpressed(ATNativeAdView view, ATAdInfo entity) {
//                            Log.i(TAG, "native ad onAdImpressed:\n" + entity.toString());
//                        }
//
//                        @Override
//                        public void onAdClicked(ATNativeAdView view, ATAdInfo entity) {
//                            Log.i(TAG, "native ad onAdClicked:\n" + entity.toString());
//                        }
//
//                        @Override
//                        public void onAdVideoStart(ATNativeAdView view) {
//                            Log.i(TAG, "native ad onAdVideoStart");
//                        }
//
//                        @Override
//                        public void onAdVideoEnd(ATNativeAdView view) {
//                            Log.i(TAG, "native ad onAdVideoEnd");
//                        }
//
//                        @Override
//                        public void onAdVideoProgress(ATNativeAdView view, int progress) {
//                            Log.i(TAG, "native ad onAdVideoProgress:" + progress);
//                        }
//                    });
//                    mNativeAd.setDislikeCallbackListener(new ATNativeDislikeListener() {
//                        @Override
//                        public void onAdCloseButtonClick(ATNativeAdView view, ATAdInfo entity) {
//                            Log.i(TAG, "native ad onAdCloseButtonClick");
//                            if (view.getParent() != null) {
//                                ((ViewGroup) view.getParent()).removeView(view);
//                                view.removeAllViews();
//                            }
//                        }
//                    });
//
//                    if (true) {
//                        mNativeAd.setDownloadConfirmListener(new NativeAd.DownloadConfirmListener() {
//                            @Override
//                            public void onDownloadConfirm(Context context, ATAdInfo atAdInfo, View clickView, ATNetworkConfirmInfo networkConfirmInfo) {
//                                /**
//                                 * Only for GDT
//                                 */
//                                if (networkConfirmInfo instanceof GDTDownloadFirmInfo) {
//                                    if (clickView != null && anyThinkRender.getDownloadDirectViews().contains(clickView)) {
//                                        //You can try to get appinfo from  ((GDTDownloadFirmInfo) networkConfirmInfo).appInfoUrl
//                                        ((GDTDownloadFirmInfo) networkConfirmInfo).confirmCallBack.onConfirm();
//                                    } else {
//                                        //Open Dialog view
//                                        Log.i(TAG, "nonDownloadConfirm open confirm dialog");
////                                    new DownloadApkConfirmDialog(context, DownloadConfirmHelper.getApkJsonInfoUrl(((GDTDownloadFirmInfo) networkConfirmInfo).appInfoUrl), ((GDTDownloadFirmInfo) networkConfirmInfo).confirmCallBack).show();
//                                        new DownloadApkConfirmDialogWebView(context, ((GDTDownloadFirmInfo) networkConfirmInfo).appInfoUrl, ((GDTDownloadFirmInfo) networkConfirmInfo).confirmCallBack).show();
//                                    }
//                                }
//                            }
//                        });
//                        anyThinkRender.setWhetherSettingDownloadConfirmListener(true);
//                    } else {
//                        anyThinkRender.setWhetherSettingDownloadConfirmListener(false);
//                    }
//
//
//                    try {
//                        mNativeAd.renderAdView(anyThinkNativeAdView, anyThinkRender);
//                    } catch (Exception e) {
//
//                    }
//
//                    anyThinkNativeAdView.addView(mCloseView);
//
//                    anyThinkNativeAdView.setVisibility(View.VISIBLE);
//                    mNativeAd.prepare(anyThinkNativeAdView, anyThinkRender.getClickView(), null);
//                } else {
//
//                }
//
//            }
//
//            @Override
//            public void onNativeAdLoadFail(AdError adError) {
//                Log.i(TAG, "onNativeAdLoadFail, " + adError.getFullErrorInfo());
//
//            }
//        });
//
//        if (anyThinkNativeAdView == null) {
//            anyThinkNativeAdView = new ATNativeAdView(mContext);
//        }
//
//        if (anyThinkNativeAdView != null) {
//            anyThinkNativeAdView.removeAllViews();
//
//            if (anyThinkNativeAdView.getParent() == null) {
//                mView.addView(anyThinkNativeAdView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, containerHeight));
//            }
//        }
//
//        Map<String, Object> localMap = new HashMap<>();
//
//        // since v5.6.4
//        localMap.put(ATAdConst.KEY.AD_WIDTH, adViewWidth);
//        localMap.put(ATAdConst.KEY.AD_HEIGHT, adViewHeight);
//
//        atNatives[mCurrentSelectIndex].setLocalExtra(localMap);
//
//        atNatives[mCurrentSelectIndex].makeAdRequest();
//        anyThinkNativeAdView.setPadding(0, 0, 0, 0);
//
//    }
//
//}
