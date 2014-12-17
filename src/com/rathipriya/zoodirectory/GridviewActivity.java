package com.rathipriya.zoodirectory;

import com.rathipriya.zoodirectory.adapter.ImageAdapter;

import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridviewActivity extends ActionBarActivity {

	MediaPlayer mp        = null;
	int height;
	int width;
	public Integer[] thumb = {
			R.drawable.elephant_3,
			R.drawable.fox, R.drawable.monkey, R.drawable.gorilla, 
			R.drawable.hippopotamus, R.drawable.bear, R.drawable.wolf,
			R.drawable.pandas, R.drawable.white_rhinoceros,
			R.drawable.lionname,R.drawable.tiger,R.drawable.cheetah,
			R.drawable.deer, R.drawable.camel,R.drawable.hyena
	};
	public Integer[] sound = {
			R.raw.elephantsound,R.raw.foxsound,R.raw.monkeysound,R.raw.gorillasound,R.raw.hipposound,
			R.raw.bearsound, R.raw.wolf,R.raw.pandasound,R.raw.rhinossound,
			R.raw.lionsound, R.raw.tigersound, R.raw.cheetahsound,R.raw.deersound,
			R.raw.camelsound,R.raw.hyenasound

	};

	private Animator mCurrentAnimator;
	private int mShortAnimationDuration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gridview);

		//get the width and height of the screen
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);   
		int screenHeight = metrics.heightPixels;
		int screenWidth = metrics.widthPixels;
		height=(int) (screenHeight / 4.5);
		width=screenWidth / 3;


		//action bar color
		android.app.ActionBar bar=getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#246B47")));

		GridView gridView = (GridView) findViewById(R.id.gridview);

		// Instance of ImageAdapter Class
		gridView.setAdapter(new ImageAdapter(this,width,height));

		// Set on item click listener to the ListView
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {

				// Display the zoomed in image in full screen
				zoomImageFromThumb(v, thumb[pos]);

				managerOfSound(sound[pos]);

			}
		});
		// Set the Animation time form the android defaults
		mShortAnimationDuration = getResources().getInteger(
				android.R.integer.config_shortAnimTime);

	}//oncreate end

	/**
	 * Manager of Sounds
	 */
	protected void managerOfSound(int sound) {
		if (mp != null) {
			mp.reset();
			mp.release();
		}
		mp = MediaPlayer.create(this, sound);

		mp.start();
	}



	private void zoomImageFromThumb(final View thumbView, int imageResId) {
		// If there's an animation in progress, cancel it immediately and
		// proceed with this one.
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}

		// Load the high-resolution "zoomed-in" image.
		final ImageView expandedImageView = (ImageView) findViewById(R.id.expanded_image);
		expandedImageView.setImageResource(imageResId);

		// Calculate the starting and ending bounds for the zoomed-in image.
		// This step
		// involves lots of math. Yay, math.
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		// The start bounds are the global visible rectangle of the thumbnail,
		// and the
		// final bounds are the global visible rectangle of the container view.
		// Also
		// set the container view's offset as the origin for the bounds, since
		// that's
		// the origin for the positioning animation properties (X, Y).
		thumbView.getGlobalVisibleRect(startBounds);
		findViewById(R.id.container).getGlobalVisibleRect(finalBounds,
				globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		// Adjust the start bounds to be the same aspect ratio as the final
		// bounds using the
		// "center crop" technique. This prevents undesirable stretching during
		// the animation.
		// Also calculate the start scaling factor (the end scaling factor is
		// always 1.0).
		float startScale;
		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
				.width() / startBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}
		// Hide the thumbnail and show the zoomed-in view. When the animation
		// begins,
		// it will position the zoomed-in view in the place of the thumbnail.
		thumbView.setAlpha(0f);
		expandedImageView.setVisibility(View.VISIBLE);

		// Set the pivot point for SCALE_X and SCALE_Y transformations to the
		// top-left corner of
		// the zoomed-in view (the default is the center of the view).
		expandedImageView.setPivotX(0f);
		expandedImageView.setPivotY(0f);

		// Construct and run the parallel animation of the four translation and
		// scale properties
		// (X, Y, SCALE_X, and SCALE_Y).
		AnimatorSet set = new AnimatorSet();
		set.play(
				ObjectAnimator.ofFloat(expandedImageView, View.X,
						startBounds.left, finalBounds.left))
						.with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
								startBounds.top, finalBounds.top))
								.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
										startScale, 1f))
										.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y,
												startScale, 1f));
		set.setDuration(mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;

		// Upon clicking the zoomed-in image, it should zoom back down to the
		// original bounds
		// and show the thumbnail instead of the expanded image.
		final float startScaleFinal = startScale;
		expandedImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mCurrentAnimator != null) {
					mCurrentAnimator.cancel();
				}

				// Animate the four positioning/sizing properties in parallel,
				// back to their
				// original values.
				AnimatorSet set = new AnimatorSet();
				set.play(
						ObjectAnimator.ofFloat(expandedImageView, View.X,
								startBounds.left))
								.with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
										startBounds.top))
										.with(ObjectAnimator.ofFloat(expandedImageView,
												View.SCALE_X, startScaleFinal))
												.with(ObjectAnimator.ofFloat(expandedImageView,
														View.SCALE_Y, startScaleFinal));
				set.setDuration(mShortAnimationDuration);
				set.setInterpolator(new DecelerateInterpolator());
				set.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}

					@Override
					public void onAnimationCancel(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}
				});
				set.start();
				mCurrentAnimator = set;
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}


}
