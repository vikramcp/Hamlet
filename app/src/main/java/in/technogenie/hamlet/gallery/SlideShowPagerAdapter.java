package in.technogenie.hamlet.gallery;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.technogenie.hamlet.R;
import in.technogenie.hamlet.beans.Upload;
import in.technogenie.hamlet.utils.ScreenUtils;

//import in.technogenie.hamlet.beans.GalleryItemVO;

/**
 * Created by amardeep on 11/3/2017.
 */

public class SlideShowPagerAdapter extends PagerAdapter {

    Context mContext;
    //Layout inflater
    LayoutInflater mLayoutInflater;
    //list of Gallery Items
    List<Upload> galleryItems;

    public SlideShowPagerAdapter(Context context, List<Upload> galleryItems) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //set galleryItems
        this.galleryItems = galleryItems;
    }

    @Override
    public int getCount() {
        return galleryItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewThumbnail);

        //load current image in viewpager
        //Picasso.get().load(new File(galleryItems.get(position).getImageUri())).fit().into(imageView);
        Picasso.get().load(galleryItems.get(position).getUrl())
                .centerInside()
                .resize(ScreenUtils.getScreenWidth(mContext), ScreenUtils.getScreenHeight(mContext))
                .into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

}