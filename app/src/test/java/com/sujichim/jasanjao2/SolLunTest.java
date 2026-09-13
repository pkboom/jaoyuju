package com.sujichim.jasanjao2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 변환에 실패하면 날짜 대신 "error" 를 돌려준다는 계약을 고정한다.
 * Ungi 는 이 값을 그대로 숫자로 읽다가 죽었었다.
 */
public class SolLunTest {

    private static final String ERROR = "error";

    private final SolLun solLun = new SolLun();

    // 달은 0부터 센다. 1976년 9월 13일 -> (1976, 8, 13)
    @Test
    public void 양력을_음력으로_바꾼다() {
        assertEquals("19760820", solLun.SolToLun(1976, 8, 13).substring(0, 8));
    }

    @Test
    public void 음력을_양력으로_바꾼다() {
        assertEquals("19760913", solLun.LunToSol(1976, 7, 20, 0).substring(0, 8));
    }

    @Test
    public void 표에_없는_해는_양력_변환에서_error() {
        assertEquals(ERROR, solLun.SolToLun(1880, 0, 1));
        assertEquals(ERROR, solLun.SolToLun(2051, 0, 1));
    }

    @Test
    public void 표에_없는_해는_음력_변환에서_error() {
        assertEquals(ERROR, solLun.LunToSol(1880, 0, 1, 0));
        assertEquals(ERROR, solLun.LunToSol(2051, 0, 1, 0));
    }

    @Test
    public void 없는_날짜는_error() {
        assertEquals(ERROR, solLun.LunToSol(1976, 7, 0, 0));
        assertEquals(ERROR, solLun.LunToSol(1976, 7, 31, 0));
        assertEquals(ERROR, solLun.LunToSol(1976, 12, 1, 0));
    }

    /** 앱이 죽던 경로: 윤달이 없는 달을 윤달로 고른 경우. */
    @Test
    public void 윤달이_없는_달을_윤달로_고르면_error() {
        // 1976년에 윤달은 8월 하나뿐이라 1월·9월에는 없다.
        assertEquals(ERROR, solLun.LunToSol(1976, 0, 20, 1));
        assertEquals(ERROR, solLun.LunToSol(1976, 8, 13, 1));
    }

    @Test
    public void 윤달이_있는_달은_평달과_다른_날짜가_나온다() {
        // 1976년 윤8월은 실재한다.
        String leap = solLun.LunToSol(1976, 7, 1, 1);
        String plain = solLun.LunToSol(1976, 7, 1, 0);

        assertTrue("윤8월은 변환되어야 한다: " + leap, leap.length() >= 8);
        assertTrue(plain.length() >= 8);
        assertTrue("윤달과 평달이 같은 날일 수 없다", !leap.equals(plain));
    }

    /** Ungi 가 쓰는 판정. "error" 는 8자가 안 되므로 걸러진다. */
    @Test
    public void error_는_날짜로_읽히지_않는다() {
        assertTrue(ERROR.length() < 8);
    }
}
