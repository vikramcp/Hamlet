package in.technogenie.hamlet.gallery;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import in.technogenie.hamlet.beans.GalleryItemVO;

public class GalleryUtils {

    //Define bucket name from which you want to take images Example '/DCIM/Camera' for camera images
    public static final String CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().toString() + "/Download";

    //method to get id of image bucket from path
    public static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }

    //method to get images
    public static List<GalleryItemVO> getImages(Context context) {
        final String[] projection = {MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
        final String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        final String[] selectionArgs = {GalleryUtils.getBucketId(CAMERA_IMAGE_BUCKET_NAME)};
        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        ArrayList<GalleryItemVO> result = new ArrayList<GalleryItemVO>(cursor.getCount());
        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            final int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            do {
                GalleryItemVO galleryItem = new GalleryItemVO(cursor.getString(dataColumn), cursor.getString(nameColumn));
                result.add(galleryItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;

    }
}
