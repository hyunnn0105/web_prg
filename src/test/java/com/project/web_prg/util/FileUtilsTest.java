package com.project.web_prg.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileUtilsTest {
    @Test
    void UUIDTest(){

        for (int i = 0; i<20; i++) {
            System.out.println(UUID.randomUUID().toString());
        }
    }
}