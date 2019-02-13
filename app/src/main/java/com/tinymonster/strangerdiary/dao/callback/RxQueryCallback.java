package com.tinymonster.strangerdiary.dao.callback;

import java.util.List;

/**
 * Created by TinyMonster on 19/01/2019.
 */

public interface RxQueryCallback<T> {
    void onDaoSuccess(List<T> list);
    void onDaoFail(String msg);
}
