package tedu.sheng.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



import tedu.sheng.R;
import tedu.sheng.ui.bMapActivity;

/**
 * Created by Administrator on 2016/6/13.
 */
public class NewsFragment extends Fragment  implements View.OnClickListener{
    private Button btnStartMap;
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_news,null);
        btnStartMap= (Button) v.findViewById(R.id.btn_start_map);
        btnStartMap.setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start_map:
                Intent intent=new Intent(getActivity() , bMapActivity.class);
                startActivity(intent);
        }
    }
}
