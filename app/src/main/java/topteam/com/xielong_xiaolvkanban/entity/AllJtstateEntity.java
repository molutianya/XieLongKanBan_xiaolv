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
    private String user1; //用户头像1
    private String user2; //用户头像2
    private String user3; //用户头像3

    public AllJtstateEntity(String jtno, int color, boolean bt_a, boolean bt_c, boolean bt_r, boolean bt_p, boolean bt_t, boolean bt_m, String gdno, String cpno, String scsl, String lpsl, String blsl, String user1, String user2, String user3) {
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
        this.user1 = user1;
        this.user2 = user2;
        this.user3 = user3;
    }

    public String getJtno() {
        return jtno;
    }

    public void setJtno(String jtno) {
        this.jtno = jtno;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isBt_a() {
        return bt_a;
    }

    public void setBt_a(boolean bt_a) {
        this.bt_a = bt_a;
    }

    public boolean isBt_c() {
        return bt_c;
    }

    public void setBt_c(boolean bt_c) {
        this.bt_c = bt_c;
    }

    public boolean isBt_r() {
        return bt_r;
    }

    public void setBt_r(boolean bt_r) {
        this.bt_r = bt_r;
    }

    public boolean isBt_p() {
        return bt_p;
    }

    public void setBt_p(boolean bt_p) {
        this.bt_p = bt_p;
    }

    public boolean isBt_t() {
        return bt_t;
    }

    public void setBt_t(boolean bt_t) {
        this.bt_t = bt_t;
    }

    public boolean isBt_m() {
        return bt_m;
    }

    public void setBt_m(boolean bt_m) {
        this.bt_m = bt_m;
    }

    public String getGdno() {
        return gdno;
    }

    public void setGdno(String gdno) {
        this.gdno = gdno;
    }

    public String getCpno() {
        return cpno;
    }

    public void setCpno(String cpno) {
        this.cpno = cpno;
    }

    public String getScsl() {
        return scsl;
    }

    public void setScsl(String scsl) {
        this.scsl = scsl;
    }

    public String getLpsl() {
        return lpsl;
    }

    public void setLpsl(String lpsl) {
        this.lpsl = lpsl;
    }

    public String getBlsl() {
        return blsl;
    }

    public void setBlsl(String blsl) {
        this.blsl = blsl;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getUser3() {
        return user3;
    }

    public void setUser3(String user3) {
        this.user3 = user3;
    }
}
