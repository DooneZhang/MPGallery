package com.beiing.gifmaker.widgets.sidebar;

import android.text.TextUtils;

import java.util.Comparator;
import java.util.List;

/**
 * Created by chenliu on 2016/4/28.<br/>
 * 描述：
 * </br>
 */
public class SideBarSupport {

    public interface SectionSupport {
        String getSection();

        String getSectionSrc();

        void setSection(String sction);
    }


    /**
     * 拼音比较器
     */
    public static class PinyinComparator implements Comparator<SectionSupport> {

        public int compare(SideBarSupport.SectionSupport t1, SideBarSupport.SectionSupport t2) {
            if (t1.getSection().equals("@") || t2.getSection().equals("#")) {
                return -1;
            } else if (t1.getSection().equals("#")
                    || t2.getSection().equals("@")) {
                return 1;
            } else {
                return t1.getSection().compareTo(t2.getSection());
            }
        }
    }

    public static void filledData(List<? extends SectionSupport> list) {
        CharacterParser characterParser = CharacterParser.getInstance();
        for (SectionSupport sortModel : list) {
            String name = sortModel.getSectionSrc();
            String pinyin = characterParser.getSelling(name);
            if (TextUtils.isEmpty(pinyin)) {
                sortModel.setSection("#");
                continue;
            }

            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                sortModel.setSection(sortString.toUpperCase());
            } else {
                sortModel.setSection("#");
            }
        }
    }


}
