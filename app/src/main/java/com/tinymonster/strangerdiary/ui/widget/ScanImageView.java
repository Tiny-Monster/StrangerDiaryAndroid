package com.tinymonster.strangerdiary.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.utils.GlideLoaderUtils;

import java.io.File;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by TinyMonster on 24/01/2019.
 */

public class ScanImageView {
    private int startPosition;
    private int currentPosition;
    private Activity activity;
    private List<String> urls;
    private Dialog dialog;
    private ImageView backImage;
    private List<View> views;
    private MyPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TextView dialog_scan_page;

    public ScanImageView(Activity activity) {
        this.activity = activity;
        init();
    }

    private void init() {
        RelativeLayout relativeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.dialog_scan_pic, null);
        backImage = (ImageView) relativeLayout.findViewById(R.id.dialog_scan_finish);
        viewPager = (ViewPager) relativeLayout.findViewById(R.id.dialog_scan_viewpager);
        dialog = new Dialog(activity, R.style.Dialog_Fullscreen);
        dialog.setContentView(relativeLayout);
        dialog_scan_page = (TextView) relativeLayout.findViewById(R.id.dialog_scan_page);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                String text = ++position + "/" + views.size();
                dialog_scan_page.setText(text);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setUrl(List<String> picUrls) {
        if (urls == null) {
            urls = new ArrayList<>();
        } else {
            urls.clear();
        }
        this.urls.addAll(picUrls);
    }

    public void create(int startPosition) throws ExecutionException, InterruptedException {
        this.startPosition = startPosition;
        this.currentPosition = startPosition;
        if(views==null){
            views = new ArrayList<>();
        }else {
            views.clear();
        }
        pagerAdapter = new MyPagerAdapter(views);
        for (final String url : urls) {
            FrameLayout frameLayout = (FrameLayout) activity.getLayoutInflater().inflate(R.layout.dialog_scan_item, null);
            final SubsamplingScaleImageView scaleImageView = frameLayout.findViewById(R.id.dialog_scan_item_subscaleimage);
            if (url.startsWith("http:")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final File file = Glide.with(activity).load(url).downloadOnly(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL).get();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    scaleImageView.setImage(ImageSource.uri(Uri.fromFile(file)));
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            } else {
                File file = new File(url);
                scaleImageView.setImage(ImageSource.uri(Uri.fromFile(file)));
            }
            views.add(frameLayout);
        }
        dialog_scan_page.setText("1/"+views.size());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(startPosition);
        dialog.show();
    }

    private class MyPagerAdapter extends PagerAdapter {
        private List<View> views;

        MyPagerAdapter(List<View> views) {
            this.views = views;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position == 0 && views.size() == 0) {
                dialog.dismiss();
                return;
            }
            if (position == views.size()) {
                container.removeView(views.get(--position));
            } else {
                container.removeView(views.get(position));
            }

        }
    }

    /**
     * 解除 清除引用
     */
    public void finish() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        activity = null;
    }
}
