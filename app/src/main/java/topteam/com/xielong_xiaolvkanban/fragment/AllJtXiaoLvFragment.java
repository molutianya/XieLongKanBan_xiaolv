package topteam.com.xielong_xiaolvkanban.fragment;

/**
 * 所有机台的总效率
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import topteam.com.xielong_xiaolvkanban.R;
import topteam.com.xielong_xiaolvkanban.entity.AllJtXiaoLvEntity;
import topteam.com.xielong_xiaolvkanban.util.App;

@SuppressLint("ValidFragment")
public class AllJtXiaoLvFragment extends Fragment {

    View view; //前端布局
    private PieChart pieChart1_v;  //昨天的饼图数据
    private PieChart pieChart2_v;  //当天的饼图数据
    private BarChart barChart_v;   //本周的当天数据
    private BarChart barChart2_v;  //每周的数据
    //饼图上方左边的小方块控件
    private TextView z1_v;
    private TextView z2_v;
    private TextView z3_v;
    private TextView z4_v;
    private TextView z5_v;
    private TextView z6_v;
    //饼图上方右边的小方块控件
    private TextView y1_v;
    private TextView y2_v;
    private TextView y3_v;
    private TextView y4_v;
    private TextView y5_v;
    private TextView y6_v;
    //保存数据的实体
    public ArrayList<BarEntry> entries = new ArrayList<>();
    //表格下方的文字
    private List<String> stateList = new ArrayList<>();   //存放机台的所有状态名称
    private List<Float> dayList = new ArrayList<>();   //存放日对比数据
    private List<Float> weekList = new ArrayList<>();   //存放日对比数据
    private List<Integer> weekList2 = new ArrayList<>();   //存放日对比数据
    private List<String> valuesX1 = new ArrayList<>(); //周对比的数据X轴
    private List<String> valuesX = new ArrayList<>(); //日对比数据X轴
    Handler handler1; //异步解析
    Handler handler2; //定时刷新数据
    TextView time01_v; //布局时间
    Handler handler; //定时刷新系统时间
    AllJtXiaoLvEntity xiaoLvEntity = new AllJtXiaoLvEntity();
    Context context;
    private String time;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        //机台状态名称和颜色
        okHttpStateAll1();
        //昨天机台状态比列
        okHttpState();
        //当前机台状态比列
        okHttpState2();
        //日对比数据
        okHttpDay();
        //周对比数据
        okHttpWeek();
        //系统时间
        okHttpTime();


        //异步解析
        handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        bingTu1();
                        bingTu2();
                        break;
                    case 3:
                        initBarChart1();
                        break;
                    case 4:
                        initBarChart2();
                        break;
                    case 5:
                        time01_v.setText(time + "");
                    default:
                        break;
                }
            }
        };


    }


    /**
     * 关联布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.all_jtxiaolv, container, false);
        initView(view);
        return view;
    }

    /**
     * 初始化所有的控件
     */
    private void initView(View view) {
        pieChart1_v = view.findViewById(R.id.consume_pie1_chart);
        pieChart2_v = view.findViewById(R.id.consume_pie2_chart);
        barChart_v = view.findViewById(R.id.bar_chart);
        barChart2_v = view.findViewById(R.id.bar_chart2);
        z1_v = view.findViewById(R.id.z1);
        z2_v = view.findViewById(R.id.z2);
        z3_v = view.findViewById(R.id.z3);
        z4_v = view.findViewById(R.id.z4);
        z5_v = view.findViewById(R.id.z5);
        z6_v = view.findViewById(R.id.z6);
        y1_v = view.findViewById(R.id.y1);
        y2_v = view.findViewById(R.id.y2);
        y3_v = view.findViewById(R.id.y3);
        y4_v = view.findViewById(R.id.y4);
        y5_v = view.findViewById(R.id.y5);
        y6_v = view.findViewById(R.id.y6);
        time01_v = view.findViewById(R.id.time);
    }

    /**
     * 昨日的饼图数据
     */
    private void bingTu1() {
        pieChart1_v.setUsePercentValues(true); //设置为显示百分比
        //  pieChart1.setDescription("");//设置描述
        // pieChart1.setDescriptionTextSize(15f);
        // pieChart1.setExtraOffsets(5, 5, 5, 5);//设置饼状图距离上下左右的偏移量
        pieChart1_v.setDrawCenterText(true); //设置可以绘制中间的文字
        pieChart1_v.setCenterTextColor(Color.BLACK); //中间的文本颜色
        pieChart1_v.setCenterTextSize(18);  //设置中间文本文字的大小
        pieChart1_v.setDrawHoleEnabled(true); //绘制中间的圆形
        pieChart1_v.setHoleColor(Color.WHITE);//饼状图中间的圆的绘制颜色
        pieChart1_v.setHoleRadius(40f);//饼状图中间的圆的半径大小
        pieChart1_v.setTransparentCircleColor(Color.BLACK);//设置圆环的颜色
        pieChart1_v.setTransparentCircleAlpha(100);//设置圆环的透明度[0,255]
        pieChart1_v.setTransparentCircleRadius(40f);//设置圆环的半径值
        pieChart1_v.setRotationEnabled(false);//设置饼状图是否可以旋转(默认为true)
        pieChart1_v.setRotationAngle(10);//设置饼状图旋转的角度

        Legend l = pieChart1_v.getLegend(); //设置比例图
        l.setMaxSizePercent(100);
        l.setTextSize(12);
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);//设置每个tab的显示位置（这个位置是指下图右边小方框部分的位置 ）
        l.setXEntrySpace(10f);
        l.setYEntrySpace(5f);//设置tab之间Y轴方向上的空白间距值
        l.setYOffset(0f);
        l.setTextColor(Color.WHITE);

        //饼状图上字体的设置
        pieChart1_v.setDrawEntryLabels(false);//设置是否绘制Label
        // pieChart1.setEntryLabelColor(Color.BLACK);//设置绘制Label的颜色
        pieChart1_v.setEntryLabelTextSize(23f);//设置绘制Label的字体大小

        // pieChart1.animateY(100, Easing.EasingOption.EaseInQuad);//设置Y轴上的绘制动画
        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();

        if (stateList.size() == 6) {
            pieEntries.add(new PieEntry(xiaoLvEntity.getF1(), stateList.get(0)));
            pieEntries.add(new PieEntry(xiaoLvEntity.getF2(), stateList.get(1)));
            pieEntries.add(new PieEntry(xiaoLvEntity.getF3(), stateList.get(2)));
            pieEntries.add(new PieEntry(xiaoLvEntity.getF4(), stateList.get(3)));
            pieEntries.add(new PieEntry(xiaoLvEntity.getF5(), stateList.get(4)));
            pieEntries.add(new PieEntry(xiaoLvEntity.getF6(), stateList.get(5)));
            z1_v.setText(xiaoLvEntity.getState1per1() + "%");
            z2_v.setText(xiaoLvEntity.getState1per2() + "%");
            z3_v.setText(xiaoLvEntity.getState1per3() + "%");
            z4_v.setText(xiaoLvEntity.getState1per4() + "%");
            z5_v.setText(xiaoLvEntity.getState1per5() + "%");
            z6_v.setText(xiaoLvEntity.getState1per6() + "%");

        }


        String centerText = "昨日数据";
        pieChart1_v.setCenterText(centerText);//设置圆环中间的文字
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        ArrayList<Integer> colors = new ArrayList<>();

        // 饼图颜色
        colors.add(Color.rgb(0, 255, 0));
        colors.add(Color.rgb(255, 255, 0));
        colors.add(Color.rgb(255, 0, 0));
        colors.add(Color.rgb(255, 0, 255));
        colors.add(Color.rgb(244, 164, 96));
        colors.add(Color.rgb(30, 144, 255));
        pieDataSet.setColors(colors);

        pieDataSet.setSliceSpace(0f);//设置选中的Tab离两边的距离
        pieDataSet.setSelectionShift(5f);//设置选中的tab的多出来的
        PieData pieData = new PieData();
        pieData.setDataSet(pieDataSet);

        //各个饼状图所占比例数字的设置
        pieData.setValueFormatter(new PercentFormatter());//设置%
        pieData.setValueTextSize(18f);
        pieData.setValueTextColor(Color.WHITE);

        pieChart1_v.setData(pieData);
        pieChart1_v.highlightValues(null);
        pieChart1_v.invalidate();
    }

    /**
     * 今天的饼图数据
     */
    private void bingTu2() {
        pieChart2_v.setUsePercentValues(true); //设置为显示百分比
        //  pieChart2.setDescription("");//设置描述
        // pieChart2.setDescriptionTextSize(15);//设置描述字体的大小
        // pieChart2.setExtraOffsets(5, 5, 5, 5);//设置饼状图距离上下左右的偏移量
        pieChart2_v.setDrawCenterText(true); //设置可以绘制中间的文字
        pieChart2_v.setCenterTextColor(Color.BLACK); //中间的文本颜色
        pieChart2_v.setCenterTextSize(18);  //设置中间文本文字的大小
        pieChart2_v.setDrawHoleEnabled(true); //绘制中间的圆形
        pieChart2_v.setHoleColor(Color.WHITE);//饼状图中间的圆的绘制颜色
        pieChart2_v.setHoleRadius(40f);//饼状图中间的圆的半径大小
        pieChart2_v.setTransparentCircleColor(Color.BLACK);//设置圆环的颜色
        pieChart2_v.setTransparentCircleAlpha(100);//设置圆环的透明度[0,255]
        pieChart2_v.setTransparentCircleRadius(40f);//设置圆环的半径值
        pieChart2_v.setRotationEnabled(false);//设置饼状图是否可以旋转(默认为true)
        // pieChart2.setRotationAngle(10);//设置饼状图旋转的角度

        Legend l = pieChart2_v.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);//设置每个tab的显示位置（这个位置是指下图右边小方框部分的位置 ）
        l.setXEntrySpace(10f);
        l.setYOffset(0f);
        l.setEnabled(false);
        l.setTextColor(Color.WHITE);
        //饼状图上字体的设置
        // pieChart2.setDrawEntryLabels(false);//设置是否绘制Label
        //  pieChart2.setEntryLabelColor(Color.BLACK);//设置绘制Label的颜色
        //  pieChart2.setEntryLabelTextSize(10f);//设置绘制Label的字体大小

        pieChart2_v.animateY(2000, Easing.EasingOption.EaseInQuad);//设置Y轴上的绘制动画
        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
        Log.d("stateList.size()erew", stateList.size() + "");
        if (stateList.size() == 6) {
            pieEntries.add(new PieEntry(xiaoLvEntity.getFf1(), ""));
            pieEntries.add(new PieEntry(xiaoLvEntity.getFf2(), ""));
            pieEntries.add(new PieEntry(xiaoLvEntity.getFf3(), ""));
            pieEntries.add(new PieEntry(xiaoLvEntity.getFf4(), ""));
            pieEntries.add(new PieEntry(xiaoLvEntity.getFf5(), ""));
            pieEntries.add(new PieEntry(xiaoLvEntity.getFf6(), ""));
            y1_v.setText(xiaoLvEntity.getPer1() + "%");
            y2_v.setText(xiaoLvEntity.getPer2() + "%");
            y3_v.setText(xiaoLvEntity.getPer3() + "%");
            y4_v.setText(xiaoLvEntity.getPer4() + "%");
            y5_v.setText(xiaoLvEntity.getPer5() + "%");
            y6_v.setText(xiaoLvEntity.getPer6() + "%");

        }

        String centerText = "当前数据";
        pieChart2_v.setCenterText(centerText);//设置圆环中间的文字
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        ArrayList<Integer> colors = new ArrayList<>();

        // 饼图颜色
        colors.add(Color.rgb(0, 255, 0));
        colors.add(Color.rgb(255, 255, 0));
        colors.add(Color.rgb(255, 0, 0));
        colors.add(Color.rgb(255, 0, 255));
        colors.add(Color.rgb(244, 164, 96));
        colors.add(Color.rgb(30, 144, 255));
        pieDataSet.setColors(colors);

        pieDataSet.setSliceSpace(0f);//设置选中的Tab离两边的距离
        pieDataSet.setSelectionShift(5f);//设置选中的tab的多出来的
        PieData pieData = new PieData();
        pieData.setDataSet(pieDataSet);

        //各个饼状图所占比例数字的设置
        pieData.setValueFormatter(new PercentFormatter());//设置%
        pieData.setValueTextSize(18f);
        pieData.setValueTextColor(Color.WHITE);

        pieChart2_v.setData(pieData);
        pieChart2_v.highlightValues(null);
        pieChart2_v.invalidate();
    }

    /**
     * 日对比柱形图
     */
    private void initBarChart1() {

        barChart_v.setDrawValueAboveBar(true);  //设置所有的数值在图形的上面,而不是图形上
        barChart_v.setTouchEnabled(false);  //进制触控
        barChart_v.setScaleEnabled(false); //设置能否缩放
        barChart_v.setPinchZoom(false);  //设置true支持两个指头向X、Y轴的缩放，如果为false，只能支持X或者Y轴的当方向缩放
        barChart_v.setDrawBarShadow(false);  //设置阴影
        barChart_v.setDrawGridBackground(false);  //设置背景是否网格显示
        barChart_v.setDescription(null); //不描述


        //X轴的数据格式
        XAxis xAxis = barChart_v.getXAxis();
        valuesX.clear();
        valuesX.add("周一");
        valuesX.add("周二");
        valuesX.add("周三");
        valuesX.add("周四");
        valuesX.add("周五");
        valuesX.add("周六");
        valuesX.add("周日");
        xAxis.setValueFormatter(new IndexAxisValueFormatter(valuesX));
        //设置位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置是否绘制网格线
        xAxis.setDrawGridLines(false);
        barChart_v.getAxisLeft().setDrawGridLines(false);
        // barChart.animateY(2500);
        //设置X轴文字剧中对齐
        xAxis.setCenterAxisLabels(false);
        //X轴最小间距
        xAxis.setGranularity(1f);
        //设置字体颜色
        xAxis.setTextColor(Color.WHITE);


        //Y轴的数据格式
        YAxis axisLeft = barChart_v.getAxisLeft();
        axisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int in = (int) value;
                return in + "%";
            }
        });
        barChart_v.animateY(2500);
        //设置Y轴刻度的最大值
        axisLeft.setAxisMinValue(0);
        axisLeft.setAxisMaxValue(100);
        barChart_v.getAxisRight().setEnabled(false);
        //设置字体颜色
        axisLeft.setTextColor(Color.WHITE);

        //设置数据
        setData01();

    }

    /**
     * 本周的当天的数据
     */
    private void setData01() {
        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        if (dayList.size() == 7) {
            yVals1.add(new BarEntry(0, dayList.get(0)));
            yVals1.add(new BarEntry(1, dayList.get(1)));
            yVals1.add(new BarEntry(2, dayList.get(2)));
            yVals1.add(new BarEntry(3, dayList.get(3)));
            yVals1.add(new BarEntry(4, dayList.get(4)));
            yVals1.add(new BarEntry(5, dayList.get(5)));
            yVals1.add(new BarEntry(6, dayList.get(6)));
        }
        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "");
        //设置多彩 也可以单一颜色
        set1.setColor(Color.parseColor("#4169E1"));
        set1.setDrawValues(false);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        barChart_v.setData(data);
        barChart_v.setFitBars(true);
        //设置文字的大小
        set1.setValueTextSize(12f);
        //设置每条柱子的宽度
        data.setBarWidth(0.7f);
        //设置字体颜色
        data.setValueTextColor(Color.WHITE);
        barChart_v.invalidate();

        for (IDataSet set : barChart_v.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());
        barChart_v.invalidate();
        barChart_v.setAutoScaleMinMaxEnabled(!barChart_v.isAutoScaleMinMaxEnabled());
        barChart_v.notifyDataSetChanged();
        barChart_v.invalidate();

    }

    /**
     * 周对比柱形图
     */
    private void initBarChart2() {

        barChart2_v.setDrawValueAboveBar(true);  //设置所有的数值在图形的上面,而不是图形上
        barChart2_v.setTouchEnabled(false);  //进制触控
        barChart2_v.setScaleEnabled(false); //设置能否缩放
        barChart2_v.setPinchZoom(false);  //设置true支持两个指头向X、Y轴的缩放，如果为false，只能支持X或者Y轴的当方向缩放
        barChart2_v.setDrawBarShadow(false);  //设置阴影
        barChart2_v.setDrawGridBackground(false);  //设置背景是否网格显示
        barChart2_v.setDescription(null); //不描述


        //X轴的数据格式
        XAxis xAxis = barChart2_v.getXAxis();
        if (weekList2.size() == 6) {
            for (int i = 0; i < weekList2.size(); i++) {
                valuesX1.add("第" + weekList2.get(i) + "周");
            }
        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(valuesX1));
        //设置位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置是否绘制网格线
        xAxis.setDrawGridLines(false);
        barChart2_v.getAxisLeft().setDrawGridLines(false);
        barChart2_v.animateY(2500);
        //设置X轴文字剧中对齐
        xAxis.setCenterAxisLabels(false);
        //X轴最小间距
        xAxis.setGranularity(1f);
        //设置字体颜色
        xAxis.setTextColor(Color.WHITE);

        //Y轴的数据格式
        YAxis axisLeft = barChart2_v.getAxisLeft();
        axisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int in = (int) value;
                return in + "%";
            }
        });
        //设置Y轴刻度的最大值
        axisLeft.setAxisMinValue(0);
        axisLeft.setAxisMaxValue(100);
        barChart2_v.getAxisRight().setEnabled(false);
        //设置字体颜色
        axisLeft.setTextColor(Color.WHITE);
        // YAxis axisRight = barChart2.getAxisRight();
        //  axisRight.setAxisMinValue(0);
        //  axisRight.setAxisMaxValue(100);
        //设置数据
        setData02();
    }

    /**
     * 周对比的数据
     */
    private void setData02() {
        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        if (weekList.size() == 6 && weekList2.size() == 6) {
            yVals1.add(new BarEntry(0, weekList.get(0)));
            yVals1.add(new BarEntry(1, weekList.get(1)));
            yVals1.add(new BarEntry(2, weekList.get(2)));
            yVals1.add(new BarEntry(3, weekList.get(3)));
            yVals1.add(new BarEntry(4, weekList.get(4)));
            yVals1.add(new BarEntry(5, weekList.get(5)));

        }

        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "");
        //设置多彩 也可以单一颜色
        set1.setColor(Color.parseColor("#00FF7F"));
        set1.setDrawValues(false);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        barChart2_v.setData(data);
        barChart2_v.setFitBars(true);
        //设置文字的大小
        set1.setValueTextSize(12f);
        //设置每条柱子的宽度
        data.setBarWidth(0.7f);
        //设置字体颜色
        data.setValueTextColor(Color.WHITE);
        barChart2_v.invalidate();

        for (IDataSet set : barChart2_v.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());
        barChart2_v.invalidate();
        barChart2_v.setAutoScaleMinMaxEnabled(!barChart2_v.isAutoScaleMinMaxEnabled());
        barChart2_v.notifyDataSetChanged();
        barChart2_v.invalidate();

    }

    /**
     * 向云服务器发送请求获取昨日机台效率
     */
    private void okHttpState() {
        ScheduledExecutorService singleThreadScheduledPool = Executors.newSingleThreadScheduledExecutor();
        //延迟1秒后，每隔1秒执行一次该任务
        singleThreadScheduledPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .build();
                    Request request = new Request.Builder()
                            .url(App.url + App.XF_Kanban_dayo)
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String jsData = response.body().string();
                    Log.i("昨天机台效率",jsData);
                    parseJSONobject(jsData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1, 60, TimeUnit.SECONDS);
    }

    /**
     * 解析从服务器获取的json数据-昨日数据
     *
     * @param jsonData 被解析的数据
     */
    private void parseJSONobject(String jsonData) {
        try {

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jr = new JSONArray(jsonObject.getString("table"));
            for (int i = 0; i < jr.length(); i++) {
                JSONObject object = new JSONObject(jr.get(i).toString());
                xiaoLvEntity.setF1(Float.parseFloat(object.getString("state1") + ""));
                xiaoLvEntity.setF2(Float.parseFloat(object.getString("state2") + ""));
                xiaoLvEntity.setF3(Float.parseFloat(object.getString("state3") + ""));
                xiaoLvEntity.setF4(Float.parseFloat(object.getString("state4") + ""));
                xiaoLvEntity.setF5(Float.parseFloat(object.getString("state5") + ""));
                xiaoLvEntity.setF6(Float.parseFloat(object.getString("state6") + ""));

                xiaoLvEntity.setState1per1(Float.parseFloat(object.getString("state1per") + ""));
                xiaoLvEntity.setState1per2(Float.parseFloat(object.getString("state2per") + ""));
                xiaoLvEntity.setState1per3(Float.parseFloat(object.getString("state3per") + ""));
                xiaoLvEntity.setState1per4(Float.parseFloat(object.getString("state4per") + ""));
                xiaoLvEntity.setState1per5(Float.parseFloat(object.getString("state5per") + ""));
                xiaoLvEntity.setState1per6(Float.parseFloat(object.getString("state6per") + ""));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向云服务器发送请求获取今天机台效率
     */
    private void okHttpState2() {
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
                            .url(App.url + App.XF_Kanban_day)
                            .build();
                    //调用okHttpClient的newCall方法创建䘝Call对象，
                    //并调用它的execute方法发送请求并获取服务器返回的数据
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i("fskdlfdsmfd",responseData);
                    //调用该方法解析从服务器返回的Json数据
                    parseJSONobject2(responseData);
                  /*  Message message = new Message();
                    message.what = 2;
                    handler1.sendMessage(message);*/
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1, 60, TimeUnit.SECONDS);
    }

    /**
     * 解析从服务器获取的json数据-今天数据
     *
     * @param jsonData 被解析的数据
     */
    private void parseJSONobject2(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jr = new JSONArray(jsonObject.getString("table"));
            for (int i = 0; i < jr.length(); i++) {
                JSONObject object = new JSONObject(jr.get(i).toString());
                xiaoLvEntity.setFf1(Float.parseFloat(object.getString("state1") + ""));
                xiaoLvEntity.setFf2(Float.parseFloat(object.getString("state2") + ""));
                xiaoLvEntity.setFf3(Float.parseFloat(object.getString("state3") + ""));
                xiaoLvEntity.setFf4(Float.parseFloat(object.getString("state4") + ""));
                xiaoLvEntity.setFf5(Float.parseFloat(object.getString("state5") + ""));
                xiaoLvEntity.setFf6(Float.parseFloat(object.getString("state6") + ""));
                xiaoLvEntity.setPer1(Float.parseFloat(object.getString("state1per") + ""));
                xiaoLvEntity.setPer2(Float.parseFloat(object.getString("state2per") + ""));
                xiaoLvEntity.setPer3(Float.parseFloat(object.getString("state3per") + ""));
                xiaoLvEntity.setPer4(Float.parseFloat(object.getString("state4per") + ""));
                xiaoLvEntity.setPer5(Float.parseFloat(object.getString("state5per") + ""));
                xiaoLvEntity.setPer6(Float.parseFloat(object.getString("state6per") + ""));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 向云服务器发送请求获取机台状态的颜色和状态
     */
    private void okHttpStateAll1() {
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
                            .url(App.url + App.GetGrandwayChart)
                            .build();
                    //调用okHttpClient的newCall方法创建䘝Call对象，
                    //并调用它的execute方法发送请求并获取服务器返回的数据
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i("状态和颜色",responseData);
                    //调用该方法解析从服务器返回的Json数据
                    parseJSONobjectAll1(responseData);
                    Message message = new Message();
                    message.what = 1;
                    handler1.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1, 60, TimeUnit.SECONDS);
    }

    /**
     * 解析从服务器获取的json数据-机台的颜色和状态
     *
     * @param jsonData 被解析的数据
     */
    private void parseJSONobjectAll1(String jsonData) {
        try {
            stateList.clear();
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jr = new JSONArray(jsonObject.getString("table"));
            for (int i = 0; i < jr.length(); i++) {
                JSONObject object = new JSONObject(jr.get(i).toString());
                stateList.add(object.getString("fname") + "");
            }
            Log.d("状态名称", stateList.size() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 向云服务器发送请求获取日对比的数据
     */
    private void okHttpDay() {
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
                            .url(App.url + App.XF_Kanban_week)
                            .build();
                    //调用okHttpClient的newCall方法创建䘝Call对象，
                    //并调用它的execute方法发送请求并获取服务器返回的数据
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i("本周当天数据",responseData);
                    //调用该方法解析从服务器返回的Json数据
                    parseJSONobjectDay(responseData);
                    Message message = new Message();
                    message.what = 3;
                    handler1.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1, 60, TimeUnit.SECONDS);
    }

    /**
     * 解析从服务器获取的json数据-日对比的数据
     *
     * @param jsonData 被解析的数据
     */
    private void parseJSONobjectDay(String jsonData) {
        try {
            dayList.clear();
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jr = new JSONArray(jsonObject.getString("table"));
            for (int i = 0; i < jr.length(); i++) {
                JSONObject object = new JSONObject(jr.get(i).toString());
                Log.d("dayList11", object.getString("fPer"));
                dayList.add(Float.parseFloat(object.getString("fPer") + ""));
            }
            Log.d("dayListefes1dfds1", dayList.size() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向云服务器发送请求获取周对比的数据
     */
    private void okHttpWeek() {
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
                            .url(App.url + App.XF_Kanban_moon)
                            .build();
                    //调用okHttpClient的newCall方法创建䘝Call对象，
                    //并调用它的execute方法发送请求并获取服务器返回的数据
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i("每周的数据",responseData);
                    //调用该方法解析从服务器返回的Json数据
                    parseJSONobjectWeek(responseData);
                    Message message = new Message();
                    message.what = 4;
                    handler1.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1, 60, TimeUnit.SECONDS);

    }

    /**
     * 解析从服务器获取的json数据-周对比的数据
     *
     * @param jsonData 被解析的数据
     */
    private void parseJSONobjectWeek(String jsonData) {
        try {
            weekList.clear();
            weekList2.clear();
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jr = new JSONArray(jsonObject.getString("table"));
            for (int i = 0; i < jr.length(); i++) {
                JSONObject object = new JSONObject(jr.get(i).toString());
                weekList2.add(Integer.parseInt(object.getString("fDate")));
                weekList.add(Float.parseFloat(object.getString("fPer")));
            }
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
                    Log.i("服务器时间",responseData);
                    parseJSONTime(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1, 1, TimeUnit.SECONDS);

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
                time = object.getString("serTime");
            }
            Message message = new Message();
            message.what = 5;
            handler1.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
