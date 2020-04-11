package com.simple.greendaodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    DaoSession daoSession = IApplication.getInstance().getDaoSession();

    public void add(View view) {
        CardBeanDao cardBeanDao = daoSession.getCardBeanDao();

        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                cardBeanDao.insert(new CardBean(null, (int) (Math.random() * 10 * 133), "四"));
                continue;
            }
            cardBeanDao.insert(new CardBean("杭州中学", (int) (Math.random() * 10 * 133), "四"));
        }

    }

    public void update(View view) {

        daoSession.insertOrReplace(new CardBean("清欢大学", 200, "二"));
    }

    public void delete(View view) {
    }

    private String TAG = "mTag";

    public void query(View view) {
//        List<CardBean> cardBeans = daoSession.loadAll(CardBean.class);
        CardBeanDao cardBeanDao = daoSession.getCardBeanDao();
//        String sql = "NUMBER IN " + "(SELECT NUMBER FROM " + cardBeanDao.getTablename() + ")";
        String sql = "SELECT " + CardBeanDao.Properties.SchoolName + " FROM " + cardBeanDao.getTablename();
        QueryBuilder<CardBean> builder = cardBeanDao.queryBuilder();
//        List<CardBean> cardBeans = daoSession.queryRaw(CardBean.class, "where number > 200");
        List<CardBean> cardBeans = cardBeanDao.queryRawCreate(sql).list();
//        List<CardBean> cardBeans = cardBeanDao.queryBuilder()
//                .where(new WhereCondition.StringCondition(sql))
//                .list();
//        List<CardBean> cardBeans = build.list();
//        List<CardBean> cardBeans = cardBeanDao.queryRaw(sql);
//        List<CardBean> cardBeans = daoSession.queryRaw(CardBean.class, sql);

        for (int i = 0; i < cardBeans.size(); i++) {
            Log.i(TAG, "query: " + cardBeans.get(i).toString());
        }
    }
}
