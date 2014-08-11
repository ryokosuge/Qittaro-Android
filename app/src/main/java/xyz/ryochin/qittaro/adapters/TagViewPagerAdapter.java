/**
 * =====================================================
 * ENCODE : UTF-8
 * CREATED AT 14/08/11.
 * CREATED BY kosuge.
 * Copyright © Samurai Factory Inc. All rights reserved.
 * ===================================================== 
 */

package xyz.ryochin.qittaro.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import xyz.ryochin.qittaro.fragments.TagFragment;
import xyz.ryochin.qittaro.models.TagModel;

public class TagViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = TagViewPagerAdapter.class.getSimpleName();
    private final TagViewPagerAdapter self = this;
    private List<TagModel> items;

    public TagViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.items = new ArrayList<TagModel>();
    }

    public TagViewPagerAdapter(FragmentManager fm, List<TagModel> items) {
        super(fm);
        this.items = items;
    }

    public void setItems(List<TagModel> items) {
        this.items = items;
    }

    public void addItem(TagModel item) {
        this.items.add(item);
    }

    public void addItems(List<TagModel> items) {
        this.items.addAll(items);
    }

    @Override
    public Fragment getItem(int position) {
        TagModel tagModel = this.items.get(position);
        return TagFragment.newInstance(tagModel.getUrlName());
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        TagModel tagModel = this.items.get(position);
        return tagModel.getName();
    }
}
