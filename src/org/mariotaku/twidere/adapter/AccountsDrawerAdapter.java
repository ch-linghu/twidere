package org.mariotaku.twidere.adapter;

import java.util.ArrayList;

import org.mariotaku.twidere.Constants;
import org.mariotaku.twidere.R;
import org.mariotaku.twidere.app.TwidereApplication;
import org.mariotaku.twidere.model.Account;
import org.mariotaku.twidere.util.ImageLoaderWrapper;
import org.mariotaku.twidere.view.holder.AccountDrawerGroupViewHolder;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountsDrawerAdapter extends BaseExpandableListAdapter implements Constants {

	private static final int GROUP_LAYOUT = R.layout.accounts_drawer_item_group;
	private static final int CHILD_LAYOUT = R.layout.menu_list_item;
	private final int mDefaultBannerWidth;
	private final ArrayList<int[]> mAccountActions = new ArrayList<int[]>();

	{
		mAccountActions.add(new int[] { R.string.view_user_profile, R.drawable.ic_menu_profile, MENU_VIEW_PROFILE });
		mAccountActions.add(new int[] { R.string.edit_profile, R.drawable.ic_menu_edit, MENU_EDIT });
		mAccountActions.add(new int[] { R.string.set_color, R.drawable.ic_menu_color_palette, MENU_SET_COLOR });
	}

	private int mBannerWidth;

	private final ImageLoaderWrapper mImageLoader;
	private Cursor mCursor;
	private Account.Indices mIndices;
	private final LayoutInflater mInflater;

	public AccountsDrawerAdapter(final Context context) {
		final TwidereApplication app = TwidereApplication.getInstance(context);
		mImageLoader = app.getImageLoaderWrapper();
		mInflater = LayoutInflater.from(context);
		mDefaultBannerWidth = context.getResources().getDisplayMetrics().widthPixels;
	}

	@Override
	public Integer getChild(final int groupPosition, final int childPosition) {
		return mAccountActions.get(childPosition)[2];
	}

	@Override
	public long getChildId(final int groupPosition, final int childPosition) {
		return ExpandableListView.getPackedPositionForChild(groupPosition, childPosition);
	}

	@Override
	public int getChildrenCount(final int groupPosition) {
		return mAccountActions.size();
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild,
			final View convertView, final ViewGroup parent) {
		final View view = convertView != null ? convertView : mInflater.inflate(CHILD_LAYOUT, null);
		final int[] item = mAccountActions.get(childPosition);
		final TextView text1 = (TextView) view.findViewById(android.R.id.text1);
		final ImageView icon = (ImageView) view.findViewById(android.R.id.icon);
		text1.setText(item[0]);
		icon.setImageResource(item[1]);
		return view;
	}

	@Override
	public Account getGroup(final int groupPosition) {
		if (mCursor == null || mCursor.isClosed()) return null;
		mCursor.moveToPosition(groupPosition);
		return new Account(mCursor, mIndices);
	}

	@Override
	public int getGroupCount() {
		if (mCursor == null || mCursor.isClosed()) return 0;
		return mCursor.getCount();
	}

	@Override
	public long getGroupId(final int groupPosition) {
		return ExpandableListView.getPackedPositionForGroup(groupPosition);
	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded, final View convertView,
			final ViewGroup parent) {
		final View view = convertView != null ? convertView : mInflater.inflate(GROUP_LAYOUT, null);
		final int expander_res = isExpanded ? R.drawable.expander_open_holo : R.drawable.expander_close_holo;
		final Object tag = view.getTag();
		final AccountDrawerGroupViewHolder holder;
		if (tag instanceof AccountDrawerGroupViewHolder) {
			holder = (AccountDrawerGroupViewHolder) tag;
		} else {
			holder = new AccountDrawerGroupViewHolder(view);
			view.setTag(holder);
		}
		final Account item = getGroup(groupPosition);
		holder.name.setText(item.name);
		holder.screen_name.setText("@" + item.screen_name);
		holder.name_container.drawRight(item.user_color);
		holder.expand_indicator.setImageResource(expander_res);
		final int width = mBannerWidth > 0 ? mBannerWidth : mDefaultBannerWidth;
		mImageLoader.displayProfileBanner(holder.profile_banner, item.profile_banner_url, width);
		mImageLoader.displayProfileImage(holder.profile_image, item.profile_image_url);
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(final int groupPosition, final int childPosition) {
		return true;
	}

	public void setAccountsCursor(final Cursor cursor) {
		mCursor = cursor;
		mIndices = cursor != null ? new Account.Indices(cursor) : null;
		notifyDataSetChanged();
	}

	public void setBannerWidth(final int width) {
		if (mBannerWidth == width) return;
		mBannerWidth = width;
		notifyDataSetChanged();
	}

}