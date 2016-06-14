package tedu.sheng.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;
import tedu.sheng.R;


/**
 * Created by Administrator on 2016/6/13.
 */
public class BankFragment extends Fragment {
    View v;
    BankHotFragment bankHotFragment;
    BankNewFragment bankNewFragment;
    BankBbFragment bankBbFragment;
    BankRadioFragment bankRadioFragment;
    BankSingerFragment bankSingerFragment;
    private  List<Fragment> fragments=new ArrayList<>();

    ScrollerViewPager viewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_bank,null);
        viewPager = (ScrollerViewPager) v.findViewById(R.id.view_pager);
        SpringIndicator springIndicator = (SpringIndicator) v.findViewById(R.id.indicator);



        PagerModelManager manager = new PagerModelManager();
        bankHotFragment=new BankHotFragment();
        bankNewFragment=new BankNewFragment();
        bankBbFragment=new BankBbFragment();
        bankRadioFragment=new BankRadioFragment();
        bankSingerFragment=new BankSingerFragment();
        fragments.add(bankHotFragment);
        fragments.add(bankNewFragment);
        fragments.add(bankBbFragment);

        fragments.add(bankSingerFragment);
        fragments.add(bankRadioFragment);
        manager.addCommonFragment(fragments,getTitles());
        ModelPagerAdapter adapter = new ModelPagerAdapter(getActivity().getSupportFragmentManager(), manager);
        viewPager.setAdapter(adapter);
        viewPager.fixScrollSpeed();

        // just set viewPager
        springIndicator.setViewPager(viewPager);


        return v;

    }
    private List<String> getTitles(){
        return Lists.newArrayList("热歌", "新歌","英文","歌手","电台");
    }




}
