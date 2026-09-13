package com.sujichim.jasanjao2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 계산부가 실제로 만들어내는 문자열을 그대로 넣고 확인한다.
 * 각 줄 끝의 공백과 마지막 줄의 잘림은 UngiCalc / check306 이 만들어내는 형태 그대로다.
 */
public class UngiResultTest {

    /** 화면을 처음 열었을 때. 날짜를 고르기 전이라 음력 줄이 없다. */
    private static final String INITIAL =
            "1976년 9월 13일\n"
                    + "좌 토목태과\n"
                    + "(비승, 담승) \n"
                    + "(비 0.5, 담 0.5) \n"
                    + "우 토화불급\n"
                    + "(비허, 소장승) \n"
                    + "(비 -1.5, 소장 6.5)";

    /** 날짜를 고른 뒤. 음력 줄이 붙는다. */
    private static final String WITH_LUNAR =
            "1976년 9월 13일\n"
                    + "(음)1976년 8월 20일\n"
                    + "좌 토목태과\n"
                    + "(비승, 담승) \n"
                    + "(비 0.5, 담 0.5) \n"
                    + "우 토화불급\n"
                    + "(비허, 소장승) \n"
                    + "(비 -1.5, 소장 6.5)";

    /** 유날. 우 블록에 (酉) 가 붙고 306일 블록은 check306 이 이미 떼어간 뒤다. */
    private static final String YU_DAY =
            "1976년 9월 12일\n"
                    + "(음)1976년 8월 19일\n"
                    + "좌 토목태과\n"
                    + "(비승, 담승) \n"
                    + "(비 0.5, 담 0.5) \n"
                    + "우(酉) 수화태과\n"
                    + "(신승, 소장승) \n"
                    + "(신 6.5, 소장 6.5)";

    @Test
    public void 날짜와_좌우_블록을_나눈다() {
        UngiResult result = UngiResult.parse(INITIAL);

        assertTrue(result.parsed());
        assertEquals("1976년 9월 13일", result.date());
        assertEquals("", result.lunarDate());
        assertEquals("", result.marker());

        assertEquals("토목태과", result.left().title());
        assertEquals("비승, 담승", result.left().organ());
        assertEquals("비 0.5, 담 0.5", result.left().index());

        assertEquals("토화불급", result.right().title());
        assertEquals("비허, 소장승", result.right().organ());
        assertEquals("비 -1.5, 소장 6.5", result.right().index());
    }

    @Test
    public void 음력_줄에_읽을_수_있는_이름을_붙인다() {
        UngiResult result = UngiResult.parse(WITH_LUNAR);

        assertEquals("1976년 9월 13일", result.date());
        assertEquals("음력 1976년 8월 20일", result.lunarDate());
        // 음력 줄이 끼어도 좌우 블록은 그대로 읽힌다.
        assertEquals("토목태과", result.left().title());
        assertEquals("토화불급", result.right().title());
    }

    @Test
    public void 윤달은_따로_표시한다() {
        UngiResult result = UngiResult.parse(
                WITH_LUNAR.replace("(음)1976년 8월 20일", "(음,윤)1976년 8월 20일"));

        assertEquals("음력(윤달) 1976년 8월 20일", result.lunarDate());
    }

    @Test
    public void 유날_표시를_제목에서_떼어낸다() {
        UngiResult result = UngiResult.parse(YU_DAY);

        assertEquals("酉", result.marker());
        assertEquals("수화태과", result.right().title());
        assertEquals("신승, 소장승", result.right().organ());
        assertEquals("신 6.5, 소장 6.5", result.right().index());
        // 좌 블록은 표시가 붙지 않는다.
        assertEquals("토목태과", result.left().title());
    }

    @Test
    public void 묘날_표시도_같다() {
        UngiResult result = UngiResult.parse(YU_DAY.replace("우(酉)", "우(卯)"));

        assertEquals("卯", result.marker());
        assertEquals("수화태과", result.right().title());
    }

    @Test
    public void 형식을_알아볼_수_없으면_원본을_남긴다() {
        UngiResult result = UngiResult.parse("계산 실패");

        assertFalse(result.parsed());
        assertEquals("계산 실패", result.raw());
        assertEquals("", result.date());
        assertEquals("", result.left().title());
    }

    @Test
    public void 좌만_있고_우가_없으면_나누지_않는다() {
        UngiResult result = UngiResult.parse("1976년 9월 13일\n좌 토목태과\n(비승, 담승)");

        assertFalse(result.parsed());
    }

    @Test
    public void null_이어도_죽지_않는다() {
        UngiResult result = UngiResult.parse(null);

        assertFalse(result.parsed());
        assertEquals("", result.raw());
    }

    @Test
    public void 줄이_모자라도_있는_만큼만_채운다() {
        UngiResult result = UngiResult.parse("1976년 9월 13일\n좌 토목태과\n우 토화불급");

        assertTrue(result.parsed());
        assertEquals("토목태과", result.left().title());
        assertEquals("", result.left().organ());
        assertEquals("", result.left().index());
        assertEquals("토화불급", result.right().title());
        assertEquals("", result.right().organ());
    }

    @Test
    public void 빈_줄과_줄_끝_공백을_무시한다() {
        UngiResult padded = UngiResult.parse(
                "  1976년 9월 13일  \n\n좌 토목태과  \n\n  (비승, 담승)   \n(비 0.5, 담 0.5) \n"
                        + "우 토화불급 \n(비허, 소장승) \n(비 -1.5, 소장 6.5) \n\n");

        assertEquals("1976년 9월 13일", padded.date());
        assertEquals("비승, 담승", padded.left().organ());
        assertEquals("비 -1.5, 소장 6.5", padded.right().index());
    }

    @Test
    public void 날짜가_없어도_좌우는_읽는다() {
        UngiResult result = UngiResult.parse(
                "좌 토목태과\n(비승, 담승)\n(비 0.5, 담 0.5)\n우 토화불급\n(비허, 소장승)\n(비 -1.5, 소장 6.5)");

        assertTrue(result.parsed());
        assertEquals("", result.date());
        assertEquals("토목태과", result.left().title());
    }

    @Test
    public void format306_은_카드와_같은_형식으로_다듬는다() {
        String block = "우(酉) 수화태과\n(신승, 소장승) \n(신 6.5, 소장 6.5)";

        assertEquals("수화태과\n신승, 소장승\n신 6.5, 소장 6.5", UngiResult.format306(block));
    }

    @Test
    public void format306_은_표시가_없어도_동작한다() {
        assertEquals("토화불급\n비허, 소장승",
                UngiResult.format306("우 토화불급\n(비허, 소장승)"));
    }

    @Test
    public void format306_은_null_을_빈_문자열로_본다() {
        assertEquals("", UngiResult.format306(null));
    }
}
