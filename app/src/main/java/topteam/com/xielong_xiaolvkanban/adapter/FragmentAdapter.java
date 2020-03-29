package topteam.com.xielong_xiaolvkanban.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragmentList;
    List<String> titl;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titl) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titl = titl;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /**
     * 得到页面的标题
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return titl.get(position);
    }
}
