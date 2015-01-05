package org.cresst.sb.irp.dao;

import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class TestAnalysisActionTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void whenGradeValid_isGradeValid_Positive() {
//        final TestAnalysisAction underTest = new TestAnalysisAction();
//        final FieldCheckType actualFieldCheckType = new FieldCheckType();
//
//        underTest.isGradeValid("11", actualFieldCheckType);
//
//        final FieldCheckType expectedFieldCheckType = new FieldCheckType();
//        expectedFieldCheckType.setFieldEmpty(false);
//        expectedFieldCheckType.setCorrectDataType(true);
//        expectedFieldCheckType.setAcceptableValue(true);
//
//        assertEquals(expectedFieldCheckType, actualFieldCheckType);
    }

    @Test
    public void whenGradeUnexpectedValie_isGradeValid_Negative() {
//        final TestAnalysisAction underTest = new TestAnalysisAction();
//        final FieldCheckType actualFieldCheckType = new FieldCheckType();
//
//        underTest.isGradeValid("-1", actualFieldCheckType);
//
//        // Blank FieldCheckType has all false properties indicating invalid field check
//        final FieldCheckType expectedFieldCheckType = new FieldCheckType();
//
//        assertEquals(expectedFieldCheckType, actualFieldCheckType);
    }

    @Test
    public void whenGradeEmpty_isGradeValid_Negative() {
//        final TestAnalysisAction underTest = new TestAnalysisAction();
//        final FieldCheckType actualFieldCheckType = new FieldCheckType();
//
//        underTest.isGradeValid("", actualFieldCheckType);
//
//        // Blank FieldCheckType has all false properties indicating invalid field check
//        final FieldCheckType expectedFieldCheckType = new FieldCheckType();
//
//        assertEquals(expectedFieldCheckType, actualFieldCheckType);
    }

    @Test
    public void whenGradeNull_isGradeValid_Negative() {
//        final TestAnalysisAction underTest = new TestAnalysisAction();
//        final FieldCheckType actualFieldCheckType = new FieldCheckType();
//
//        underTest.isGradeValid(null, actualFieldCheckType);
//
//        // Blank FieldCheckType has all false properties indicating invalid field check
//        final FieldCheckType expectedFieldCheckType = new FieldCheckType();
//
//        assertEquals(expectedFieldCheckType, actualFieldCheckType);
    }
}