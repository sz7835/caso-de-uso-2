package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IPersonaNaturalDao;
import com.delta.deltanet.models.entity.PersonaNatural;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonaNaturalServiceImpl implements IPersonaNaturalService {

    @Autowired
    IPersonaNaturalDao personaNaturalDao;
    @Override
    public PersonaNatural save(PersonaNatural personaNatural) {
        return personaNaturalDao.save(personaNatural);
    }

    public PersonaNatural findById(Long id){
        return personaNaturalDao.getById(id);
    }


}
