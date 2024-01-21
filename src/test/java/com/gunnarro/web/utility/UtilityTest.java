package com.gunnarro.web.utility;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilityTest {

    @Test
    void calculateDaysBetweenDates_cv_experience() {
        // Mai 2023 - Januar 2024 MasterCard
        assertEquals("8 måneder", Utility.calculateDiffBetweenDates(LocalDate.of(2023, Month.MAY, 1), LocalDate.of(2024, Month.JANUARY, 1)));
        // Februar 2022 - April 2023 CatalystOne
        assertEquals("1 år 2 måneder", Utility.calculateDiffBetweenDates(LocalDate.of(2022, Month.FEBRUARY, 1), LocalDate.of(2023, Month.APRIL, 1)));
        // August 2013 - Januar 2022 Telenor
        assertEquals("8 år 5 måneder", Utility.calculateDiffBetweenDates(LocalDate.of(2013, Month.AUGUST, 1), LocalDate.of(2022, Month.JANUARY, 1)));
        // Januar 2008 - Juli 2014 TeliaSonera
        assertEquals("6 år 2 måneder", Utility.calculateDiffBetweenDates(LocalDate.of(2008, Month.MAY, 1), LocalDate.of(2014, Month.JULY, 1)));
        // Juni 2005 - Desember 2007 Software Innovation AS
        assertEquals("2 år 6 måneder", Utility.calculateDiffBetweenDates(LocalDate.of(2005, Month.JUNE, 1), LocalDate.of(2007, Month.DECEMBER, 1)));
        // Januar 2002 - Mai 2005 Telenor AS
        assertEquals("3 år 4 måneder", Utility.calculateDiffBetweenDates(LocalDate.of(2002, Month.JANUARY, 1), LocalDate.of(2005, Month.MAY, 1)));
        // Juni 2000 - Juli 2002 Accenture ANS
        assertEquals("1 år 10 måneder", Utility.calculateDiffBetweenDates(LocalDate.of(2000, Month.FEBRUARY, 1), LocalDate.of(2001, Month.DECEMBER, 1)));
        // Mai 1999 - Januar 2000 UiO
        assertEquals("8 måneder", Utility.calculateDiffBetweenDates(LocalDate.of(1999, Month.MAY, 1), LocalDate.of(2000, Month.JANUARY, 1)));
    }

    @Test
    void calculateDaysBetweenDates_cv_education() {
           }
}
