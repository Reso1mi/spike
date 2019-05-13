package top.imlgw.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imlgw.spike.dao.SpikeUserDao;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.entity.User;

/**
 * @author imlgw.top
 * @date 2019/5/12 18:03
 */
@Service
public class SpikeUserService {

    @Autowired
    private SpikeUserDao spikeUserDao;

    public SpikeUser getById(long id) {
        return spikeUserDao.getById(id);
    }

    public  Boolean insert(SpikeUser spikeUser){
        return  spikeUserDao.insert(spikeUser);
    }
}
