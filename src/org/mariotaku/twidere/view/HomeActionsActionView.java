package org.mariotaku.twidere.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.mariotaku.twidere.R;

public class HomeActionsActionView extends FrameLayout {

	private final ImageView mIconView;
	private final ProgressBar mProgressBar;

	public HomeActionsActionView(final Context context) {
		this(context, null);
	}

	public HomeActionsActionView(final Context context, final AttributeSet attrs) {
		this(context, attrs, android.R.attr.actionButtonStyle);
	}

	public HomeActionsActionView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		inflate(context, R.layout.action_item_home_actions, this);
		mIconView = (ImageView) findViewById(android.R.id.icon);
		mProgressBar = (ProgressBar) findViewById(android.R.id.progress);
	}

	public void setIcon(final Bitmap bm) {
		mIconView.setImageBitmap(bm);
	}

	public void setIcon(final Drawable drawable) {
		mIconView.setImageDrawable(drawable);
	}

	public void setIcon(final int resId) {
		mIconView.setImageResource(resId);
	}

	public void setShowProgress(final boolean showProgress) {
		mProgressBar.setVisibility(showProgress ? View.VISIBLE : View.GONE);
		mIconView.setVisibility(showProgress ? View.GONE : View.VISIBLE);
	}

	public void setTitle(final CharSequence title) {
		setContentDescription(title);
	}

	public void setTitle(final int title) {
		setTitle(getResources().getText(title));
	}

}
