//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jeesite.common.entity;

public class ResultBean<T> {
    private boolean success;
    private int code;
    private T data;
    private Integer total;
    private String result;
    private Object other;
    private String errDesc;

    public ResultBean(String result, int code, String errDesc, boolean success) {
        this.result = result;
        this.code = code;
        this.errDesc = errDesc;
        this.success = success;
    }

    public ResultBean(String result, T data, int code, boolean success) {
        this.result = result;
        this.data = data;
        this.code = code;
        this.success = success;
    }

    public ResultBean(String result, T data, int code, Integer total) {
        this.result = result;
        this.data = data;
        this.code = code;
        this.success = true;
        this.total = total;
    }

    public ResultBean(String result, T data, int code, boolean success, Object other) {
        this.result = result;
        this.data = data;
        this.code = code;
        this.success = success;
        this.other = other;
    }

    private ResultBean() {
    }

    public boolean isSuccess() {
        return this.success;
    }

    public int getCode() {
        return this.code;
    }

    public T getData() {
        return this.data;
    }

    public Integer getTotal() {
        return this.total;
    }

    public String getResult() {
        return this.result;
    }

    public Object getOther() {
        return this.other;
    }

    public String getErrDesc() {
        return this.errDesc;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public void setTotal(final Integer total) {
        this.total = total;
    }

    public void setResult(final String result) {
        this.result = result;
    }

    public void setOther(final Object other) {
        this.other = other;
    }

    public void setErrDesc(final String errDesc) {
        this.errDesc = errDesc;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ResultBean)) {
            return false;
        } else {
            ResultBean<?> other = (ResultBean)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.isSuccess() != other.isSuccess()) {
                return false;
            } else if (this.getCode() != other.getCode()) {
                return false;
            } else {
                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                Object this$total = this.getTotal();
                Object other$total = other.getTotal();
                if (this$total == null) {
                    if (other$total != null) {
                        return false;
                    }
                } else if (!this$total.equals(other$total)) {
                    return false;
                }

                String this$errDesc;
                String other$errDesc;
                label65: {
                    this$errDesc = this.getResult();
                    other$errDesc = other.getResult();
                    if (this$errDesc == null) {
                        if (other$errDesc == null) {
                            break label65;
                        }
                    } else if (this$errDesc.equals(other$errDesc)) {
                        break label65;
                    }

                    return false;
                }

                Object this$other = this.getOther();
                Object other$other = other.getOther();
                if (this$other == null) {
                    if (other$other != null) {
                        return false;
                    }
                } else if (!this$other.equals(other$other)) {
                    return false;
                }

                this$errDesc = this.getErrDesc();
                other$errDesc = other.getErrDesc();
                if (this$errDesc == null) {
                    if (other$errDesc != null) {
                        return false;
                    }
                } else if (!this$errDesc.equals(other$errDesc)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ResultBean;
    }

    public int hashCode() {
        int result = 59 + (this.isSuccess() ? 79 : 97);
        result = result * 59 + this.getCode();
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        Object $total = this.getTotal();
        result = result * 59 + ($total == null ? 43 : $total.hashCode());
        Object $result = this.getResult();
        result = result * 59 + ($result == null ? 43 : $result.hashCode());
        Object $other = this.getOther();
        result = result * 59 + ($other == null ? 43 : $other.hashCode());
        Object $errDesc = this.getErrDesc();
        result = result * 59 + ($errDesc == null ? 43 : $errDesc.hashCode());
        return result;
    }

    public String toString() {
        return "ResultBean(success=" + this.isSuccess() + ", code=" + this.getCode() + ", data=" + this.getData() + ", total=" + this.getTotal() + ", result=" + this.getResult() + ", other=" + this.getOther() + ", errDesc=" + this.getErrDesc() + ")";
    }
}
