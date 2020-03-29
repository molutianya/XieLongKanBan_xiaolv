package topteam.com.xielong_xiaolvkanban.fragment;
/**
 * 所有的机台总状态看板
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import topteam.com.xielong_xiaolvkanban.R;
import topteam.com.xielong_xiaolvkanban.adapter.AllJtstateAdapter;
import topteam.com.xielong_xiaolvkanban.entity.AllJtstateEntity;
import topteam.com.xielong_xiaolvkanban.util.App;

@SuppressLint("ValidFragment")
public class AllJtstateFragment extends Fragment {

    View view; //前端主布局
    GridView gridView_v;
    List<AllJtstateEntity> dataList = new ArrayList<>();
    AllJtstateAdapter adapter;
    //表头数据控件
    TextView a_zl_v;
    TextView c_zl_v;
    TextView r_zl_v;
    TextView p_zl_v;
    TextView t_zl_v;
    TextView m_zl_v;
    TextView zcsc_v;
    TextView djsc_v;
    TextView tjsc_v;
    TextView bzrs_v;
    TextView zgrs_v;

    //存放机台数据的集合
    List<String> fMachineNrList = new ArrayList<>(); //机台编号和状态
    List<Integer> fColorList = new ArrayList<>(); //标题颜色
    List<Boolean> fYctjList = new ArrayList<>(); //异常停机
    List<Boolean> fCcxzqList = new ArrayList<>(); //超成型周期
    List<Boolean> fBlyjList = new ArrayList<>(); //不良预警
    List<Boolean> fClpsList = new ArrayList<>(); //超良品数
    List<Boolean> fZlcsList = new ArrayList<>(); //指令超时
    List<Boolean> fHdtxList = new ArrayList<>(); //换单提醒
    List<String> fNrList = new ArrayList<>(); //工单编号
    List<String> fItemNrList = new ArrayList<>(); //产品编号
    // List<String> fMouldNameNrList = new ArrayList<>(); //模具名称
    List<String> fSchqtyNrList = new ArrayList<>(); //生产数量
    List<String> fLpqtyList = new ArrayList<>(); //良品数量
    List<String> fBlqtyList = new ArrayList<>(); //不良品数量


    //看板表头数据
    String fyctj; //异常停机
    String fccxzq; //超成型周期
    String fblyj; //不良预警
    String fzlcs; //指令超时
    String fclps; //超良品数
    String fhdtx; //换单提示
    String fscjt; // 生产机台
    String fddjt; //待单机台
    String fycyx; //停产机台
    String bzrs;//投入人数
    String zgrs; //在岗人数

    Handler handler; //异步解析
    TextView time01; //布局时间
    String time; //系统时间
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        //获取表头数据
        okHttpZhiling();
         okHttpState();
        okHttpTime();


        //异步解析
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        a_zl_v.setText(fyctj + "");
                        c_zl_v.setText(fccxzq + "");
                        r_zl_v.setText(fblyj + "");
                        p_zl_v.setText(fclps + "");
                        t_zl_v.setText(fzlcs + "");
                        m_zl_v.setText(fhdtx + "");
                        zcsc_v.setText(fscjt + "");
                        djsc_v.setText(fddjt + "");
                        tjsc_v.setText(fycyx + "");
                        bzrs_v.setText(bzrs + "");
                        zgrs_v.setText(zgrs + "");
                        break;
                    case 2:
                           // testData();
                        try {
                            for (int i = 0; i < fMachineNrList.size(); i++) {
                                AllJtstateEntity entity = new AllJtstateEntity(
                                        fMachineNrList.get(i) + "",
                                        fColorList.get(i),
                                        fYctjList.get(i),
                                        fCcxzqList.get(i),
                                        fBlyjList.get(i),
                                        fClpsList.get(i),
                                        fZlcsList.get(i),
                                        fHdtxList.get(i),
                                        fNrList.get(i),
                                        fItemNrList.get(i),
                                        fSchqtyNrList.get(i),
                                        fLpqtyList.get(i),
                                        fBlqtyList.get(i)

                                );
                                dataList.add(entity);
                            }
                            adapter = new AllJtstateAdapter(dataList, context);
                            gridView_v.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        time01.setText(time);
                        break;
                    default:
                        break;
                }
            }
        };

    }


    /**
     * 关联前端布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.all_jtstate, container, false);
        initShow(view);
        return view;
    }

    /**
     * 初始化所有控件
     */
    private void initShow(View view) {
        gridView_v = view.findViewById(R.id.grid);
        a_zl_v = view.findViewById(R.id.a_zl);
        c_zl_v = view.findViewById(R.id.c_zl);
        r_zl_v = view.findViewById(R.id.r_zl);
        p_zl_v = view.findViewById(R.id.p_zl);
        t_zl_v = view.findViewById(R.id.t_zl);
        m_zl_v = view.findViewById(R.id.m_zl);
        zcsc_v = view.findViewById(R.id.zcsc);
        tjsc_v = view.findViewById(R.id.tjsc);
        djsc_v = view.findViewById(R.id.djsc);
        time01 = view.findViewById(R.id.time);
        bzrs_v = view.findViewById(R.id.bzrs);
        zgrs_v = view.findViewById(R.id.zgrs);
    }

    /**
     * 向云服务器发送请求获取表头数据
     */
    private void okHttpZhiling() {
        ScheduledExecutorService singleThreadScheduledPool = Executors.newSingleThreadScheduledExecutor();
        //延迟1秒后，每隔1秒执行一次该任务
        singleThreadScheduledPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    //创建一个OkHttpClient对象
                    OkHttpClient client = new OkHttpClient();
                    //创建一个Request用来发送请求
                    Request request = new Request.Builder()
                            //请求的目标地址
                            .url(App.url + App.GetGrandwayKanban1_Hz)
                            .build();
                    //调用okHttpClient的newCall方法创建䘝Call对象，
                    //并调用它的execute方法发送请求并获取服务器返回的数据
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i("jfsogjrgrhr",responseData+"");
                    //调用该方法解析从服务器返回的Json数据
                    parseJSONobjectZl(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1, 10, TimeUnit.SECONDS);


    }

    /**
     * 解析表头数据
     *
     * @param jsonData 传入将要解析的Json数据
     */
    private void parseJSONobjectZl(String jsonData) {
        try {

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jr = new JSONArray(jsonObject.getString("table"));
            for (int i = 0; i < jr.length(); i++) {
                JSONObject object = new JSONObject(jr.get(i).toString());
                fyctj = object.getString("fyctj") + "";
                fccxzq = object.getString("fccxzq") + "";
                fblyj = object.getString("fblyj") + "";
                fzlcs = object.getString("fzlcs") + "";
                fclps = object.getString("fclps") + "";
                fhdtx = object.getString("fhdtx") + "";
                fscjt = object.getString("fscjt") + "";
                fddjt = object.getString("fddjt") + "";
                fycyx = object.getString("fycyx") + "";
                bzrs = object.getString("fstdWork") + "";
                zgrs = object.getString("fOnWork") + "";
            }

            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 向云服务器发送请求获取所有的机台数据
     */
    private void okHttpState() {
        ScheduledExecutorService singleThreadScheduledPool = Executors.newSingleThreadScheduledExecutor();
        //延迟1秒后，每隔1秒执行一次该任务
        singleThreadScheduledPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    //创建一个OkHttpClient对象
                    OkHttpClient client = new OkHttpClient();
                    //创建一个Request用来发送请求
                    Request request = new Request.Builder()
                            //请求的目标地址
                            .url(App.url + App.GetGrandwayKanban1)
                            .build();
                    //调用okHttpClient的newCall方法创建䘝Call对象，
                    //并调用它的execute方法发送请求并获取服务器返回的数据
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i("jofjergoerjgr",responseData+"");
                    //调用该方法解析从服务器返回的Json数据
                    parseJSONobject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1, 10, TimeUnit.SECONDS);
    }

    /**
     * 解析所有的机台数据
     *
     * @param jsonData 被解析的Json数据
     */
    private void parseJSONobject(String jsonData) {
        try {
            //在重新获取数据之前，把之前的数据都清空
            fMachineNrList.clear();
            fColorList.clear();
            fYctjList.clear();
            fCcxzqList.clear();
            fBlyjList.clear();
            fClpsList.clear();
            fZlcsList.clear();
            fHdtxList.clear();
            fNrList.clear();
            fItemNrList.clear();
            // fMouldNameNrList.clear();
            fSchqtyNrList.clear();
            fLpqtyList.clear();
            fBlqtyList.clear();
            dataList.clear();


            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jr = new JSONArray(jsonObject.getString("table"));
            for (int i = 0; i < jr.length(); i++) {
                JSONObject object = new JSONObject(jr.get(i).toString());
                fMachineNrList.add(object.getString("fMachineNr") + "       " + object.getString("fMachineState"));
                fColorList.add(Integer.parseInt(object.getString("fColor") + ""));
                fYctjList.add(Boolean.parseBoolean(object.getString("fYctj") + ""));
                fCcxzqList.add(Boolean.parseBoolean(object.getString("fCcxzq") + ""));
                fBlyjList.add(Boolean.parseBoolean(object.getString("fBlyj") + ""));
                fClpsList.add(Boolean.parseBoolean(object.getString("fClps") + ""));
                fZlcsList.add(Boolean.parseBoolean(object.getString("fZlcs") + ""));
                fHdtxList.add(Boolean.parseBoolean(object.getString("fHdtx") + ""));
                fNrList.add(object.getString("fNr") + "");
                fItemNrList.add(object.getString("fcpName"));
                //  fMouldNameNrList.add(object.getString("fworkerName1")+"  "+object.getString("fworkerName2"));
                //  fMouldNameNrList.add(object.getString("fMouldName"));
                fSchqtyNrList.add(object.getString("fSchqty") + "");
                fLpqtyList.add(object.getString("fLpqty") + "");
                fBlqtyList.add(object.getString("fBlqty") + "");

            }
            Message message = new Message();
            message.what = 2;
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送请求获取服务器时间
     */
    private void okHttpTime() {

        ScheduledExecutorService singleThreadScheduledPool = Executors.newSingleThreadScheduledExecutor();
        //延迟1秒后，每隔1秒执行一次该任务
        singleThreadScheduledPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(App.url + App.GetSertime)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONTime(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1, 1, TimeUnit.SECONDS);

       /* new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(App.url+App.GetSertime)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONTime(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

    /**
     * 解析服务器时间
     *
     * @param json 被解析的数据
     */
    private void parseJSONTime(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jr = new JSONArray(jsonObject.getString("table"));
            for (int i = 0; i < jr.length(); i++) {
                JSONObject object = new JSONObject(jr.get(i).toString());
                time = object.getString("serTime") + "";
            }
            Message message = new Message();
            message.what = 3;
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
