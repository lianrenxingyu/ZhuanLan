package io.bxbxbai.zhuanlan.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import butterknife.ButterKnife;
import io.bxbxbai.common.view.CircularLoadingView;
import io.bxbxbai.zhuanlan.R;
import io.bxbxbai.zhuanlan.adapter.PeopleListAdapter;
import io.bxbxbai.zhuanlan.bean.User;
import io.bxbxbai.zhuanlan.bean.UserEntity;
import io.bxbxbai.zhuanlan.core.Api;
import io.bxbxbai.zhuanlan.core.DataCenter;
import io.bxbxbai.zhuanlan.core.SimpleCallback;
import io.bxbxbai.zhuanlan.core.ZhuanLanApi;

import java.util.List;
import java.util.Map;

/**
 * 主界面中的作者列表，用fragment碎片实现
 *
 * @author bxbxbai
 */
public class PeopleListFragment extends Fragment {

    private RecyclerView recyclerView;
    private CircularLoadingView mLoadingView;
    private PeopleListAdapter mAdapter;

    //    控件的实例化

    /**
     * @param inflater
     * @param container          容器，是指fragment的容器
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.common_list, container, false);
        recyclerView = ButterKnife.findById(v, R.id.recycler_view);

        //设置recycleView的布局方式，这里是线性，相关的有横向滚动
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLoadingView = ButterKnife.findById(v, R.id.v_loading);
        return v;
    }

    // listview设置适配器，这个步骤也可以在onCreateView完成
    //onViewCreated当视图被创建完毕后
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new PeopleListAdapter(getActivity());
        //recycleView没有setOnItemClickListener，监听事件需要在适配器里面实现，
        // 好处是可以对每个列表项内部不同控件实现监听
        recyclerView.setAdapter(mAdapter);
        getUserIdList();
    }

    //    获取本地数据库中存储的用户id列表
    private void getUserIdList() {
        String[] ids = getActivity().getResources().getStringArray(R.array.people_ids);
        Map<String, UserEntity> map = new ArrayMap<>();
    //  在数据中心DataCenter中查询已经保存的用户列表
        List<UserEntity> list = DataCenter.instance().queryAll(UserEntity.class);
        for (UserEntity entity : list) {
            map.put(entity.getSlug(), entity);
        }

        for (String id : ids) {
            UserEntity entity = map.get(id);
            if (entity != null) {
                mAdapter.addItem(entity);
                showListView();
            } else {
                makeRequest(id);
            }
        }
    }
//  请求某一个id的数据，请求的是某一个人的数据
    private void makeRequest(String id) {
//        封装了retrofit的使用细节
        Api api = ZhuanLanApi.getZhuanlanApi();
//        发起网络请求，重写onResponse方法，不过怎么发起的网络请求并不是很明白，这里没有见到URL和一些其他的东西
        api.getUser(id).enqueue(new SimpleCallback<User>() {
            @Override
            public void onResponse(User user, int code, String msg) {
                showListView();
                mAdapter.addItem(user.toUserEntity());//添加项目
                DataCenter.instance().save(user.toUserEntity());//存入数据中心
            }
        });
    }

    private void showListView() {
        recyclerView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
    }

    //    返回一个PeopleListFragment实例，基本算是一个工厂方法了
    public static PeopleListFragment newInstance() {
        Bundle bundle = new Bundle();
        PeopleListFragment fragment = new PeopleListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
