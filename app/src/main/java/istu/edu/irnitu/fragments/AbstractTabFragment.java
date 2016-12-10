package istu.edu.irnitu.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * NewFitGid
 * Created by Александр on 15.11.2016.
 * Contact on luck.alex13@gmail.com
 * Copyright Aleksandr Novikov 2016
 */

public class AbstractTabFragment extends Fragment {
    protected String title,cookiesLogin ;
    protected int typeTop;
    protected View view;
    protected Context activityContext;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setActivityContext(Context activityContext) {
        this.activityContext = activityContext;
    }

    public void setCookiesLogin(String cookiesLogin) {
        this.cookiesLogin = cookiesLogin;
    }

    public void setTypeTop(int typeTop) {
        this.typeTop = typeTop;
    }
}