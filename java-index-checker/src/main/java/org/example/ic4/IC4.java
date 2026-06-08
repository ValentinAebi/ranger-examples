package org.example.ic4;

import org.checkerframework.common.value.qual.MinLen;
import org.checkerframework.common.value.qual.ArrayLen;

public class IC4 {
    
    String getThirdElement_1(String @MinLen(3) [] arr) {
        return arr[2];
    }

    String getThirdElement_2(String @ArrayLen(3) [] arr) {
        return arr[2];
    }

}
