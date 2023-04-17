package com.nimofy.nasaimagestealer;

import com.nimofy.nasaimagestealer.entities.Picture;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService2 {


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void test(Picture detachedPicture) {
        detachedPicture.setImgSrc("detached src 5");

    }
}
