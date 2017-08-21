package com.moon.meojium.database.dao;

import com.moon.meojium.base.BaseRetrofitService;
import com.moon.meojium.model.UpdateResult;
import com.moon.meojium.database.service.UserService;

import retrofit2.Call;

/**
 * Created by moon on 2017. 8. 17..
 */

public class UserDao extends BaseRetrofitService {
    private static UserDao dao;
    private UserService service;

    public static UserDao getInstance() {
        if (dao == null) {
            synchronized (UserDao.class) {
                if (dao == null) {
                    dao = new UserDao();
                }
            }
        }
        return dao;
    }

    private UserDao() {
        init();
        setClass(UserService.class);
    }

    private void setClass(Class<?> type) {
        service = (UserService) retrofit.create(type);
    }

    public Call<UpdateResult> addUser(String id, String nickname) {
        return service.addUser(id, nickname);
    }

    public Call<UpdateResult> updateNickname(String id, String nickname) {
        return service.updateNickname(id, nickname);
    }
}
