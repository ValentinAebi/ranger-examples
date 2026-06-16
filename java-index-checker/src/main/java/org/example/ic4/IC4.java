package org.example.ic4;

import org.checkerframework.common.value.qual.MinLen;
import org.checkerframework.common.value.qual.ArrayLen;

public class IC4 {
    
    String getThirdElement_1_correct(String @MinLen(3) [] arr) {
        return arr[2];
    }

    String getThirdElement_1_buggy(String @MinLen(3) [] arr) {
        return arr[3];
    }

    String getThirdElement_2_correct(String @ArrayLen(3) [] arr) {
        return arr[2];
    }

    String getThirdElement_2_buggy(String @ArrayLen(3) [] arr) {
        return arr[3];
    }

}
