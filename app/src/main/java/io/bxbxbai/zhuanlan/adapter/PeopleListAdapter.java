package io.bxbxbai.zhuanlan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import io.bxbxbai.common.view.BaseRecyclerAdapter;
import io.bxbxbai.common.view.BaseViewHolder;
import io.bxbxbai.zhuanlan.bean.UserEntity;
import io.bxbxbai.zhuanlan.ui.PostListActivity;

/**
 * @author bxbxbai
 *
 * 首界面的RecycleView的自定义Adapter，同时也定义了监听事件，
 * recycleView没有setOnItemClickListener，监听事件需要在适配器里面实现，好处是可以对每个列表项内部不同控件实现监听
 *
 * 在这里需要注意的一点是每一个item项（一个列表项）被看做是一个UserEntity类型
 */
public class PeopleListAdapter extends BaseRecyclerAdapter<UserEntity> {

    public PeopleListAdapter(Context context) {
        super(context);
        setOnItemClickListener(new OnItemClickListener<UserEntity>() {
            @Override
            public void onItemClick(View view, int i, UserEntity userEntity) {
//                当主界面的列表项点击的时候，开启新的界面
                PostListActivity.start(getContext(), userEntity);
            }
        });
    }

    @Override
    public BaseViewHolder<UserEntity> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PeopleViewHolder(parent);
    }
}
