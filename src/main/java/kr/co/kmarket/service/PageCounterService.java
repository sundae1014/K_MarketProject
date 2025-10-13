package kr.co.kmarket.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PageCounterService {

    // IP 또는 쿠키별 마지막 방문일 기록
    private final Map<String, LocalDate> visitorMap = new HashMap<>();

    // 페이지별 날짜별 방문자 수 기록
    private final Map<String, Map<LocalDate, Integer>> pageVisits = new HashMap<>();

    private int totalVisits = 0;

    /**
     * 방문자 카운트
     */
    public synchronized void countVisit(HttpServletRequest request, HttpServletResponse response) {
        String visitorKey = getVisitorKey(request, response); // IP+쿠키
        LocalDate today = LocalDate.now();

        // 이미 오늘 방문했으면 중복 카운트 방지
        if (visitorMap.containsKey(visitorKey) && visitorMap.get(visitorKey).equals(today)) {
            return;
        }

        // 총 방문자 증가
        totalVisits++;

        // 오늘 방문 기록 갱신
        visitorMap.put(visitorKey, today);

        // 페이지별 방문 기록
        String pageId = request.getRequestURI();
        pageVisits.putIfAbsent(pageId, new HashMap<>());
        Map<LocalDate, Integer> pageMap = pageVisits.get(pageId);
        pageMap.put(today, pageMap.getOrDefault(today, 0) + 1);
    }

    /**
     * 방문자 고유 키 생성 (IP + Cookie)
     */
    private String getVisitorKey(HttpServletRequest request, HttpServletResponse response) {
        String ip = request.getRemoteAddr();
        Cookie[] cookies = request.getCookies();
        String cookieId = null;

        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("visitorId".equals(c.getName())) {
                    cookieId = c.getValue();
                    break;
                }
            }
        }

        if (cookieId == null) {
            cookieId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("visitorId", cookieId);
            cookie.setMaxAge(24 * 60 * 60); // 1일
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return ip + "_" + cookieId;
    }


    public int getTotalVisitsLastTwoDays() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        return pageVisits.values().stream()
                .mapToInt(m -> m.getOrDefault(today, 0) + m.getOrDefault(yesterday, 0))
                .sum();
    }

    public int getTodayVisits() {
        LocalDate today = LocalDate.now();
        return pageVisits.values().stream()
                .mapToInt(m -> m.getOrDefault(today, 0))
                .sum();
    }

    public int getYesterdayVisits() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return pageVisits.values().stream()
                .mapToInt(m -> m.getOrDefault(yesterday, 0))
                .sum();
    }
}