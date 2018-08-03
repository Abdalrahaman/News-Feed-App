package com.example.pro_abdo.news_app;

public class New {

    private String mSectionName;
    private String mTitle;
    private String[] mAuthorName;
    private String mDate;
    private String mUrl;

    public New(String sectionName, String title, String date, String url, String[] authorName) {

        this.mSectionName = sectionName;
        this.mTitle = title;
        this.mAuthorName = authorName;
        this.mDate = date;
        this.mUrl = url;

    }


    public String getmSectionName() {
        return mSectionName;
    }

    public void setmSectionName(String mSectionName) {
        this.mSectionName = mSectionName;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String[] getmAuthorName() {
        return mAuthorName;
    }

    public void setmAuthorName(String[] mAuthorName) {
        this.mAuthorName = mAuthorName;
    }
}
