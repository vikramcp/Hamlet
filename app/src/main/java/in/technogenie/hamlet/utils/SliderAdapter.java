package in.technogenie.hamlet.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.technogenie.hamlet.R;

public class SliderAdapter extends PagerAdapter {


    private Context context;
    //private List<Integer> color;
    //private List<String> colorName;
    //private List<String> imageName;

    private int[] GalImages;


    public SliderAdapter(Context context, int[] GalImages) {
        this.context = context;
        this.GalImages = GalImages;
    }


/*
    public SliderAdapter(Context context, List<String> imageName) {
        this.context = context;
        this.imageName = imageName;
    }
*/

    @Override
    public int getCount() {
        return GalImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        //TextView textView = (TextView) view.findViewById(R.id.sliderTextView);
        ImageView imageView = (ImageView) view.findViewById(R.id.sliderImage);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        int padding = 10;
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(GalImages[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
