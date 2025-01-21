package com.uni2k.sccode;

/**返回的码内容
 * created by：lynn.shao on 2024/7/5.
 */
public class UniCodeHintType {
    private boolean TRY_HARDER;
    private String EC_LEVEL;//容错级别
    private boolean NEED_RESULT_CALLBACK;
    private String CHARACTER_SET;
    private String SECURE_KEY;//秘钥
    private String NOTE;
    private int MARGIN;//边距
    private String SECURE_CONTENT;//加密码内容
    private String CONTENT;//码内容
    private String MODULE_SRC;//模板
    private String BPG_COMMAND;
    private  byte[] BPG_BYTE;//图片
    private String IMAGE;
    private String MASK;
    private String VERSION;



    public boolean isTRY_HARDER() {
        return TRY_HARDER;
    }

    public void setTRY_HARDER(boolean TRY_HARDER) {
        this.TRY_HARDER = TRY_HARDER;
    }

    public String getEC_LEVEL() {
        return EC_LEVEL;
    }

    public void setEC_LEVEL(String EC_LEVEL) {
        this.EC_LEVEL = EC_LEVEL;
    }

    public boolean isNEED_RESULT_CALLBACK() {
        return NEED_RESULT_CALLBACK;
    }

    public void setNEED_RESULT_CALLBACK(boolean NEED_RESULT_CALLBACK) {
        this.NEED_RESULT_CALLBACK = NEED_RESULT_CALLBACK;
    }

    public String getCHARACTER_SET() {
        return CHARACTER_SET;
    }

    public void setCHARACTER_SET(String CHARACTER_SET) {
        this.CHARACTER_SET = CHARACTER_SET;
    }

    public String getSECURE_KEY() {
        return SECURE_KEY;
    }

    public void setSECURE_KEY(String SECURE_KEY) {
        this.SECURE_KEY = SECURE_KEY;
    }

    public String getNOTE() {
        return NOTE;
    }

    public void setNOTE(String NOTE) {
        this.NOTE = NOTE;
    }

    public int getMARGIN() {
        return MARGIN;
    }

    public void setMARGIN(int MARGIN) {
        this.MARGIN = MARGIN;
    }

    public String getSECURE_CONTENT() {
        return SECURE_CONTENT;
    }

    public void setSECURE_CONTENT(String SECURE_CONTENT) {
        this.SECURE_CONTENT = SECURE_CONTENT;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getMODULE_SRC() {
        return MODULE_SRC;
    }

    public void setMODULE_SRC(String MODULE_SRC) {
        this.MODULE_SRC = MODULE_SRC;
    }

    public String getBPG_COMMAND() {
        return BPG_COMMAND;
    }

    public void setBPG_COMMAND(String BPG_COMMAND) {
        this.BPG_COMMAND = BPG_COMMAND;
    }

    public byte[] getBPG_BYTE() {
        return BPG_BYTE;
    }

    public void setBPG_BYTE(byte[] BPG_BYTE) {
        this.BPG_BYTE = BPG_BYTE;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getMASK() {
        return MASK;
    }

    public void setMASK(String MASK) {
        this.MASK = MASK;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }
}


