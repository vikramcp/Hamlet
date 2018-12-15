package in.technogenie.hamlet.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import in.technogenie.hamlet.R;
import in.technogenie.hamlet.beans.GalleryItemVO;
import in.technogenie.hamlet.utils.ScreenUtils;


public class GalleryStripAdapter extends RecyclerView.Adapter {
    //Declare list of GalleryItems
    List<GalleryItemVO> galleryItems;
    Context context;
    GalleryStripCallBacks mStripCallBacks;
    GalleryItemVO mCurrentSelected;

    public GalleryStripAdapter(List<GalleryItemVO> galleryItems, Context context, GalleryStripCallBacks StripCallBacks, int CurrentPosition) {
        //set galleryItems
        this.galleryItems = galleryItems;
        this.context = context;
        //set stripcallbacks
        this.mStripCallBacks = StripCallBacks;
        //set current selected
        mCurrentSelected = galleryItems.get(CurrentPosition);
        //set current selected item as selected
        mCurrentSelected.setSelected(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.gallery_strip_item, parent, false);
        SquareLayout squareLayout = row.findViewById(R.id.squareLayout);
        return new GalleryStripItemHolder(squareLayout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //get Curent Gallery Item
        GalleryItemVO mCurrentItem = galleryItems.get(position);
        //get thumb square size 1/6 of screen width
        final int thumbSize = ScreenUtils.getScreenWidth(context) / 6;
        //cast holder to galleryStripItemHolder
        GalleryStripItemHolder galleryStripItemHolder = (GalleryStripItemHolder) holder;
        //get thumb size bitmap by using ThumbnailUtils
        /*Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mCurrentItem.getImageUri()),
                thumbSize, thumbSize);*/
        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeResource(context.getResources(), mCurrentItem.getImageUri()),
                thumbSize, thumbSize);
        //set thumbnail
        galleryStripItemHolder.imageViewThumbnail.setImageBitmap(ThumbImage);
        //set current selected
        if (mCurrentItem.isSelected()) {
            galleryStripItemHolder.imageViewThumbnail.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            //value 0 removes any background color
            galleryStripItemHolder.imageViewThumbnail.setBackgroundColor(0);
        }
        galleryStripItemHolder.imageViewThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call onGalleryStripItemSelected on click and pass position
                mStripCallBacks.onGalleryStripItemSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }

    public class GalleryStripItemHolder extends RecyclerView.ViewHolder {
        ImageView imageViewThumbnail;

        public GalleryStripItemHolder(View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
        }
    }

    //interface for communication on gallery strip interactions
    public interface GalleryStripCallBacks {
        void onGalleryStripItemSelected(int position);
    }

    //Method to highlight  selected item on gallery strip
    public void setSelected(int position) {
        //remove current selection
        mCurrentSelected.setSelected(false);
        //notify recyclerview that we changed  item to update its view
        notifyItemChanged(galleryItems.indexOf(mCurrentSelected));
        //select gallery item
        galleryItems.get(position).setSelected(true);
        //notify recyclerview that we changed  item to update its view
        notifyItemChanged(position);
        //set current selected
        mCurrentSelected = galleryItems.get(position);

    }

    //method to remove selection
    public void removeSelection() {
        mCurrentSelected.setSelected(false);
    }


}
