package it.faustobe.santibailor.util;

import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class ImageLoadingUtil {
    public static void loadImage(ImageView imageView, String imageUrl, int placeholderResId) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(placeholderResId)
                .error(placeholderResId)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(imageView);
    }

    public static void loadImage(ImageView imageView, int resourceId, int placeholderResId) {
        Glide.with(imageView.getContext())
                .load(resourceId)
                .placeholder(placeholderResId)
                .error(placeholderResId)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(imageView);
    }
}
