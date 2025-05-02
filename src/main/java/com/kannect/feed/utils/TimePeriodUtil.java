package com.kannect.feed.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

@Component
public class TimePeriodUtil {
	
	 public static Pair<LocalDateTime, LocalDateTime> getPeriodBounds(String period) {
	        LocalDateTime now = LocalDateTime.now();
	        return switch (period.toLowerCase()) {
	            case "weekly" -> Pair.of(now.minusWeeks(1), now);
	            case "monthly" -> Pair.of(now.minusMonths(1), now);
	            case "quarterly" -> Pair.of(now.minusMonths(3), now);
	            case "yearly" -> Pair.of(now.minusYears(1), now);
	            default -> throw new IllegalArgumentException("Unsupported period type: " + period);
	        };
	    }
	 
	 public static Pair<LocalDateTime, LocalDateTime> getCurrentPeriodBounds(String period) {
	        LocalDate today = LocalDate.now();
	        LocalDateTime start;
	        LocalDateTime end;

	        switch (period.toLowerCase()) {
	            case "current_week":
	                LocalDate weekStart = today.with(DayOfWeek.MONDAY);
	                LocalDate weekEnd = weekStart.plusDays(6);
	                start = weekStart.atStartOfDay();
	                end = weekEnd.atTime(23, 59, 59);
	                break;

	            case "current_month":
	                LocalDate monthStart = today.withDayOfMonth(1);
	                LocalDate monthEnd = today.with(TemporalAdjusters.lastDayOfMonth());
	                start = monthStart.atStartOfDay();
	                end = monthEnd.atTime(23, 59, 59);
	                break;

	            case "current_quarter":
	                int currentQuarter = (today.getMonthValue() - 1) / 3 + 1;
	                Month quarterStartMonth = Month.of((currentQuarter - 1) * 3 + 1);
	                Month quarterEndMonth = Month.of(currentQuarter * 3);
	                LocalDate quarterStart = LocalDate.of(today.getYear(), quarterStartMonth, 1);
	                LocalDate quarterEnd = LocalDate.of(today.getYear(), quarterEndMonth, quarterEndMonth.length(today.isLeapYear()));
	                start = quarterStart.atStartOfDay();
	                end = quarterEnd.atTime(23, 59, 59);
	                break;

	            case "current_year":
	                start = LocalDate.of(today.getYear(), 1, 1).atStartOfDay();
	                end = LocalDate.of(today.getYear(), 12, 31).atTime(23, 59, 59);
	                break;

	            default:
	                throw new IllegalArgumentException("Invalid period type: " + period);
	        }

	        return new ImmutablePair<>(start, end);
	    }
	 
	 
	 public static Pair<LocalDate, LocalDate> getCurrentWeekBounds() {
		    LocalDate now = LocalDate.now();
		    LocalDate start = now.with(DayOfWeek.MONDAY);
		    LocalDate end = now.with(DayOfWeek.SUNDAY);
		    return Pair.of(start, end);
		}

		public static Pair<LocalDate, LocalDate> getCurrentMonthBounds() {
		    LocalDate now = LocalDate.now();
		    return Pair.of(now.withDayOfMonth(1), now.withDayOfMonth(now.lengthOfMonth()));
		}

		public static Pair<LocalDate, LocalDate> getCurrentQuarterBounds() {
		    LocalDate now = LocalDate.now();
		    int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
		    LocalDate start = LocalDate.of(now.getYear(), (currentQuarter - 1) * 3 + 1, 1);
		    LocalDate end = start.plusMonths(2).withDayOfMonth(start.plusMonths(2).lengthOfMonth());
		    return Pair.of(start, end);
		}

		public static Pair<LocalDate, LocalDate> getCurrentYearBounds() {
		    int year = LocalDate.now().getYear();
		    return Pair.of(LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31));
		}


}
