package com.nimofy.nasaimagestealer;

import com.nimofy.nasaimagestealer.repo.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestService2 testService2;
    private final PictureRepository pictureRepository;
    private final PlatformTransactionManager platformTransactionManager;

    //    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public void test() {
        var picture = pictureRepository.findById(1L).orElseThrow();
        System.out.println("CLASS:" + testService2.getClass());
        testService2.test(picture);

    }

}
