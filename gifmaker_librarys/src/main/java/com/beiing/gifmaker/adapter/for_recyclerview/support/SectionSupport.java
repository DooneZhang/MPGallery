package com.beiing.gifmaker.adapter.for_recyclerview.support;

/**
 * Created by chenliu on 2016/4/25.<br/>
 * 描述：
 * </br>
 */
public abstract class SectionSupport<T> {
    /**该item是否显示section**/
    private boolean showSection;

    /**section布局中显示的数据**/
    protected abstract T getSection();

    /**比较下一个item section 是否和上一个相等，相等就不显示section**/
    protected abstract boolean equeals();

    public boolean isShowSection() {
        return showSection;
    }

    public void setShowSection(boolean showSection) {
        this.showSection = showSection;
    }
}
