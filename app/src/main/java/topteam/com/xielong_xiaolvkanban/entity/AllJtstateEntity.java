package topteam.com.xielong_xiaolvkanban.entity;
/**
 * 这是当前所有机台的数据实体类
 */



public class AllJtstateEntity {
    private String jtno; //机台编号
    private int color; //机台颜色
    private boolean bt_a; //异常停机图标
    private boolean bt_c; //超成型周期图标
    private boolean bt_r; //不良预警图标
    private boolean bt_p; //超良品数图标
    private boolean bt_t; //指令超时图标
    private boolean bt_m; //换单提示图标
    private String gdno; //工单编号
    private String cpno; //产品编号
  //  private String mjname; //模具名称
    private String scsl; //生产数量
    private String lpsl; //良品数量
    private String blsl; //不良品数量

    public AllJtstateEntity(String jtno, int color, boolean bt_a, boolean bt_c, boolean bt_r, boolean bt_p, boolean bt_t, boolean bt_m, String gdno, String cpno,  String scsl, String lpsl, String blsl) {
        this.jtno = jtno;
        this.color = color;
        this.bt_a = bt_a;
        this.bt_c = bt_c;
        this.bt_r = bt_r;
        this.bt_p = bt_p;
        this.bt_t = bt_t;
        this.bt_m = bt_m;
        this.gdno = gdno;
        this.cpno = cpno;
        this.scsl = scsl;
        this.lpsl = lpsl;
        this.blsl = blsl;
    }

    public String getJtno() {
        return jtno;
    }

    public int getColor() {
        return color;
    }

    public String getCpno() {
        return cpno;
    }

    public String getGdno() {
        return gdno;
    }



    public String getScsl() {
        return scsl;
    }

    public String getLpsl() {
        return lpsl;
    }

    public String getBlsl() {
        return blsl;
    }

    public boolean isBt_a() {
        return bt_a;
    }

    public boolean isBt_c() {
        return bt_c;
    }

    public boolean isBt_r() {
        return bt_r;
    }

    public boolean isBt_p() {
        return bt_p;
    }

    public boolean isBt_t() {
        return bt_t;
    }

    public boolean isBt_m() {
        return bt_m;
    }
}
