package com.sujichim.jasanjao2;

import java.util.ArrayList;
import java.util.List;

/**
 * 운기 계산 결과 문자열을 화면에 뿌릴 수 있는 형태로 나눈다.
 *
 * 계산부가 만들어내는 형식:
 * <pre>
 * 1976년 9월 13일
 * (음)1976년 8월 20일      // 날짜를 고른 경우에만
 * 좌 토목태과
 * (비승, 담승)
 * (비 0.5, 담 0.5)
 * 우(酉) 수화태과          // (酉)/(卯)는 묘·유날에만
 * (신승, 소장승)
 * (신 6.5, 소장 6.5)
 * </pre>
 */
public final class UngiResult {

    /** 좌 또는 우 한 덩어리. */
    public static final class Block {
        private final String title;
        private final String organ;
        private final String index;

        Block(String title, String organ, String index) {
            this.title = title;
            this.organ = organ;
            this.index = index;
        }

        /** 오행, 예: "토목태과". */
        public String title() {
            return title;
        }

        /** 장부, 예: "비승, 담승". */
        public String organ() {
            return organ;
        }

        /** 지수, 예: "비 0.5, 담 0.5". */
        public String index() {
            return index;
        }
    }

    private static final Block EMPTY = new Block("", "", "");

    private final boolean parsed;
    private final String raw;
    private final String date;
    private final String lunarDate;
    private final String marker;
    private final Block left;
    private final Block right;

    private UngiResult(boolean parsed, String raw, String date, String lunarDate,
                       String marker, Block left, Block right) {
        this.parsed = parsed;
        this.raw = raw;
        this.date = date;
        this.lunarDate = lunarDate;
        this.marker = marker;
        this.left = left;
        this.right = right;
    }

    /** 형식을 알아볼 수 없으면 {@link #parsed()} 가 false 이고 {@link #raw()} 만 채워진다. */
    public static UngiResult parse(String result) {
        if (result == null) {
            return new UngiResult(false, "", "", "", "", EMPTY, EMPTY);
        }

        List<String> lines = new ArrayList<String>();
        for (String line : result.split("\n")) {
            line = line.trim();
            if (line.length() > 0) {
                lines.add(line);
            }
        }

        int leftAt = -1, rightAt = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (leftAt < 0 && lines.get(i).startsWith("좌")) {
                leftAt = i;
            } else if (leftAt >= 0 && rightAt < 0 && lines.get(i).startsWith("우")) {
                rightAt = i;
            }
        }
        if (leftAt < 0 || rightAt < 0) {
            return new UngiResult(false, result, "", "", "", EMPTY, EMPTY);
        }

        String date = leftAt > 0 ? lines.get(0) : "";
        String lunar = leftAt > 1 ? lunarLabel(lines.get(1)) : "";

        String[] leftMarker = new String[1];
        Block left = block(lines, leftAt, rightAt, leftMarker);
        String[] rightMarker = new String[1];
        Block right = block(lines, rightAt, lines.size(), rightMarker);

        String marker = rightMarker[0].length() > 0 ? rightMarker[0] : leftMarker[0];
        return new UngiResult(true, result, date, lunar, marker, left, right);
    }

    /**
     * 입태 306일 덩어리를 카드와 같은 형식으로 다듬는다.
     * "우(酉) 수화태과\n(신승, 소장승)" -> "수화태과\n신승, 소장승"
     */
    public static String format306(String block) {
        if (block == null) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        for (String line : block.split("\n")) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.startsWith("우") || line.startsWith("좌")) {
                line = stripMarker(line.substring(1).trim(), new String[1]);
            } else {
                line = unwrap(line);
            }
            if (out.length() > 0) {
                out.append("\n");
            }
            out.append(line);
        }
        return out.toString();
    }

    /** 결과 문자열이 예상한 형식이었는지. */
    public boolean parsed() {
        return parsed;
    }

    /** 나누지 못했을 때 그대로 보여줄 원본. */
    public String raw() {
        return raw;
    }

    /** 양력 날짜, 예: "1976년 9월 13일". */
    public String date() {
        return date;
    }

    /** 음력 날짜, 예: "음력 1976년 8월 20일". 날짜를 고르기 전에는 빈 문자열. */
    public String lunarDate() {
        return lunarDate;
    }

    /** 묘·유날 표시 "卯" 또는 "酉". 해당하지 않으면 빈 문자열. */
    public String marker() {
        return marker;
    }

    public Block left() {
        return left;
    }

    public Block right() {
        return right;
    }

    private static Block block(List<String> lines, int from, int to, String[] markerOut) {
        markerOut[0] = "";
        String head = stripMarker(lines.get(from).substring(1).trim(), markerOut);
        String organ = from + 1 < to ? unwrap(lines.get(from + 1)) : "";
        String index = from + 2 < to ? unwrap(lines.get(from + 2)) : "";
        return new Block(head, organ, index);
    }

    /** 앞에 붙은 "(卯)" 같은 표시를 떼어내고 markerOut 에 담는다. */
    private static String stripMarker(String head, String[] markerOut) {
        markerOut[0] = "";
        if (head.startsWith("(")) {
            int close = head.indexOf(')');
            if (close > 0) {
                markerOut[0] = head.substring(1, close);
                return head.substring(close + 1).trim();
            }
        }
        return head;
    }

    /** "(비승, 담승)" -> "비승, 담승" */
    private static String unwrap(String line) {
        line = line.trim();
        if (line.startsWith("(")) {
            line = line.substring(1);
        }
        if (line.endsWith(")")) {
            line = line.substring(0, line.length() - 1);
        }
        return line.trim();
    }

    /** "(음)1976년 8월 20일" -> "음력 1976년 8월 20일" */
    private static String lunarLabel(String line) {
        if (line.startsWith("(음,윤)")) {
            return "음력(윤달) " + line.substring(5).trim();
        }
        if (line.startsWith("(음)")) {
            return "음력 " + line.substring(3).trim();
        }
        return line;
    }
}
